package local.pphilfre.datamodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import local.pphilfre.Player;
import local.pphilfre.World;

/**
 * Represents a criterion for game end conditions.
 */
public class ConditionCriterion {
    private String type; // e.g., "PLAYER_HAS_ITEM", "PLAYER_IN_ROOM", "FLAG_SET"
    private String itemId;
    private String roomId;
    private String flagName;
    private String flagValue;

    public ConditionCriterion() {
        // Default constructor for Jackson
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("itemId")
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @JsonProperty("roomId")
    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @JsonProperty("flagName")
    public String getFlagName() {
        return flagName;
    }

    public void setFlagName(String flagName) {
        this.flagName = flagName;
    }

    @JsonProperty("flagValue")
    public String getFlagValue() {
        return flagValue;
    }

    public void setFlagValue(String flagValue) {
        this.flagValue = flagValue;
    }

    /**
     * Checks if this criterion is met.
     *
     * @param player The current player
     * @param world The game world
     * @return true if the criterion is met, false otherwise
     */
    @JsonIgnore
    public boolean isMet(Player player, World world) {
        switch (type) {
            case "PLAYER_HAS_ITEM":
                return itemId != null && player.hasItem(itemId);
                
            case "PLAYER_IN_ROOM":
                return roomId != null && roomId.equals(player.getCurrentRoomId());
                
            case "FLAG_SET":
                if (flagName != null && flagValue != null) {
                    String actualValue = world.getGlobalFlagValue(flagName);
                    return flagValue.equals(actualValue);
                }
                return false;
                
            case "PUZZLE_SOLVED":
                return itemId != null && world.isPuzzleSolved(itemId);
                
            default:
                return false;
        }
    }
}
