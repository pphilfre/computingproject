package local.pphilfre.datamodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import local.pphilfre.Player;
import local.pphilfre.World;

/**
 * Represents an exit from one room to another, with locking mechanisms and requirements.
 */
public class Exit {
    private String targetRoomId;
    private boolean isInitiallyLocked;
    private String lockedMessage;
    private String unlockedMessage;
    private String requiredItemIdToUnlock;
    private String requiredPuzzleIdSolved;
    private String requiredFlagName;
    private String requiredFlagValue;
    
    @JsonIgnore
    private boolean currentLockedState;

    public Exit() {
        // Default constructor for Jackson
    }

    @JsonProperty("targetRoomId")
    public String getTargetRoomId() {
        return targetRoomId;
    }

    public void setTargetRoomId(String targetRoomId) {
        this.targetRoomId = targetRoomId;
    }

    @JsonProperty("isInitiallyLocked")
    public boolean isInitiallyLocked() {
        return isInitiallyLocked;
    }

    public void setInitiallyLocked(boolean initiallyLocked) {
        this.isInitiallyLocked = initiallyLocked;
    }

    @JsonProperty("lockedMessage")
    public String getLockedMessage() {
        return lockedMessage;
    }

    public void setLockedMessage(String lockedMessage) {
        this.lockedMessage = lockedMessage;
    }

    @JsonProperty("unlockedMessage")
    public String getUnlockedMessage() {
        return unlockedMessage;
    }

    public void setUnlockedMessage(String unlockedMessage) {
        this.unlockedMessage = unlockedMessage;
    }

    @JsonProperty("requiredItemIdToUnlock")
    public String getRequiredItemIdToUnlock() {
        return requiredItemIdToUnlock;
    }

    public void setRequiredItemIdToUnlock(String requiredItemIdToUnlock) {
        this.requiredItemIdToUnlock = requiredItemIdToUnlock;
    }

    @JsonProperty("requiredPuzzleIdSolved")
    public String getRequiredPuzzleIdSolved() {
        return requiredPuzzleIdSolved;
    }

    public void setRequiredPuzzleIdSolved(String requiredPuzzleIdSolved) {
        this.requiredPuzzleIdSolved = requiredPuzzleIdSolved;
    }

    @JsonProperty("requiredFlagName")
    public String getRequiredFlagName() {
        return requiredFlagName;
    }

    public void setRequiredFlagName(String requiredFlagName) {
        this.requiredFlagName = requiredFlagName;
    }

    @JsonProperty("requiredFlagValue")
    public String getRequiredFlagValue() {
        return requiredFlagValue;
    }

    public void setRequiredFlagValue(String requiredFlagValue) {
        this.requiredFlagValue = requiredFlagValue;
    }

    @JsonIgnore
    public boolean isCurrentLockedState() {
        return currentLockedState;
    }

    @JsonIgnore
    public void setCurrentLockedState(boolean currentLockedState) {
        this.currentLockedState = currentLockedState;
    }

    /**
     * Checks if the exit is currently locked based on puzzle solution, items, or flags.
     * 
     * @param player The current player
     * @param world The game world
     * @return true if the exit is locked, false otherwise
     */
    @JsonIgnore
    public boolean isCurrentlyLocked(Player player, World world) {
        // If we're tracking explicit locked state, use that
        if (currentLockedState) {
            return true;
        }
        
        // Check for puzzle-based lock
        if (requiredPuzzleIdSolved != null && !world.isPuzzleSolved(requiredPuzzleIdSolved)) {
            return true;
        }
        
        // Check for item-based lock
        if (requiredItemIdToUnlock != null && !player.hasItem(requiredItemIdToUnlock)) {
            return true;
        }
        
        // Check for flag-based lock
        if (requiredFlagName != null && requiredFlagValue != null) {
            String actualValue = world.getGlobalFlagValue(requiredFlagName);
            if (!requiredFlagValue.equals(actualValue)) {
                return true;
            }
        }
        
        // No locks apply
        return false;
    }
    
    /**
     * Checks if the player can pass through this exit.
     *
     * @param player The current player
     * @param world The game world
     * @return true if the player can pass, false otherwise
     */
    @JsonIgnore
    public boolean canPlayerPass(Player player, World world) {
        return !isCurrentlyLocked(player, world);
    }
}
