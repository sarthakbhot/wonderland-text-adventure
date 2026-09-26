/**
 * Name: Sarthak Bhot
 * Student ID: B36069375
 * Date: April 22, 2026
 * File: Exit.java
 * Purpose: Stores one direction that leads from the current room
 * to another room in the game map.
 */
public class Exit {
    // Direction the player can type, like east or north
    private String direction;

    // Id of the room this exit leads to
    private String targetId;

    // Builds one exit from a direction and a target room id.
    public Exit(String direction, String targetId) {
        // Store where this exit goes.
        this.direction = direction;
        this.targetId = targetId;
    }

    // Used when printing exits and when moving to the next room.
    public String getDirection() {
        return direction;
    }

    public String getTargetId() {
        return targetId;
    }
}
