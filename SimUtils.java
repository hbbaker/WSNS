import java.util.Random;

/**
 * Utility class to help with the Simulation code 
 */
public class SimUtils {

    Random rng; 
    double gridSizeX; 
    double gridSizeY; 
    double maxTemp; 
    double maxPressure; 

    public SimUtils(double gX, double gY, double maxT, double maxP) {
        rng = new Random(); 
        gridSizeX = gX; 
        gridSizeY = gY; 
        maxTemp = maxT; 
        maxPressure = maxP; 
    }

    public double generateX() {
        return rng.nextDouble(gridSizeX);
    }

    public double generateY() {
        return rng.nextDouble(gridSizeY);
    }
    
    public double generateTemp() {
        return rng.nextDouble(maxTemp);
    }

    public double generatePressure() {
        return rng.nextDouble(maxPressure);
    }

    public double calcDistance(double pos1X, double pos1Y, double pos2X, double pos2Y) {
        return Math.sqrt(Math.pow(pos2X - pos1X, 2) + Math.pow(pos2Y - pos1Y, 2)); 
    }

    public int randomRange(int max) {
        return rng.nextInt(max); 
    }
}
