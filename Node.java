/**
 * Abstract base type for Nodes (Can be used to implement new types of nodes in the future)
 */
public abstract class Node {
    protected Battery battery;  
    protected double x; 
    protected  double y; 

    public double getBatteryLife() {
        return battery.getBatteryLife(); 
    }

    public void setPosition(float x, float y) {
        this.x = x; 
        this.y = y; 
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y; 
    }
}
