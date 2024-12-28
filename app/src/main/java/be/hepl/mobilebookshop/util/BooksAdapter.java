package be.hepl.mobilebookshop.util;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import be.hepl.entity.BookElement;
import be.hepl.mobilebookshop.R;

import java.util.ArrayList;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder> {

    private final ArrayList<BookElement> books;

    public BooksAdapter(ArrayList<BookElement> books) {
        this.books = books;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_element, parent, false);
        return new BookViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        BookElement book = books.get(position);

        holder.idTextView.setText("ID: " + book.getId());
        holder.titleTextView.setText("Titre: " + book.getTitle());
        holder.authorTextView.setText("Auteur: " + book.getAuthor());
        holder.subjectTextView.setText("Sujet: " + book.getSubject());
        holder.isbnTextView.setText("ISBN: " + book.getISBN());
        holder.pagesTextView.setText("Pages: " + book.getPageCount());
        holder.yearTextView.setText("Année: " + book.getPublishYear());
        holder.priceTextView.setText(String.format("Prix: %.2f€", book.getPrice()));
        holder.stockTextView.setText("Quantité en stock: " + book.getStockQuantity());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateBooks(ArrayList<BookElement> newBooks) {
        // Vide la liste actuelle des livres
        books.clear();
        // Ajoute tous les nouveaux livres à la liste
        books.addAll(newBooks);
        // Notifie l'adaptateur que les données ont changé, afin qu'il puisse rafraîchir l'affichage
        notifyDataSetChanged();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {

        TextView idTextView, titleTextView, authorTextView, subjectTextView;
        TextView isbnTextView, pagesTextView, yearTextView, priceTextView, stockTextView;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.book_id);
            titleTextView = itemView.findViewById(R.id.book_title);
            authorTextView = itemView.findViewById(R.id.book_author);
            subjectTextView = itemView.findViewById(R.id.book_subject);
            isbnTextView = itemView.findViewById(R.id.book_isbn);
            pagesTextView = itemView.findViewById(R.id.book_pages);
            yearTextView = itemView.findViewById(R.id.book_year);
            priceTextView = itemView.findViewById(R.id.book_price);
            stockTextView = itemView.findViewById(R.id.book_stock);
        }
    }
}
