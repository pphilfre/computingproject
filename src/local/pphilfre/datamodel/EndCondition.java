package local.pphilfre.datamodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import local.pphilfre.Player;
import local.pphilfre.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a condition that ends the game.
 */
public class EndCondition {
    private String type; // "WIN" or "LOSE"
    private String message;
    private List<ConditionCriterion> criteria;

    public EndCondition() {
        // Default constructor for Jackson
        this.criteria = new ArrayList<>();
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("criteria")
    public List<ConditionCriterion> getCriteria() {
        return criteria;
    }

    public void setCriteria(List<ConditionCriterion> criteria) {
        this.criteria = criteria != null ? criteria : new ArrayList<>();
    }

    /**
     * Checks if all criteria for this end condition are met.
     *
     * @param player The current player
     * @param world The game world
     * @return true if all criteria are met, false otherwise
     */
    @JsonIgnore
    public boolean areCriteriaMet(Player player, World world) {
        if (criteria == null || criteria.isEmpty()) {
            return false;
        }
        
        for (ConditionCriterion criterion : criteria) {
            if (!criterion.isMet(player, world)) {
                return false;
            }
        }
        
        return true;
    }
}
