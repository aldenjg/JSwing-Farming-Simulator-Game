import java.awt.*;
import java.util.HashMap;
import javax.swing.*;

// Shop dialog for purchasing items
class ShopDialog extends JDialog {
    private int playerMoney;
    private Inventory inventory;
    private HashMap<ItemType, JSpinner> quantitySpinners;
    private JLabel totalCostLabel;
    private int totalCost;
    
    public ShopDialog(JFrame parent, int playerMoney, Inventory inventory) {
        super(parent, "Farm Shop", true);
        this.playerMoney = playerMoney;
        this.inventory = inventory;
        this.quantitySpinners = new HashMap<>();
        this.totalCost = 0;
        
        setSize(400, 400);
        setLocationRelativeTo(parent);
        
        // Create UI
        createShopUI();
    }
    
    private void createShopUI() {
        setLayout(new BorderLayout());
        
        // Create shop items panel
        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        
        // Add header
        JPanel headerPanel = new JPanel(new GridLayout(1, 4));
        headerPanel.add(new JLabel("Item"));
        headerPanel.add(new JLabel("Price"));
        headerPanel.add(new JLabel("In Stock"));
        headerPanel.add(new JLabel("Buy Quantity"));
        itemsPanel.add(headerPanel);
        
        // Add each shop item
        for (ItemType itemType : ItemType.values()) {
            JPanel itemPanel = new JPanel(new GridLayout(1, 4));
            
            // Item name
            itemPanel.add(new JLabel(itemType.getDisplayName()));
            
            // Item price
            itemPanel.add(new JLabel("$" + itemType.getPrice()));
            
            // Current quantity
            itemPanel.add(new JLabel(String.valueOf(inventory.getItemCount(itemType))));
            
            // Buy quantity spinner
            JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
            quantitySpinner.addChangeListener(e -> updateTotalCost());
            quantitySpinners.put(itemType, quantitySpinner);
            itemPanel.add(quantitySpinner);
            
            itemsPanel.add(itemPanel);
        }
        
        // Add scrolling if needed
        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        add(scrollPane, BorderLayout.CENTER);
        
        // Create bottom panel with total cost and buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        // Total cost label
        totalCostLabel = new JLabel("Total Cost: $0");
        JPanel costPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        costPanel.add(totalCostLabel);
        costPanel.add(new JLabel("    Available Money: $" + playerMoney));
        bottomPanel.add(costPanel, BorderLayout.NORTH);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton buyButton = new JButton("Buy");
        JButton cancelButton = new JButton("Cancel");
        
        buyButton.addActionListener(e -> processPurchase());
        cancelButton.addActionListener(e -> dispose());
        
        buttonsPanel.add(buyButton);
        buttonsPanel.add(cancelButton);
        bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void updateTotalCost() {
        totalCost = 0;
        
        for (ItemType itemType : ItemType.values()) {
            JSpinner spinner = quantitySpinners.get(itemType);
            int quantity = (Integer) spinner.getValue();
            totalCost += quantity * itemType.getPrice();
        }
        
        totalCostLabel.setText("Total Cost: $" + totalCost);
    }
    
    private void processPurchase() {
        if (totalCost > playerMoney) {
            JOptionPane.showMessageDialog(this, "You don't have enough money for this purchase!");
            return;
        }
        
        if (totalCost == 0) {
            JOptionPane.showMessageDialog(this, "You haven't selected any items to buy!");
            return;
        }
        
        // Process the purchase
        for (ItemType itemType : ItemType.values()) {
            JSpinner spinner = quantitySpinners.get(itemType);
            int quantity = (Integer) spinner.getValue();
            
            if (quantity > 0) {
                inventory.addItem(itemType, quantity);
            }
        }
        
        // Deduct money
        playerMoney -= totalCost;
        
        JOptionPane.showMessageDialog(this, "Purchase successful! You spent $" + totalCost);
        dispose();
    }
    
    public int getPlayerMoney() {
        return playerMoney;
    }
}