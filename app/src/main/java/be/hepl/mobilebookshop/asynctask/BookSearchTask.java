package be.hepl.mobilebookshop.asynctask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import be.hepl.entity.BookElement;
import be.hepl.mobilebookshop.protocol.BSPPClient;
import be.hepl.mobilebookshop.util.BooksAdapter;

import java.util.ArrayList;

public class BookSearchTask extends AsyncTask<Object, Void, ArrayList<BookElement>> {

    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private final BooksAdapter booksAdapter;

    public BookSearchTask(Context context, BooksAdapter booksAdapter) {
        this.context = context;
        this.booksAdapter = booksAdapter;
    }

    @Override
    protected ArrayList<BookElement> doInBackground(Object... params) {
        Integer bookId = (Integer) params[0];
        String title = (String) params[1];
        String lastName = (String) params[2];
        String firstName = (String) params[3];
        String subjectName = (String) params[4];
        Float maxPrice = (Float) params[5];

        // Effectue la requête de recherche de livres
        return BSPPClient.selectBook(bookId, title, lastName, firstName, subjectName, maxPrice);
    }

    @Override
    protected void onPostExecute(ArrayList<BookElement> results) {
        // Met à jour l'interface utilisateur après la recherche
        if (results.isEmpty()) {
            Toast.makeText(context, "Aucun livre trouvé", Toast.LENGTH_SHORT).show();
        }
        booksAdapter.updateBooks(results);
    }
}
