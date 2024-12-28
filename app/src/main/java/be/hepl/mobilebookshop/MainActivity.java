package be.hepl.mobilebookshop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Références aux vues
        EditText lastNameInput = findViewById(R.id.last_name_input);
        EditText firstNameInput = findViewById(R.id.first_name_input);
        CheckBox newClientCheckbox = findViewById(R.id.new_client_checkbox);
        Button connectButton = findViewById(R.id.connect_button);

        // Gestion du clic sur le bouton de connexion
        connectButton.setOnClickListener(v -> {
            // Récupération des données utilisateur
            String lastName = lastNameInput.getText().toString().trim();
            String firstName = firstNameInput.getText().toString().trim();
            boolean isNewClient = newClientCheckbox.isChecked();

            // Vérification de l'entrée utilisateur
            if (lastName.isEmpty() || firstName.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Appel du protocole dans un thread séparé (obligatoire pour éviter le blocage de l'UI)
            new Thread(() -> {
                int clientId = BSPPClient.loginClient(this, lastName, firstName, isNewClient);

                // Mise à jour de l'UI en fonction du résultat
                runOnUiThread(() -> {
                    if (clientId >= 0) {
                        Toast.makeText(this, "Connexion réussie ! ID client : " + clientId, Toast.LENGTH_LONG).show();

                        // Création d'un Intent pour lancer l'activité ShopActivity
                        Intent intent = new Intent(MainActivity.this, ShopActivity.class);
                        startActivity(intent); // Démarre l'activité ShopActivity
                        finish(); // Termine MainActivity pour ne pas revenir en arrière
                    } else {
                        Toast.makeText(this, "Erreur lors de la connexion : " + clientId, Toast.LENGTH_LONG).show();
                    }
                });
            }).start();
        });
    }
}
