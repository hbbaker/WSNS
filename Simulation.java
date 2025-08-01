
import java.util.ArrayList;



public class Simulation {
    // set up simulation (constructor)
        // initialize simUtils
        // initialize DataNodes
        // initialize Sink

    SimUtils util; 
    int numSteps; 
    public ArrayList<DataCollector> nodes; 

    // EDIT: Generate multiple sinks, generate data collectors, network will connect the closest nodes to the sinks, sinks will aggregate 
    // and transmit to the top level network

    // The node and sink arrays are made public so that I can simulate injecting nodes into the system in a nice way on multiple test runs 
    Sink dataSink;
    public ArrayList<Sink> dataSinks; 
    ArrayList<Node> deadNodes; 
    ArrayList<Integer> allowedIDs;

    private double avgTemp; 
    private double avgPress; 
    private double avgLatency;
    double LATENCY_FACTOR = 0.01; 
    private final int killStep; 

    // Original deprecated code from HW1 implementation 

    // public Simulation(double gridSizeX, double gridSizeY, double maxTemp, double maxPressure, double batteryLife, double sinkBatteryLife, double transmissionCost, double receptionCost, int numNodes, int numTimeSteps) {
    //     util = new SimUtils(gridSizeX, gridSizeY, maxTemp, maxPressure); 
    //     numSteps = numTimeSteps; 
    //     nodes = new ArrayList<>(numNodes); 
    //     for(int i = 0; i < numNodes; i++) {
    //         nodes.add(new DataCollector(i, util.generateX(), util.generateY(), batteryLife, transmissionCost)); 
    //     }

    //     dataSink = new Sink(0, util.generateX(), util.generateY(), sinkBatteryLife, receptionCost, nodes); 
    //     numSteps = numTimeSteps; 
    //     avgTemp = 0.0; 
    //     avgPress = 0.0; 
    // }

    public Simulation(double gridSizeX, double gridSizeY, double maxTemp, double maxPressure, double batteryLife, double sinkBatteryLife, double transmissionCost, double receptionCost, int numNodes, int numSinks, int numTimeSteps) {
        util = new SimUtils(gridSizeX, gridSizeY, maxTemp, maxPressure); 
        numSteps = numTimeSteps; 
        nodes = new ArrayList<>(numNodes); 
        deadNodes = new ArrayList<>(); 
        dataSinks = new ArrayList<>(numSinks);
        allowedIDs = new ArrayList<>(); 
        killStep = util.randomRange(numSteps);  
        for(int i = 0; i < numNodes; i++) {
            nodes.add(new DataCollector(i, util.generateX(), util.generateY(), batteryLife, transmissionCost)); 
            allowedIDs.add(nodes.get(i).getID());
        }

        for(int i = 0; i < numSinks; i++) {
            dataSinks.add(new Sink(i, util.generateX(), util.generateY(), sinkBatteryLife, receptionCost, new ArrayList<>())); 
        }
    }

    public void runClusterSimulation() {
        boolean killedSink = false; 
        boolean topoThisRun = false; 

        topologizeNetwork(nodes, dataSinks);
        for(int i = 0; i < dataSinks.size(); i++){
            System.out.println("SINK " + dataSinks.get(i).getID() + ": (X: " + dataSinks.get(i).getX() + ", Y: " + dataSinks.get(i).getY() + ")");
            ArrayList<DataCollector> connectedNodes = dataSinks.get(i).getConnectedNodes(); 
            for(int j = 0; j < connectedNodes.size(); j++) {
                System.out.println("\t Connected Node ID: " + connectedNodes.get(j).getID() + " (X: " + connectedNodes.get(j).getX() + ", Y: " + connectedNodes.get(j).getY());
            }
        }

        for(int i = 0; i < numSteps; i++) {
            System.out.println("Time Step: " + i);

            // check for dead sinks and retopologize
            if (dataSinks.isEmpty()) {
                System.out.println("Ending Sim: All Sinks dead");
                break; 
            }
            for(int j = 0; j < dataSinks.size(); j++) {
                if(dataSinks.get(j).battery.getBatteryLife() <= 0.0){
                    deadNodes.add(dataSinks.get(j)); 
                    dataSinks.remove(dataSinks.get(j));
                    if (dataSinks.isEmpty()) {
                        System.out.println("Ending Sim: All Sinks dead");
                        break; 
                    }
                    clearConnectedNodes();
                    topologizeNetwork(nodes, dataSinks);
                    topoThisRun = true; 
                }
            }

            // move the sinks every 20 steps
            if (i % 10 == 0 && i != 0 && topoThisRun != true) {
                clearConnectedNodes();
                System.out.println("Moving Sink Nodes...");
                for(Sink s : dataSinks){
                    s.updateX(util.generateX());
                    s.updateY(util.generateY());
                }
                topologizeNetwork(nodes, dataSinks);
                for(int j = 0; j < dataSinks.size(); j++){
                    System.out.println("SINK " + dataSinks.get(j).getID() + ": (X: " + dataSinks.get(j).getX() + ", Y: " + dataSinks.get(j).getY() + ")");
                    ArrayList<DataCollector> connectedNodes = dataSinks.get(j).getConnectedNodes(); 
                    for(int k = 0; k < connectedNodes.size(); k++) {
                        System.out.println("\t Connected Node: " + connectedNodes.get(k).getID() + " (X: " + connectedNodes.get(k).getX() + ", Y: " + connectedNodes.get(k).getY());
                    }
                }
            }
            
            // Each node collect data
            for(DataCollector n : nodes) {
                n.readTemp(util.generateTemp()); 
                n.readPressure(util.generatePressure()); 
            }
            // send to sinks / basic intrusion detection 
            for(Sink s : dataSinks) {
                for(DataCollector dc : s.getConnectedNodes()) {
                    if (!allowedIDs.contains(dc.getID())) {
                        System.out.println("WARNING: UNAUTHORIZED NODE PUSHING DATA TO STREAM"); 
                    }
                }
                s.getReadings();
            }
            // aggregate at sinks
            ArrayList<DataPacket> aggregatedData = new ArrayList<>(); 
            for(Sink s : dataSinks) {
                if(!s.getConnectedNodes().isEmpty()){
                    aggregatedData.add(s.aggregateData());
                }   
            }
            // report simulation state at each time step
            for(DataPacket dp : aggregatedData){
                System.out.println("Sink " + dp.getID() + ": Avg Temp: " + dp.getTemp() + ", Avg Pressure: " + dp.getPressure());
            }
            // randomly kill one of the sinks halfway through
            if (i == killStep && killedSink != true) {
                int nodeToKill = util.randomRange(dataSinks.size()-1); 
                System.out.println("Killing random sink Node: " + nodeToKill);
                dataSinks.get(nodeToKill).battery.kill();
                killedSink = true; 
            }
            topoThisRun = false; 
        }
    }

