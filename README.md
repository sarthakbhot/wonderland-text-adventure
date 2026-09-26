# Wonderland — Text Adventure Game

A console-based text adventure game written in Java, set in Alice's Wonderland. Explore interconnected locations, collect hidden treasures, chat with classic characters, and stash your loot in the safe room.

## Gameplay

- **6 explorable locations** — Rabbit Hole Entrance, Wonder Hall (your safe room), the Rabbit House, Mushroom Grove, the Tea Party Table, and the Queen's Garden
- **Command parser with shortcuts** — type natural commands like `go north`, `take golden key`, `talk mad hatter`, or use shortcuts (`n`, `l`, `i`, `get`, …). Type `help` in-game for the full list
- **NPCs with dialogue** — White Rabbit, Caterpillar, Cheshire Cat, Mad Hatter, and the Queen of Hearts
- **Inventory + stash system** — carry items with you, or store treasures safely in Wonder Hall
- **Treasure hunt** — track down the hidden treasures scattered across Wonderland
- **Data-driven world** — locations, items, characters, and actions all load from plain text files, so the game world can be edited without touching any code

## Project structure

```
src/    Java source files and the game's data files (locations, items, characters, actions)
docs/   Final project reflection and test plan
```

## How to run

Requires Java 8 or newer. From the project root:

```bash
javac -d out src/*.java
java -cp out WonderlandGame
```

## Built with

Java — no external libraries or dependencies.
