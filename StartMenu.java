import java.awt.*;
import javax.swing.*;

// Start menu class
class StartMenu extends JFrame {
    public StartMenu() {
        setTitle("Daniel's Corn Harvesting Simulator");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create content panel with background
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Create a gradient background
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(230, 250, 230),
                    0, getHeight(), new Color(180, 220, 180));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw corn stalks
                g.setColor(new Color(0, 120, 0));
                for (int i = 0; i < 10; i++) {
                    int x = 50 + i * 60;
                    g.fillRect(x, 150, 8, 200);
                    g.fillOval(x - 10, 120, 15, 40);
                    g.fillOval(x + 3, 100, 15, 40);
                    
                    // Draw corn
                    g.setColor(new Color(240, 240, 100));
                    g.fillOval(x - 2, 170, 12, 30);
                    g.setColor(new Color(0, 120, 0));
                }
            }
        };
        contentPanel.setLayout(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel("Daniel's Corn Harvesting Simulator");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Grow, harvest, and protect your corn farm!");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        titlePanel.add(Box.createVerticalGlue());
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        titlePanel.add(subtitleLabel);
        titlePanel.add(Box.createVerticalGlue());
        
        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        
        JButton playButton = new JButton("Play Game");
        JButton settingsButton = new JButton("Settings");
        JButton exitButton = new JButton("Exit");
        
        // Style buttons
        Dimension buttonSize = new Dimension(150, 40);
        playButton.setMaximumSize(buttonSize);
        settingsButton.setMaximumSize(buttonSize);
        exitButton.setMaximumSize(buttonSize);
        
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        playButton.setFont(new Font("Arial", Font.BOLD, 14));
        settingsButton.setFont(new Font("Arial", Font.PLAIN, 14));
        exitButton.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Add action listeners
        playButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new CornHarvestGame());
        });
        
        settingsButton.addActionListener(e -> showSettings());
        
        exitButton.addActionListener(e -> System.exit(0));
        
        // Add buttons to panel
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(playButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(settingsButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(exitButton);
        buttonPanel.add(Box.createVerticalGlue());
        
        // Add panels to content panel
        contentPanel.add(titlePanel, BorderLayout.NORTH);
        contentPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // Add content panel to frame
        add(contentPanel);
        
        setVisible(true);
    }
    
    private void showSettings() {
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        
        JLabel difficultyLabel = new JLabel("Difficulty:");
        String[] difficulties = {"Easy", "Normal", "Hard"};
        JComboBox<String> difficultyComboBox = new JComboBox<>(difficulties);
        difficultyComboBox.setSelectedIndex(1); // Default to Normal
        
        JLabel soundLabel = new JLabel("Sound Effects:");
        JCheckBox soundCheckBox = new JCheckBox("Enabled");
        soundCheckBox.setSelected(true);
        
        JLabel musicLabel = new JLabel("Background Music:");
        JCheckBox musicCheckBox = new JCheckBox("Enabled");
        musicCheckBox.setSelected(true);
        
        // Add components to panel
        settingsPanel.add(difficultyLabel);
        settingsPanel.add(difficultyComboBox);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        settingsPanel.add(soundLabel);
        settingsPanel.add(soundCheckBox);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        settingsPanel.add(musicLabel);
        settingsPanel.add(musicCheckBox);
        
        JOptionPane.showMessageDialog(this, settingsPanel, "Game Settings", JOptionPane.PLAIN_MESSAGE);
    }
}