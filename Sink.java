import java.util.ArrayList; 

/**
 * Implementation of a variation of the Node type that can be used as a Data Sink for a collection of DataCollector Nodes. 
 */
public class Sink extends Node{
    private int nodeID; 
    private final ArrayList<DataCollector> nodesConnected; 
    private final ArrayList<DataPacket> nodeData; 
    private final double receptionCost;  
    StringBuilder sb; 
    
    public Sink(int id, double initialX, double initialY, double batteryLife, double receptionCost, ArrayList<DataCollector> nodes) {
        nodeID = id; 
        this.x = initialX; 
        this.y = initialY; 
        this.battery = new Battery(batteryLife);
        this.receptionCost = receptionCost; 
        nodesConnected = nodes;  
        nodeData = new ArrayList<>();  
        sb = new StringBuilder(); 
    }

    public ArrayList<DataCollector> getConnectedNodes() {
        return nodesConnected; 
    }

    public void addNode(DataCollector node){
        nodesConnected.add(node); 
    }

    public void removeNodes() {
        nodesConnected.clear();
    }

    public void updateX(double newX) {
        this.x = newX; 
    }

    public void updateY(double newY) {
        this.y = newY; 
    }

    public int getID() {
        return nodeID; 
    }

    public void getReadings() {
        for(int i = 0; i < nodesConnected.size(); i++) {
            DataPacket currentNode = nodesConnected.get(i).getReadings(); 
            if (currentNode != null) { // CHECK THIS
                battery.updateBatteryLife(battery.getBatteryLife() - receptionCost);
                nodeData.add(currentNode); 
            }
        } 
    }

    public DataPacket aggregateData() {
        double avgTemp = 0.0; 
        double avgPress = 0.0; 

        for(int i = 0; i < nodeData.size(); i++){
            avgTemp += nodeData.get(i).getTemp(); 
            avgPress += nodeData.get(i).getPressure(); 
        }

        avgTemp = avgTemp / nodeData.size(); 
        avgPress = avgPress / nodeData.size(); 

        return new DataPacket(nodeID, avgTemp, avgPress, 0.0, 0.0, 0.0); 
    }

    public ArrayList<DataPacket> returnAllReadings() {
        return nodeData; 
    }

    public void printNodeStates() { 
        for(int i = 0; i < nodeData.size(); i++) {
            DataPacket dp = nodeData.get(i);
            System.out.println("(Node: " + dp.getID() + ", Temp: " + dp.getTemp() + ", Pressure: " + dp.getPressure() + ", X: " + dp.getX() +", Y: "+ dp.getY() + ", Battery: "+ dp.getBattery() +")");

            // sb.append("Node " + i + ": ");
            // for(int j = 0; j < nodeData[i].length; j++){
            //     if (nodeData[i][4] < 0.0) {
            //         sb.append("DEAD");
            //         break; 
            //     } else {
            //         if (j == nodeData[i].length - 1) {
            //             sb.append(nodeData[i][j].toString());
            //         } else {
            //             sb.append(nodeData[i][j].toString() + ", "); 
            //         }
            //     }
            // }
            // sb.append("\n"); 
            // System.out.println(sb);
            // sb.setLength(0);
        }
    }
}
