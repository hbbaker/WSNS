/**
 * Main Entry point of the simulation... currently you must edit the main file in order to change the simulation parameters. 
 * I will be adding support for command line arguments on the next assignment that I use this tool for! 
 */
public class Main {
    public static void main(String[] args) {
        Simulation sim = new Simulation(10000.0, 10000.0, 100.0, 500.0, 50.0, 1000.0, 0.05, 0.05, 1000, 10); 
        sim.runSimulation(); 
    }
}
