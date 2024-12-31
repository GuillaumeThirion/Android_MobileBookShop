package be.hepl.mobilebookshop.activity;

import android.content.Intent;
import android.os.Bundle;

import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import be.hepl.mobilebookshop.asynctask.BookSearchTask;
import be.hepl.mobilebookshop.asynctask.LogoutTask;
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
        booksAdapter = new BooksAdapter(new ArrayList<>(), this);
        booksRecyclerView.setAdapter(booksAdapter);

        // Gestion du clic sur le bouton "Rechercher"
        searchButton.setOnClickListener(v -> performSearch());

        // Gestion du clic sur le bouton "Déconnexion"
        logoutButton.setOnClickListener(v -> new LogoutTask(this).execute());

        // Gestion du clic sur le bouton "Voir le panier"
        caddyButton.setOnClickListener(v -> {
            // Lance CaddyActivity
            Intent intent = new Intent(this, CaddyActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void performSearch() {
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
        new BookSearchTask(this, booksAdapter).execute(bookId, title, lastName, firstName, subjectName, maxPrice);
    }
}
