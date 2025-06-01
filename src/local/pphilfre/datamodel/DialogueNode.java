package local.pphilfre.datamodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents a node in an NPC's dialogue tree.
 */
public class DialogueNode {
    private String text;
    private List<DialogueResponseOption> responses;
    private String triggersQuestId;
    private String givesItemId;
    private String requiresItemId;
    private String setsNpcFlag;
    private String setsGlobalFlag;
    private boolean endsDialogue;

    public DialogueNode() {
        // Default constructor for Jackson
        this.responses = new ArrayList<>();
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("responses")
    public List<DialogueResponseOption> getResponses() {
        return responses;
    }

    public void setResponses(List<DialogueResponseOption> responses) {
        this.responses = responses != null ? responses : new ArrayList<>();
    }

    @JsonProperty("triggersQuestId")
    public String getTriggersQuestId() {
        return triggersQuestId;
    }

    public void setTriggersQuestId(String triggersQuestId) {
        this.triggersQuestId = triggersQuestId;
    }

    @JsonProperty("givesItemId")
    public String getGivesItemId() {
        return givesItemId;
    }

    public void setGivesItemId(String givesItemId) {
        this.givesItemId = givesItemId;
    }

    @JsonProperty("requiresItemId")
    public String getRequiresItemId() {
        return requiresItemId;
    }

    public void setRequiresItemId(String requiresItemId) {
        this.requiresItemId = requiresItemId;
    }

    @JsonProperty("setsNpcFlag")
    public String getSetsNpcFlag() {
        return setsNpcFlag;
    }

    public void setSetsNpcFlag(String setsNpcFlag) {
        this.setsNpcFlag = setsNpcFlag;
    }

    @JsonProperty("setsGlobalFlag")
    public String getSetsGlobalFlag() {
        return setsGlobalFlag;
    }

    public void setSetsGlobalFlag(String setsGlobalFlag) {
        this.setsGlobalFlag = setsGlobalFlag;
    }

    @JsonProperty("endsDialogue")
    public boolean isEndsDialogue() {
        return endsDialogue;
    }

    public void setEndsDialogue(boolean endsDialogue) {
        this.endsDialogue = endsDialogue;
    }
}
