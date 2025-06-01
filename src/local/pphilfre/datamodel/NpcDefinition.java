package local.pphilfre.datamodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Defines the static properties of an NPC.
 */
public class NpcDefinition {
    private String id;
    private String name;
    private String presenceDescription;
    private String initialDialogueNodeId;
    private Map<String, DialogueNode> dialogueTree;
    private List<String> initialItemIds;
    private Map<String, String> initialNpcFlags;

    public NpcDefinition() {
        // Default constructor for Jackson
        this.dialogueTree = new HashMap<>();
        this.initialItemIds = new ArrayList<>();
        this.initialNpcFlags = new HashMap<>();
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("presenceDescription")
    public String getPresenceDescription() {
        return presenceDescription;
    }

    public void setPresenceDescription(String presenceDescription) {
        this.presenceDescription = presenceDescription;
    }

    @JsonProperty("initialDialogueNodeId")
    public String getInitialDialogueNodeId() {
        return initialDialogueNodeId;
    }

    public void setInitialDialogueNodeId(String initialDialogueNodeId) {
        this.initialDialogueNodeId = initialDialogueNodeId;
    }

    @JsonProperty("dialogueTree")
    public Map<String, DialogueNode> getDialogueTree() {
        return dialogueTree;
    }

    public void setDialogueTree(Map<String, DialogueNode> dialogueTree) {
        this.dialogueTree = dialogueTree != null ? dialogueTree : new HashMap<>();
    }

    @JsonProperty("initialItemIds")
    public List<String> getInitialItemIds() {
        return initialItemIds;
    }

    public void setInitialItemIds(List<String> initialItemIds) {
        this.initialItemIds = initialItemIds != null ? initialItemIds : new ArrayList<>();
    }

    @JsonProperty("initialNpcFlags")
    public Map<String, String> getInitialNpcFlags() {
        return initialNpcFlags;
    }

    public void setInitialNpcFlags(Map<String, String> initialNpcFlags) {
        this.initialNpcFlags = initialNpcFlags != null ? initialNpcFlags : new HashMap<>();
    }
}
