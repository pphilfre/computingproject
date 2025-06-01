package local.pphilfre;

import local.pphilfre.datamodel.*;
import local.pphilfre.utils.JsonUtil;
import local.pphilfre.CommandParser.ParsedCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Controls the game logic and orchestrates interactions between player, world, and commands.
 */
public class GameController {
    private World world;
    private Player player;
    private CommandParser commandParser;
    private boolean gameOver;
    private GameDefinition gameDefinition;

    /**
     * Creates a new game controller.
     */
    public GameController() {
        this.commandParser = new CommandParser();
        this.gameOver = false;
    }

    /**
     * Starts the game by loading the world definition and initializing the game state.
     *
     * @param worldFilePath Path to the world.json file
     * @throws IOException If there's an error loading the world file
     */
    public void startGame(String worldFilePath) throws IOException {
        // Load the game definition
        gameDefinition = JsonUtil.loadGameDefinition(worldFilePath, GameDefinition.class);
        
        // Validate the game definition
        List<String> validationErrors = gameDefinition.validate();
        if (!validationErrors.isEmpty()) {
            System.out.println("WARNING: The game definition has the following issues:");
            for (String error : validationErrors) {
                System.out.println("- " + error);
            }
            System.out.println();
        }
        
        // Create the world and player
        world = new World(gameDefinition);
        PlayerStart playerStart = gameDefinition.getPlayerStart();
        player = new Player(playerStart.getStartRoomId(), playerStart.getInitialInventory(), world);
        
        // Display welcome message and initial room
        GameInfo gameInfo = gameDefinition.getGameInfo();
        System.out.println();
        System.out.println("========================================");
        System.out.println(gameInfo.getGameTitle() + " v" + gameInfo.getVersion());
        System.out.println("========================================");
        System.out.println();
        System.out.println(gameInfo.getWelcomeMessage());
        System.out.println();
        
        displayCurrentRoom();
    }

    /**
     * Checks if the game is over.
     *
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Processes a command from the player.
     *
     * @param rawInput The raw input string
     */
    public void processCommand(String rawInput) {
        if (rawInput == null || rawInput.trim().isEmpty()) {
            System.out.println("Please enter a command.");
            return;
        }
        
        ParsedCommand parsedCommand = commandParser.parse(rawInput);
        String verb = parsedCommand.getVerb();
        
        switch (verb) {
            case "go":
            case "move":
            case "walk":
                handleGo(parsedCommand);
                break;
                
            case "take":
            case "get":
            case "grab":
            case "pick":
                handleTake(parsedCommand);
                break;
                
            case "drop":
                handleDrop(parsedCommand);
                break;
                
            case "use":
                handleUse(parsedCommand);
                break;
                
            case "talk":
                handleTalk(parsedCommand);
                break;
                
            case "combine":
                handleCombine(parsedCommand);
                break;
                
            case "examine":
            case "look":
            case "inspect":
                if (parsedCommand.getDirectObject().isEmpty()) {
                    handleLook();
                } else {
                    handleExamine(parsedCommand);
                }
                break;
                
            case "inventory":
            case "i":
                handleInventory();
                break;
                
            case "save":
                handleSave(parsedCommand.getDirectObject());
                break;
                
            case "load":
                handleLoad(parsedCommand.getDirectObject());
                break;
                
            case "help":
            case "?":
                handleHelp();
                break;
                
            case "quit":
            case "exit":
                handleQuit();
                break;
                
            default:
                System.out.println("I don't understand that command. Type 'help' for a list of commands.");
        }
        
        checkGameEndConditions();
    }

