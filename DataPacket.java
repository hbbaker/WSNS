public class DataPacket {
    private double temp; 
    private double pressure; 
    private double battery; 
    private double x; 
    private double y; 
    private int id; 

    public DataPacket(int id, double t, double p, double x, double y, double battery){
        this.id = id; 
        this.temp = t; 
        this.pressure = p; 
        this.x = x; 
        this.y = y; 
        this.battery = battery; 
    }

    public int getID() {
        return id; 
    }

    public double getTemp(){
        return temp;
    }
    public double getPressure(){
        return pressure;
    }
    public double getX(){
        return x; 
    }
    public double getY(){
        return y; 
    }

    public double getBattery() {
        return battery; 
    }
}
