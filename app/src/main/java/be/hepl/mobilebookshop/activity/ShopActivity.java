package be.hepl.mobilebookshop.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import be.hepl.entity.BookElement;
import be.hepl.mobilebookshop.protocol.BSPPClient;
import be.hepl.mobilebookshop.util.BooksAdapter;
import be.hepl.mobilebookshop.R;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {

    private EditText bookIdField, titleField, lastNameField, firstNameField, subjectField, maxPriceField;
    private BooksAdapter booksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        // Références aux vues
        Button logoutButton = findViewById(R.id.logout_button);
        Button caddyButton = findViewById(R.id.caddy_button);
        bookIdField = findViewById(R.id.book_id_field);
        titleField = findViewById(R.id.title_field);
        lastNameField = findViewById(R.id.last_name_field);
        firstNameField = findViewById(R.id.first_name_field);
        subjectField = findViewById(R.id.subject_field);
        maxPriceField = findViewById(R.id.max_price_field);
        Button searchButton = findViewById(R.id.search_button);
        RecyclerView booksRecyclerView = findViewById(R.id.books_recycler_view);

        booksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        booksAdapter = new BooksAdapter(new ArrayList<>());
        booksRecyclerView.setAdapter(booksAdapter);

        // Gestion du clic sur le bouton de recherche
        searchButton.setOnClickListener(v -> performSearch());

        // Gestion du clic sur le bouton de logout
        logoutButton.setOnClickListener(v -> new LogoutTask().execute());
    }

    private void performSearch() {
        // Récupère les valeurs des champs
        String bookIdText = bookIdField.getText().toString().trim();
        String title = titleField.getText().toString().trim();
        String lastName = lastNameField.getText().toString().trim();
        String firstName = firstNameField.getText().toString().trim();
        String subjectName = subjectField.getText().toString().trim();
        String maxPriceText = maxPriceField.getText().toString().trim();

        Integer bookId = null;
        if (!bookIdText.isEmpty()) {
            try {
                bookId = Integer.parseInt(bookIdText);
            } catch (NumberFormatException ex) {
                Toast.makeText(this, "L'id du livre doit être un nombre entier", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Assigne explicitement null à la place des chaînes vides
        title = title.isEmpty() ? null : title;
        lastName = lastName.isEmpty() ? null : lastName;
        firstName = firstName.isEmpty() ? null : firstName;
        subjectName = subjectName.isEmpty() ? null : subjectName;

        Float maxPrice = null;
        if (!maxPriceText.isEmpty()) {
            try {
                maxPrice = Float.parseFloat(maxPriceText);
            } catch (NumberFormatException ex) {
                Toast.makeText(this, "Le prix du livre doit être un nombre décimal", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Exécution d'une AsyncTask pour rechercher les livres
        new BookSearchTask().execute(bookId, title, lastName, firstName, subjectName, maxPrice);
    }

    private class LogoutTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            // Ferme la connexion avec le serveur (ce qui revient à annuler le panier)
            BSPPClient.cancelCaddy();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // Redirige l'utilisateur vers la MainActivity après la déconnexion
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private class BookSearchTask extends AsyncTask<Object, Void, ArrayList<BookElement>> {

        @Override
        protected ArrayList<BookElement> doInBackground(Object... params) {
            Integer bookId = (Integer) params[0];
            String title = (String) params[1];
            String lastName = (String) params[2];
            String firstName = (String) params[3];
            String subjectName = (String) params[4];
            Float maxPrice = (Float) params[5];

            // Effectue la recherche dans le client BSPP
            return BSPPClient.selectBook(bookId, title, lastName, firstName, subjectName, maxPrice);
        }

        @Override
        protected void onPostExecute(ArrayList<BookElement> results) {
            super.onPostExecute(results);

            // Met à jour l'interface utilisateur après la recherche
            if (results.isEmpty()) {
                Toast.makeText(ShopActivity.this, "Aucun livre trouvé", Toast.LENGTH_SHORT).show();
            } else {
                booksAdapter.updateBooks(results);
            }
        }
    }
}