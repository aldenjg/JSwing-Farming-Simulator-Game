import java.util.ArrayList;

// Game win/loss conditions checker
class GameStateChecker {
    private static final int WIN_MONEY_THRESHOLD = 5000;
    private static final int BANKRUPTCY_THRESHOLD = -500;
    
    public static boolean checkWinCondition(int money) {
        return money >= WIN_MONEY_THRESHOLD;
    }
    
    public static boolean checkLossCondition(int money, ArrayList<CropTile> farmTiles) {
        // Check for bankruptcy
        if (money <= BANKRUPTCY_THRESHOLD) {
            return true;
        }
        
        // Check if all crops are dead and no money to buy seeds
        if (money < ItemType.SEED.getPrice()) {
            boolean allEmpty = true;
            for (CropTile tile : farmTiles) {
                if (!tile.isEmpty() && !tile.isDead()) {
                    allEmpty = false;
                    break;
                }
            }
            return allEmpty;
        }
        
        return false;
    }
}
