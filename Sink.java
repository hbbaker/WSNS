/**
 * Implementation of a variation of the Node type that can be used as a Data Sink for a collection of DataCollector Nodes. 
 */
public class Sink extends Node{
    private final DataCollector[] nodesConnected; 
    private final Double[][] nodeData; 
    private final double receptionCost;  
    StringBuilder sb; 
    
    public Sink(double initialX, double initialY, double batteryLife, double receptionCost, DataCollector[] nodes) {
        this.x = initialX; 
        this.y = initialY; 
        this.battery = new Battery(batteryLife);
        this.receptionCost = receptionCost; 
        nodesConnected = nodes;  
        nodeData = new Double[nodes.length][5];  
        sb = new StringBuilder(); 
    }

    public void getReadings() {
        for(int i = 0; i < nodesConnected.length; i++) {
            battery.updateBatteryLife(battery.getBatteryLife() - receptionCost);
            nodeData[i] = nodesConnected[i].getReadings(); 
        } 
    }

    public Double[][] returnAllReadings() {
        return nodeData; 
    }

    public void printNodeStates() { 
        for(int i = 0; i < nodeData.length; i++) {
            sb.append("Node " + i + ": ");
            for(int j = 0; j < nodeData[i].length; j++){
                if (nodeData[i][4] < 0.0) {
                    sb.append("DEAD");
                    break; 
                } else {
                    if (j == nodeData[i].length - 1) {
                        sb.append(nodeData[i][j].toString());
                    } else {
                        sb.append(nodeData[i][j].toString() + ", "); 
                    }
                }
            }
            sb.append("\n"); 
            System.out.println(sb);
            sb.setLength(0);
        }
    }
}
