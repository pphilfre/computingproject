package local.pphilfre.datamodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the runtime state of an NPC instance.
 */
public class NpcInstance {
    private String definitionId;
    private String currentRoomId;
    private String currentDialogueNodeId;
    private List<String> inventoryItemIds;
    private Map<String, String> npcSpecificFlags;

    public NpcInstance() {
        // Default constructor for Jackson
        this.inventoryItemIds = new ArrayList<>();
        this.npcSpecificFlags = new HashMap<>();
    }

    public NpcInstance(String definitionId, String currentRoomId, String initialDialogueNodeId, 
                      List<String> initialItems, Map<String, String> initialFlags) {
        this.definitionId = definitionId;
        this.currentRoomId = currentRoomId;
        this.currentDialogueNodeId = initialDialogueNodeId;
        this.inventoryItemIds = initialItems != null ? new ArrayList<>(initialItems) : new ArrayList<>();
        this.npcSpecificFlags = initialFlags != null ? new HashMap<>(initialFlags) : new HashMap<>();
    }

    @JsonProperty("definitionId")
    public String getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(String definitionId) {
        this.definitionId = definitionId;
    }

    @JsonProperty("currentRoomId")
    public String getCurrentRoomId() {
        return currentRoomId;
    }

    public void setCurrentRoomId(String currentRoomId) {
        this.currentRoomId = currentRoomId;
    }

    @JsonProperty("currentDialogueNodeId")
    public String getCurrentDialogueNodeId() {
        return currentDialogueNodeId;
    }

    public void setCurrentDialogueNodeId(String currentDialogueNodeId) {
        this.currentDialogueNodeId = currentDialogueNodeId;
    }

    @JsonProperty("inventoryItemIds")
    public List<String> getInventoryItemIds() {
        return inventoryItemIds;
    }

    public void setInventoryItemIds(List<String> inventoryItemIds) {
        this.inventoryItemIds = inventoryItemIds != null ? inventoryItemIds : new ArrayList<>();
    }

    @JsonProperty("npcSpecificFlags")
    public Map<String, String> getNpcSpecificFlags() {
        return npcSpecificFlags;
    }

    public void setNpcSpecificFlags(Map<String, String> npcSpecificFlags) {
        this.npcSpecificFlags = npcSpecificFlags != null ? npcSpecificFlags : new HashMap<>();
    }

    /**
     * Adds an item to the NPC's inventory.
     *
     * @param itemId The ID of the item to add
     */
    public void addItem(String itemId) {
        if (itemId != null && !inventoryItemIds.contains(itemId)) {
            inventoryItemIds.add(itemId);
        }
    }

    /**
     * Removes an item from the NPC's inventory.
     *
     * @param itemId The ID of the item to remove
     * @return true if the item was removed, false otherwise
     */
    public boolean removeItem(String itemId) {
        return inventoryItemIds.remove(itemId);
    }

    /**
     * Checks if the NPC has a specific item.
     *
     * @param itemId The ID of the item to check for
     * @return true if the NPC has the item, false otherwise
     */
    public boolean hasItem(String itemId) {
        return inventoryItemIds.contains(itemId);
    }

    /**
     * Gets the value of an NPC-specific flag.
     *
     * @param flagName The name of the flag
     * @return The value of the flag, or null if it's not set
     */
    public String getNpcFlag(String flagName) {
        return npcSpecificFlags.get(flagName);
    }

    /**
     * Sets an NPC-specific flag.
     *
     * @param flagName The name of the flag
     * @param value The value to set
     */
    public void setNpcFlag(String flagName, String value) {
        if (flagName != null) {
            if (value != null) {
                npcSpecificFlags.put(flagName, value);
            } else {
                npcSpecificFlags.remove(flagName);
            }
        }
    }
}
