package local.pphilfre.datamodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import local.pphilfre.Player;
import local.pphilfre.World;

/**
 * Defines the conditions required to solve a puzzle.
 */
public class PuzzleSolutionCondition {
    private String type; // e.g., "ITEM_USED_ON_TARGET", "NPC_STATE_REACHED"
    private String requiredItemId;
    private String requiredTargetId;
    private String requiredNpcId;
    private String requiredNpcFlag;
    private String requiredNpcFlagValue;
    private String requiredGlobalFlag;
    private String requiredGlobalFlagValue;

    public PuzzleSolutionCondition() {
        // Default constructor for Jackson
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("requiredItemId")
    public String getRequiredItemId() {
        return requiredItemId;
    }

    public void setRequiredItemId(String requiredItemId) {
        this.requiredItemId = requiredItemId;
    }

    @JsonProperty("requiredTargetId")
    public String getRequiredTargetId() {
        return requiredTargetId;
    }

    public void setRequiredTargetId(String requiredTargetId) {
        this.requiredTargetId = requiredTargetId;
    }

    @JsonProperty("requiredNpcId")
    public String getRequiredNpcId() {
        return requiredNpcId;
    }

    public void setRequiredNpcId(String requiredNpcId) {
        this.requiredNpcId = requiredNpcId;
    }

    @JsonProperty("requiredNpcFlag")
    public String getRequiredNpcFlag() {
        return requiredNpcFlag;
    }

    public void setRequiredNpcFlag(String requiredNpcFlag) {
        this.requiredNpcFlag = requiredNpcFlag;
    }

    @JsonProperty("requiredNpcFlagValue")
    public String getRequiredNpcFlagValue() {
        return requiredNpcFlagValue;
    }

    public void setRequiredNpcFlagValue(String requiredNpcFlagValue) {
        this.requiredNpcFlagValue = requiredNpcFlagValue;
    }

    @JsonProperty("requiredGlobalFlag")
    public String getRequiredGlobalFlag() {
        return requiredGlobalFlag;
    }

    public void setRequiredGlobalFlag(String requiredGlobalFlag) {
        this.requiredGlobalFlag = requiredGlobalFlag;
    }

    @JsonProperty("requiredGlobalFlagValue")
    public String getRequiredGlobalFlagValue() {
        return requiredGlobalFlagValue;
    }

    public void setRequiredGlobalFlagValue(String requiredGlobalFlagValue) {
        this.requiredGlobalFlagValue = requiredGlobalFlagValue;
    }

    /**
     * Checks if the condition is met given the player and world state.
     *
     * @param player The current player
     * @param world The game world
     * @param itemUsedId Optional item ID if the condition is checking for item usage
     * @param targetId Optional target ID if the condition is checking for a target
     * @return true if the condition is met, false otherwise
     */
    @JsonIgnore
    public boolean isMet(Player player, World world, String itemUsedId, String targetId) {
        switch (type) {
            case "ITEM_USED_ON_TARGET":
                return itemUsedId != null && itemUsedId.equals(requiredItemId) &&
                       targetId != null && targetId.equals(requiredTargetId);
                
            case "NPC_STATE_REACHED":
                if (requiredNpcId != null) {
                    NpcInstance npc = world.getNpcInstance(requiredNpcId);
                    if (npc != null && requiredNpcFlag != null) {
                        String flagValue = npc.getNpcFlag(requiredNpcFlag);
                        return requiredNpcFlagValue.equals(flagValue);
                    }
                }
                return false;
                
            case "GLOBAL_FLAG_SET":
                if (requiredGlobalFlag != null) {
                    String flagValue = world.getGlobalFlagValue(requiredGlobalFlag);
                    return requiredGlobalFlagValue != null && requiredGlobalFlagValue.equals(flagValue);
                }
                return false;
                
            case "PLAYER_HAS_ITEM":
                return requiredItemId != null && player.hasItem(requiredItemId);
                
            default:
                return false;
        }
    }
}
