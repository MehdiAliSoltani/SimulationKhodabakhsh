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
public class VmAgent {

    double vmtotalAllocatedMips;
    double vmtotalRequestedMips;
    double vmmipsUtilization;

    public VmAgent(double vmtotalAllocatedMips, double vmtotalRequestedMips, double vmmipsUtilization) {
        this.vmtotalAllocatedMips = vmtotalAllocatedMips;
        this.vmtotalRequestedMips = vmtotalRequestedMips;
        this.vmmipsUtilization = vmmipsUtilization;
    }

    public double getVmtotalAllocatedMips() {
        return vmtotalAllocatedMips;
    }

    public void setVmtotalAllocatedMips(double vmtotalAllocatedMips) {
        this.vmtotalAllocatedMips = vmtotalAllocatedMips;
    }

    public double getVmtotalRequestedMips() {
        return vmtotalRequestedMips;
    }

    public void setVmtotalRequestedMips(double vmtotalRequestedMips) {
        this.vmtotalRequestedMips = vmtotalRequestedMips;
    }

    public double getVmmipsUtilization() {
        return vmmipsUtilization;
    }

    public void setVmmipsUtilization(double vmmipsUtilization) {
        this.vmmipsUtilization = vmmipsUtilization;
    }
    public double getMipsOverAllocated(){
        return getVmtotalRequestedMips() - getVmtotalAllocatedMips();
    }
    
    
}
