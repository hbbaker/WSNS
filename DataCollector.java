/**
 * Implementation of a Data Collection Node class 
 */
public class DataCollector extends Node {

    private double tempReading = 0.0; 
    private double pressureReading = 0.0; 
    private final double transmissionCost; 
    
    public DataCollector(double initialX, double initialY, double batteryLife, double transmissionCost) {
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

    public Double[] getReadings() {
        battery.updateBatteryLife(battery.getBatteryLife() - transmissionCost);
        return new Double[] {getTemp(), getPressure(), getX(), getY(), battery.getBatteryLife()}; 
    }
    
}