    //Simple distance based algorithm to connect datacollectors to the closest sink node 
    public void topologizeNetwork(ArrayList<DataCollector> nodes, ArrayList<Sink> sinks){
        for(int i = 0; i < nodes.size(); i++){
            if (nodes.get(i).getBatteryLife() <= 0.0) {
                //Battery Dead
            } else {
                Sink closestSink = sinks.get(0);
                Double closestDistance = util.calcDistance(nodes.get(i).getX(), nodes.get(i).getY(), sinks.get(0).getX(), sinks.get(0).getY());
                for(int j = 1; j < sinks.size(); j++){ 
                    Double currentDistance; 
                    currentDistance = util.calcDistance(nodes.get(i).getX(), nodes.get(i).getY(), sinks.get(j).getX(), sinks.get(j).getY());
                    if(currentDistance < closestDistance) {
                        closestSink = sinks.get(j); 
                        closestDistance = currentDistance; 
                    }
                }
                closestSink.addNode(nodes.get(i));
            }
        }
    }


    // // set up time step loop 
    // public void runSimulation() {
    //     System.out.println("Node data shown in format: [TEMP, PRESSURE, XPosition, YPosition, BatteryLife]");
    //     for(int i = 0; i < numSteps; i++){
    //         double currentStepTempAvg = 0.0; 
    //         double currentStepPressAvg = 0.0; 
    //         if (dataSink.getBatteryLife() <= 0.0) {
    //             System.out.println("SINK BATTERY DEAD: ENDING SIMULATION");
    //             break;
    //         }
    //         // Each node collect data
    //         for(DataCollector n : nodes) {
    //             n.readTemp(util.generateTemp()); 
    //             n.readPressure(util.generatePressure()); 
    //             currentStepTempAvg += n.getTemp(); 
    //             currentStepPressAvg += n.getPressure();
    //         }
    //         // Sink pulls data 
    //         dataSink.getReadings();
    //         System.out.println("Simulation at timestep: " + i );
    //         System.out.println("SINK Battery Life: " + dataSink.getBatteryLife());

    //         // Print data and do time step analytics calculations 
    //         dataSink.printNodeStates();
    //         System.out.println("Step Avg Data: [TEMP: " + (currentStepTempAvg/nodes.size()) + ", PRESS: " + (currentStepPressAvg/nodes.size()) + "]");
    //         avgTemp += currentStepTempAvg/nodes.size(); 
    //         avgPress += currentStepPressAvg/nodes.size(); 
    //     } 

    //     // Overall simulation analysis calculations 
    //     System.out.println("--------------------------------------------");
    //     System.out.println("SIMULATION Avg Data: [TEMP: " + (avgTemp/numSteps) + ", PRESS: " + (avgPress/numSteps) + "]");
    //     System.out.println("SIMULATION Avg Latency: " + calculateAverageLatency());
    //     System.out.println("--------------------------------------------");
    // }

    //Avg Latency (MAKE RANDOM IN FUTURE)
    // LATENCY IS CURRENTLY CALCULATRED BY A DISTANCE MULTIPLIED BY A LATENCY FACTOR TO SIMULATE LONGER LATENCIES FOR FURTHER AWAY NODES
    public double calculateAverageLatency(){
        for(int i = 0; i < nodes.size(); i++){
            double nodeLatency = util.calcDistance(nodes.get(i).getX(), nodes.get(i).getY(), dataSink.getX(), dataSink.getY()) * LATENCY_FACTOR; 
            avgLatency += nodeLatency; 
        }
        return avgLatency/nodes.size(); 
    }
     
    // helper funcs if needed 
    private void clearConnectedNodes() {
        for(Sink s : dataSinks) {
            s.removeNodes();
        }
    }
}
