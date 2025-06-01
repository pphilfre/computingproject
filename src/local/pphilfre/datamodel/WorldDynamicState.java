package local.pphilfre.datamodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the dynamic state of the game world for saving and loading.
 */
public class WorldDynamicState {
    private Map<String, List<String>> roomItemStates;  // roomId -> list of itemIds
    private Map<String, Boolean> puzzleSolvedStates;   // puzzleId -> solved status
    private Map<String, NpcInstance> npcInstanceStates;  // npcDefId -> NpcInstance
    private Map<String, String> globalFlagStates;      // flagName -> value
    private Map<String, Boolean> roomExitLockedStates; // "roomId_exitDir" -> locked status

    public WorldDynamicState() {
        // Default constructor for Jackson
        this.roomItemStates = new HashMap<>();
        this.puzzleSolvedStates = new HashMap<>();
        this.npcInstanceStates = new HashMap<>();
        this.globalFlagStates = new HashMap<>();
        this.roomExitLockedStates = new HashMap<>();
    }

    @JsonProperty("roomItemStates")
    public Map<String, List<String>> getRoomItemStates() {
        return roomItemStates;
    }

    public void setRoomItemStates(Map<String, List<String>> roomItemStates) {
        this.roomItemStates = roomItemStates != null ? roomItemStates : new HashMap<>();
    }

    @JsonProperty("puzzleSolvedStates")
    public Map<String, Boolean> getPuzzleSolvedStates() {
        return puzzleSolvedStates;
    }

    public void setPuzzleSolvedStates(Map<String, Boolean> puzzleSolvedStates) {
        this.puzzleSolvedStates = puzzleSolvedStates != null ? puzzleSolvedStates : new HashMap<>();
    }

    @JsonProperty("npcInstanceStates")
    public Map<String, NpcInstance> getNpcInstanceStates() {
        return npcInstanceStates;
    }

    public void setNpcInstanceStates(Map<String, NpcInstance> npcInstanceStates) {
        this.npcInstanceStates = npcInstanceStates != null ? npcInstanceStates : new HashMap<>();
    }

    @JsonProperty("globalFlagStates")
    public Map<String, String> getGlobalFlagStates() {
        return globalFlagStates;
    }

    public void setGlobalFlagStates(Map<String, String> globalFlagStates) {
        this.globalFlagStates = globalFlagStates != null ? globalFlagStates : new HashMap<>();
    }

    @JsonProperty("roomExitLockedStates")
    public Map<String, Boolean> getRoomExitLockedStates() {
        return roomExitLockedStates;
    }

    public void setRoomExitLockedStates(Map<String, Boolean> roomExitLockedStates) {
        this.roomExitLockedStates = roomExitLockedStates != null ? roomExitLockedStates : new HashMap<>();
    }
}
