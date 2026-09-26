# Wonderland — Project Summary

## The game
A console-based text adventure written in Java, set in Alice's Wonderland. The player explores 6 interconnected locations (Rabbit Hole Entrance, Wonder Hall, Rabbit House, Mushroom Grove, Tea Party Table, Queen's Garden), collects items, talks to NPCs (White Rabbit, Caterpillar, Cheshire Cat, Mad Hatter, Queen of Hearts), and manages an inventory plus a stash system. The world is data-driven — locations, items, characters, and actions all load from plain text files, so the game can be re-skinned without touching code. Built with pure Java, no external dependencies.

**Win condition:** collect all three treasure items and stash them in the safe room (Wonder Hall).
**Lose condition:** take the flamingo in the Queen's Garden and use it — the Queen reacts and the game ends.

## Development reflection (from `Final_Project_Reflection.pdf`)
- The individual classes (Exit, Player, Item, Location, Inventory) were straightforward; the hard part was wiring everything together in `WonderlandGame.java` — the game loop, room changes, item interactions, file loading, and command handling.
- Biggest struggles: `ArrayList`s and turning lines from the text files into usable objects.
- Breakthrough: storyboarded the entire game on an iPad first — every room, connection, item, and room role — before writing code.
- A friend playtested an early rough version and found it confusing, which led to clearer room descriptions, hints, a help system, and an explicit explanation of the goal and controls.
- Added both a win condition (stash 3 treasures) and a lose condition (the flamingo incident) to make it feel like a complete game.
- Takeaways: object-oriented design, file I/O, debugging, testing, and thinking about the player's experience.

## Test plan (from `Final_Project_TestPlan.pdf`)
- **Win path:** player collects all three treasures and stashes them in Wonder Hall → win message prints.
- **Lose path:** player takes the flamingo in the Queen's Garden and uses it → the Queen reacts → game over.
