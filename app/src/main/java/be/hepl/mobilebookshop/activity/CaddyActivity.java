package be.hepl.mobilebookshop.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import be.hepl.entity.CaddyItemElement;
import be.hepl.mobilebookshop.R;
import be.hepl.mobilebookshop.asynctask.ClearCaddyTask;
import be.hepl.mobilebookshop.asynctask.CancelCaddyTask;
import be.hepl.mobilebookshop.asynctask.PayCaddyTask;
import be.hepl.mobilebookshop.util.CaddyItemsAdapter;
import be.hepl.mobilebookshop.util.CaddyManager;
import be.hepl.mobilebookshop.util.LanguageManager;

public class CaddyActivity extends AppCompatActivity {

    private CaddyItemsAdapter caddyItemsAdapter;


    /* OVERRIDE METHODS */

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
        caddyItemsAdapter = new CaddyItemsAdapter(CaddyManager.getCaddyItems(), this);
        caddyRecyclerView.setAdapter(caddyItemsAdapter);

        // Gestion du clic sur le bouton "Déconnexion"
        logoutButton.setOnClickListener(v -> new CancelCaddyTask(this).execute());

        // Gestion du clic sur le bouton "Retour"
        backButton.setOnClickListener(v -> {
            // Lance ShopActivity
            Intent intent = new Intent(this, ShopActivity.class);
            startActivity(intent);
            finish();
        });

        // Gestion du clic sur le bouton "Vider le panier"
        clearCaddyButton.setOnClickListener(v -> new ClearCaddyTask(this).execute());

        // Gestion du clic sur le bouton "Payer"
        payCaddyButton.setOnClickListener(v -> new PayCaddyTask(this).execute());
    }

    @SuppressLint({"NotifyDataSetChanged", "DefaultLocale"})
    @Override
    protected void onResume() {
        super.onResume();

        // Met à jour le panier à chaque reprise de l'activité
        caddyItemsAdapter.updateCaddyItems(CaddyManager.getCaddyItems());
        // Met à jour le prix total
        updateTotalPrice();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        // Applique la langue configurée
        Configuration config = LanguageManager.getConfiguration();
        super.attachBaseContext(newBase.createConfigurationContext(config));
    }


    /* GETTERS */

    public CaddyItemsAdapter getCaddyItemsAdapter() {
        return caddyItemsAdapter;
    }


    /* OTHER METHODS */

    @SuppressLint("DefaultLocale")
    public void updateTotalPrice() {
        float totalPrice = 0.0f;

        // Calcul du prix total
        for (CaddyItemElement caddyItem : CaddyManager.getCaddyItems()) {
            totalPrice += caddyItem.getPrice() * caddyItem.getQuantity();
        }

        // Met à jour le TextView du prix total
        String totalPriceLabel = getString(R.string.total_price_label);
        TextView totalPriceTextView = findViewById(R.id.total_price);
        totalPriceTextView.setText(String.format(totalPriceLabel + " %.2f€", totalPrice));
    }
}
