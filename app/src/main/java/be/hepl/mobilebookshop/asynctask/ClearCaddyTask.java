package be.hepl.mobilebookshop.asynctask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import be.hepl.entity.CaddyItemElement;
import be.hepl.mobilebookshop.R;
import be.hepl.mobilebookshop.activity.CaddyActivity;
import be.hepl.mobilebookshop.protocol.BSPPClient;
import be.hepl.mobilebookshop.util.CaddyManager;

import java.util.ArrayList;

public class ClearCaddyTask extends AsyncTask<Void, Void, Boolean> {

    @SuppressLint("StaticFieldLeak")
    private final Context context;


    /* CONSTRUCTOR */

    public ClearCaddyTask(Context context) {
        this.context = context;
    }


    /* OVERRIDE METHODS */

    @Override
    protected Boolean doInBackground(Void... voids) {
        ArrayList<CaddyItemElement> caddyItems = CaddyManager.getCaddyItems();

        if (caddyItems.isEmpty()) {
            return false;
        }

        // Vide le panier en base de données
        for (CaddyItemElement caddyItem : caddyItems) {
            BSPPClient.delCaddyItem(caddyItem.getBookId(), caddyItem.getQuantity());
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            // Vide le panier localement
            CaddyManager.clearCaddy();

            if (context instanceof CaddyActivity caddyActivity) {
                // Met à jour l'affichage du panier
                caddyActivity.getCaddyItemsAdapter().updateCaddyItems(new ArrayList<>());
                // Met à jour le prix total
                caddyActivity.updateTotalPrice();
            }
        } else {
            Toast.makeText(context, context.getString(R.string.toast_clear_caddy_error), Toast.LENGTH_SHORT).show();
        }
    }
}
