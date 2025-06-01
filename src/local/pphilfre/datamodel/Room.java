package local.pphilfre.datamodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import local.pphilfre.Player;
import local.pphilfre.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a room in the game world.
 */
public class Room {
    private String id;
    private String name;
    private String baseDescription;
    private List<String> itemIds;
    private List<String> npcIds;
    private Map<String, Exit> exits;

    public Room() {
        // Default constructor for Jackson
        this.itemIds = new ArrayList<>();
        this.npcIds = new ArrayList<>();
        this.exits = new HashMap<>();
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

    @JsonProperty("baseDescription")
    public String getBaseDescription() {
        return baseDescription;
    }

    public void setBaseDescription(String baseDescription) {
        this.baseDescription = baseDescription;
    }

    @JsonProperty("itemIds")
    public List<String> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<String> itemIds) {
        this.itemIds = itemIds != null ? itemIds : new ArrayList<>();
    }

    @JsonProperty("npcIds")
    public List<String> getNpcIds() {
        return npcIds;
    }

    public void setNpcIds(List<String> npcIds) {
        this.npcIds = npcIds != null ? npcIds : new ArrayList<>();
    }

    @JsonProperty("exits")
    public Map<String, Exit> getExits() {
        return exits;
    }

    public void setExits(Map<String, Exit> exits) {
        this.exits = exits != null ? exits : new HashMap<>();
    }

    /**
     * Gets a formatted description of the room that includes its base description,
     * visible items, NPCs, and available exits.
     *
     * @param world The game world
     * @param player The current player
     * @return A formatted description of the room
     */
    @JsonIgnore
    public String getFormattedDescription(World world, Player player) {
        StringBuilder description = new StringBuilder();
        
        // Add the room name and basic description
        description.append(name).append("\n");
        description.append("----------------------------------\n");
        description.append(baseDescription).append("\n\n");
        
        // Add items in the room
        List<String> roomItems = world.getItemsInRoom(id);
        if (!roomItems.isEmpty()) {
            description.append("You can see: ");
            for (int i = 0; i < roomItems.size(); i++) {
                String itemId = roomItems.get(i);
                Item item = world.getItemDefinition(itemId);
                if (item != null) {
                    description.append(item.getName());
                    if (i < roomItems.size() - 1) {
                        description.append(", ");
                    }
                }
            }
            description.append("\n");
        }
        
        // Add NPCs in the room
        List<NpcInstance> roomNpcs = world.getNpcsInRoom(id);
        if (!roomNpcs.isEmpty()) {
            description.append("\n");
            for (NpcInstance npcInstance : roomNpcs) {
                NpcDefinition npcDef = world.getNpcDefinition(npcInstance.getDefinitionId());
                if (npcDef != null && npcDef.getPresenceDescription() != null) {
                    description.append(npcDef.getPresenceDescription()).append("\n");
                } else if (npcDef != null) {
                    description.append(npcDef.getName()).append(" is here.\n");
                }
            }
        }
        
        // Add available exits
        if (!exits.isEmpty()) {
            description.append("\nExits: ");
            List<String> availableExits = new ArrayList<>();
            
            for (Map.Entry<String, Exit> exit : exits.entrySet()) {
                String direction = exit.getKey();
                Exit exitObj = exit.getValue();
                
                if (!exitObj.isCurrentlyLocked(player, world)) {
                    availableExits.add(direction);
                } else if (exitObj.getLockedMessage() != null && !exitObj.getLockedMessage().isEmpty()) {
                    description.append("\nThe exit to the ").append(direction)
                            .append(" ").append(exitObj.getLockedMessage());
                }
            }
            
            if (!availableExits.isEmpty()) {
                for (int i = 0; i < availableExits.size(); i++) {
                    description.append(availableExits.get(i));
                    if (i < availableExits.size() - 1) {
                        description.append(", ");
                    }
                }
            } else {
                description.append("None available");
            }
        }
        
        return description.toString();
    }
}
