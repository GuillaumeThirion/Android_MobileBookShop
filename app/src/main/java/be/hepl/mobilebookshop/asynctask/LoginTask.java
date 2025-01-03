package be.hepl.mobilebookshop.asynctask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import be.hepl.mobilebookshop.R;
import be.hepl.mobilebookshop.activity.ShopActivity;
import be.hepl.mobilebookshop.protocol.BSPPClient;

public class LoginTask extends AsyncTask<Object, Void, Integer> {

    @SuppressLint("StaticFieldLeak")
    private final Context context;


    /* CONSTRUCTOR */

    public LoginTask(Context context) {
        this.context = context;
    }


    /* OVERRIDE METHODS */

    @Override
    protected void onPreExecute() {
        // Code exécuté avant le début de la tâche (UI Thread)
        super.onPreExecute();
        Toast toast = Toast.makeText(context, context.getString(R.string.toast_connection), Toast.LENGTH_SHORT);
        toast.show();
        new android.os.Handler().postDelayed(toast::cancel, 750);
    }

    @Override
    protected Integer doInBackground(Object... params) {
        String lastName = (String) params[0];
        String firstName = (String) params[1];
        boolean isNewClient = (boolean) params[2];

        return BSPPClient.loginClient(context, lastName, firstName, isNewClient);
    }

    @Override
    protected void onPostExecute(Integer clientId) {
        if (clientId >= 0) {
            Toast.makeText(context, context.getString(R.string.toast_login_success) + " " + clientId, Toast.LENGTH_SHORT).show();

            // Lance ShopActivity
            Intent intent = new Intent(context, ShopActivity.class);
            context.startActivity(intent);

            // Si le contexte est une activité, celle-ci doit se terminer
            if (context instanceof android.app.Activity) {
                ((android.app.Activity) context).finish();
            }
        } else {
            String errorMessage = context.getString(R.string.toast_login_error_label) + " ";

            switch (clientId) {
                case -1: {
                    errorMessage += context.getString(R.string.toast_login_error_client_already_exists);
                    break;
                }
                case -2: {
                    errorMessage += context.getString(R.string.toast_login_error_client_does_not_exist);
                    break;
                }
                case -3: {
                    errorMessage += context.getString(R.string.toast_login_error_fields_not_complete);
                    break;
                }
                case -4: {
                    errorMessage += context.getString(R.string.toast_login_error_other);
                    break;
                }
            }

            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}
