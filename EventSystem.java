import java.util.ArrayList;
import java.util.Random;

// Class to manage random events at night
class EventSystem {
    private Random random;
    
    // Weather probabilities
    private int goodWeatherChance = 15;
    private int badWeatherChance = 15;
    private int neutralWeatherChance = 70;
    
    // Event probabilities
    private int pestInvasionChance = 10;
    private int robberyChance = 3;
    private int goodBugsChance = 7;
    private int nothingEventChance = 80;
    
    // Natural disaster probabilities
    private int droughtChance = 5;
    private int floodChance = 4;
    private int tornadoChance = 1;
    private int nothingDisasterChance = 90;
    
    // Weather state
    private int currentTemperature;
    private boolean isDroughtActive;
    private int droughtDaysRemaining;
    
    public EventSystem() {
        random = new Random();
        isDroughtActive = false;
        droughtDaysRemaining = 0;
        updateWeather();
    }
    
    private void updateWeather() {
        int weatherRoll = random.nextInt(100);
        if (weatherRoll < goodWeatherChance) {
            // Good weather: 24-30°C
            currentTemperature = 24 + random.nextInt(7);
        } else if (weatherRoll < goodWeatherChance + badWeatherChance) {
            // Bad weather: <20°C or >35°C
            currentTemperature = random.nextBoolean() ? 
                random.nextInt(20) : 35 + random.nextInt(10);
        } else {
            // Neutral weather: 20-24°C or 30-35°C
            currentTemperature = random.nextBoolean() ?
                20 + random.nextInt(5) : 30 + random.nextInt(6);
        }
    }
    
    public ArrayList<String> processNightEvents(ArrayList<CropTile> farmTiles, Inventory inventory) {
        ArrayList<String> eventMessages = new ArrayList<>();
        
        // Process weather effects
        if (currentTemperature >= 24 && currentTemperature <= 30) {
            // Good weather: reduce growth time by 2 days
            for (CropTile tile : farmTiles) {
                if (!tile.isEmpty() && !tile.isDead()) {
                    tile.accelerateGrowth(2);
                }
            }
            eventMessages.add("Good weather today! Crops grew faster.");
        } else if (currentTemperature < 20 || currentTemperature > 35) {
            // Bad weather: increase growth time by 1 day
            for (CropTile tile : farmTiles) {
                if (!tile.isEmpty() && !tile.isDead()) {
                    tile.delayGrowth(1);
                }
            }
            eventMessages.add("Bad weather today! Crops grew slower.");
        }
        
        // Process drought if active
        if (isDroughtActive) {
            droughtDaysRemaining--;
            if (droughtDaysRemaining <= 0) {
                isDroughtActive = false;
                eventMessages.add("The drought has ended!");
            } else {
                eventMessages.add("Drought continues! No water available for " + droughtDaysRemaining + " more days.");
            }
        }
        
        // Process events
        int eventRoll = random.nextInt(100);
        if (eventRoll < pestInvasionChance) {
            // Pest invasion: destroys 1-2 corn plants
            int plantsToDestroy = 1 + random.nextInt(2);
            int destroyed = 0;
            for (CropTile tile : farmTiles) {
                if (!tile.isEmpty() && !tile.isDead() && !tile.isProtected() && destroyed < plantsToDestroy) {
                    tile.setStage(CropStage.DEAD);
                    destroyed++;
                }
            }
            if (destroyed > 0) {
                eventMessages.add("Pest invasion! " + destroyed + " crops were destroyed.");
            }
        } else if (eventRoll < pestInvasionChance + robberyChance) {
            // Robbery: lose 10-50% of money/inventory
            int percentage = 10 + random.nextInt(41); // 10-50%
            int moneyLost = (int)(inventory.getTotalValue() * percentage / 100.0);
            inventory.removeRandomItems(percentage);
            eventMessages.add("Robbery! Lost " + percentage + "% of your inventory and money.");
        } else if (eventRoll < pestInvasionChance + robberyChance + goodBugsChance) {
            // Good bugs: reduces growth time by 1 day
            for (CropTile tile : farmTiles) {
                if (!tile.isEmpty() && !tile.isDead()) {
                    tile.accelerateGrowth(1);
                }
            }
            eventMessages.add("Good bugs visited! Crops grew faster.");
        }
        
        // Process natural disasters
        int disasterRoll = random.nextInt(100);
        if (disasterRoll < droughtChance) {
            // Drought: stops growth for 2 days
            isDroughtActive = true;
            droughtDaysRemaining = 2;
            eventMessages.add("Drought has started! No water available for 2 days.");
        } else if (disasterRoll < droughtChance + floodChance) {
            // Flood: destroys 25% of planted crops
            int totalCrops = 0;
            for (CropTile tile : farmTiles) {
                if (!tile.isEmpty() && !tile.isDead()) {
                    totalCrops++;
                }
            }
            int cropsToDestroy = (int)(totalCrops * 0.25);
            int destroyed = 0;
            for (CropTile tile : farmTiles) {
                if (!tile.isEmpty() && !tile.isDead() && destroyed < cropsToDestroy) {
                    tile.setStage(CropStage.DEAD);
                    destroyed++;
                }
            }
            if (destroyed > 0) {
                eventMessages.add("Flood! " + destroyed + " crops were destroyed.");
            }
        } else if (disasterRoll < droughtChance + floodChance + tornadoChance) {
            // Tornado: lose everything except money
            for (CropTile tile : farmTiles) {
                tile.setStage(CropStage.EMPTY);
            }
            inventory.clear();
            eventMessages.add("Tornado! All crops and inventory were lost!");
        }
        
        // Update weather for next day
        updateWeather();
        
        return eventMessages;
    }
    
    public int getCurrentTemperature() {
        return currentTemperature;
    }
    
    public boolean isDroughtActive() {
        return isDroughtActive;
    }
    
    public int getDroughtDaysRemaining() {
        return droughtDaysRemaining;
    }
}