    /**
     * Handles the 'go' command.
     *
     * @param cmd The parsed command
     */
    private void handleGo(ParsedCommand cmd) {
        String direction = cmd.getDirectObject();
        if (direction.isEmpty()) {
            System.out.println("Go where? Please specify a direction.");
            return;
        }
        
        Room currentRoom = world.getRoom(player.getCurrentRoomId());
        if (currentRoom == null) {
            System.out.println("Error: Current room not found.");
            return;
        }
        
        Map<String, Exit> exits = currentRoom.getExits();
        Exit exit = exits.get(direction);
        
        if (exit == null) {
            System.out.println("There is no exit in that direction.");
            return;
        }
        
        if (!exit.canPlayerPass(player, world)) {
            if (exit.getLockedMessage() != null && !exit.getLockedMessage().isEmpty()) {
                System.out.println(exit.getLockedMessage());
            } else {
                System.out.println("You can't go that way.");
            }
            return;
        }
        
        // Move to the new room
        player.setCurrentRoomId(exit.getTargetRoomId());
        
        if (exit.getUnlockedMessage() != null && !exit.getUnlockedMessage().isEmpty()) {
            System.out.println(exit.getUnlockedMessage());
        }
        
        displayCurrentRoom();
    }

    /**
     * Handles the 'take' command.
     *
     * @param cmd The parsed command
     */
    private void handleTake(ParsedCommand cmd) {
        String itemName = cmd.getDirectObject();
        if (itemName.isEmpty()) {
            System.out.println("Take what? Please specify an item.");
            return;
        }
        
        String itemId = world.getItemInRoomByName(itemName, player.getCurrentRoomId());
        if (itemId == null) {
            System.out.println("You don't see that here.");
            return;
        }
        
        Item item = world.getItemDefinition(itemId);
        if (!item.isTakeable()) {
            System.out.println("You can't take that.");
            return;
        }
        
        world.removeItemFromRoom(player.getCurrentRoomId(), itemId);
        player.addItem(itemId);
        
        System.out.println("You take the " + item.getName() + ".");
    }

    /**
     * Handles the 'drop' command.
     *
     * @param cmd The parsed command
     */
    private void handleDrop(ParsedCommand cmd) {
        String itemName = cmd.getDirectObject();
        if (itemName.isEmpty()) {
            System.out.println("Drop what? Please specify an item.");
            return;
        }
        
        String itemId = player.getItemFromInventoryByName(itemName);
        if (itemId == null) {
            System.out.println("You don't have that.");
            return;
        }
        
        player.removeItem(itemId);
        world.addItemToRoom(player.getCurrentRoomId(), itemId);
        
        Item item = world.getItemDefinition(itemId);
        System.out.println("You drop the " + item.getName() + ".");
    }

