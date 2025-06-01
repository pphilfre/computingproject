package local.pphilfre.datamodel;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the complete game state for saving and loading.
 */
public class GameState {
    private PlayerState playerState;
    private WorldDynamicState worldDynamicState;
    private String gameVersion;  // Store this to handle save compatibility in future versions
    private long saveTimestamp;  // When the game was saved

    public GameState() {
        // Default constructor for Jackson
    }
    
    public GameState(PlayerState playerState, WorldDynamicState worldDynamicState, 
                     String gameVersion) {
        this.playerState = playerState;
        this.worldDynamicState = worldDynamicState;
        this.gameVersion = gameVersion;
        this.saveTimestamp = System.currentTimeMillis();
    }

    @JsonProperty("playerState")
    public PlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    @JsonProperty("worldDynamicState")
    public WorldDynamicState getWorldDynamicState() {
        return worldDynamicState;
    }

    public void setWorldDynamicState(WorldDynamicState worldDynamicState) {
        this.worldDynamicState = worldDynamicState;
    }

    @JsonProperty("gameVersion")
    public String getGameVersion() {
        return gameVersion;
    }

    public void setGameVersion(String gameVersion) {
        this.gameVersion = gameVersion;
    }

    @JsonProperty("saveTimestamp")
    public long getSaveTimestamp() {
        return saveTimestamp;
    }

    public void setSaveTimestamp(long saveTimestamp) {
        this.saveTimestamp = saveTimestamp;
    }
}
