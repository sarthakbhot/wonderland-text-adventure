/**
 * Name: Sarthak Bhot
 * Student ID: B36069375
 * Date: April 22, 2026
 * File: Inventory.java
 * Purpose: Stores item ids for the player's inventory and safe stash
 * without duplicating full Item objects.
 */
import java.util.ArrayList;

public class Inventory {

    // Stores the ids of items in this inventory.
    private ArrayList<String> itemIds;

    public Inventory() {
        // Each inventory starts empty.
        this.itemIds = new ArrayList<String>();
    }

    // Adds the item only if it is not already stored.
    public void add(String itemId) {
        if (!contains(itemId)) {
            itemIds.add(itemId);
        }
    }

    // Remove returns true if the id was found.
    public boolean remove(String itemId) {
        return itemIds.remove(itemId);
    }

    // Used to check whether the player already has an item.
    public boolean contains(String itemId) {
        return itemIds.contains(itemId);
    }

    // Returns the raw list so the main game can look up item names.
    public ArrayList<String> getItemIds() {
        return itemIds;
    }

    // Used before printing messages like "You are carrying nothing."
    public boolean isEmpty() {
        return itemIds.isEmpty();
    }
}
