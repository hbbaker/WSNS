# WSNS
---
## A small Wireless Sensor Network Simulation Tool 
### Henry Baker 
### SDSU EE662 Summer 2025
---
## Overview
    - WSNS has Node types that can be used to create different Nodes (i.e Sinks and DataCollectors)
    - DataCollectors use DataPackets to send information to Sinks
        - Can make a field in Node class to allow for data transmission between different node types (i.e canRecieve bool)
        - May want to implement an abstract type for defining different datapacket structs
    - Simulation objects handle node instances, as well as run the simulation steps 
        - **This is where the main code work should be done to implement new protocols and routing setups**
        - Instantiating a simulation takes in all of the parameters for number of nodes, power limitations, simulation steps, etc. 
        - To build new simulations, just add a new function and call it from Main
    - Batteries are used by all Node objects for power consumption data
    - SimUtils contains all of the necessary supplementary tools used by the Simulation class. Add support functionality here. 
    - To test bigger program outputs, pipe the output of running Main to a .txt file:    

        javac Main.java
        java Main > output.txt