    /**
     * Handles the 'use' command.
     *
     * @param cmd The parsed command
     */
    private void handleUse(ParsedCommand cmd) {
        String itemToUseName = cmd.getDirectObject();
        String targetName = cmd.getIndirectObject();
        
        if (itemToUseName.isEmpty()) {
            System.out.println("Use what? Please specify an item.");
            return;
        }
        
        // Check if the player has the item
        String itemId = player.getItemFromInventoryByName(itemToUseName);
        if (itemId == null) {
            System.out.println("You don't have that.");
            return;
        }
        
        Item item = world.getItemDefinition(itemId);
        
        // If no target specified, try using the item by itself
        if (targetName.isEmpty()) {
            UseEffect selfUseEffect = item.getUseEffectForTarget("self", itemId);
            if (selfUseEffect != null) {
                processPuzzleTrigger(selfUseEffect.getTriggersPuzzleId(), itemId, "self");
                return;
            }
            
            // No self-use effect found
            System.out.println("You need to specify what to use the " + item.getName() + " on.");
            return;
        }
        
        // Try to find the target in the room
        String targetItemId = world.getItemInRoomByName(targetName, player.getCurrentRoomId());
        if (targetItemId != null) {
            // Found a target item
            UseEffect useEffect = item.getUseEffectForTarget("item", targetItemId);
            if (useEffect != null) {
                // Item has a specific effect on this target
                if (useEffect.getTriggersPuzzleId() != null) {
                    processPuzzleTrigger(useEffect.getTriggersPuzzleId(), itemId, targetItemId);
                } else if (useEffect.getSuccessMessage() != null) {
                    System.out.println(useEffect.getSuccessMessage());
                    
                    if (useEffect.isConsumesItem()) {
                        player.removeItem(itemId);
                        System.out.println("The " + item.getName() + " is consumed.");
                    }
                    
                    if (useEffect.getSetsFlagName() != null) {
                        world.setGlobalFlag(useEffect.getSetsFlagName(), useEffect.getSetsFlagValue());
                    }
                } else {
                    System.out.println("You use the " + item.getName() + " on the " + 
                                      world.getItemDefinition(targetItemId).getName() + " but nothing happens.");
                }
                return;
            }
        }
        
        // Try to find an NPC as the target
        NpcInstance targetNpc = world.getNpcInstanceInRoomByName(targetName, player.getCurrentRoomId());
        if (targetNpc != null) {
            UseEffect useEffect = item.getUseEffectForTarget("npc", targetNpc.getDefinitionId());
            if (useEffect != null) {
                if (useEffect.getTriggersPuzzleId() != null) {
                    processPuzzleTrigger(useEffect.getTriggersPuzzleId(), itemId, targetNpc.getDefinitionId());
                } else if (useEffect.getSuccessMessage() != null) {
                    System.out.println(useEffect.getSuccessMessage());
                    
                    if (useEffect.isConsumesItem()) {
                        player.removeItem(itemId);
                    }
                    
                    if (useEffect.getSetsFlagName() != null) {
                        world.setGlobalFlag(useEffect.getSetsFlagName(), useEffect.getSetsFlagValue());
                    }
                } else {
                    System.out.println("You use the " + item.getName() + " on " + 
                                      world.getNpcDefinition(targetNpc.getDefinitionId()).getName() + 
                                      " but nothing happens.");
                }
                return;
            }
        }
        
        // Check for room features as targets
        for (Map.Entry<String, Puzzle> puzzleEntry : world.getGameDefinition().getPuzzles().entrySet()) {
            Puzzle puzzle = puzzleEntry.getValue();
            PuzzleSolutionCondition condition = puzzle.getSolutionCondition();
            
            if (condition != null && 
                condition.getRequiredItemId() != null && condition.getRequiredItemId().equals(itemId) &&
                condition.getRequiredTargetId() != null && 
                targetName.toLowerCase().contains(condition.getRequiredTargetId().toLowerCase())) {
                
                processPuzzleTrigger(puzzleEntry.getKey(), itemId, condition.getRequiredTargetId());
                return;
            }
        }
        
        // No valid target found or no effect
        System.out.println("You can't use the " + item.getName() + " on that.");
    }

    /**
     * Processes a puzzle trigger from using an item.
     *
     * @param puzzleId The ID of the puzzle to trigger
     * @param itemId The ID of the item being used
     * @param targetId The ID of the target
     */
    private void processPuzzleTrigger(String puzzleId, String itemId, String targetId) {
        if (puzzleId == null) {
            return;
        }
        
        Puzzle puzzle = world.getPuzzleDefinition(puzzleId);
        if (puzzle == null) {
            return;
        }
        
        boolean solved = puzzle.attemptSolve(player, world, itemId, targetId);
        
        if (solved) {
            // Consume the item if specified in the puzzle
            PuzzleSolutionCondition condition = puzzle.getSolutionCondition();
            if (condition != null && condition.getRequiredItemId() != null && 
                condition.getRequiredItemId().equals(itemId)) {
                
                Item item = world.getItemDefinition(itemId);
                if (item != null) {
                    UseEffect effect = item.getUseEffectForTarget("puzzle", puzzleId);
                    if (effect != null && effect.isConsumesItem()) {
                        player.removeItem(itemId);
                        System.out.println("The " + item.getName() + " is consumed.");
                    }
                }
            }
        }
    }

