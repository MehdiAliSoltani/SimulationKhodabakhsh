/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Agents;

/**
 *
 * @author Novin Pendar
 */
public class ServerAgent {
    double hostMaxAvaiiableMips;
    double hostMaxCurrentUtilization;
    double hostPreviousUtilizationMips;
    double hostPreviousUtilizationCpu;

    public double getHostMaxAvaiiableMips() {
        return hostMaxAvaiiableMips;
    }

    public void setHostMaxAvaiiableMips(double hostMaxAvaiiableMips) {
        this.hostMaxAvaiiableMips = hostMaxAvaiiableMips;
    }

    public double getHostMaxCurrentUtilization() {
        return hostMaxCurrentUtilization;
    }

    public void setHostMaxCurrentUtilization(double hostMaxCurrentUtilization) {
        this.hostMaxCurrentUtilization = hostMaxCurrentUtilization;
    }

    public double getHostPreviousUtilizationMips() {
        return hostPreviousUtilizationMips;
    }

    public void setHostPreviousUtilizationMips(double hostPreviousUtilizationMips) {
        this.hostPreviousUtilizationMips = hostPreviousUtilizationMips;
    }

    public double getHostPreviousUtilizationCpu() {
        return hostPreviousUtilizationCpu;
    }

    public void setHostPreviousUtilizationCpu(double hostPreviousUtilizationCpu) {
        this.hostPreviousUtilizationCpu = hostPreviousUtilizationCpu;
    }
    

}
/*
MaxAvailableMips # 3820.8235546157807
MaxUtilization# 0.0
PreviousUtilizationMips# 179.17644538421928
PreviousUtilizationCpu# 0.04479411134605482
*/