/**
 * Main Entry point of the simulation... currently you must edit the main file in order to change the simulation parameters. 
 * I will be adding support for command line arguments on the next assignment that I use this tool for! 
 */
public class Main {
    public static void main(String[] args) { 

        System.out.println("Running simple baseline simulation");
        Simulation sim = new Simulation(1000.0, 1000.0, 100.0, 50.0, 100.0, 400.0, 0.01, 0.01, 30, 5, 10000); 
        sim.runClusterSimulation();
        System.out.println("Simulation Finished");

        System.out.println("Running simulation to test killing data nodes");
        sim = new Simulation(1000.0, 1000.0, 100.0, 50.0, 100.0, 200.0, 1.5, 0.01, 10, 2, 1000); 
        sim.runClusterSimulation();
        System.out.println("Simulation Finished");

        System.out.println("Running simulation to test killing sink nodes");
        sim = new Simulation(1000.0, 1000.0, 100.0, 50.0, 100.0, 200.0, 0.01, 1.00, 300, 5, 100); 
        sim.runClusterSimulation();
        System.out.println("Simulation Finished");

        System.out.println("Running simulation to test intrusion detection");
        sim = new Simulation(1000.0, 1000.0, 100.0, 50.0, 100.0, 200.0, 1.5, 0.01, 10, 2, 100); 
        sim.nodes.add(new DataCollector(100, 300.00, 30.00, 100, 0));
        sim.runClusterSimulation();
        System.out.println("Simulation Finished");
    }
}
