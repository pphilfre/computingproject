package local.pphilfre.datamodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an item in the game.
 */
public class Item {
    private String id;
    private String name;
    private String description;
    private boolean takeable;
    private List<String> canBeCombinedWith;
    private String combinationResultItemId;
    private Map<String, UseEffect> useEffects; // Key format: "type:id", e.g., "item:rusty_lock" or "npc:innkeeper"

    public Item() {
        // Default constructor for Jackson
        this.canBeCombinedWith = new ArrayList<>();
        this.useEffects = new HashMap<>();
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

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("takeable")
    public boolean isTakeable() {
        return takeable;
    }

    public void setTakeable(boolean takeable) {
        this.takeable = takeable;
    }

    @JsonProperty("canBeCombinedWith")
    public List<String> getCanBeCombinedWith() {
        return canBeCombinedWith;
    }

    public void setCanBeCombinedWith(List<String> canBeCombinedWith) {
        this.canBeCombinedWith = canBeCombinedWith != null ? canBeCombinedWith : new ArrayList<>();
    }

    @JsonProperty("combinationResultItemId")
    public String getCombinationResultItemId() {
        return combinationResultItemId;
    }

    public void setCombinationResultItemId(String combinationResultItemId) {
        this.combinationResultItemId = combinationResultItemId;
    }

    @JsonProperty("useEffects")
    public Map<String, UseEffect> getUseEffects() {
        return useEffects;
    }

    public void setUseEffects(Map<String, UseEffect> useEffects) {
        this.useEffects = useEffects != null ? useEffects : new HashMap<>();
    }

    /**
     * Gets the effect of using this item on a target.
     *
     * @param targetType The type of the target (e.g., "item", "npc", "room")
     * @param targetId The ID of the target
     * @return The UseEffect for the target, or null if none exists
     */
    public UseEffect getUseEffectForTarget(String targetType, String targetId) {
        String key = targetType + ":" + targetId;
        return useEffects.get(key);
    }
}
