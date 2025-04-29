package com.team22;

/**
 * Entry point for the Space Debris Tracking System.
 * Initializes the system, loads data, and starts the user interaction loop.
 */

public class RunSimulation {

    /**
     * Main method to start the program.
     * 
     * @param args command-line arguments (not used)
     */

    public static void main(String[] args) {
        MissionControl control = new MissionControl();
        control.loadData("data/rso_metrics.csv");
        control.runSimulation();
    }
}
