# Text Adventure Game Engine

A flexible, data-driven text adventure game engine written in Java. This engine allows you to create complex interactive fiction games where all game content is defined in external JSON files, making it easy to create new adventures without modifying the code.

## Features

- **Data-Driven Design**: All game content (rooms, items, NPCs, puzzles) is loaded from a `world.json` file.
- **Rich Game Elements**:
  - **Rooms** with dynamic descriptions, items, NPCs, and exits
  - **Items** that can be taken, used, examined, and combined
  - **NPCs** with branching dialogue trees and state tracking
  - **Puzzles** with complex solving conditions and dynamic effects
  - **Game End Conditions** for defining win/lose scenarios
- **Save/Load System**: Save your progress and restore it later
- **Natural Command Parser**: Handles commands like "use torch on brazier" or "talk to ghost"

## Requirements

- Java 11 or higher
- Maven for building (optional)

## Building and Running

### Using Maven

```bash
# Build the project
mvn clean package

# Run the game
java -jar target/text-adventure-engine-1.0-SNAPSHOT-jar-with-dependencies.jar
```

### Without Maven

```bash
# Compile
javac -cp . -d target src/local/pphilfre/*.java src/local/pphilfre/datamodel/*.java src/local/pphilfre/utils/*.java

# Run
java -cp target:libs/* local.pphilfre.AdventureGame
```

## Game Structure

The game is defined in a `world.json` file with the following structure:

- **gameInfo**: Title, version, welcome message, help text
- **playerStart**: Starting room ID and initial inventory
- **rooms**: Map of room IDs to Room objects
- **items**: Map of item IDs to Item objects
- **npcs**: Map of NPC IDs to NpcDefinition objects
- **puzzles**: Map of puzzle IDs to Puzzle objects
- **endConditions**: List of conditions that end the game
- **globalFlags**: Map of global flag names to values

## Extending the Game

To create your own adventure:

1. Study the example `world.json` file to understand the structure
2. Create a new JSON file with your own game content
3. Run the game with your custom file: `java -jar game.jar your_world.json`

## Example Commands

- `go north` - Move to the north
- `take key` - Pick up a key
- `examine scroll` - Look closely at a scroll
- `use torch on brazier` - Use one object on another
- `talk to ghost` - Start a conversation with an NPC
- `combine rope and hook` - Combine two items
- `inventory` - Show your inventory
- `save mygame` - Save your progress
- `load mygame` - Restore a saved game

## License

This project is open source and available for anyone to use and modify.