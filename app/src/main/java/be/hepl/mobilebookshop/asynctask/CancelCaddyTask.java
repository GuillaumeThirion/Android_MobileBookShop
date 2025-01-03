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

public class CancelCaddyTask extends AsyncTask<Void, Void, Boolean> {

    @SuppressLint("StaticFieldLeak")
    private final Context context;


    /* CONSTRUCTOR */

    public CancelCaddyTask(Context context) {
        this.context = context;
    }


    /* OVERRIDE METHODS */

    @Override
    protected Boolean doInBackground(Void... voids) {
        // Annule le panier
        return BSPPClient.cancelCaddy();
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            Toast.makeText(context, context.getString(R.string.toast_cancel_caddy_success), Toast.LENGTH_SHORT).show();
        }

        // Ferme la connexion avec le serveur
        BSPPClient.closeConnection();
        // Vide le panier localement
        CaddyManager.clearCaddy();

        // Redirige l'utilisateur vers la MainActivity après la déconnexion
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);

        // Si le contexte est une activité, celle-ci doit se terminer
        if (context instanceof android.app.Activity) {
            ((android.app.Activity) context).finish();
        }
    }
}
