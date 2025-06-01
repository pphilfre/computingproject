package local.pphilfre.datamodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import local.pphilfre.Player;
import local.pphilfre.World;

/**
 * Defines an effect that occurs when a puzzle is solved.
 */
public class PuzzleEffect {
    private String type; // e.g., "UNLOCK_EXIT", "SPAWN_ITEM", "REMOVE_ITEM", "MOVE_NPC"
    private String targetRoomId;
    private String exitDirection;
    private String itemIdToSpawnOrRemove;
    private String npcIdToMove;
    private String destinationRoomId;
    private String flagToSet;
    private String flagValue;

    public PuzzleEffect() {
        // Default constructor for Jackson
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("targetRoomId")
    public String getTargetRoomId() {
        return targetRoomId;
    }

    public void setTargetRoomId(String targetRoomId) {
        this.targetRoomId = targetRoomId;
    }

    @JsonProperty("exitDirection")
    public String getExitDirection() {
        return exitDirection;
    }

    public void setExitDirection(String exitDirection) {
        this.exitDirection = exitDirection;
    }

    @JsonProperty("itemIdToSpawnOrRemove")
    public String getItemIdToSpawnOrRemove() {
        return itemIdToSpawnOrRemove;
    }

    public void setItemIdToSpawnOrRemove(String itemIdToSpawnOrRemove) {
        this.itemIdToSpawnOrRemove = itemIdToSpawnOrRemove;
    }

    @JsonProperty("npcIdToMove")
    public String getNpcIdToMove() {
        return npcIdToMove;
    }

    public void setNpcIdToMove(String npcIdToMove) {
        this.npcIdToMove = npcIdToMove;
    }

    @JsonProperty("destinationRoomId")
    public String getDestinationRoomId() {
        return destinationRoomId;
    }

    public void setDestinationRoomId(String destinationRoomId) {
        this.destinationRoomId = destinationRoomId;
    }

    @JsonProperty("flagToSet")
    public String getFlagToSet() {
        return flagToSet;
    }

    public void setFlagToSet(String flagToSet) {
        this.flagToSet = flagToSet;
    }

    @JsonProperty("flagValue")
    public String getFlagValue() {
        return flagValue;
    }

    public void setFlagValue(String flagValue) {
        this.flagValue = flagValue;
    }

    /**
     * Apply this effect to the world and player.
     *
     * @param world The game world
     * @param player The current player
     */
    @JsonIgnore
    public void apply(World world, Player player) {
        switch (type) {
            case "UNLOCK_EXIT":
                if (targetRoomId != null && exitDirection != null) {
                    world.setExitLockedState(targetRoomId, exitDirection, false);
                }
                break;
                
            case "LOCK_EXIT":
                if (targetRoomId != null && exitDirection != null) {
                    world.setExitLockedState(targetRoomId, exitDirection, true);
                }
                break;
                
            case "SPAWN_ITEM":
                if (targetRoomId != null && itemIdToSpawnOrRemove != null) {
                    world.addItemToRoom(targetRoomId, itemIdToSpawnOrRemove);
                }
                break;
                
            case "REMOVE_ITEM":
                if (targetRoomId != null && itemIdToSpawnOrRemove != null) {
                    world.removeItemFromRoom(targetRoomId, itemIdToSpawnOrRemove);
                }
                break;
                
            case "MOVE_NPC":
                if (npcIdToMove != null && destinationRoomId != null) {
                    NpcInstance npc = world.getNpcInstance(npcIdToMove);
                    if (npc != null) {
                        npc.setCurrentRoomId(destinationRoomId);
                    }
                }
                break;
                
            case "SET_FLAG":
                if (flagToSet != null && flagValue != null) {
                    world.setGlobalFlag(flagToSet, flagValue);
                }
                break;
        }
    }
}
