package be.hepl.mobilebookshop.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import be.hepl.mobilebookshop.R;
import be.hepl.mobilebookshop.asynctask.LoginTask;
import be.hepl.mobilebookshop.util.LanguageManager;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    /* OVERRIDE METHODS */

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
                Toast.makeText(this, getString(R.string.toast_fill_all_fields), Toast.LENGTH_SHORT).show();
                return;
            }

            // Exécution d'une AsyncTask pour effectuer le login du client
            new LoginTask(this).execute(lastName, firstName, isNewClient);
        });

        // Initialisation du Spinner pour choisir la langue
        Spinner languageSpinner = findViewById(R.id.language_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.languages_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        // Positionne le Spinner sur la langue actuelle
        String currentLanguage = LanguageManager.getLanguageCode();
        if (currentLanguage.equals("en")) {
            languageSpinner.setSelection(0);
        } else if (currentLanguage.equals("fr")) {
            languageSpinner.setSelection(1);
        }

        // Gestion du changement de langue
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    changeLanguage("en");
                } else if (position == 1) {
                    changeLanguage("fr");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
    }

    // Change la langue de l'application
    private void changeLanguage(String languageCode) {
        // Vérifie la langue actuelle avant de procéder au changement
        String currentLanguage = Locale.getDefault().getLanguage();

        if (!currentLanguage.equals(languageCode)) {
            // Stocke la langue dans le singleton
            LanguageManager.setLanguageCode(languageCode);

            // Change la langue
            Configuration config = LanguageManager.getConfiguration();
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

            // Recrée l'activité en appliquant les changements de langue
            recreate();
        }
    }
}
