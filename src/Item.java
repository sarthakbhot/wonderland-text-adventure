/**
 * Name: Sarthak Bhot
 * Student ID: B36069375
 * Date: April 22, 2026
 * File: Item.java
 * Purpose: Stores item data such as name, location,
 * and whether an item counts as treasure.
 */
public class Item {

    // A short internal id used by the program.
    private String id;

    // The full name shown to the player.
    private String name;

    // The item's current location.
    // This can also become "inventory", "stash", or "used".
    private String locationId;

    // true if this item counts as treasure for score / winning
    private boolean treasure;

    // Builds one item from the data file.
    public Item(String id, String name, String locationId, boolean treasure) {

        // Most of these values come straight from one line in items.txt.
        this.id = id;
        this.name = name;
        this.locationId = locationId;
        this.treasure = treasure;
    }

    // Matches the player's text against the item name.
    public boolean matches(String text) {
        String searchText = text.toLowerCase().trim();
        String itemName = name.toLowerCase().trim();
        String flatSearchText = searchText.replace(" ", "");
        String flatItemName = itemName.replace(" ", "");

        // Check the full item name first.
        if (itemName.equals(searchText)) {
            return true;
        }

        // Also allow partial matches from the item name.
        if (itemName.contains(searchText)) {
            return true;
        }

        // Also match if the player leaves spaces out.
        if (flatItemName.equals(flatSearchText)) {
            return true;
        }

        return flatItemName.contains(flatSearchText);
    }

    // These getters and setters let the main game update item state.
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getLocationId() {
        return locationId;
    }
    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
    public boolean isTreasure() {
        return treasure;
    }
}
