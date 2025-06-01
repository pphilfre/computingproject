package local.pphilfre.datamodel;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a dialogue response option that the player can select.
 */
public class DialogueResponseOption {
    private String text;
    private String targetNodeId;
    private String requiresPlayerItem;
    private String requiresNpcFlag;
    private String requiresGlobalFlag;

    public DialogueResponseOption() {
        // Default constructor for Jackson
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("targetNodeId")
    public String getTargetNodeId() {
        return targetNodeId;
    }

    public void setTargetNodeId(String targetNodeId) {
        this.targetNodeId = targetNodeId;
    }

    @JsonProperty("requiresPlayerItem")
    public String getRequiresPlayerItem() {
        return requiresPlayerItem;
    }

    public void setRequiresPlayerItem(String requiresPlayerItem) {
        this.requiresPlayerItem = requiresPlayerItem;
    }

    @JsonProperty("requiresNpcFlag")
    public String getRequiresNpcFlag() {
        return requiresNpcFlag;
    }

    public void setRequiresNpcFlag(String requiresNpcFlag) {
        this.requiresNpcFlag = requiresNpcFlag;
    }

    @JsonProperty("requiresGlobalFlag")
    public String getRequiresGlobalFlag() {
        return requiresGlobalFlag;
    }

    public void setRequiresGlobalFlag(String requiresGlobalFlag) {
        this.requiresGlobalFlag = requiresGlobalFlag;
    }
}
