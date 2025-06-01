package local.pphilfre.datamodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the state of the player for saving and loading.
 */
public class PlayerState {
    private String currentRoomId;
    private List<String> inventoryItemIds;
    private Map<String, String> playerFlags;

    public PlayerState() {
        // Default constructor for Jackson
        this.inventoryItemIds = new ArrayList<>();
        this.playerFlags = new HashMap<>();
    }
    
    public PlayerState(String currentRoomId, List<String> inventoryItemIds, Map<String, String> playerFlags) {
        this.currentRoomId = currentRoomId;
        this.inventoryItemIds = inventoryItemIds != null ? new ArrayList<>(inventoryItemIds) : new ArrayList<>();
        this.playerFlags = playerFlags != null ? new HashMap<>(playerFlags) : new HashMap<>();
    }

    @JsonProperty("currentRoomId")
    public String getCurrentRoomId() {
        return currentRoomId;
    }

    public void setCurrentRoomId(String currentRoomId) {
        this.currentRoomId = currentRoomId;
    }

    @JsonProperty("inventoryItemIds")
    public List<String> getInventoryItemIds() {
        return inventoryItemIds;
    }

    public void setInventoryItemIds(List<String> inventoryItemIds) {
        this.inventoryItemIds = inventoryItemIds != null ? inventoryItemIds : new ArrayList<>();
    }

    @JsonProperty("playerFlags")
    public Map<String, String> getPlayerFlags() {
        return playerFlags;
    }

    public void setPlayerFlags(Map<String, String> playerFlags) {
        this.playerFlags = playerFlags != null ? playerFlags : new HashMap<>();
    }
}
