package local.pphilfre.datamodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.ArrayList;

/**
 * Defines the player's starting position and inventory.
 */
public class PlayerStart {
    private String startRoomId;
    private List<String> initialInventory;

    public PlayerStart() {
        // Default constructor for Jackson
        this.initialInventory = new ArrayList<>();
    }

    public PlayerStart(String startRoomId, List<String> initialInventory) {
        this.startRoomId = startRoomId;
        this.initialInventory = initialInventory != null ? initialInventory : new ArrayList<>();
    }

    @JsonProperty("startRoomId")
    public String getStartRoomId() {
        return startRoomId;
    }

    public void setStartRoomId(String startRoomId) {
        this.startRoomId = startRoomId;
    }

    @JsonProperty("initialInventory")
    public List<String> getInitialInventory() {
        return initialInventory;
    }

    public void setInitialInventory(List<String> initialInventory) {
        this.initialInventory = initialInventory != null ? initialInventory : new ArrayList<>();
    }
}
