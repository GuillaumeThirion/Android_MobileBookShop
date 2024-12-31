package be.hepl.mobilebookshop.util;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import be.hepl.entity.BookElement;
import be.hepl.entity.CaddyItemElement;
import be.hepl.mobilebookshop.R;
import be.hepl.mobilebookshop.activity.ShopActivity;
import be.hepl.mobilebookshop.asynctask.AddCaddyItemTask;

import java.util.ArrayList;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder> {

    private final ArrayList<BookElement> books;
    private final ShopActivity shopActivity;

    public BooksAdapter(ArrayList<BookElement> books, ShopActivity shopActivity) {
        this.books = books;
        this.shopActivity = shopActivity;
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
        holder.pageCountTextView.setText("Nombre de pages: " + book.getPageCount());
        holder.publishYearTextView.setText("Année de publication: " + book.getPublishYear());
        holder.priceTextView.setText(String.format("Prix: %.2f€", book.getPrice()));
        holder.stockQuantityTextView.setText("Quantité en stock: " + book.getStockQuantity());

        // Gestion du clic sur le bouton "Ajouter au panier"
        CaddyItemElement caddyItem = new CaddyItemElement(book.getId(), book.getTitle(), book.getPrice(), 1);
        holder.addToCaddyButton.setOnClickListener(v -> {
            // Exécution d'une AsyncTask pour ajouter l'article au panier
            new AddCaddyItemTask(v.getContext(), null, caddyItem).execute();
            // Recherche les livres et met à jour l'affichage
            shopActivity.performSearch();
        });
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
        TextView isbnTextView, pageCountTextView, publishYearTextView, priceTextView, stockQuantityTextView;
        Button addToCaddyButton;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.book_id);
            titleTextView = itemView.findViewById(R.id.book_title);
            authorTextView = itemView.findViewById(R.id.book_author);
            subjectTextView = itemView.findViewById(R.id.book_subject);
            isbnTextView = itemView.findViewById(R.id.book_isbn);
            pageCountTextView = itemView.findViewById(R.id.book_page_count);
            publishYearTextView = itemView.findViewById(R.id.book_publish_year);
            priceTextView = itemView.findViewById(R.id.book_price);
            stockQuantityTextView = itemView.findViewById(R.id.book_stock_quantity);
            addToCaddyButton = itemView.findViewById(R.id.add_to_caddy_button);
        }
    }
}
