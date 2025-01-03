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


    /* CONSTRUCTOR */

    public BooksAdapter(ArrayList<BookElement> books, ShopActivity shopActivity) {
        this.books = books;
        this.shopActivity = shopActivity;
    }


    /* OVERRIDE METHODS */

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

        // Récupère le contexte depuis shopActivity
        String bookIdLabel = shopActivity.getString(R.string.book_id_label);
        String titleLabel = shopActivity.getString(R.string.title_label);
        String authorLabel = shopActivity.getString(R.string.book_author_label);
        String subjectLabel = shopActivity.getString(R.string.subject_label);
        String isbnLabel = shopActivity.getString(R.string.book_isbn_label);
        String pageCountLabel = shopActivity.getString(R.string.book_page_count_label);
        String publishYearLabel = shopActivity.getString(R.string.book_publish_year_label);
        String priceLabel = shopActivity.getString(R.string.book_price_label);
        String stockQuantityLabel = shopActivity.getString(R.string.book_stock_quantity_label);

        // Utilisation des chaînes localisées
        holder.idTextView.setText(bookIdLabel + " " + book.getId());
        holder.titleTextView.setText(titleLabel + " " + book.getTitle());
        holder.authorTextView.setText(authorLabel + " " + book.getAuthor());
        holder.subjectTextView.setText(subjectLabel + " " + book.getSubject());
        holder.isbnTextView.setText(isbnLabel + " " + book.getISBN());
        holder.pageCountTextView.setText(pageCountLabel + " " + book.getPageCount());
        holder.publishYearTextView.setText(publishYearLabel + " " + book.getPublishYear());
        holder.priceTextView.setText(String.format(priceLabel + " %.2f€", book.getPrice()));
        holder.stockQuantityTextView.setText(stockQuantityLabel + " " + book.getStockQuantity());

        // Gestion du clic sur le bouton "Ajouter au panier"
        CaddyItemElement caddyItem = new CaddyItemElement(book.getId(), book.getTitle(), book.getPrice(), 1);
        holder.addToCaddyButton.setOnClickListener(v -> new AddCaddyItemTask(shopActivity, caddyItem).execute());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }


    /* OTHER METHODS */

    @SuppressLint("NotifyDataSetChanged")
    public void updateBooks(ArrayList<BookElement> newBooks) {
        // Vide la liste actuelle des livres
        books.clear();
        // Ajoute tous les nouveaux livres à la liste
        books.addAll(newBooks);
        // Notifie l'adaptateur que les données ont changé, afin qu'il puisse rafraîchir l'affichage
        notifyDataSetChanged();
    }


    /* INNER CLASSES */

    public static class BookViewHolder extends RecyclerView.ViewHolder {

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
