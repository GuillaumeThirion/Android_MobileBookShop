package be.hepl.mobilebookshop.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import be.hepl.entity.CaddyItemElement;
import be.hepl.mobilebookshop.R;
import be.hepl.mobilebookshop.protocol.BSPPClient;
import be.hepl.mobilebookshop.util.CaddyItemsAdapter;
import be.hepl.mobilebookshop.util.CaddyManager;

import java.util.ArrayList;

public class CaddyActivity extends AppCompatActivity {

    private CaddyItemsAdapter caddyItemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caddy);

        // Références aux vues
        Button logoutButton = findViewById(R.id.logout_button);
        Button backButton = findViewById(R.id.back_button);
        Button clearCaddyButton = findViewById(R.id.clear_caddy_button);
        Button payCaddyButton = findViewById(R.id.pay_caddy_button);
        RecyclerView caddyRecyclerView = findViewById(R.id.caddy_recycler_view);

        caddyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        caddyItemsAdapter = new CaddyItemsAdapter(CaddyManager.getCaddyItems());
        caddyRecyclerView.setAdapter(caddyItemsAdapter);

        // Gestion du clic sur le bouton "Déconnexion"
        logoutButton.setOnClickListener(v -> new LogoutTask().execute());

        // Gestion du clic sur le bouton "Retour"
        backButton.setOnClickListener(v -> {
            // Lance ShopActivity
            Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
            startActivity(intent);
            finish();
        });

        // Gestion du clic sur le bouton "Vider le panier"
        clearCaddyButton.setOnClickListener(v -> new ClearCaddyTask().execute());

        // Gestion du clic sur le bouton "Payer"
        payCaddyButton.setOnClickListener(v -> new PayCaddyTask().execute());
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        // Met à jour le panier à chaque reprise de l'activité
        ArrayList<CaddyItemElement> caddyItems = CaddyManager.getCaddyItems();
        Log.d("CaddyActivity", "Caddy items: " + caddyItems);
        caddyItemsAdapter.updateCaddyItems(caddyItems);
    }

    private class LogoutTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            // Ferme la connexion avec le serveur (ce qui revient à annuler le panier)
            BSPPClient.cancelCaddy();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // Redirige l'utilisateur vers la MainActivity après la déconnexion
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class ClearCaddyTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<CaddyItemElement> caddyItems = caddyItemsAdapter.getCaddyItems();

            if (caddyItems == null || caddyItems.isEmpty()) {
                return false;
            }

            // Vide le panier (supprime tous les articles)
            for (CaddyItemElement caddyItem : caddyItems) {
                BSPPClient.delCaddyItem(caddyItem.getBookId(), caddyItem.getQuantity());
            }
            CaddyManager.clearCaddy();

            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            caddyItemsAdapter.updateCaddyItems(new ArrayList<>());

            if (success) {
                Toast.makeText(getApplicationContext(), "Le panier a été vidé", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Le panier est vide !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class PayCaddyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            // Paie le panier
            BSPPClient.payCaddy();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // Redirige l'utilisateur vers la MainActivity après le paiement
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}