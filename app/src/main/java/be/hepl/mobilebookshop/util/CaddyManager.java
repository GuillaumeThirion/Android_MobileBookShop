package be.hepl.mobilebookshop.util;

import be.hepl.entity.CaddyItemElement;

import java.util.ArrayList;

public class CaddyManager {

    private static final ArrayList<CaddyItemElement> caddyItems = new ArrayList<>();


    /* STATIC METHODS */

    public static ArrayList<CaddyItemElement> getCaddyItems() {
        return new ArrayList<>(caddyItems); // Renvoie une copie de la liste pour éviter les modifications externes
    }

    public static void addCaddyItem(CaddyItemElement caddyItem) {
        // Vérifie si le livre existe déjà dans le panier
        for (CaddyItemElement existingItem : caddyItems) {
            if (existingItem.getBookId() == caddyItem.getBookId()) {
                // Met à jour la quantité
                existingItem.setQuantity(existingItem.getQuantity() + caddyItem.getQuantity());
                return;
            }
        }
        // Ajoute un nouvel article si le livre ne se trouve pas encore dans le panier
        caddyItems.add(caddyItem);
    }

    public static void delCaddyItem(CaddyItemElement caddyItem) {
        // Recherche le livre dans le panier
        for (CaddyItemElement existingItem : caddyItems) {
            if (existingItem.getBookId() == caddyItem.getBookId()) {
                int quantityInCaddy = existingItem.getQuantity();
                int quantityToDelete = caddyItem.getQuantity();

                if (quantityInCaddy == quantityToDelete) {
                    // Supprime l'article
                    caddyItems.remove(existingItem);
                } else {
                    // Met à jour la quantité
                    existingItem.setQuantity(quantityInCaddy - quantityToDelete);
                }

                return;
            }
        }
    }

    public static void clearCaddy() {
        caddyItems.clear();
    }
}
