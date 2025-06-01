package local.pphilfre.datamodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Root object for the game definition, containing all static game data.
 */
public class GameDefinition {
    private GameInfo gameInfo;
    private PlayerStart playerStart;
    private Map<String, Room> rooms;
    private Map<String, Item> items;
    private Map<String, NpcDefinition> npcs;
    private Map<String, Puzzle> puzzles;
    private List<EndCondition> endConditions;
    private Map<String, String> globalFlags;

    public GameDefinition() {
        // Default constructor for Jackson
        this.rooms = new HashMap<>();
        this.items = new HashMap<>();
        this.npcs = new HashMap<>();
        this.puzzles = new HashMap<>();
        this.endConditions = new ArrayList<>();
        this.globalFlags = new HashMap<>();
    }

    @JsonProperty("gameInfo")
    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    @JsonProperty("playerStart")
    public PlayerStart getPlayerStart() {
        return playerStart;
    }

    public void setPlayerStart(PlayerStart playerStart) {
        this.playerStart = playerStart;
    }

    @JsonProperty("rooms")
    public Map<String, Room> getRooms() {
        return rooms;
    }

    public void setRooms(Map<String, Room> rooms) {
        this.rooms = rooms != null ? rooms : new HashMap<>();
    }

    @JsonProperty("items")
    public Map<String, Item> getItems() {
        return items;
    }

    public void setItems(Map<String, Item> items) {
        this.items = items != null ? items : new HashMap<>();
    }

    @JsonProperty("npcs")
    public Map<String, NpcDefinition> getNpcs() {
        return npcs;
    }

    public void setNpcs(Map<String, NpcDefinition> npcs) {
        this.npcs = npcs != null ? npcs : new HashMap<>();
    }

    @JsonProperty("puzzles")
    public Map<String, Puzzle> getPuzzles() {
        return puzzles;
    }

    public void setPuzzles(Map<String, Puzzle> puzzles) {
        this.puzzles = puzzles != null ? puzzles : new HashMap<>();
    }

    @JsonProperty("endConditions")
    public List<EndCondition> getEndConditions() {
        return endConditions;
    }

    public void setEndConditions(List<EndCondition> endConditions) {
        this.endConditions = endConditions != null ? endConditions : new ArrayList<>();
    }

    @JsonProperty("globalFlags")
    public Map<String, String> getGlobalFlags() {
        return globalFlags;
    }

    public void setGlobalFlags(Map<String, String> globalFlags) {
        this.globalFlags = globalFlags != null ? globalFlags : new HashMap<>();
    }

    /**
     * Validates the game definition to ensure all referenced IDs exist.
     * 
     * @return A list of validation errors, empty if the definition is valid
     */
    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        
        // Check that player start room exists
        if (playerStart != null && playerStart.getStartRoomId() != null) {
            if (!rooms.containsKey(playerStart.getStartRoomId())) {
                errors.add("Player start room ID '" + playerStart.getStartRoomId() + "' does not exist.");
            }
            
            // Check that all initial inventory items exist
            for (String itemId : playerStart.getInitialInventory()) {
                if (!items.containsKey(itemId)) {
                    errors.add("Initial inventory item ID '" + itemId + "' does not exist.");
                }
            }
        } else {
            errors.add("Player start is not defined properly.");
        }
        
        // Check room references
        for (Map.Entry<String, Room> roomEntry : rooms.entrySet()) {
            Room room = roomEntry.getValue();
            
            // Check item references
            for (String itemId : room.getItemIds()) {
                if (!items.containsKey(itemId)) {
                    errors.add("Room '" + room.getId() + "' references non-existent item '" + itemId + "'.");
                }
            }
            
            // Check NPC references
            for (String npcId : room.getNpcIds()) {
                if (!npcs.containsKey(npcId)) {
                    errors.add("Room '" + room.getId() + "' references non-existent NPC '" + npcId + "'.");
                }
            }
            
            // Check exit references
            for (Map.Entry<String, Exit> exitEntry : room.getExits().entrySet()) {
                Exit exit = exitEntry.getValue();
                if (!rooms.containsKey(exit.getTargetRoomId())) {
                    errors.add("Exit from room '" + room.getId() + "' to '" + exit.getTargetRoomId() + "' references a non-existent room.");
                }
                
                if (exit.getRequiredItemIdToUnlock() != null && !items.containsKey(exit.getRequiredItemIdToUnlock())) {
                    errors.add("Exit from room '" + room.getId() + "' requires a non-existent item '" + exit.getRequiredItemIdToUnlock() + "'.");
                }
                
                if (exit.getRequiredPuzzleIdSolved() != null && !puzzles.containsKey(exit.getRequiredPuzzleIdSolved())) {
                    errors.add("Exit from room '" + room.getId() + "' requires a non-existent puzzle '" + exit.getRequiredPuzzleIdSolved() + "'.");
                }
            }
        }
        
        // Additional validations could be added here for puzzles, items, NPCs, etc.
        
        return errors;
    }
}
