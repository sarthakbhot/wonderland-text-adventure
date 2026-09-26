/**
 * Name: Sarthak Bhot
 * Student ID: B36069375
 * Date: April 22, 2026
 * File: Location.java
 * Purpose: Stores one room in the game, including its descriptions,
 * exits, and whether the room has been visited before.
 */
import java.util.ArrayList;

public class Location {
    // Internal room id used by the program
    private String id;

    // Room name shown to the player
    private String name;

    // Description shown the first time the player enters
    private String firstDescription;

    // Description shown on later visits
    private String repeatDescription;

    // All exits that lead to other rooms
    private ArrayList<Exit> exits;

    // Tracks whether the player has visited before
    private boolean visited;

    // Builds one room and starts it as not visited.
    public Location(String id, String name,
                    String firstDescription, String repeatDescription) {

        // These values come from one line in locations.txt.
        this.id = id;
        this.name = name;
        this.firstDescription = firstDescription;
        this.repeatDescription = repeatDescription;
        this.exits = new ArrayList<Exit>();
        this.visited = false;
    }

    // Adds one exit that points to another room.
    public void addExit(String direction, String targetId) {
        exits.add(new Exit(direction, targetId));
    }

    // Returns the exit for a direction, or null if it does not exist.
    public Exit findExit(String direction) {
        for (int i = 0; i < exits.size(); i++) {
            Exit exit = exits.get(i);

            // Return the first exit whose direction matches.
            if (exit.getDirection().equals(direction)) {
                return exit;
            }
        }

        return null;
    }

    // These getters let the main game read room data when needed.
    public String getId() {
        return id;
    }

    // Printed as the room title.
    public String getName() {
        return name;
    }

    // Used the first time the player enters the room.
    public String getFirstDescription() {
        return firstDescription;
    }

    // Used on later visits to the same room.
    public String getRepeatDescription() {
        return repeatDescription;
    }

    // Returns the list of exits connected to this room.
    public ArrayList<Exit> getExits() {
        return exits;
    }

    // Used to decide whether to show the first or repeat description.
    public boolean isVisited() {
        return visited;
    }

    // Marks the room as visited after the first description is shown.
    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
