import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;


// Custom farm tile panel that shows crop visually
class FarmTilePanel extends JPanel {
    private CropTile tile;
    private boolean selected;
    private Color emptyColor = new Color(200, 180, 150); // Light brown for soil
    private Color seedColor = new Color(120, 100, 80);   // Dark brown for planted seed
    private Color seedlingColor = new Color(150, 220, 150); // Light green for seedling
    private Color growingColor = new Color(100, 180, 100);  // Medium green for growing
    private Color matureColor = new Color(220, 220, 100);   // Yellow for mature corn
    private Color deadColor = new Color(150, 100, 80);      // Brown for dead plants
    private Color wateredOverlay = new Color(100, 100, 255, 50); // Blue tint for watered
    private Color selectedBorder = new Color(255, 100, 100); // Red for selected
    private Color protectedBorder = new Color(100, 255, 100); // Green for protected
    
    public FarmTilePanel(CropTile tile) {
        this.tile = tile;
        this.selected = false;
        
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setPreferredSize(new Dimension(64, 64));
        
        // Add mouse listener for selection
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selected = !selected;
                tile.setSelected(selected);
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Fill background based on crop stage
        Color baseColor;
        switch (tile.getStage()) {
            case EMPTY:
                baseColor = emptyColor;
                break;
            case SEED:
                baseColor = seedColor;
                break;
            case SEEDLING:
                baseColor = seedlingColor;
                break;
            case GROWING:
                baseColor = growingColor;
                break;
            case MATURE:
                baseColor = matureColor;
                break;
            case DEAD:
                baseColor = deadColor;
                break;
            default:
                baseColor = emptyColor;
        }
        
        g.setColor(baseColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        // Draw crop
        if (tile.getStage() != CropStage.EMPTY && tile.getStage() != CropStage.DEAD) {
            drawCrop(g);
        }
        
        // Draw watered overlay
        if (tile.isWatered()) {
            g.setColor(wateredOverlay);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        
        // Draw selection border
        if (selected) {
            g.setColor(selectedBorder);
            g.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
            g.drawRect(2, 2, getWidth() - 5, getHeight() - 5);
        }
        
        // Draw protected border
        if (tile.isProtected()) {
            g.setColor(protectedBorder);
            Graphics2D g2d = (Graphics2D) g;
            Stroke oldStroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
            g2d.drawRect(3, 3, getWidth() - 7, getHeight() - 7);
            g2d.setStroke(oldStroke);
        }
    }
    
    private void drawCrop(Graphics g) {
        int centerX = getWidth() / 2;
        int width, height;
        
        switch (tile.getStage()) {
            case SEED:
                // Draw small dot
                g.setColor(Color.BLACK);
                g.fillOval(centerX - 3, getHeight() - 10, 6, 6);
                break;
                
            case SEEDLING:
                // Draw small plant
                g.setColor(new Color(0, 120, 0));
                g.fillRect(centerX - 1, getHeight() - 20, 2, 15);
                g.fillOval(centerX - 5, getHeight() - 25, 10, 10);
                break;
                
            case GROWING:
                // Draw medium plant
                g.setColor(new Color(0, 100, 0));
                g.fillRect(centerX - 2, getHeight() - 30, 4, 25);
                
                // Leaves
                g.fillOval(centerX - 10, getHeight() - 35, 8, 15);
                g.fillOval(centerX + 2, getHeight() - 30, 8, 15);
                g.fillOval(centerX - 8, getHeight() - 25, 8, 15);
                break;
                
            case MATURE:
                // Draw full corn plant
                g.setColor(new Color(0, 100, 0));
                g.fillRect(centerX - 2, getHeight() - 40, 4, 35);
                
                // Leaves
                g.fillOval(centerX - 15, getHeight() - 40, 12, 20);
                g.fillOval(centerX + 3, getHeight() - 45, 12, 20);
                g.fillOval(centerX - 12, getHeight() - 30, 10, 15);
                
                // Corn cob
                g.setColor(new Color(240, 240, 100));
                g.fillOval(centerX - 3, getHeight() - 35, 8, 20);
                break;
        }
    }
    
    public void update() {
        repaint();
    }
    
    public boolean isSelected() {
        return selected;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
        this.tile.setSelected(selected);
        repaint();
    }
    
    public CropTile getTile() {
        return tile;
    }
}