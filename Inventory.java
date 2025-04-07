import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

// Enum for different item types in the game
enum ItemType {
    SEED(10, "Corn Seed"),
    FERTILIZER(25, "Fertilizer"),
    BUG_KILLER(30, "Bug Killer"),
    FARM_HELPER(150, "Farm Helper"),
    SECURITY_FENCE(200, "Security Fence");
    
    private final int price;
    private final String displayName;
    
    ItemType(int price, String displayName) {
        this.price = price;
        this.displayName = displayName;
    }
    
    public int getPrice() {
        return price;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
//SEED SPROUT,YOUNG,MATURE,OVERGROWN,DEAD.
// Enum for crop growth stages
enum CropStage {
    EMPTY,
    SEED,
    SEEDLING,
    GROWING,
    MATURE,
    DEAD
}

// Class to manage player's inventory
class Inventory {
    private HashMap<ItemType, Integer> items;
    private Random random;
    
    public Inventory() {
        items = new HashMap<>();
        random = new Random();
        
        // Initialize all items to 0
        for (ItemType type : ItemType.values()) {
            items.put(type, 0);
        }
    }
    
    public void addItem(ItemType type, int count) {
        int currentCount = items.getOrDefault(type, 0);
        items.put(type, currentCount + count);
    }
    
    public boolean useItem(ItemType type, int count) {
        int currentCount = items.getOrDefault(type, 0);
        if (currentCount >= count) {
            items.put(type, currentCount - count);
            return true;
        }
        return false;
    }
    
    public int getItemCount(ItemType type) {
        return items.getOrDefault(type, 0);
    }
    
    public boolean hasItem(ItemType type) {
        return getItemCount(type) > 0;
    }
    
    public int getTotalValue() {
        int total = 0;
        for (ItemType type : items.keySet()) {
            total += items.get(type) * type.getPrice();
        }
        return total;
    }
    
    public void removeRandomItems(int percentage) {
        ArrayList<ItemType> itemTypes = new ArrayList<>(items.keySet());
        for (ItemType type : itemTypes) {
            int currentCount = items.get(type);
            if (currentCount > 0) {
                int itemsToRemove = (int)(currentCount * percentage / 100.0);
                if (itemsToRemove > 0) {
                    items.put(type, currentCount - itemsToRemove);
                }
            }
        }
    }
    
    public void clear() {
        for (ItemType type : items.keySet()) {
            items.put(type, 0);
        }
    }
}