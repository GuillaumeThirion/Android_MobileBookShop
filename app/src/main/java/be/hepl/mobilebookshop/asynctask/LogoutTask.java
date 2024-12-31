package be.hepl.mobilebookshop.asynctask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import be.hepl.mobilebookshop.activity.MainActivity;
import be.hepl.mobilebookshop.protocol.BSPPClient;
import be.hepl.mobilebookshop.util.CaddyManager;

public class LogoutTask extends AsyncTask<Void, Void, Void> {

    @SuppressLint("StaticFieldLeak")
    private final Context context;

    public LogoutTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        // Annule le panier et ferme la connexion avec le serveur
        BSPPClient.cancelCaddy();
        BSPPClient.closeConnection();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
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
