package be.hepl.mobilebookshop.asynctask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import be.hepl.mobilebookshop.activity.ShopActivity;
import be.hepl.mobilebookshop.protocol.BSPPClient;

public class LoginTask extends AsyncTask<Void, Void, Integer> {

    @SuppressLint("StaticFieldLeak")
    private final Context context;

    private final String lastName;
    private final String firstName;
    private final boolean isNewClient;

    public LoginTask(Context context, String lastName, String firstName, boolean isNewClient) {
        this.context = context;
        this.lastName = lastName;
        this.firstName = firstName;
        this.isNewClient = isNewClient;
    }

    @Override
    protected void onPreExecute() {
        // Code exécuté avant le début de la tâche (UI Thread)
        super.onPreExecute();
        Toast toast = Toast.makeText(context, "Connexion en cours...", Toast.LENGTH_SHORT);
        toast.show();
        new android.os.Handler().postDelayed(toast::cancel, 750);
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        return BSPPClient.loginClient(context, lastName, firstName, isNewClient);
    }

    @Override
    protected void onPostExecute(Integer clientId) {
        if (clientId >= 0) {
            Toast.makeText(context, "Connexion réussie ! ID client : " + clientId, Toast.LENGTH_SHORT).show();

            // Lance ShopActivity
            Intent intent = new Intent(context, ShopActivity.class);
            context.startActivity(intent);

            // Si le contexte est une activité, celle-ci doit se terminer
            if (context instanceof android.app.Activity) {
                ((android.app.Activity) context).finish();
            }
        } else {
            Toast.makeText(context, "Erreur lors de la connexion : " + clientId, Toast.LENGTH_LONG).show();
        }
    }
}
