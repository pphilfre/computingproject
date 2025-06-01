package local.pphilfre.datamodel;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the effects of using an item on a target.
 */
public class UseEffect {
    private String triggersPuzzleId;
    private String successMessage;
    private String failureMessage;
    private boolean consumesItem;
    private String setsFlagName;
    private String setsFlagValue;

    public UseEffect() {
        // Default constructor for Jackson
    }

    @JsonProperty("triggersPuzzleId")
    public String getTriggersPuzzleId() {
        return triggersPuzzleId;
    }

    public void setTriggersPuzzleId(String triggersPuzzleId) {
        this.triggersPuzzleId = triggersPuzzleId;
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

    @JsonProperty("consumesItem")
    public boolean isConsumesItem() {
        return consumesItem;
    }

    public void setConsumesItem(boolean consumesItem) {
        this.consumesItem = consumesItem;
    }

    @JsonProperty("setsFlagName")
    public String getSetsFlagName() {
        return setsFlagName;
    }

    public void setSetsFlagName(String setsFlagName) {
        this.setsFlagName = setsFlagName;
    }

    @JsonProperty("setsFlagValue") 
    public String getSetsFlagValue() {
        return setsFlagValue;
    }

    public void setSetsFlagValue(String setsFlagValue) {
        this.setsFlagValue = setsFlagValue;
    }
}
