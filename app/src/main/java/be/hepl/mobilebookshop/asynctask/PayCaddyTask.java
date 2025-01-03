package be.hepl.mobilebookshop.asynctask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import be.hepl.mobilebookshop.R;
import be.hepl.mobilebookshop.activity.MainActivity;
import be.hepl.mobilebookshop.protocol.BSPPClient;
import be.hepl.mobilebookshop.util.CaddyManager;

public class PayCaddyTask extends AsyncTask<Void, Void, Boolean> {

    @SuppressLint("StaticFieldLeak")
    private final Context context;


    /* CONSTRUCTOR */

    public PayCaddyTask(Context context) {
        this.context = context;
    }


    /* OVERRIDE METHODS */

    @Override
    protected Boolean doInBackground(Void... voids) {
        // Paie le panier
        return BSPPClient.payCaddy();
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            // Ferme la connexion avec le serveur
            BSPPClient.closeConnection();
            // Vide le panier localement
            CaddyManager.clearCaddy();

            // Redirige l'utilisateur vers la MainActivity après le paiement
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);

            // Si le contexte est une activité, celle-ci doit se terminer
            if (context instanceof android.app.Activity) {
                ((android.app.Activity) context).finish();
            }
            Toast.makeText(context, context.getString(R.string.toast_pay_caddy_success), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getString(R.string.toast_pay_caddy_error), Toast.LENGTH_SHORT).show();
        }
    }
}
