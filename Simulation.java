public class Simulation {
    // set up simulation (constructor)
        // initialize simUtils
        // initialize DataNodes
        // initialize Sink

    SimUtils util; 
    int numSteps; 
    DataCollector[] nodes; 
    Sink dataSink;

    private double avgTemp; 
    private double avgPress; 
    private double avgLatency;
    double LATENCY_FACTOR = 0.01; 

    public Simulation(double gridSizeX, double gridSizeY, double maxTemp, double maxPressure, double batteryLife, double sinkBatteryLife, double transmissionCost, double receptionCost, int numNodes, int numTimeSteps) {
        util = new SimUtils(gridSizeX, gridSizeY, maxTemp, maxPressure); 
        numSteps = numTimeSteps; 
        nodes = new DataCollector[numNodes]; 
        for(int i = 0; i < numNodes; i++) {
            nodes[i] = new DataCollector(util.generateX(), util.generateY(),batteryLife, transmissionCost); 
        }

        dataSink = new Sink(util.generateX(), util.generateY(), sinkBatteryLife, receptionCost, nodes); 
        numSteps = numTimeSteps; 
        avgTemp = 0.0; 
        avgPress = 0.0; 
    }


    // set up time step loop 
    public void runSimulation() {
        System.out.println("Node data shown in format: [TEMP, PRESSURE, XPosition, YPosition, BatteryLife]");
        for(int i = 0; i < numSteps; i++){
            double currentStepTempAvg = 0.0; 
            double currentStepPressAvg = 0.0; 
            if (dataSink.getBatteryLife() <= 0.0) {
                System.out.println("SINK BATTERY DEAD: ENDING SIMULATION");
                break;
            }
            // Each node collect data
            for(DataCollector n : nodes) {
                n.readTemp(util.generateTemp()); 
                n.readPressure(util.generatePressure()); 
                currentStepTempAvg += n.getTemp(); 
                currentStepPressAvg += n.getPressure();
            }
            // Sink pulls data 
            dataSink.getReadings();
            System.out.println("Simulation at timestep: " + i );
            System.out.println("SINK Battery Life: " + dataSink.getBatteryLife());

            // Print data and do time step analytics calculations 
            dataSink.printNodeStates();
            System.out.println("Step Avg Data: [TEMP: " + (currentStepTempAvg/nodes.length) + ", PRESS: " + (currentStepPressAvg/nodes.length) + "]");
            avgTemp += currentStepTempAvg/nodes.length; 
            avgPress += currentStepPressAvg/nodes.length; 
        } 

        // Overall simulation analysis calculations 
        System.out.println("--------------------------------------------");
        System.out.println("SIMULATION Avg Data: [TEMP: " + (avgTemp/numSteps) + ", PRESS: " + (avgPress/numSteps) + "]");
        System.out.println("SIMULATION Avg Latency: " + calculateAverageLatency());
        System.out.println("--------------------------------------------");
    }

    //Avg Latency (MAKE RANDOM IN FUTURE)
    // LATENCY IS CURRENTLY CALCULATRED BY A DISTANCE MULTIPLIED BY A LATENCY FACTOR TO SIMULATE LONGER LATENCIES FOR FURTHER AWAY NODES
    public double calculateAverageLatency(){
        for(int i = 0; i < nodes.length; i++){
            double nodeLatency = util.calcDistance(nodes[i].getX(), nodes[i].getY(), dataSink.getX(), dataSink.getY()) * LATENCY_FACTOR; 
            avgLatency += nodeLatency; 
        }
        return avgLatency/nodes.length; 
    }
     
    // helper funcs if needed 
}
