/**
 * Name: Sarthak Bhot
 * Student ID: B36069375
 * Date: April 22, 2026
 * File: Player.java
 * Purpose: Stores the player's current state, including location
 * and turn count.
 */
public class Player {

    // The id of the room the player is currently in
    private String currentLocationId;

    // Counts how many actions the player has taken
    private int turns;

    // Sets the starting room and default player values.
    public Player(String startingLocationId) {
        this.currentLocationId = startingLocationId;
        this.turns = 0;
    }

    // These access methods let the main game read and update player state.
    public String getCurrentLocationId() {
        return currentLocationId;
    }

    // The main game updates this after movement.
    public void setCurrentLocationId(String currentLocationId) {
        this.currentLocationId = currentLocationId;
    }

    public int getTurns() {
        return turns;
    }

    // Called after a successful move to track progress.
    public void advanceTurn() {
        turns++;
    }
}
