package be.hepl.mobilebookshop.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import be.hepl.mobilebookshop.R;
import be.hepl.mobilebookshop.protocol.BSPPClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Références aux vues
        EditText lastNameInput = findViewById(R.id.last_name_input);
        EditText firstNameInput = findViewById(R.id.first_name_input);
        CheckBox newClientCheckbox = findViewById(R.id.new_client_checkbox);
        Button loginButton = findViewById(R.id.login_button);

        // Gestion du clic sur le bouton de login
        loginButton.setOnClickListener(v -> {
            // Récupération des données utilisateur
            String lastName = lastNameInput.getText().toString().trim();
            String firstName = firstNameInput.getText().toString().trim();
            boolean isNewClient = newClientCheckbox.isChecked();

            // Vérification de l'entrée utilisateur
            if (lastName.isEmpty() || firstName.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Exécution d'une AsyncTask pour effectuer le login du client
            new LoginTask(lastName, firstName, isNewClient).execute();
        });
    }

    private class LoginTask extends AsyncTask<Void, Void, Integer> {

        private final String lastName;
        private final String firstName;
        private final boolean isNewClient;

        public LoginTask(String lastName, String firstName, boolean isNewClient) {
            this.lastName = lastName;
            this.firstName = firstName;
            this.isNewClient = isNewClient;
        }

        @Override
        protected void onPreExecute() {
            // Code exécuté avant le début de la tâche (UI Thread)
            super.onPreExecute();
            Toast toast = Toast.makeText(getApplicationContext(), "Connexion en cours...", Toast.LENGTH_SHORT);
            toast.show();
            new android.os.Handler().postDelayed(toast::cancel, 750);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            // Code exécuté en arrière-plan (thread de fond)
            return BSPPClient.loginClient(getApplicationContext(), lastName, firstName, isNewClient);
        }

        @Override
        protected void onPostExecute(Integer clientId) {
            // Code exécuté après la fin de la tâche (UI Thread)
            super.onPostExecute(clientId);

            if (clientId >= 0) {
                Toast.makeText(getApplicationContext(), "Connexion réussie ! ID client : " + clientId, Toast.LENGTH_SHORT).show();

                // Lance ShopActivity
                Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Erreur lors de la connexion : " + clientId, Toast.LENGTH_LONG).show();
            }
        }
    }
}
