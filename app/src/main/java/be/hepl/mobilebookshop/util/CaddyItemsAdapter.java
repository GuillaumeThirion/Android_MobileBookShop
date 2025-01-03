package be.hepl.mobilebookshop.util;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import be.hepl.entity.CaddyItemElement;
import be.hepl.mobilebookshop.R;
import be.hepl.mobilebookshop.activity.CaddyActivity;
import be.hepl.mobilebookshop.asynctask.AddCaddyItemTask;
import be.hepl.mobilebookshop.asynctask.DelCaddyItemTask;

import java.util.ArrayList;

public class CaddyItemsAdapter extends RecyclerView.Adapter<CaddyItemsAdapter.CaddyItemViewHolder> {

    private final ArrayList<CaddyItemElement> caddyItems;
    private final CaddyActivity caddyActivity;


    /* CONSTRUCTOR */

    public CaddyItemsAdapter(ArrayList<CaddyItemElement> caddyItems, CaddyActivity caddyActivity) {
        this.caddyItems = caddyItems;
        this.caddyActivity = caddyActivity;
    }


    /* OVERRIDE METHODS */

    @NonNull
    @Override
    public CaddyItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.caddy_item_element, parent, false);
        return new CaddyItemViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull CaddyItemViewHolder holder, int position) {
        CaddyItemElement caddyItem = caddyItems.get(position);

        // Récupère le contexte depuis caddyActivity
        String bookIdLabel = caddyActivity.getString(R.string.book_id_label);
        String titleLabel = caddyActivity.getString(R.string.title_label);
        String priceLabel = caddyActivity.getString(R.string.book_price_label);
        String quantityLabel = caddyActivity.getString(R.string.quantity_label);

        // Utilisation des chaînes localisées
        holder.bookIdTextView.setText(bookIdLabel + " " + caddyItem.getBookId());
        holder.titleTextView.setText(titleLabel + " " + caddyItem.getTitle());
        holder.priceTextView.setText(String.format(priceLabel + " %.2f€", caddyItem.getPrice()));
        holder.quantityTextView.setText(quantityLabel + " " + caddyItem.getQuantity());

        // Article à ajouter ou à supprimer lors d'un clic sur les boutons "+" et "-"
        CaddyItemElement updateItem = new CaddyItemElement(caddyItem.getBookId(), caddyItem.getTitle(), caddyItem.getPrice(), 1);

        // Gestion du clic sur le bouton "+"
        holder.increaseQuantityButton.setOnClickListener(v -> new AddCaddyItemTask(caddyActivity, updateItem).execute());

        // Gestion du clic sur le bouton "-"
        holder.decreaseQuantityButton.setOnClickListener(v -> new DelCaddyItemTask(caddyActivity, updateItem).execute());
    }

    @Override
    public int getItemCount() {
        return caddyItems.size();
    }


    /* OTHER METHODS */

    @SuppressLint("NotifyDataSetChanged")
    public void updateCaddyItems(ArrayList<CaddyItemElement> newCaddyItems) {
        // Vide la liste actuelle des articles
        caddyItems.clear();
        // Ajoute tous les nouveaux articles à la liste
        caddyItems.addAll(newCaddyItems);
        // Notifie l'adaptateur que les données ont changé, afin qu'il puisse rafraîchir l'affichage
        notifyDataSetChanged();
    }


    /* INNER CLASSES */

    public static class CaddyItemViewHolder extends RecyclerView.ViewHolder {

        TextView bookIdTextView, titleTextView, priceTextView, quantityTextView;
        Button increaseQuantityButton, decreaseQuantityButton;

        public CaddyItemViewHolder(@NonNull View itemView) {
            super(itemView);
            bookIdTextView = itemView.findViewById(R.id.book_id);
            titleTextView = itemView.findViewById(R.id.book_title);
            priceTextView = itemView.findViewById(R.id.book_price);
            quantityTextView = itemView.findViewById(R.id.book_quantity);
            increaseQuantityButton = itemView.findViewById(R.id.increase_quantity_button);
            decreaseQuantityButton = itemView.findViewById(R.id.decrease_quantity_button);
        }
    }
}