    /**
     * Handles the 'talk' command.
     *
     * @param cmd The parsed command
     */
    private void handleTalk(ParsedCommand cmd) {
        String npcName = cmd.getDirectObject();
        if (npcName.isEmpty()) {
            System.out.println("Talk to whom? Please specify an NPC.");
            return;
        }
        
        NpcInstance npc = world.getNpcInstanceInRoomByName(npcName, player.getCurrentRoomId());
        if (npc == null) {
            System.out.println("There's no one like that here to talk to.");
            return;
        }
        
        NpcDefinition npcDef = world.getNpcDefinition(npc.getDefinitionId());
        if (npcDef == null) {
            System.out.println("Error: NPC definition not found.");
            return;
        }
        
        // Start dialogue loop
        Scanner scanner = new Scanner(System.in);
        String currentNodeId = npc.getCurrentDialogueNodeId();
        
        while (true) {
            DialogueNode node = npcDef.getDialogueTree().get(currentNodeId);
            if (node == null) {
                System.out.println("Error: Dialogue node not found.");
                break;
            }
            
            // Display NPC's dialogue
            System.out.println("\n" + npcDef.getName() + ": " + node.getText());
            
            // Handle item giving
            if (node.getGivesItemId() != null) {
                Item item = world.getItemDefinition(node.getGivesItemId());
                if (item != null) {
                    player.addItem(node.getGivesItemId());
                    System.out.println("\nYou received: " + item.getName());
                }
            }
            
            // Handle flag setting
            if (node.getSetsNpcFlag() != null) {
                String[] parts = node.getSetsNpcFlag().split("=", 2);
                if (parts.length == 2) {
                    npc.setNpcFlag(parts[0], parts[1]);
                }
            }
            
            if (node.getSetsGlobalFlag() != null) {
                String[] parts = node.getSetsGlobalFlag().split("=", 2);
                if (parts.length == 2) {
                    world.setGlobalFlag(parts[0], parts[1]);
                }
            }
            
            // End dialogue if marked as such
            if (node.isEndsDialogue() || node.getResponses().isEmpty()) {
                break;
            }
            
            // Filter valid responses
            List<DialogueResponseOption> validResponses = new ArrayList<>();
            for (DialogueResponseOption response : node.getResponses()) {
                boolean isValid = true;
                
                if (response.getRequiresPlayerItem() != null && !player.hasItem(response.getRequiresPlayerItem())) {
                    isValid = false;
                }
                
                if (response.getRequiresNpcFlag() != null) {
                    String[] parts = response.getRequiresNpcFlag().split("=", 2);
                    if (parts.length == 2) {
                        String actualValue = npc.getNpcFlag(parts[0]);
                        if (actualValue == null || !actualValue.equals(parts[1])) {
                            isValid = false;
                        }
                    }
                }
                
                if (response.getRequiresGlobalFlag() != null) {
                    String[] parts = response.getRequiresGlobalFlag().split("=", 2);
                    if (parts.length == 2) {
                        String actualValue = world.getGlobalFlagValue(parts[0]);
                        if (actualValue == null || !actualValue.equals(parts[1])) {
                            isValid = false;
                        }
                    }
                }
                
                if (isValid) {
                    validResponses.add(response);
                }
            }
            
            if (validResponses.isEmpty()) {
                System.out.println("\nThe conversation ends.");
                break;
            }
            
            // Display valid responses
            System.out.println("\nYour responses:");
            for (int i = 0; i < validResponses.size(); i++) {
                System.out.println((i + 1) + ". " + validResponses.get(i).getText());
            }
            System.out.println("0. End conversation");
            
            // Get player's choice
            int choice = -1;
            while (choice < 0 || choice > validResponses.size()) {
                System.out.print("\n> ");
                try {
                    String input = scanner.nextLine().trim();
                    if (input.equals("0")) {
                        choice = 0;
                        break;
                    }
                    choice = Integer.parseInt(input);
                    if (choice < 1 || choice > validResponses.size()) {
                        System.out.println("Please enter a valid option number (1-" + validResponses.size() + " or 0 to end).");
                        choice = -1;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a number.");
                }
            }
            
            if (choice == 0) {
                System.out.println("\nYou end the conversation.");
                break;
            }
            
            // Update dialogue node
            currentNodeId = validResponses.get(choice - 1).getTargetNodeId();
            npc.setCurrentDialogueNodeId(currentNodeId);
        }
    }

    /**
     * Handles the 'combine' command.
     *
     * @param cmd The parsed command
     */
    private void handleCombine(ParsedCommand cmd) {
        String combinationStr = cmd.getDirectObject();
        
        if (combinationStr.isEmpty()) {
            System.out.println("Combine what? Please specify the items to combine.");
            return;
        }
        
        // Parse the combination string to extract two item names
        String[] parts = combinationStr.split(" and | with | using ");
        if (parts.length < 2) {
            System.out.println("Please specify two items to combine (e.g., 'combine X and Y').");
            return;
        }
        
        String item1Name = parts[0].trim();
        String item2Name = parts[1].trim();
        
        // Get the items from the player's inventory
        String item1Id = player.getItemFromInventoryByName(item1Name);
        String item2Id = player.getItemFromInventoryByName(item2Name);
        
        if (item1Id == null) {
            System.out.println("You don't have a " + item1Name + ".");
            return;
        }
        
        if (item2Id == null) {
            System.out.println("You don't have a " + item2Name + ".");
            return;
        }
        
        // Check if the items can be combined
        Item item1 = world.getItemDefinition(item1Id);
        Item item2 = world.getItemDefinition(item2Id);
        
        if (item1.getCanBeCombinedWith() != null && item1.getCanBeCombinedWith().contains(item2Id)) {
            // Items can be combined
            String resultItemId = item1.getCombinationResultItemId();
            
            if (resultItemId != null) {
                // Remove the original items
                player.removeItem(item1Id);
                player.removeItem(item2Id);
                
                // Add the result item
                player.addItem(resultItemId);
                
                Item resultItem = world.getItemDefinition(resultItemId);
                System.out.println("You combine the " + item1.getName() + " and the " + 
                                  item2.getName() + " to create a " + resultItem.getName() + ".");
            } else {
                System.out.println("You try to combine the items, but nothing happens.");
            }
        } else if (item2.getCanBeCombinedWith() != null && item2.getCanBeCombinedWith().contains(item1Id)) {
            // Try the other way around
            String resultItemId = item2.getCombinationResultItemId();
            
            if (resultItemId != null) {
                // Remove the original items
                player.removeItem(item1Id);
                player.removeItem(item2Id);
                
                // Add the result item
                player.addItem(resultItemId);
                
                Item resultItem = world.getItemDefinition(resultItemId);
                System.out.println("You combine the " + item1.getName() + " and the " + 
                                  item2.getName() + " to create a " + resultItem.getName() + ".");
            } else {
                System.out.println("You try to combine the items, but nothing happens.");
            }
        } else {
            System.out.println("You can't combine those items.");
        }
    }

    /**
     * Handles the 'examine' command.
     *
     * @param cmd The parsed command
     */
    private void handleExamine(ParsedCommand cmd) {
        String targetName = cmd.getDirectObject();
        if (targetName.isEmpty()) {
            System.out.println("Examine what?");
            return;
        }
        
        // First check the player's inventory
        String itemId = player.getItemFromInventoryByName(targetName);
        if (itemId != null) {
            Item item = world.getItemDefinition(itemId);
            System.out.println(item.getDescription());
            return;
        }
        
        // Then check the current room
        itemId = world.getItemInRoomByName(targetName, player.getCurrentRoomId());
        if (itemId != null) {
            Item item = world.getItemDefinition(itemId);
            System.out.println(item.getDescription());
            return;
        }
        
        // Check for NPCs
        NpcInstance npc = world.getNpcInstanceInRoomByName(targetName, player.getCurrentRoomId());
        if (npc != null) {
            NpcDefinition npcDef = world.getNpcDefinition(npc.getDefinitionId());
            System.out.println(npcDef.getPresenceDescription());
            return;
        }
        
        // Check if it's a special feature of the room
        if (targetName.equals("room") || targetName.equals("area") || targetName.equals("here")) {
            displayCurrentRoom();
            return;
        }
        
        System.out.println("You don't see anything like that here.");
    }

    /**
     * Handles the 'inventory' command.
     */
    private void handleInventory() {
        player.displayInventory();
    }

    /**
     * Handles the 'look' command.
     */
    private void handleLook() {
        displayCurrentRoom();
    }

    /**
     * Handles the 'save' command.
     *
     * @param saveName The name of the save file
     */
    private void handleSave(String saveName) {
        if (saveName.isEmpty()) {
            saveName = "default";
        }
        
        String fileName = "saves/" + saveName + ".json";
        
        try {
            PlayerState playerState = player.getState();
            WorldDynamicState worldState = world.getDynamicState();
            
            GameState gameState = new GameState(
                playerState,
                worldState,
                gameDefinition.getGameInfo().getVersion()
            );
            
            JsonUtil.saveObjectToJsonFile(gameState, fileName);
            System.out.println("Game saved successfully to " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }

    /**
     * Handles the 'load' command.
     *
     * @param saveName The name of the save file
     */
    private void handleLoad(String saveName) {
        if (saveName.isEmpty()) {
            saveName = "default";
        }
        
        String fileName = "saves/" + saveName + ".json";
        
        try {
            GameState gameState = JsonUtil.loadObjectFromJsonFile(fileName, GameState.class);
            
            // Check version compatibility
            String currentVersion = gameDefinition.getGameInfo().getVersion();
            String savedVersion = gameState.getGameVersion();
            
            if (savedVersion != null && !savedVersion.equals(currentVersion)) {
                System.out.println("Warning: Save file version (" + savedVersion + 
                                 ") differs from current version (" + currentVersion + ").");
                System.out.println("Some features may not work as expected.");
            }
            
            // Restore player state
            player.restoreState(gameState.getPlayerState());
            
            // Restore world state
            world.restoreDynamicState(gameState.getWorldDynamicState());
            
            System.out.println("Game loaded successfully from " + fileName);
            displayCurrentRoom();
        } catch (IOException e) {
            System.out.println("Error loading game: " + e.getMessage());
        }
    }

    /**
     * Handles the 'help' command.
     */
    private void handleHelp() {
        System.out.println(gameDefinition.getGameInfo().getHelpText());
    }

    /**
     * Handles the 'quit' command.
     */
    private void handleQuit() {
        System.out.println("Are you sure you want to quit? (y/n)");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim().toLowerCase();
        
        if (input.equals("y") || input.equals("yes")) {
            gameOver = true;
            System.out.println("Thanks for playing " + gameDefinition.getGameInfo().getGameTitle() + "!");
        } else {
            System.out.println("Continuing game.");
        }
    }

    /**
     * Displays the current room to the player.
     */
    private void displayCurrentRoom() {
        String roomId = player.getCurrentRoomId();
        Room room = world.getRoom(roomId);
        
        if (room == null) {
            System.out.println("Error: Current room not found.");
            return;
        }
        
        System.out.println("\n" + room.getFormattedDescription(world, player));
    }

    /**
     * Checks if any game end conditions have been met.
     */
    private void checkGameEndConditions() {
        List<EndCondition> endConditions = gameDefinition.getEndConditions();
        
        if (endConditions == null || endConditions.isEmpty()) {
            return;
        }
        
        for (EndCondition ec : endConditions) {
            if (ec.areCriteriaMet(player, world)) {
                System.out.println("\n" + ec.getMessage());
                
                if ("WIN".equalsIgnoreCase(ec.getType())) {
                    System.out.println("\nCongratulations! You have won the game!");
                } else if ("LOSE".equalsIgnoreCase(ec.getType())) {
                    System.out.println("\nGame over.");
                }
                
                gameOver = true;
                break;
            }
        }
    }
}
