package be.hepl.mobilebookshop.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import be.hepl.entity.CaddyItemElement;
import be.hepl.mobilebookshop.R;
import be.hepl.mobilebookshop.asynctask.ClearCaddyTask;
import be.hepl.mobilebookshop.asynctask.LogoutTask;
import be.hepl.mobilebookshop.asynctask.PayCaddyTask;
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
        caddyItemsAdapter = new CaddyItemsAdapter(CaddyManager.getCaddyItems(), this);
        caddyRecyclerView.setAdapter(caddyItemsAdapter);

        // Gestion du clic sur le bouton "Déconnexion"
        logoutButton.setOnClickListener(v -> new LogoutTask(this).execute());

        // Gestion du clic sur le bouton "Retour"
        backButton.setOnClickListener(v -> {
            // Lance ShopActivity
            Intent intent = new Intent(this, ShopActivity.class);
            startActivity(intent);
            finish();
        });

        // Gestion du clic sur le bouton "Vider le panier"
        clearCaddyButton.setOnClickListener(v -> {
            new ClearCaddyTask(this, caddyItemsAdapter).execute();
            updateTotalPrice();
        });

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

    @SuppressLint("DefaultLocale")
    public void updateTotalPrice() {
        float totalPrice = 0.0f;

        for (CaddyItemElement caddyItem : CaddyManager.getCaddyItems()) {
            totalPrice += caddyItem.getPrice() * caddyItem.getQuantity();
        }

        // Référence au TextView du prix total
        TextView totalPriceTextView = findViewById(R.id.total_price);
        totalPriceTextView.setText(String.format("Prix total: %.2f€", totalPrice));
    }
}
