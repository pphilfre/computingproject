package local.pphilfre;

import local.pphilfre.datamodel.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the game world and manages its dynamic state.
 */
public class World {
    private GameDefinition gameDefinition;
    
    // Dynamic world state
    private Map<String, List<String>> currentRoomItems;  // roomId -> list of itemIds
    private Map<String, Boolean> puzzleSolvedStatus;     // puzzleId -> solved status
    private Map<String, NpcInstance> npcInstances;       // npcDefinitionId -> NpcInstance
    private Map<String, String> currentGlobalFlags;      // flagName -> value
    private Map<String, Boolean> currentExitLockedStates; // "roomId_exitDir" -> locked status

    /**
     * Creates a new game world from a game definition.
     *
     * @param gameDefinition The game definition to use
     */
    public World(GameDefinition gameDefinition) {
        this.gameDefinition = gameDefinition;
        
        // Initialize dynamic state
        initializeWorldState();
    }

    /**
     * Initializes the world state from the game definition.
     */
    private void initializeWorldState() {
        // Initialize room items from room definitions
        currentRoomItems = new HashMap<>();
        for (Map.Entry<String, Room> entry : gameDefinition.getRooms().entrySet()) {
            String roomId = entry.getKey();
            Room room = entry.getValue();
            currentRoomItems.put(roomId, new ArrayList<>(room.getItemIds()));
        }
        
        // Set all puzzles to unsolved
        puzzleSolvedStatus = new HashMap<>();
        for (String puzzleId : gameDefinition.getPuzzles().keySet()) {
            puzzleSolvedStatus.put(puzzleId, false);
        }
        
        // Create NPC instances based on NPC definitions and their initial placements
        npcInstances = new HashMap<>();
        for (Map.Entry<String, NpcDefinition> entry : gameDefinition.getNpcs().entrySet()) {
            String npcId = entry.getKey();
            NpcDefinition npcDef = entry.getValue();
            
            // Find which room this NPC starts in
            String startRoomId = null;
            for (Map.Entry<String, Room> roomEntry : gameDefinition.getRooms().entrySet()) {
                if (roomEntry.getValue().getNpcIds().contains(npcId)) {
                    startRoomId = roomEntry.getKey();
                    break;
                }
            }
            
            if (startRoomId != null) {
                NpcInstance npcInstance = new NpcInstance(
                    npcId,
                    startRoomId,
                    npcDef.getInitialDialogueNodeId(),
                    npcDef.getInitialItemIds(),
                    npcDef.getInitialNpcFlags()
                );
                npcInstances.put(npcId, npcInstance);
            }
        }
        
        // Initialize global flags from game definition
        currentGlobalFlags = new HashMap<>(gameDefinition.getGlobalFlags());
        
        // Initialize exit locked states from room definitions
        currentExitLockedStates = new HashMap<>();
        for (Map.Entry<String, Room> roomEntry : gameDefinition.getRooms().entrySet()) {
            String roomId = roomEntry.getKey();
            Room room = roomEntry.getValue();
            
            for (Map.Entry<String, Exit> exitEntry : room.getExits().entrySet()) {
                String direction = exitEntry.getKey();
                Exit exit = exitEntry.getValue();
                String exitKey = roomId + "_" + direction;
                
                currentExitLockedStates.put(exitKey, exit.isInitiallyLocked());
            }
        }
    }

    /**
     * Gets a room definition by ID.
     *
     * @param roomId The room ID
     * @return The room definition, or null if not found
     */
    public Room getRoom(String roomId) {
        return gameDefinition.getRooms().get(roomId);
    }

    /**
     * Gets an item definition by ID.
     *
     * @param itemId The item ID
     * @return The item definition, or null if not found
     */
    public Item getItemDefinition(String itemId) {
        return gameDefinition.getItems().get(itemId);
    }

    /**
     * Gets an NPC definition by ID.
     *
     * @param npcId The NPC definition ID
     * @return The NPC definition, or null if not found
     */
    public NpcDefinition getNpcDefinition(String npcId) {
        return gameDefinition.getNpcs().get(npcId);
    }

    /**
     * Gets a puzzle definition by ID.
     *
     * @param puzzleId The puzzle ID
     * @return The puzzle definition, or null if not found
     */
    public Puzzle getPuzzleDefinition(String puzzleId) {
        return gameDefinition.getPuzzles().get(puzzleId);
    }

