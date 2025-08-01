/**
 * Simulates a battery component for Node types. (Will support more module types going forward)
 */
public class Battery {
    private double batteryLife; 
    
    public Battery (double startingBatteryLife){
        batteryLife = startingBatteryLife; 
    }

    public void updateBatteryLife(double newBatteryLife){
        batteryLife = newBatteryLife; 
    }

    public double getBatteryLife() {
        return batteryLife; 
    }

    public void kill() {
        batteryLife = 0.0; 
    }
}
