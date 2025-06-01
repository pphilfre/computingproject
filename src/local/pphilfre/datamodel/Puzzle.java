package local.pphilfre.datamodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import local.pphilfre.Player;
import local.pphilfre.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a puzzle in the game.
 */
public class Puzzle {
    private String id;
    private String description;
    private PuzzleSolutionCondition solutionCondition;
    private String successMessage;
    private String failureMessage;
    private String alreadySolvedMessage;
    private List<PuzzleEffect> effectsOnSolve;
    private String setsFlagOnSolve;
    private String setsFlagValueOnSolve;
    
    @JsonIgnore
    private boolean solved = false;

    public Puzzle() {
        // Default constructor for Jackson
        this.effectsOnSolve = new ArrayList<>();
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("solutionCondition")
    public PuzzleSolutionCondition getSolutionCondition() {
        return solutionCondition;
    }

    public void setSolutionCondition(PuzzleSolutionCondition solutionCondition) {
        this.solutionCondition = solutionCondition;
    }

    @JsonProperty("successMessage")
    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    @JsonProperty("failureMessage")
    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    @JsonProperty("alreadySolvedMessage")
    public String getAlreadySolvedMessage() {
        return alreadySolvedMessage;
    }

    public void setAlreadySolvedMessage(String alreadySolvedMessage) {
        this.alreadySolvedMessage = alreadySolvedMessage;
    }

    @JsonProperty("effectsOnSolve")
    public List<PuzzleEffect> getEffectsOnSolve() {
        return effectsOnSolve;
    }

    public void setEffectsOnSolve(List<PuzzleEffect> effectsOnSolve) {
        this.effectsOnSolve = effectsOnSolve != null ? effectsOnSolve : new ArrayList<>();
    }

    @JsonProperty("setsFlagOnSolve")
    public String getSetsFlagOnSolve() {
        return setsFlagOnSolve;
    }

    public void setSetsFlagOnSolve(String setsFlagOnSolve) {
        this.setsFlagOnSolve = setsFlagOnSolve;
    }

    @JsonProperty("setsFlagValueOnSolve")
    public String getSetsFlagValueOnSolve() {
        return setsFlagValueOnSolve;
    }

    public void setSetsFlagValueOnSolve(String setsFlagValueOnSolve) {
        this.setsFlagValueOnSolve = setsFlagValueOnSolve;
    }

    @JsonIgnore
    public boolean isSolved() {
        return solved;
    }

    @JsonIgnore
    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    /**
     * Attempts to solve the puzzle with the given context.
     *
     * @param player The current player
     * @param world The game world
     * @param itemUsedId Optional ID of the item used
     * @param targetId Optional ID of the target
     * @return true if the puzzle was solved, false otherwise
     */
    @JsonIgnore
    public boolean attemptSolve(Player player, World world, String itemUsedId, String targetId) {
        if (solved) {
            if (alreadySolvedMessage != null && !alreadySolvedMessage.isEmpty()) {
                System.out.println(alreadySolvedMessage);
            }
            return false; // Already solved, no need to solve again
        }

        if (solutionCondition != null && solutionCondition.isMet(player, world, itemUsedId, targetId)) {
            solved = true;
            world.setPuzzleSolved(id, true);
            
            if (successMessage != null && !successMessage.isEmpty()) {
                System.out.println(successMessage);
            }
            
            // Apply effects
            if (effectsOnSolve != null) {
                world.applyPuzzleEffects(effectsOnSolve, player);
            }
            
            // Set flag if specified
            if (setsFlagOnSolve != null && setsFlagValueOnSolve != null) {
                world.setGlobalFlag(setsFlagOnSolve, setsFlagValueOnSolve);
            }
            
            return true;
        } else {
            if (failureMessage != null && !failureMessage.isEmpty()) {
                System.out.println(failureMessage);
            }
            return false;
        }
    }
}