    /**
     * Gets the list of item IDs currently in a room.
     *
     * @param roomId The room ID
     * @return The list of item IDs, or an empty list if the room doesn't exist
     */
    public List<String> getItemsInRoom(String roomId) {
        return currentRoomItems.getOrDefault(roomId, new ArrayList<>());
    }

    /**
     * Adds an item to a room.
     *
     * @param roomId The room ID
     * @param itemId The item ID
     */
    public void addItemToRoom(String roomId, String itemId) {
        List<String> items = currentRoomItems.computeIfAbsent(roomId, k -> new ArrayList<>());
        if (!items.contains(itemId)) {
            items.add(itemId);
        }
    }

    /**
     * Removes an item from a room.
     *
     * @param roomId The room ID
     * @param itemId The item ID
     * @return true if the item was removed, false otherwise
     */
    public boolean removeItemFromRoom(String roomId, String itemId) {
        List<String> items = currentRoomItems.get(roomId);
        return items != null && items.remove(itemId);
    }

    /**
     * Gets an item in a room by its name.
     *
     * @param itemName The item name
     * @param roomId The room ID
     * @return The item ID if found, null otherwise
     */
    public String getItemInRoomByName(String itemName, String roomId) {
        if (itemName == null || itemName.trim().isEmpty() || roomId == null) {
            return null;
        }
        
        String normalizedName = itemName.toLowerCase().trim();
        List<String> roomItems = getItemsInRoom(roomId);
        
        for (String itemId : roomItems) {
            Item item = getItemDefinition(itemId);
            if (item != null && item.getName().toLowerCase().contains(normalizedName)) {
                return itemId;
            }
        }
        
        return null;
    }

    /**
     * Checks if a puzzle is solved.
     *
     * @param puzzleId The puzzle ID
     * @return true if the puzzle is solved, false otherwise
     */
    public boolean isPuzzleSolved(String puzzleId) {
        return puzzleSolvedStatus.getOrDefault(puzzleId, false);
    }

    /**
     * Sets the solved status of a puzzle.
     *
     * @param puzzleId The puzzle ID
     * @param solved The solved status
     */
    public void setPuzzleSolved(String puzzleId, boolean solved) {
        puzzleSolvedStatus.put(puzzleId, solved);
        
        Puzzle puzzle = getPuzzleDefinition(puzzleId);
        if (puzzle != null) {
            puzzle.setSolved(solved);
        }
    }

    /**
     * Gets an NPC instance by its definition ID.
     *
     * @param npcDefinitionId The NPC definition ID
     * @return The NPC instance, or null if not found
     */
    public NpcInstance getNpcInstance(String npcDefinitionId) {
        return npcInstances.get(npcDefinitionId);
    }

    /**
     * Gets an NPC instance in a room by its name.
     *
     * @param npcName The NPC name
     * @param roomId The room ID
     * @return The NPC instance if found, null otherwise
     */
    public NpcInstance getNpcInstanceInRoomByName(String npcName, String roomId) {
        if (npcName == null || npcName.trim().isEmpty() || roomId == null) {
            return null;
        }
        
        String normalizedName = npcName.toLowerCase().trim();
        
        for (NpcInstance npc : npcInstances.values()) {
            if (roomId.equals(npc.getCurrentRoomId())) {
                NpcDefinition def = getNpcDefinition(npc.getDefinitionId());
                if (def != null && def.getName().toLowerCase().contains(normalizedName)) {
                    return npc;
                }
            }
        }
        
        return null;
    }

    /**
     * Gets a list of NPC instances in a room.
     *
     * @param roomId The room ID
     * @return The list of NPC instances
     */
    public List<NpcInstance> getNpcsInRoom(String roomId) {
        List<NpcInstance> result = new ArrayList<>();
        
        for (NpcInstance npc : npcInstances.values()) {
            if (roomId.equals(npc.getCurrentRoomId())) {
                result.add(npc);
            }
        }
        
        return result;
    }

    /**
     * Gets the value of a global flag.
     *
     * @param flagName The flag name
     * @return The flag value, or null if not set
     */
    public String getGlobalFlagValue(String flagName) {
        return currentGlobalFlags.get(flagName);
    }

