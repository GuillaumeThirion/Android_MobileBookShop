package be.hepl.mobilebookshop.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import be.hepl.mobilebookshop.R;
import be.hepl.mobilebookshop.asynctask.LoginTask;

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
            new LoginTask(this, lastName, firstName, isNewClient).execute();
        });
    }
}
