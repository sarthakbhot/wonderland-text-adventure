/**
 * Name: Sarthak Bhot
 * Student ID: B36069375
 * Date: April 22, 2026
 * File: Character.java
 * Purpose: Stores each character's name, room, and dialogue
 * so the player can interact with characters in the game.
 */
public class Character {

    // Short internal id used by the program.
    private String id;

    // Character name shown to the player.
    private String name;

    // The id of the room where the character is located.
    private String locationId;

    // What the character says when the player talks to them.
    private String dialogue;

    // Builds one character from the data file.
    public Character(String id, String name, String locationId, String dialogue) {

        // These values come from one line in characters.txt.
        this.id = id;
        this.name = name;
        this.locationId = locationId;
        this.dialogue = dialogue;
    }

    // Matches what the player typed against the character name.
    public boolean matches(String text) {
        String searchText = text.toLowerCase().trim();
        String characterName = name.toLowerCase().trim();
        String flatSearchText = searchText.replace(" ", "");
        String flatCharacterName = characterName.replace(" ", "");

        // Check the full character name first.
        if (characterName.equals(searchText)) {
            return true;
        }

        // Also allow partial matches from the main name.
        if (characterName.contains(searchText)) {
            return true;
        }

        // Also match if the player leaves spaces out.
        if (flatCharacterName.equals(flatSearchText)) {
            return true;
        }

        return flatCharacterName.contains(flatSearchText);
    }

    // These getters let the main game read character information.
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getLocationId() {
        return locationId;
    }
    public String getDialogue() {
        return dialogue;
    }
}
