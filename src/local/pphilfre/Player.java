package local.pphilfre;

import local.pphilfre.datamodel.Item;
import local.pphilfre.datamodel.PlayerState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the player in the game.
 */
public class Player {
    private String currentRoomId;
    private List<String> inventoryItemIds;
    private Map<String, String> playerFlags;
    private World world; // Reference to the game world for item lookups

    /**
     * Creates a new player with the specified parameters.
     *
     * @param startingRoomId The ID of the room where the player starts
     * @param initialInventory The initial inventory items
     * @param world Reference to the game world
     */
    public Player(String startingRoomId, List<String> initialInventory, World world) {
        this.currentRoomId = startingRoomId;
        this.inventoryItemIds = initialInventory != null ? new ArrayList<>(initialInventory) : new ArrayList<>();
        this.playerFlags = new HashMap<>();
        this.world = world;
    }

    /**
     * Gets the current room ID.
     *
     * @return The current room ID
     */
    public String getCurrentRoomId() {
        return currentRoomId;
    }

    /**
     * Sets the current room ID.
     *
     * @param currentRoomId The room ID to set
     */
    public void setCurrentRoomId(String currentRoomId) {
        this.currentRoomId = currentRoomId;
    }

    /**
     * Adds an item to the player's inventory.
     *
     * @param itemId The ID of the item to add
     */
    public void addItem(String itemId) {
        if (itemId != null && !inventoryItemIds.contains(itemId)) {
            inventoryItemIds.add(itemId);
        }
    }

    /**
     * Removes an item from the player's inventory.
     *
     * @param itemId The ID of the item to remove
     * @return true if the item was removed, false otherwise
     */
    public boolean removeItem(String itemId) {
        return inventoryItemIds.remove(itemId);
    }

    /**
     * Checks if the player has a specific item.
     *
     * @param itemId The ID of the item to check for
     * @return true if the player has the item, false otherwise
     */
    public boolean hasItem(String itemId) {
        return inventoryItemIds.contains(itemId);
    }

    /**
     * Gets an item from the player's inventory by name.
     *
     * @param name The name of the item to find
     * @return The item ID if found, null otherwise
     */
    public String getItemFromInventoryByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        
        String normalizedName = name.toLowerCase().trim();
        
        for (String itemId : inventoryItemIds) {
            Item item = world.getItemDefinition(itemId);
            if (item != null && item.getName().toLowerCase().contains(normalizedName)) {
                return itemId;
            }
        }
        
        return null;
    }

    /**
     * Displays the player's inventory to the console.
     */
    public void displayInventory() {
        if (inventoryItemIds.isEmpty()) {
            System.out.println("Your inventory is empty.");
            return;
        }
        
        System.out.println("Inventory:");
        System.out.println("----------");
        
        for (String itemId : inventoryItemIds) {
            Item item = world.getItemDefinition(itemId);
            if (item != null) {
                System.out.println("- " + item.getName());
            }
        }
    }

    /**
     * Gets the value of a player flag.
     *
     * @param flagName The name of the flag
     * @return The value of the flag, or null if it's not set
     */
    public String getPlayerFlag(String flagName) {
        return playerFlags.get(flagName);
    }

    /**
     * Sets a player flag.
     *
     * @param flagName The name of the flag
     * @param value The value to set
     */
    public void setPlayerFlag(String flagName, String value) {
        if (flagName != null) {
            if (value != null) {
                playerFlags.put(flagName, value);
            } else {
                playerFlags.remove(flagName);
            }
        }
    }

    /**
     * Gets a list of item IDs in the player's inventory.
     *
     * @return The list of item IDs
     */
    public List<String> getInventoryItemIds() {
        return new ArrayList<>(inventoryItemIds);
    }

    /**
     * Gets the current player state for saving.
     *
     * @return The current player state
     */
    public PlayerState getState() {
        return new PlayerState(currentRoomId, inventoryItemIds, playerFlags);
    }

    /**
     * Restores the player state from a saved state.
     *
     * @param state The state to restore from
     */
    public void restoreState(PlayerState state) {
        if (state != null) {
            this.currentRoomId = state.getCurrentRoomId();
            this.inventoryItemIds = new ArrayList<>(state.getInventoryItemIds());
            this.playerFlags = new HashMap<>(state.getPlayerFlags());
        }
    }
}
