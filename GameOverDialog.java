import java.awt.*;
import javax.swing.*;


// Game over dialog
class GameOverDialog extends JDialog {
    private boolean isWin;
    private int finalMoney;
    private int daysSurvived;
    private JFrame parentFrame;
    
    public GameOverDialog(JFrame parent, boolean isWin, int finalMoney, int daysSurvived) {
        super(parent, isWin ? "Victory!" : "Game Over", true);
        this.parentFrame = parent;
        this.isWin = isWin;
        this.finalMoney = finalMoney;
        this.daysSurvived = daysSurvived;
        
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        createUI();
    }
    
    private void createUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title label
        JLabel titleLabel = new JLabel(isWin ? "Victory!" : "Game Over");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Summary label
        JLabel summaryLabel = new JLabel();
        if (isWin) {
            summaryLabel.setText("Congratulations! You've become a successful corn farmer!");
        } else {
            summaryLabel.setText("Your farm has failed. Better luck next time!");
        }
        summaryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        summaryLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        // Stats panel
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(2, 1, 5, 5));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Stats"));
        
        JLabel moneyLabel = new JLabel("Final Money: $" + finalMoney);
        JLabel daysLabel = new JLabel("Days Survived: " + daysSurvived);
        
        statsPanel.add(moneyLabel);
        statsPanel.add(daysLabel);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton playAgainButton = new JButton("Play Again");
        JButton exitButton = new JButton("Exit");
        
        playAgainButton.addActionListener(e -> {
            dispose();
            parentFrame.dispose();
            SwingUtilities.invokeLater(() -> new CornHarvestGame());
        });
        
        exitButton.addActionListener(e -> {
            dispose();
            parentFrame.dispose();
            SwingUtilities.invokeLater(() -> new StartMenu());
        });
        
        buttonPanel.add(playAgainButton);
        buttonPanel.add(exitButton);
        
        // Add components to panel
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(summaryLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(statsPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(buttonPanel);
        
        add(panel);
    }
}