    /**
     * Sets a global flag.
     *
     * @param flagName The flag name
     * @param value The flag value
     */
    public void setGlobalFlag(String flagName, String value) {
        if (flagName != null) {
            if (value != null) {
                currentGlobalFlags.put(flagName, value);
            } else {
                currentGlobalFlags.remove(flagName);
            }
        }
    }

    /**
     * Checks if an exit is currently locked.
     *
     * @param roomId The room ID
     * @param direction The exit direction
     * @return true if the exit is locked, false otherwise
     */
    public boolean isExitCurrentlyLocked(String roomId, String direction) {
        String exitKey = roomId + "_" + direction;
        Boolean lockedState = currentExitLockedStates.get(exitKey);
        
        if (lockedState != null) {
            return lockedState;
        } else {
            // Fall back to the initial state in the room definition
            Room room = getRoom(roomId);
            if (room != null) {
                Exit exit = room.getExits().get(direction);
                if (exit != null) {
                    return exit.isInitiallyLocked();
                }
            }
            return false;
        }
    }

    /**
     * Sets the locked state of an exit.
     *
     * @param roomId The room ID
     * @param direction The exit direction
     * @param locked The locked state
     */
    public void setExitLockedState(String roomId, String direction, boolean locked) {
        String exitKey = roomId + "_" + direction;
        currentExitLockedStates.put(exitKey, locked);
        
        // Also update the Exit object for consistency
        Room room = getRoom(roomId);
        if (room != null) {
            Exit exit = room.getExits().get(direction);
            if (exit != null) {
                exit.setCurrentLockedState(locked);
            }
        }
    }

    /**
     * Applies puzzle effects to the world.
     *
     * @param effects The puzzle effects to apply
     * @param player The current player
     */
    public void applyPuzzleEffects(List<PuzzleEffect> effects, Player player) {
        if (effects == null) return;
        
        for (PuzzleEffect effect : effects) {
            effect.apply(this, player);
        }
    }

    /**
     * Gets the current dynamic state of the world for saving.
     *
     * @return The world dynamic state
     */
    public WorldDynamicState getDynamicState() {
        WorldDynamicState state = new WorldDynamicState();
        
        state.setRoomItemStates(new HashMap<>(currentRoomItems));
        state.setPuzzleSolvedStates(new HashMap<>(puzzleSolvedStatus));
        state.setNpcInstanceStates(new HashMap<>(npcInstances));
        state.setGlobalFlagStates(new HashMap<>(currentGlobalFlags));
        state.setRoomExitLockedStates(new HashMap<>(currentExitLockedStates));
        
        return state;
    }

    /**
     * Restores the world dynamic state from a saved state.
     *
     * @param state The state to restore from
     */
    public void restoreDynamicState(WorldDynamicState state) {
        if (state == null) return;
        
        // Restore room items
        currentRoomItems = new HashMap<>(state.getRoomItemStates());
        
        // Restore puzzle solved status
        puzzleSolvedStatus = new HashMap<>(state.getPuzzleSolvedStates());
        for (Map.Entry<String, Boolean> entry : puzzleSolvedStatus.entrySet()) {
            Puzzle puzzle = getPuzzleDefinition(entry.getKey());
            if (puzzle != null) {
                puzzle.setSolved(entry.getValue());
            }
        }
        
        // Restore NPC instances
        npcInstances = new HashMap<>(state.getNpcInstanceStates());
        
        // Restore global flags
        currentGlobalFlags = new HashMap<>(state.getGlobalFlagStates());
        
        // Restore exit locked states
        currentExitLockedStates = new HashMap<>(state.getRoomExitLockedStates());
        
        // Update Exit objects for consistency
        for (Map.Entry<String, Boolean> entry : currentExitLockedStates.entrySet()) {
            String[] parts = entry.getKey().split("_", 2);
            if (parts.length == 2) {
                String roomId = parts[0];
                String direction = parts[1];
                Room room = getRoom(roomId);
                if (room != null) {
                    Exit exit = room.getExits().get(direction);
                    if (exit != null) {
                        exit.setCurrentLockedState(entry.getValue());
                    }
                }
            }
        }
    }

    /**
     * Gets the game definition.
     *
     * @return The game definition
     */
    public GameDefinition getGameDefinition() {
        return gameDefinition;
    }
}
