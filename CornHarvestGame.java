import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class CornHarvestGame extends JFrame {
    // Constants
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;
    private static final int TILE_SIZE = 64;
    private static final int INITIAL_MONEY = 500;
    private static final int INITIAL_LAND_SIZE = 4;
    private static final int INITIAL_LAND_LENGTH = 4;
    private static final int INITIAL_LAND_WIDTH = 4; // 4x4 farm
    
    // Game state
    private int money;
    private int day;
    private ArrayList<CropTile> farmTiles;
    private ArrayList<FarmTilePanel> tilePanels;
    private Inventory inventory;
    private Random random;
    private boolean isDaytime;
    
    // UI Components
    private JPanel gamePanel;
    private JPanel farmPanel;
    private JPanel controlPanel;
    private JPanel statsPanel;
    private JPanel inventoryPanel;
    private JLabel moneyLabel;
    private JLabel dayLabel;
    private JLabel currentActionLabel;
    
    // Game mechanics
    private EventSystem eventSystem;
    private GameActionsController actionsController;
    private SoundManager soundManager;
    
    public CornHarvestGame() {
        setTitle("Daniel's Corn Harvesting Simulator");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initGame();
        createUI();
        
        setVisible(true);
        
        // Show game instructions
        showInstructions();
    }
    
    private void initGame() {
        money = INITIAL_MONEY;
        day = 1;
        farmTiles = new ArrayList<>();
        tilePanels = new ArrayList<>();
        inventory = new Inventory();
        random = new Random();
        isDaytime = true;
        
        // Initialize farm with empty tiles
        for (int i = 0; i < INITIAL_LAND_SIZE * INITIAL_LAND_SIZE; i++) {
            farmTiles.add(new CropTile());
        }
        
        // Add initial items to inventory
        inventory.addItem(ItemType.SEED, 10);
        inventory.addItem(ItemType.FERTILIZER, 3);
        inventory.addItem(ItemType.BUG_KILLER, 2);
        
        // Initialize event system
        eventSystem = new EventSystem();
        
        // Initialize actions controller
        actionsController = new GameActionsController(this);
        
        // Initialize sound manager
        soundManager = new SoundManager();
        soundManager.startBackgroundMusic();
    }
    
    private void createUI() {
        // Main layout
        setLayout(new BorderLayout());
        
        // Create panels
        createFarmPanel();
        createControlPanel();
        createStatsPanel();
        createInventoryPanel();
        
        // Add panels to frame
        add(farmPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);
        add(statsPanel, BorderLayout.NORTH);
        add(inventoryPanel, BorderLayout.SOUTH);
    }
    
    private void createFarmPanel() {
        farmPanel = new JPanel();
        farmPanel.setLayout(new GridLayout(INITIAL_LAND_LENGTH, INITIAL_LAND_WIDTH, 2, 2));
        farmPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        farmPanel.setBackground(new Color(180, 120, 70)); // Brown background for farm
        
        // Create visual representations of farm tiles
        for (CropTile tile : farmTiles) {
            FarmTilePanel tilePanel = new FarmTilePanel(tile);
            tilePanels.add(tilePanel);
            farmPanel.add(tilePanel);
            
            // Add tile panel to actions controller
            actionsController.addTilePanel(tilePanel);
        }
    }
    
    private void createControlPanel() {
        controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));
        controlPanel.setPreferredSize(new Dimension(200, WINDOW_HEIGHT));
        controlPanel.setBackground(new Color(230, 230, 250)); // Light lavender background
        
        // Current action label
        currentActionLabel = new JLabel("Current Action: None");
        currentActionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlPanel.add(currentActionLabel);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Add buttons for player actions
        JButton plantButton = new JButton("Plant Seeds");
        JButton waterButton = new JButton("Water Crops");
        JButton fertilizeButton = new JButton("Apply Fertilizer");
        JButton protectButton = new JButton("Apply Bug Killer");
        JButton harvestButton = new JButton("Harvest Crops");
        JButton shopButton = new JButton("Shop");
        JButton sleepButton = new JButton("Sleep (Next Day)");
        JButton cancelButton = new JButton("Cancel Action");
        
        // Make all buttons the same width
        Dimension buttonSize = new Dimension(180, 30);
        plantButton.setMaximumSize(buttonSize);
        waterButton.setMaximumSize(buttonSize);
        fertilizeButton.setMaximumSize(buttonSize);
        protectButton.setMaximumSize(buttonSize);
        harvestButton.setMaximumSize(buttonSize);
        shopButton.setMaximumSize(buttonSize);
        sleepButton.setMaximumSize(buttonSize);
        cancelButton.setMaximumSize(buttonSize);
        
        // Add button icons or colors
        plantButton.setBackground(new Color(200, 255, 200));
        waterButton.setBackground(new Color(200, 200, 255));
        fertilizeButton.setBackground(new Color(200, 255, 200));
        protectButton.setBackground(new Color(200, 255, 200));
        harvestButton.setBackground(new Color(255, 255, 200));
        
        // Center-align buttons
        plantButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        waterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        fertilizeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        protectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        harvestButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        shopButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        sleepButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add action listeners
        plantButton.addActionListener(e -> setGameAction(GameActionsController.ActionType.PLANT));
        waterButton.addActionListener(e -> setGameAction(GameActionsController.ActionType.WATER));
        fertilizeButton.addActionListener(e -> setGameAction(GameActionsController.ActionType.FERTILIZE));
        protectButton.addActionListener(e -> setGameAction(GameActionsController.ActionType.PROTECT));
        harvestButton.addActionListener(e -> setGameAction(GameActionsController.ActionType.HARVEST));
        shopButton.addActionListener(e -> openShop());
        sleepButton.addActionListener(e -> endDay());
        cancelButton.addActionListener(e -> cancelAction());
        
        // Add buttons to panel
        controlPanel.add(plantButton);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(waterButton);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(fertilizeButton);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(protectButton);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(harvestButton);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(cancelButton);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        controlPanel.add(shopButton);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        controlPanel.add(sleepButton);
        controlPanel.add(Box.createVerticalGlue());
    }
    
    private void createStatsPanel() {
        statsPanel = new JPanel();
        statsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statsPanel.setBackground(new Color(250, 250, 220)); // Light yellow background
        
        // Create labels for game stats
        moneyLabel = new JLabel("Money: $" + money);
        dayLabel = new JLabel("Day: " + day + " (Daytime)");
        JLabel tempLabel = new JLabel("Temperature: " + eventSystem.getCurrentTemperature() + "°C");
        
        // Add labels to panel
        statsPanel.add(moneyLabel);
        statsPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        statsPanel.add(dayLabel);
        statsPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        statsPanel.add(tempLabel);
    }
    
    private void createInventoryPanel() {
        inventoryPanel = new JPanel();
        inventoryPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        inventoryPanel.setBorder(BorderFactory.createTitledBorder("Inventory"));
        inventoryPanel.setBackground(new Color(220, 250, 220)); // Light green background
        
        updateInventoryPanel();
    }
    
    private void updateInventoryPanel() {
        inventoryPanel.removeAll();
        
        // Add inventory items
        for (ItemType type : ItemType.values()) {
            JLabel itemLabel = new JLabel(type.getDisplayName() + ": " + inventory.getItemCount(type));
            itemLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            inventoryPanel.add(itemLabel);
        }
        
        inventoryPanel.revalidate();
        inventoryPanel.repaint();
    }
    
    // Game action methods
    private void setGameAction(GameActionsController.ActionType action) {
        // Check if action is valid based on inventory
        switch (action) {
            case PLANT:
                if (inventory.getItemCount(ItemType.SEED) <= 0) {
                    JOptionPane.showMessageDialog(this, "You don't have any seeds to plant!");
                    return;
                }
                break;
            case FERTILIZE:
                if (inventory.getItemCount(ItemType.FERTILIZER) <= 0) {
                    JOptionPane.showMessageDialog(this, "You don't have any fertilizer!");
                    return;
                }
                break;
            case PROTECT:
                if (inventory.getItemCount(ItemType.BUG_KILLER) <= 0) {
                    JOptionPane.showMessageDialog(this, "You don't have any bug killer!");
                    return;
                }
                break;
        }
        
        // Set action
        actionsController.setAction(action);
        currentActionLabel.setText("Current Action: " + action);
        
        // Execute action if any tiles are already selected
        int moneyEarned = actionsController.executeAction(inventory);
        money += moneyEarned;
        
        // Play appropriate sound
        switch (action) {
            case PLANT:
                soundManager.playSound("plant");
                break;
            case WATER:
                soundManager.playSound("water");
                break;
            case HARVEST:
                if (moneyEarned > 0) {
                    soundManager.playSound("harvest");
                }
                break;
            case FERTILIZE:
            case PROTECT:
                // Generic action sound could be added here
                break;
        }
        
        // Update UI
        updateStats();
        updateInventoryPanel();
    }
    
    private void cancelAction() {
        actionsController.setAction(GameActionsController.ActionType.NONE);
        actionsController.deselectAll();
        currentActionLabel.setText("Current Action: None");
    }
    
    private void openShop() {
        // Open shop dialog
        ShopDialog shopDialog = new ShopDialog(this, money, inventory);
        shopDialog.setVisible(true);
        
        // Update money after shopping
        int newMoney = shopDialog.getPlayerMoney();
        
        // Play sound if purchase was made
        if (newMoney < money) {
            soundManager.playSound("buy");
        }
        
        money = newMoney;
        
        // Update UI
        updateStats();
        updateInventoryPanel();
    }
    
    private void endDay() {
        // Display event probabilities before sleeping
        showEventProbabilities();
        
        // Play night sound
        soundManager.playSound("night");
        
        // Simulate night events
        processNightEvents();
        
        // Grow crops
        growCrops();
        
        // Increment day
        day++;
        isDaytime = true;
        
        // Update UI
        updateStats();
        updateInventoryPanel();
        updateFarmVisuals();
        
        // Check win/loss conditions
        checkGameState();
        
        if (!isGameOver()) {
            JOptionPane.showMessageDialog(this, "It's now Day " + day + "!");
        }
    }
    
    private boolean isGameOver() {
        return GameStateChecker.checkWinCondition(money) || 
               GameStateChecker.checkLossCondition(money, farmTiles);
    }
    
    private void checkGameState() {
        // Check for win condition
        if (GameStateChecker.checkWinCondition(money)) {
            soundManager.playSound("win");
            GameOverDialog gameOverDialog = new GameOverDialog(this, true, money, day);
            gameOverDialog.setVisible(true);
        }
        
        // Check for loss condition
        else if (GameStateChecker.checkLossCondition(money, farmTiles)) {
            soundManager.playSound("lose");
            GameOverDialog gameOverDialog = new GameOverDialog(this, false, money, day);
            gameOverDialog.setVisible(true);
        }
    }
    
    private void showEventProbabilities() {
        StringBuilder message = new StringBuilder("Event Probabilities:\n\n");
        
        message.append("Weather:\n");
        message.append("- Good Weather (24-30°C): 15%\n");
        message.append("- Bad Weather (<20°C or >35°C): 15%\n");
        message.append("- Neutral Weather: 70%\n\n");
        
        message.append("Events:\n");
        message.append("- Pest Invasion: 10%\n");
        message.append("- Robbery: 3%\n");
        message.append("- Good Bugs: 7%\n");
        message.append("- Nothing: 80%\n\n");
        
        message.append("Natural Disasters:\n");
        message.append("- Drought: 5%\n");
        message.append("- Flood: 4%\n");
        message.append("- Tornado: 1%\n");
        message.append("- Nothing: 90%\n");
        
        JOptionPane.showMessageDialog(this, message.toString(), "Event Forecast", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void processNightEvents() {
        ArrayList<String> eventMessages = eventSystem.processNightEvents(farmTiles, inventory);
        
        if (!eventMessages.isEmpty()) {
            StringBuilder message = new StringBuilder("Night Events:\n");
            for (String eventMessage : eventMessages) {
                message.append("- ").append(eventMessage).append("\n");
                
                // Play appropriate sound effects based on event type
                if (eventMessage.contains("bug") || eventMessage.contains("Bug")) {
                    soundManager.playSound("bug");
                } else if (eventMessage.contains("flood") || eventMessage.contains("Flood")) {
                    soundManager.playSound("flood");
                } else if (eventMessage.contains("stole") || eventMessage.contains("thieves")) {
                    // Could add a robbery sound here
                }
            }
            
            JOptionPane.showMessageDialog(this, message.toString(), "Night Events", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "The night was peaceful. Nothing happened to your farm!");
        }
    }
    
    private void growCrops() {
        for (CropTile tile : farmTiles) {
            tile.grow();
        }
    }
    
    private void updateStats() {
        moneyLabel.setText("Money: $" + money);
        dayLabel.setText("Day: " + day + (isDaytime ? " (Daytime)" : " (Night)"));
        JLabel tempLabel = (JLabel)statsPanel.getComponent(4);
        tempLabel.setText("Temperature: " + eventSystem.getCurrentTemperature() + "°C");
    }
    
    private void updateFarmVisuals() {
        for (FarmTilePanel panel : tilePanels) {
            panel.update();
        }
    }
    
    private void showInstructions() {
        String instructions = 
            "Welcome to Daniel's Corn Harvesting Simulator!\n\n" +
            "How to play:\n" +
            "1. Plant seeds on empty tiles\n" +
            "2. Water your crops daily to keep them alive\n" +
            "3. Apply fertilizer to make them grow faster\n" +
            "4. Protect crops from pests using bug killer\n" +
            "5. Harvest mature corn for money\n" +
            "6. Buy upgrades and supplies from the shop\n" +
            "7. End each day to progress, but beware of night events!\n\n" +
            "Good luck with your farm!";
        
        JOptionPane.showMessageDialog(this, instructions, "Game Instructions", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Show start menu first
        SwingUtilities.invokeLater(() -> new StartMenu());
    }
}