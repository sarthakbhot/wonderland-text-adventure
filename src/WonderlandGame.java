/**
 * Name: Sarthak Bhot
 * Student ID: B36069375
 * Date: April 22, 2026
 * File: WonderlandGame.java
 * Purpose: Main class for the Wonderland text adventure.
 * It loads the data files, runs the game loop, and handles player actions.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class WonderlandGame {

    // These lists store the whole game world in memory after we load the text files.
    private ArrayList<Location> locations;
    private ArrayList<Item> items;
    private ArrayList<Character> characters;
    private ArrayList<Action> actions;

    // These objects store the player's state and input handling.
    private Player player;
    private Inventory inventory;
    private Inventory safeStash;
    private Control control;
    private boolean gameOver;

    public WonderlandGame() {
        // Start with empty lists, then fill them from the text files.
        locations = new ArrayList<Location>();
        items = new ArrayList<Item>();
        characters = new ArrayList<Character>();
        actions = new ArrayList<Action>();

        player = new Player("rabbit_hole_entrance");
        inventory = new Inventory();
        safeStash = new Inventory();
        control = new Control();
        gameOver = false;
        loadGameData();
    }

    public static void main(String[] args) {
        WonderlandGame game = new WonderlandGame();
        game.run();
    }

    // Starts the game, shows the opening text, and keeps reading commands.
    public void run() {
        showWelcome();
        describeCurrentLocation();

        // Keep looping until the player wins or quits.
        while (!gameOver) {
            String input = control.getInput();

            if (input.trim().equals("")) {
                System.out.println("Please type a command.");
                continue;
            }

            // The first word becomes the action, and the rest becomes the target text.
            Action action = control.findAction(input, actions);
            String objectText = control.getObjectText(input);

            if (action == null) {
                System.out.println("I do not understand that command.");
                showHelp();
            } else {
                processAction(action, objectText);
            }
        }
    }

    private void loadGameData() {
        try {
            // Each method reads one text file and turns the text into objects.
            // This keeps the game content outside the Java code.
            loadLocations();
            loadItems();
            loadCharacters();
            loadActions();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not load the game data files.", e);
        }
    }

    private void loadLocations() throws FileNotFoundException {
        // Opens the file named locations.txt.
        // The helper method openDataFile() looks in the src folder.
        Scanner scanner = openDataFile("locations.txt");

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (isCommentOrBlank(line)) {
                continue;
            }

            // Split one room line into its main fields.
            String[] parts = line.split("\\|", -1);

            // Build the room from the main room fields.
            Location location = new Location(
                    parts[0],
                    parts[1],
                    parts[2],
                    parts[3]
            );

            // The last field stores all exits for this room.
            String[] exits = parts[4].split(",");

            for (int i = 0; i < exits.length; i++) {
                String exitText = exits[i].trim();

                if (!exitText.equals("")) {
                    // Split each exit into a direction and a target room.
                    String[] exitParts = exitText.split("=");
                    location.addExit(exitParts[0].trim(), exitParts[1].trim());
                }
            }

            // Store the finished Location object in the list of rooms.
            locations.add(location);
        }

        scanner.close();
    }

    private void loadItems() throws FileNotFoundException {
        // Opens items.txt from the src folder.
        Scanner scanner = openDataFile("items.txt");

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (isCommentOrBlank(line)) {
                continue;
            }

            // Split one item line into its fields.
            String[] parts = line.split("\\|", -1);

            // Use those fields to build one Item object.
            // Each line becomes one item in the game world.
            items.add(new Item(
                    parts[0],
                    parts[1],
                    parts[2],
                    Boolean.parseBoolean(parts[3])
            ));
        }

        scanner.close();
    }

    private void loadCharacters() throws FileNotFoundException {
        // Opens characters.txt from the src folder.
        Scanner scanner = openDataFile("characters.txt");

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (isCommentOrBlank(line)) {
                continue;
            }

            // Split one character line into its fields.
            String[] parts = line.split("\\|", -1);

            // Use those fields to build one Character object.
            // Each line becomes one person the player can meet.
            characters.add(new Character(
                    parts[0],
                    parts[1],
                    parts[2],
                    parts[3]
            ));
        }

        scanner.close();
    }

    private void loadActions() throws FileNotFoundException {
        // Opens actions.txt from the src folder.
        Scanner scanner = openDataFile("actions.txt");

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (isCommentOrBlank(line)) {
                continue;
            }

            // Split one action line into its fields.
            String[] parts = line.split("\\|", -1);

            // Build one Action object from the line.
            actions.add(new Action(
                    parts[0],
                    Boolean.parseBoolean(parts[1]),
                    makeShortcutsFromText(parts[2])
            ));
        }

        scanner.close();
    }

    // Chooses which game method to run for the player's command.
    private void processAction(Action action, String objectText) {
        // Some actions are directions by themselves, like "north" or "east".
        if (action.isDirection()) {
            movePlayer(action.getName());
            return;
        }

        String actionName = action.getName();

        // For non-direction commands, send the player to the right method.
        switch (actionName) {
            case "go":
                if (objectText.equals("")) {
                    System.out.println("Go where?");
                    showExitsHere();
                } else {
                    movePlayer(objectText);
                }
                break;
            case "look":
                describeCurrentLocation();
                break;
            case "inventory":
                showInventory();
                break;
            case "take":
                takeItem(objectText);
                break;
            case "drop":
                dropItem(objectText);
                break;
            case "talk":
                talkToCharacter(objectText);
                break;
            case "give":
                giveItem(objectText);
                break;
            case "use":
                useItem(objectText);
                break;
            case "stash":
                stashItem(objectText);
                break;
            case "help":
                showHelp();
                break;
            case "quit":
                System.out.println("You leave Wonderland for now.");
                gameOver = true;
                break;
            default:
                System.out.println("Nothing happens.");
        }
    }

    // Moves the player if the current room has that exit.
    private void movePlayer(String directionText) {
        // Convert shortcuts like "n" into full words like "north".
        String direction = normalizeDirection(directionText);

        if (direction == null) {
            System.out.println("That is not a direction I understand.");
            showExitsHere();
            return;
        }

        Exit exit = getCurrentLocation().findExit(direction);

        if (exit == null) {
            System.out.println("You cannot go that way from here.");
            showExitsHere();
            return;
        }

        player.setCurrentLocationId(exit.getTargetId());
        player.advanceTurn();
        describeCurrentLocation();
    }

    // Prints the room name, description, people, items, exits, and a hint.
    private void describeCurrentLocation() {
        Location location = getCurrentLocation();

        System.out.println();
        System.out.println(location.getName());

        // First visits show the longer description.
        if (!location.isVisited()) {
            System.out.println(location.getFirstDescription());
            location.setVisited(true);
        } else {
            // Later visits show the shorter repeat description.
            System.out.println(location.getRepeatDescription());
        }

        showCharactersHere();
        showItemsHere();
        showExitsHere();
        showRoomHint();
    }

    // Shows which characters are in the current room.
    private void showCharactersHere() {
        ArrayList<Character> visibleCharacters = getCharactersInCurrentLocation();

        if (!visibleCharacters.isEmpty()) {
            ArrayList<String> names = new ArrayList<String>();

            // Turn the visible character objects into display names.
            for (int i = 0; i < visibleCharacters.size(); i++) {
                names.add(visibleCharacters.get(i).getName());
            }

            // Print the list and suggest a simple talk command.
            System.out.println("People here: " + makeListText(names));
            System.out.println("Try: talk " + visibleCharacters.get(0).getName().toLowerCase() + ".");
        }
    }

    // Shows which items are currently visible in the room.
    private void showItemsHere() {
        ArrayList<Item> visibleItems = getVisibleItemsInCurrentLocation();

        if (!visibleItems.isEmpty()) {
            ArrayList<String> names = new ArrayList<String>();

            // Turn the visible item objects into display names.
            for (int i = 0; i < visibleItems.size(); i++) {
                names.add(visibleItems.get(i).getName());
            }

            // Print the item names as one readable sentence.
            System.out.println("Items here: " + makeListText(names));
        }
    }

    // Shows the movement choices the player can type.
    private void showExitsHere() {
        // Build a list like "go east" and "go west".
        ArrayList<String> options = getMovementOptions();
        System.out.println("Your movement options here are: " + makeListText(options));
    }

    // Gives the player a simple next-step hint based on the room.
    private void showRoomHint() {
        String locationId = player.getCurrentLocationId();

        if (locationId.equals("rabbit_hole_entrance")) {
            System.out.println("What to do next: type go down to enter Wonderland.");
        } else if (locationId.equals("wonder_hall")) {
            System.out.println("What to do next: explore another room, then come back here to stash treasure.");
        } else if (locationId.equals("rabbit_house")) {
            if (inventory.contains("watch") || safeStash.contains("watch")) {
                System.out.println("What to do next: return to Wonder Hall when you are ready to stash treasure.");
            } else if (!inventory.contains("gloves")) {
                System.out.println("What to do next: take the gloves and the fan, then give the gloves to the rabbit.");
            } else {
                System.out.println("What to do next: while carrying the gloves, type give gloves to rabbit.");
            }
        } else if (locationId.equals("tea_party_table")) {
            System.out.println("What to do next: take the tea cup. It is one of the treasure items.");
        } else if (locationId.equals("mushroom_grove")) {
            System.out.println("What to do next: talk to the Caterpillar, then keep exploring for treasure.");
        } else if (locationId.equals("queen_garden")) {
            System.out.println("What to do next: be careful here. The Queen is watching her garden closely.");
        }
    }

    // Lets the player pick up an item from the current room.
    private void takeItem(String objectText) {
        if (objectText.equals("")) {
            System.out.println("Take what?");
            return;
        }

        // The item has to be in the current room.
        Item item = findItemInCurrentLocation(objectText);

        if (item == null) {
            System.out.println("That item is not here.");
            showItemsHere();
            return;
        }

        // Move the item into the player's inventory.
        // Most items are simple pickups that add flavor to the room.
        inventory.add(item.getId());
        item.setLocationId("inventory");
        System.out.println("You take the " + item.getName() + ".");

        if (item.isTreasure()) {
            // Remind the player that treasure should be stored in Wonder Hall.
            System.out.println("That is a treasure item. Later, return to Wonder Hall and type stash " + item.getName().toLowerCase() + ".");
        }
    }

    // Lets the player drop an item into the current room.
    private void dropItem(String objectText) {
        if (objectText.equals("")) {
            System.out.println("Drop what?");
            return;
        }

        // Only items the player is already carrying can be dropped.
        Item item = findItemInInventory(objectText);

        if (item == null) {
            System.out.println("You are not carrying that.");
            return;
        }

        inventory.remove(item.getId());
        // After dropping it, the item belongs to the current room again.
        item.setLocationId(player.getCurrentLocationId());
        System.out.println("You drop the " + item.getName() + ".");
    }

    // Talks to a character and prints any special hints for that character.
    private void talkToCharacter(String objectText) {
        if (objectText.equals("")) {
            System.out.println("Talk to whom?");
            return;
        }

        // The character must be in the same room as the player.
        Character character = findCharacterInCurrentLocation(objectText);

        if (character == null) {
            System.out.println("That character is not here.");
            showCharactersHere();
            return;
        }

        // Start by printing the character's normal dialogue.
        System.out.println(character.getDialogue());

        // Some characters give special hints or puzzle help.
        if (character.getId().equals("white_rabbit")) {
            if (inventory.contains("gloves") && !inventory.contains("watch") && !safeStash.contains("watch")) {
                System.out.println("Hint: try give gloves to rabbit.");
            } else if (!inventory.contains("watch") && !safeStash.contains("watch")) {
                System.out.println("Hint: the Rabbit seems to care about the gloves in this room.");
            }
        } else if (character.getId().equals("caterpillar")) {
            System.out.println("Hint: not every room has treasure, but talking often helps.");
        } else if (character.getId().equals("cheshire_cat")) {
            System.out.println("Hint: the three treasures are the white fan, the tea cup, and the pocket watch.");
        } else if (character.getId().equals("mad_hatter")) {
            System.out.println("Hint: the tea cup here counts as treasure.");
        } else if (character.getId().equals("queen_of_hearts")) {
            System.out.println("Hint: do not meddle with the Queen's croquet things for too long.");
        }
    }

    // Handles giving an item to a character, mainly the glove puzzle.
    private void giveItem(String objectText) {
        if (objectText.equals("")) {
            System.out.println("Give what?");
            return;
        }

        String itemText = objectText;
        String targetText = "";

        // Split commands like "give gloves to rabbit" into item and target.
        if (objectText.contains(" to ")) {
            String[] parts = objectText.split(" to ", 2);
            itemText = parts[0].trim();
            targetText = parts[1].trim();
        }

        Item item = findItemInInventory(itemText);

        if (item == null) {
            System.out.println("You are not carrying that.");
            return;
        }

        Character targetCharacter;

        // If the player just types "give gloves", default to the rabbit here.
        if (targetText.equals("")) {
            targetCharacter = findCharacterInCurrentLocation("rabbit");
        } else {
            targetCharacter = findCharacterInCurrentLocation(targetText);
        }

        if (targetCharacter == null) {
            System.out.println("There is no one here to give that to.");
            return;
        }

        // The main give puzzle in this game is returning the gloves to the rabbit.
        if (item.getId().equals("gloves") && targetCharacter.getId().equals("white_rabbit")) {
            inventory.remove(item.getId());
            item.setLocationId("used");

            if (!inventory.contains("watch") && !safeStash.contains("watch")) {
                // The watch starts hidden, then appears as the rabbit's reward.
                Item watch = getItemById("watch");

                if (watch != null) {
                    watch.setLocationId("inventory");
                    inventory.add("watch");
                }
            }

            System.out.println("The White Rabbit is grateful for the gloves and gives you his pocket watch.");
            return;
        }

        System.out.println("That does not seem useful.");
    }

    // Only a few items do something when used.
    // Most items are just there as treasures, puzzle items, or room details.
    private void useItem(String objectText) {
        if (objectText.equals("")) {
            System.out.println("Use what?");
            return;
        }

        Item item = findItemInInventory(objectText);

        if (item == null) {
            System.out.println("You need to be carrying that first.");
            return;
        }

        if (item.getId().equals("fan")) {
            System.out.println("You wave the white fan. It is light, delicate, and clearly valuable.");
            return;
        }

        if (item.getId().equals("drink_bottle")) {
            // This keeps the famous Wonderland item in the game without adding more rules.
            System.out.println("The bottle says DRINK ME, but you decide not to risk it right now.");
            return;
        }

        if (item.getId().equals("cake")) {
            // Same idea here: it adds Wonderland flavor without extra mechanics.
            System.out.println("The cake looks tempting, but you leave it alone for now.");
            return;
        }

        if (item.getId().equals("flamingo") && player.getCurrentLocationId().equals("queen_garden")) {
            // This is the main lose condition in the current version of the game.
            System.out.println("You lift the flamingo like a croquet mallet.");
            System.out.println("The Queen sees this at once and screams, Off with your head!");
            System.out.println("You lose.");
            gameOver = true;
            return;
        }

        System.out.println("Nothing important happens.");
    }
    // Stores an item safely in Wonder Hall.
    private void stashItem(String objectText) {
        // Stashing is only allowed in the safe room.
        if (!player.getCurrentLocationId().equals("wonder_hall")) {
            System.out.println("You can only stash treasure in Wonder Hall.");
            return;
        }

        if (objectText.equals("")) {
            System.out.println("Stash what?");
            return;
        }

        Item item = findItemInInventory(objectText);

        if (item == null) {
            System.out.println("You are not carrying that.");
            return;
        }

        // Move the item out of inventory and into the safe stash.
        inventory.remove(item.getId());
        safeStash.add(item.getId());
        item.setLocationId("stash");

        if (item.isTreasure()) {
            // Treasure is what counts toward the win condition.
            System.out.println("You stash the " + item.getName() + " safely.");
            System.out.println("Treasure progress: " + countTreasureInStash() + " of 3 treasures stashed.");
        } else {
            System.out.println("You store the " + item.getName() + ", but only treasure items count toward winning.");
        }

        checkVictory();
    }

    // Ends the game if all treasure has been stashed.
    private void checkVictory() {
        if (countTreasureInStash() >= 3) {
            // Once all three treasures are stored, the game ends in a win.
            System.out.println();
            System.out.println("You have safely stashed all three treasures in Wonder Hall.");
            System.out.println("You win!");
            gameOver = true;
        }
    }

    // Shows the player's turn count, carried items, and stash progress.
    private void showInventory() {
        // Start with the main player status.
        System.out.println("Turns: " + player.getTurns());
        System.out.println("Treasure progress: " + countTreasureInStash() + " of 3 treasures stashed.");

        // Print the carried item names instead of just their ids.
        if (inventory.isEmpty()) {
            System.out.println("You are carrying nothing.");
        } else {
            ArrayList<String> names = new ArrayList<String>();

            for (int i = 0; i < inventory.getItemIds().size(); i++) {
                Item item = getItemById(inventory.getItemIds().get(i));

                if (item != null) {
                    names.add(item.getName());
                }
            }

            System.out.println("You are carrying: " + makeListText(names));
        }

        // Also show what has already been stored safely.
        if (!safeStash.isEmpty()) {
            ArrayList<String> names = new ArrayList<String>();

            for (int i = 0; i < safeStash.getItemIds().size(); i++) {
                Item item = getItemById(safeStash.getItemIds().get(i));

                if (item != null) {
                    names.add(item.getName());
                }
            }

            System.out.println("Already stashed: " + makeListText(names));
        }

        System.out.println("Needed treasures: white fan, tea cup, and pocket watch.");
    }

    // Prints the title and basic instructions at the start of the game.
    private void showWelcome() {
        System.out.println("Wonderland Game");
        System.out.println("By Sarthak Bhot");
        System.out.println("----------------");
        System.out.println("Goal: Find the white fan, tea cup, and pocket watch.");
        System.out.println("Safe Room: Bring them to Wonder Hall and type stash <item>.");
        System.out.println("Movement: north, south, east, west, down");
        System.out.println("Shortcuts: n, s, e, w, d");
        System.out.println("Commands: look, take, drop, talk, give, use, stash, inventory, help, quit");
    }

    // Prints a reminder of the goal and a few useful commands.
    private void showHelp() {
        // Print the goal first, then show commands based on the current room.
        System.out.println("How to play:");
        System.out.println("1. Explore rooms and collect the three treasure items.");
        System.out.println("2. Return to Wonder Hall with each treasure.");
        System.out.println("3. Type stash <item> in Wonder Hall to store it.");
        System.out.println("4. Talk to characters for hints.");
        System.out.println("5. In the Rabbit House, give gloves to rabbit to earn the pocket watch.");
        System.out.println("6. Some special items can be used with the command use <item>.");
        System.out.println();
        System.out.println("You are currently in " + getCurrentLocation().getName() + ".");
        showExitsHere();

        ArrayList<Item> visibleItems = getVisibleItemsInCurrentLocation();
        if (!visibleItems.isEmpty()) {
            System.out.println("Try: take " + visibleItems.get(0).getName().toLowerCase() + ".");
        }

        ArrayList<Character> visibleCharacters = getCharactersInCurrentLocation();
        if (!visibleCharacters.isEmpty()) {
            System.out.println("Try: talk " + visibleCharacters.get(0).getName().toLowerCase() + ".");
        }

        if (player.getCurrentLocationId().equals("wonder_hall")) {
            System.out.println("Since you are in Wonder Hall, you can also type stash <item>.");
        }
    }

    // Returns the full Location object for the player's current room.
    private Location getCurrentLocation() {
        return getLocationById(player.getCurrentLocationId());
    }

    // Looks through the location list and finds a room by its id.
    private Location getLocationById(String id) {
        for (int i = 0; i < locations.size(); i++) {
            if (locations.get(i).getId().equals(id)) {
                return locations.get(i);
            }
        }

        return null;
    }

    // Looks through the item list and finds an item by its id.
    private Item getItemById(String id) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(id)) {
                return items.get(i);
            }
        }

        return null;
    }

    // Finds a matching item that is in the current room.
    private Item findItemInCurrentLocation(String text) {
        ArrayList<Item> visibleItems = getVisibleItemsInCurrentLocation();

        // Return the first visible item that matches the typed name.
        for (int i = 0; i < visibleItems.size(); i++) {
            if (visibleItems.get(i).matches(text)) {
                return visibleItems.get(i);
            }
        }

        return null;
    }

    // Finds a matching item that the player is carrying.
    private Item findItemInInventory(String text) {
        // Inventory stores ids, so each id must be turned back into an Item first.
        for (int i = 0; i < inventory.getItemIds().size(); i++) {
            Item item = getItemById(inventory.getItemIds().get(i));

            if (item != null && item.matches(text)) {
                return item;
            }
        }

        return null;
    }

    // Finds a matching character in the current room.
    private Character findCharacterInCurrentLocation(String text) {
        ArrayList<Character> visibleCharacters = getCharactersInCurrentLocation();

        // Return the first character whose name matches the typed text.
        for (int i = 0; i < visibleCharacters.size(); i++) {
            if (visibleCharacters.get(i).matches(text)) {
                return visibleCharacters.get(i);
            }
        }

        return null;
    }

    // Builds a list of items that are in the current room.
    private ArrayList<Item> getVisibleItemsInCurrentLocation() {
        ArrayList<Item> visibleItems = new ArrayList<Item>();

        // Items are visible when their location matches the current room.
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);

            if (item.getLocationId().equals(player.getCurrentLocationId())) {
                visibleItems.add(item);
            }
        }

        return visibleItems;
    }

    // Builds a list of characters that are in the current room.
    private ArrayList<Character> getCharactersInCurrentLocation() {
        ArrayList<Character> visibleCharacters = new ArrayList<Character>();

        // Characters are visible if their location matches the player's room.
        for (int i = 0; i < characters.size(); i++) {
            Character character = characters.get(i);

            if (character.getLocationId().equals(player.getCurrentLocationId())) {
                visibleCharacters.add(character);
            }
        }

        return visibleCharacters;
    }

    // Turns the current room exits into commands like "go east".
    private ArrayList<String> getMovementOptions() {
        ArrayList<String> options = new ArrayList<String>();
        ArrayList<Exit> exits = getCurrentLocation().getExits();

        // Turn each Exit object into a command the player can type.
        // Example result: "go east" or "go south".
        for (int i = 0; i < exits.size(); i++) {
            options.add("go " + exits.get(i).getDirection());
        }

        return options;
    }

    // Counts how many treasure items are safely stored in the stash.
    private int countTreasureInStash() {
        int count = 0;

        // Look through the stash and count only treasure items.
        for (int i = 0; i < safeStash.getItemIds().size(); i++) {
            Item item = getItemById(safeStash.getItemIds().get(i));

            if (item != null && item.isTreasure()) {
                count++;
            }
        }

        return count;
    }

    // Accepts short forms like n, s, e, w, and d.
    private String normalizeDirection(String text) {
        String direction = text.toLowerCase().trim();

        // Accept both full direction words and one-letter shortcuts.
        if (direction.equals("north") || direction.equals("n")) {
            return "north";
        }
        if (direction.equals("south") || direction.equals("s")) {
            return "south";
        }
        if (direction.equals("east") || direction.equals("e")) {
            return "east";
        }
        if (direction.equals("west") || direction.equals("w")) {
            return "west";
        }
        if (direction.equals("down") || direction.equals("d")) {
            return "down";
        }

        return null;
    }

    private Scanner openDataFile(String fileName) throws FileNotFoundException {
        // The game reads its text files from the src folder.
        // These extra paths help it still work from common IDE run locations.
        String[] paths = {
                "src/" + fileName,
                fileName
        };

        for (int i = 0; i < paths.length; i++) {
            File file = new File(paths[i]);

            if (file.exists()) {
                return new Scanner(file);
            }
        }

        throw new FileNotFoundException(fileName);
    }

    // The text files use blank lines and # comments for notes, so ignore those.
    private boolean isCommentOrBlank(String line) {
        // Ignore empty lines and comment lines that start with #.
        return line.equals("") || line.startsWith("#");
    }

    // This is only for command shortcuts in actions.txt, like n or i.
    private ArrayList<String> makeShortcutsFromText(String text) {
        ArrayList<String> shortcuts = new ArrayList<String>();

        if (text.trim().equals("")) {
            return shortcuts;
        }

        // Split the shortcut text at commas.
        String[] parts = text.split(",");

        // Add each cleaned shortcut to the list.
        for (int i = 0; i < parts.length; i++) {
            shortcuts.add(parts[i].trim());
        }

        return shortcuts;
    }

    // This keeps printed lists easy to read, like "fan, cup, and watch".
    private String makeListText(ArrayList<String> names) {
        if (names.size() == 0) {
            return "";
        }

        if (names.size() == 1) {
            return names.get(0);
        }

        if (names.size() == 2) {
            return names.get(0) + " and " + names.get(1);
        }

        String result = "";

        // For longer lists, build text like "fan, cup, and watch".
        for (int i = 0; i < names.size(); i++) {
            if (i == names.size() - 1) {
                result = result + "and " + names.get(i);
            } else {
                result = result + names.get(i) + ", ";
            }
        }

        return result;
    }
}
