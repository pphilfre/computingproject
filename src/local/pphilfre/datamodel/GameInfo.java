package local.pphilfre.datamodel;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the basic information about the game.
 */
public class GameInfo {
    private String gameTitle;
    private String version;
    private String welcomeMessage;
    private String helpText;

    public GameInfo() {
        // Default constructor for Jackson
    }

    public GameInfo(String gameTitle, String version, String welcomeMessage, String helpText) {
        this.gameTitle = gameTitle;
        this.version = version;
        this.welcomeMessage = welcomeMessage;
        this.helpText = helpText;
    }

    @JsonProperty("gameTitle")
    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty("welcomeMessage")
    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    @JsonProperty("helpText")
    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }
}
