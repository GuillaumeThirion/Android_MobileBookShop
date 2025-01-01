package be.hepl.mobilebookshop.asynctask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import be.hepl.entity.CaddyItemElement;
import be.hepl.mobilebookshop.activity.CaddyActivity;
import be.hepl.mobilebookshop.protocol.BSPPClient;
import be.hepl.mobilebookshop.util.CaddyManager;

public class DelCaddyItemTask extends AsyncTask<Void, Void, Boolean> {
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private final CaddyItemElement caddyItem;

    public DelCaddyItemTask(Context context, CaddyItemElement caddyItem) {
        this.context = context;
        this.caddyItem = caddyItem;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        // Supprime l'article en base de données
        return BSPPClient.delCaddyItem(caddyItem.getBookId(), caddyItem.getQuantity());
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            // Supprime l'article localement
            CaddyManager.delCaddyItem(caddyItem);

            if (context instanceof CaddyActivity caddyActivity) {
                // Met à jour l'affichage du panier
                caddyActivity.getCaddyItemsAdapter().updateCaddyItems(CaddyManager.getCaddyItems());
                // Met à jour le prix total
                caddyActivity.updateTotalPrice();
            }
        } else {
            Toast.makeText(context, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
        }
    }
}
