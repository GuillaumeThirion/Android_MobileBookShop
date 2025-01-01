package be.hepl.mobilebookshop.asynctask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import be.hepl.entity.CaddyItemElement;
import be.hepl.mobilebookshop.activity.CaddyActivity;
import be.hepl.mobilebookshop.activity.ShopActivity;
import be.hepl.mobilebookshop.protocol.BSPPClient;
import be.hepl.mobilebookshop.util.CaddyManager;

public class AddCaddyItemTask extends AsyncTask<Void, Void, Boolean> {
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private final CaddyItemElement caddyItem;

    public AddCaddyItemTask(Context context, CaddyItemElement caddyItem) {
        this.context = context;
        this.caddyItem = caddyItem;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        // Ajoute l'article en base de données
        return BSPPClient.addCaddyItem(caddyItem.getBookId(), caddyItem.getQuantity());
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            // Ajoute l'article localement
            CaddyManager.addCaddyItem(caddyItem);

            if (context instanceof ShopActivity shopActivity) {
                // Recherche les livres et met à jour l'affichage du magasin
                shopActivity.performSearch();
            } else if (context instanceof CaddyActivity caddyActivity) {
                // Met à jour l'affichage du panier
                caddyActivity.getCaddyItemsAdapter().updateCaddyItems(CaddyManager.getCaddyItems());
                // Met à jour le prix total
                caddyActivity.updateTotalPrice();
            }
        } else {
            Toast.makeText(context, "Erreur lors de l'ajout au panier", Toast.LENGTH_SHORT).show();
        }
    }
}
