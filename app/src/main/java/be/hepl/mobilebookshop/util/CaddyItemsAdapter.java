package be.hepl.mobilebookshop.util;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import be.hepl.entity.CaddyItemElement;
import be.hepl.mobilebookshop.R;

import java.util.ArrayList;

public class CaddyItemsAdapter extends RecyclerView.Adapter<CaddyItemsAdapter.CaddyItemViewHolder> {

    private final ArrayList<CaddyItemElement> caddyItems;

    public CaddyItemsAdapter(ArrayList<CaddyItemElement> caddyItems) {
        this.caddyItems = caddyItems;
    }

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

        holder.bookIdTextView.setText("ID: " + caddyItem.getBookId());
        holder.titleTextView.setText("Titre: " + caddyItem.getTitle());
        holder.priceTextView.setText(String.format("Prix: %.2f€", caddyItem.getPrice()));
        holder.quantityTextView.setText("Quantité: " + caddyItem.getQuantity());
    }

    @Override
    public int getItemCount() {
        return caddyItems.size();
    }

    public ArrayList<CaddyItemElement> getCaddyItems() {
        return caddyItems;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateCaddyItems(ArrayList<CaddyItemElement> newCaddyItems) {
        // Vide la liste actuelle des articles
        caddyItems.clear();
        // Ajoute tous les nouveaux articles à la liste
        caddyItems.addAll(newCaddyItems);
        // Notifie l'adaptateur que les données ont changé, afin qu'il puisse rafraîchir l'affichage
        notifyDataSetChanged();
    }

    static class CaddyItemViewHolder extends RecyclerView.ViewHolder {

        TextView bookIdTextView, titleTextView, priceTextView, quantityTextView;

        public CaddyItemViewHolder(@NonNull View itemView) {
            super(itemView);
            bookIdTextView = itemView.findViewById(R.id.book_id);
            titleTextView = itemView.findViewById(R.id.book_title);
            priceTextView = itemView.findViewById(R.id.book_price);
            quantityTextView = itemView.findViewById(R.id.book_quantity);
        }
    }
}
