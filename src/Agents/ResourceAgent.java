/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import ExteraCloudSim.HostPower;
import simulation.AppConstants;
import simulation.Simulation;

/**
 *
 * @author Novin Pendar
 */
public class ResourceAgent {

    public double getSystemMaxAvaiiableMips() {
        double mips = 0;
        for (int i = 0; i < AppConstants.NUM_DATACENTER; i++) {
            for (HostPower hostpower : Simulation.getCOMPUTE_SERVER_LIST(i)) {
                mips = mips + hostpower.getServeragent().getHostMaxAvaiiableMips();
            }
        }
        return mips;
    }

    public double getSystemMaxCurrentUtilization() {
        double utilization = 0;
        int totalNumServer = 0;
        for (int i = 0; i < AppConstants.NUM_DATACENTER; i++) {
            for (HostPower hostpower : Simulation.getCOMPUTE_SERVER_LIST(i)) {
                utilization = utilization + hostpower.getServeragent().getHostMaxCurrentUtilization();
                totalNumServer++;
            }
        }
        utilization = utilization / totalNumServer;

        return utilization;
    }

    public double getApproximationNumberofPes() {
        double systemUtilization = getSystemMaxCurrentUtilization();
        double currentCapasity = 1 - systemUtilization;
        double approximationNumPes = 0;
        for (int i = 0; i < AppConstants.NUM_DATACENTER; i++) {
            approximationNumPes = approximationNumPes + (currentCapasity * AppConstants.NUM_COMPUTE_SERVERS[i] * AppConstants.NUM_PE_per_CS);
        }
        return approximationNumPes;
    }

}
