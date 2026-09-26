/**
 * Name: Sarthak Bhot
 * Student ID: B36069375
 * Date: April 22, 2026
 * File: Action.java
 * Purpose: Stores one player command, whether it is a direction,
 * and any shortcut words that can also trigger the command.
 */
import java.util.ArrayList;

public class Action {

    // The main command word.
    private String name;

    // True when this action is a direction command.
    private boolean direction;

    // Shortcuts like "i" for inventory or "n" for north.
    private ArrayList<String> shortcuts;

    // Builds one action from the data file.
    public Action(String name, boolean direction, ArrayList<String> shortcuts) {
        // Each action keeps its main command word and any shortcuts.
        this.name = name;
        this.direction = direction;
        this.shortcuts = shortcuts;
    }

    // Checks the main action word and any shortcuts.
    public boolean matches(String text) {
        String searchText = text.toLowerCase().trim();
        String actionName = name.toLowerCase().trim();

        // Check the main command word first.
        if (actionName.equals(searchText)) {
            return true;
        }

        // If that fails, check the shortcut list.
        for (int i = 0; i < shortcuts.size(); i++) {
            String shortcut = shortcuts.get(i).toLowerCase().trim();

            if (shortcut.equals(searchText)) {
                return true;
            }
        }

        return false;
    }

    // Simple access methods used by the main game.
    public String getName() {
        return name;
    }

    public boolean isDirection() {
        return direction;
    }

}
