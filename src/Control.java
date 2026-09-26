/**
 * Name: Sarthak Bhot
 * Student ID: B36069375
 * Date: April 22, 2026
 * File: Control.java
 * Purpose: Reads player input, cleans the text, and separates
 * the action word from the object text.
 */
import java.util.ArrayList;
import java.util.Scanner;

public class Control {

    // Scanner for reading keyboard input
    private Scanner scanner;

    public Control() {
        // One Scanner is enough for the whole game.
        scanner = new Scanner(System.in);
    }

    public String getInput() {
        System.out.print("> ");
        // If input ends, return quit so the game can close cleanly.
        if (!scanner.hasNextLine()) {
            return "quit";
        }
        // Read the full line so multi-word commands still work.
        return scanner.nextLine();
    }

    // Uses the first word of the input to find the matching action.
    public Action findAction(String input, ArrayList<Action> actions) {
        String cleanedInput = normalizeInput(input);

        // Split the input into words
        String[] parts = cleanedInput.split(" ");

        // If the player typed nothing, return null
        if (parts.length == 0 || parts[0].equals("")) {
            return null;
        }

        String firstWord = parts[0];

        // Look through all actions and find a match
        for (int i = 0; i < actions.size(); i++) {
            Action action = actions.get(i);

            if (action.matches(firstWord)) {
                return action;
            }
        }

        return null;
    }

    // Returns everything after the action word.
    public String getObjectText(String input) {
        String cleanedInput = normalizeInput(input);

        int firstSpace = cleanedInput.indexOf(" ");

        // If there is no second word, return empty text
        if (firstSpace == -1) {
            return "";
        }

        String objectText = cleanedInput.substring(firstSpace + 1).trim();

        // Remove small filler words so commands like
        // "talk to rabbit" and "take the key" still work.
        boolean changed = true;

        // Keep checking until no filler word is left at the start.
        while (changed) {
            changed = false;

            if (objectText.startsWith("to ")) {
                objectText = objectText.substring(3).trim();
                changed = true;
            } else if (objectText.startsWith("the ")) {
                objectText = objectText.substring(4).trim();
                changed = true;
            } else if (objectText.startsWith("a ")) {
                objectText = objectText.substring(2).trim();
                changed = true;
            } else if (objectText.startsWith("an ")) {
                objectText = objectText.substring(3).trim();
                changed = true;
            } else if (objectText.startsWith("at ")) {
                objectText = objectText.substring(3).trim();
                changed = true;
            }
        }

        // Return the cleaned object text for the game to use.
        return objectText;
    }

    // Lowercases the input and removes punctuation so comparisons are easier.
    private String normalizeInput(String input) {
        String cleanedInput = input.toLowerCase();

        // Turn punctuation into spaces so words still split cleanly.
        cleanedInput = cleanedInput.replace(".", " ");
        cleanedInput = cleanedInput.replace(",", " ");
        cleanedInput = cleanedInput.replace("!", " ");
        cleanedInput = cleanedInput.replace("?", " ");
        cleanedInput = cleanedInput.replace(";", " ");
        cleanedInput = cleanedInput.replace(":", " ");
        cleanedInput = cleanedInput.replace("\"", " ");
        cleanedInput = cleanedInput.replace("'", " ");
        cleanedInput = cleanedInput.replace("(", " ");
        cleanedInput = cleanedInput.replace(")", " ");

        // Remove any doubled spaces created by the replacements above.
        while (cleanedInput.contains("  ")) {
            cleanedInput = cleanedInput.replace("  ", " ");
        }

        return cleanedInput.trim();
    }
}
