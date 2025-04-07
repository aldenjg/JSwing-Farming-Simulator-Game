import java.util.Random;
// Class to represent a single farm tile with crop
class CropTile {
    private CropStage stage;
    private int growthProgress; // 0-100
    private boolean isWatered;
    private boolean hasFertilizer;
    private boolean isProtected; // Protected by bug killer
    private boolean isSelected;
    private int growthDelay; // Days of growth delay
    private int growthAcceleration; // Days of growth acceleration
    
    public CropTile() {
        stage = CropStage.EMPTY;
        growthProgress = 0;
        isWatered = false;
        hasFertilizer = false;
        isProtected = false;
        isSelected = false;
        growthDelay = 0;
        growthAcceleration = 0;
    }
    
    public boolean isEmpty() {
        return stage == CropStage.EMPTY;
    }
    
    public boolean isMature() {
        return stage == CropStage.MATURE;
    }
    
    public boolean isDead() {
        return stage == CropStage.DEAD;
    }
    
    public void plant() {
        if (isEmpty()) {
            stage = CropStage.SEED;
            growthProgress = 0;
        }
    }
    
    public void water() {
        isWatered = true;
    }
    
    public void applyFertilizer() {
        hasFertilizer = true;
    }
    
    public void applyBugKiller() {
        isProtected = true;
    }
    
    public void harvest() {
        stage = CropStage.EMPTY;
        growthProgress = 0;
        isWatered = false;
        hasFertilizer = false;
        isProtected = false;
    }
    
    public void accelerateGrowth(int days) {
        growthAcceleration += days;
    }
    
    public void delayGrowth(int days) {
        growthDelay += days;
    }
    
    public void grow() {
        if (stage == CropStage.EMPTY || stage == CropStage.DEAD) {
            return;
        }
        
        // If not watered, chance to die
        if (!isWatered) {
            if (new Random().nextInt(100) < 40) { // 40% chance to die without water
                stage = CropStage.DEAD;
                return;
            }
        }
        
        // Reset watered status for next day
        isWatered = false;
        
        // Calculate growth amount
        int growthAmount = hasFertilizer ? 20 : 10; // Fertilizer doubles growth rate
        
        // Apply growth acceleration/delay
        if (growthAcceleration > 0) {
            growthAmount *= 2; // Double growth rate
            growthAcceleration--;
        }
        if (growthDelay > 0) {
            growthAmount = 0; // No growth
            growthDelay--;
        }
        
        // Add growth
        growthProgress += growthAmount;
        
        // Update stage based on growth progress
        if (growthProgress >= 100) {
            stage = CropStage.MATURE;
        } else if (growthProgress >= 70) {
            stage = CropStage.GROWING;
        } else if (growthProgress >= 30) {
            stage = CropStage.SEEDLING;
        }
        
        // Reset fertilizer effect (one-time use)
        hasFertilizer = false;
    }
    
    public CropStage getStage() {
        return stage;
    }
    
    public void setStage(CropStage stage) {
        this.stage = stage;
    }
    
    public int getGrowthProgress() {
        return growthProgress;
    }
    
    public boolean isWatered() {
        return isWatered;
    }
    
    public boolean isProtected() {
        return isProtected;
    }
    
    public void setProtected(boolean isProtected) {
        this.isProtected = isProtected;
    }
    
    public boolean isSelected() {
        return isSelected;
    }
    
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}