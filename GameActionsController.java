import java.util.ArrayList;
import javax.swing.*;

// Game actions controller to handle tile actions
class GameActionsController {
    private CornHarvestGame game;
    private ArrayList<FarmTilePanel> tilePanels;
    private ActionType currentAction;
    
    enum ActionType {
        NONE,
        PLANT,
        WATER,
        HARVEST,
        FERTILIZE,
        PROTECT
    }
    
    public GameActionsController(CornHarvestGame game) {
        this.game = game;
        this.tilePanels = new ArrayList<>();
        this.currentAction = ActionType.NONE;
    }
    
    public void addTilePanel(FarmTilePanel panel) {
        tilePanels.add(panel);
    }
    
    public void setAction(ActionType action) {
        currentAction = action;
    }
    
    public ActionType getCurrentAction() {
        return currentAction;
    }
    
    // Execute the current action on selected tiles
    public int executeAction(Inventory inventory) {
        int actionCount = 0;
        int moneyEarned = 0;
        
        for (FarmTilePanel panel : tilePanels) {
            if (panel.isSelected()) {
                CropTile tile = panel.getTile();
                
                switch (currentAction) {
                    case PLANT:
                        if (tile.isEmpty() && inventory.useItem(ItemType.SEED, 1)) {
                            tile.plant();
                            actionCount++;
                        }
                        break;
                        
                    case WATER:
                        if (!tile.isEmpty() && !tile.isDead()) {
                            tile.water();
                            actionCount++;
                        }
                        break;
                        
                    case HARVEST:
                        if (tile.isMature()) {
                            // Harvest mature corn for money
                            int cornValue = 30; // Base value for corn
                            moneyEarned += cornValue;
                            tile.harvest();
                            actionCount++;
                        }
                        break;
                        
                    case FERTILIZE:
                        if (!tile.isEmpty() && !tile.isDead() && inventory.useItem(ItemType.FERTILIZER, 1)) {
                            tile.applyFertilizer();
                            actionCount++;
                        }
                        break;
                        
                    case PROTECT:
                        if (!tile.isEmpty() && !tile.isDead() && inventory.useItem(ItemType.BUG_KILLER, 1)) {
                            tile.setProtected(true);
                            actionCount++;
                        }
                        break;
                }
                
                // Update the tile visual
                panel.update();
                
                // Deselect the tile after action
                panel.setSelected(false);
            }
        }
        
        // Return the action result
        if (actionCount > 0) {
            String message = "";
            switch (currentAction) {
                case PLANT:
                    message = "Planted " + actionCount + " corn seeds.";
                    break;
                case WATER:
                    message = "Watered " + actionCount + " crops.";
                    break;
                case HARVEST:
                    message = "Harvested " + actionCount + " crops for $" + moneyEarned + ".";
                    break;
                case FERTILIZE:
                    message = "Applied fertilizer to " + actionCount + " crops.";
                    break;
                case PROTECT:
                    message = "Protected " + actionCount + " crops from bugs.";
                    break;
            }
            
            // Reset current action
            currentAction = ActionType.NONE;
            
            JOptionPane.showMessageDialog(null, message);
            return moneyEarned;
        } else {
            if (currentAction != ActionType.NONE) {
                JOptionPane.showMessageDialog(null, "No valid tiles selected for this action!");
            }
            return 0;
        }
    }
    
    public void deselectAll() {
        for (FarmTilePanel panel : tilePanels) {
            panel.setSelected(false);
        }
    }
}