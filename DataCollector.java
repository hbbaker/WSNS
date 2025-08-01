/**
 * Implementation of a Data Collection Node class 
 */
public class DataCollector extends Node {

    private final int nodeID; 
    private double tempReading = 0.0; 
    private double pressureReading = 0.0; 
    private final double transmissionCost; 
    
    public DataCollector(int id, double initialX, double initialY, double batteryLife, double transmissionCost) {
        nodeID = id; 
        this.x = initialX; 
        this.y = initialY; 
        this.battery = new Battery(batteryLife);
        this.transmissionCost = transmissionCost;  
    }

    public void readTemp(double temp) {
        if (battery.getBatteryLife() < 0.0) {
            tempReading = 0.0; 
        } else {
            tempReading = temp;
        }
         
    }

    public void readPressure(double pressure) {
        if (battery.getBatteryLife() < 0.0) {
            pressureReading = 0.0; 
        } else {
            pressureReading = pressure;
        } 
    }

    public double getTemp() {
        return tempReading; 
    }

    public double getPressure() {
        return pressureReading; 
    }

    public int getID() {
        return nodeID; 
    }

    public DataPacket getReadings() {
        if (battery.getBatteryLife() <= 0.0) {
            return null; 
        } else {
            battery.updateBatteryLife(battery.getBatteryLife() - transmissionCost);
            return new DataPacket(nodeID, getTemp(), getPressure(), getX(), getY(), battery.getBatteryLife()); 
        } 
    }
}
