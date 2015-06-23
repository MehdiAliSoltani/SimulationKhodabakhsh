/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExteraCloudSim;

import Agents.VmAgent;
import Test.CloudSimExample1;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.ResCloudlet;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerVm;

/**
 *
 * @author Novin Pendar
 */
public class VmPower extends PowerVm {

    int vmType;
    private VmAgent vmagent;
    private Queue vmQueue;
    private List<CloudletPower> vmCloudletList = new ArrayList<CloudletPower>();
   
    public VmPower(int id,
            int userId,
            double mips,
            int pesNumber,
            int ram,
            long bw,
            long size,
            int priority,
            String vmm,
            CloudletScheduler cloudletScheduler,
            double schedulingInterval) {
        super(id, userId, mips, pesNumber, ram, bw, size, priority, vmm, cloudletScheduler, schedulingInterval);
        createVmagent();
    }

    public VmAgent getVmagent() {
        return vmagent;
    }

    public void createVmagent() {
        double totalRequestedMips = getCurrentRequestedTotalMips();
        double totalAllocatedMips = getMips() * getNumberOfPes();
        double totalUtilization = totalRequestedMips / getMips() * 100;
        vmagent = new VmAgent(totalAllocatedMips, totalRequestedMips, totalUtilization);
    }

    
    //TO DO
    public void VmAgent() {

    }

    @Override
    public List<Double> getCurrentRequestedMips() {
        List<Double> currentRequestedMips = getCloudletScheduler().getCurrentRequestedMips();
        if (isBeingInstantiated()) {
            currentRequestedMips = new ArrayList<Double>();
            for (int i = 0; i < getNumberOfPes(); i++) {
                currentRequestedMips.add(getMips());
            }
        }
        return currentRequestedMips;
    }

    /**
     * Gets the current requested total mips.
     *
     * @return the current requested total mips
     */
    @Override
    public double getCurrentRequestedTotalMips() {
        double totalRequestedMips = 0;
        for (double mips : getCurrentRequestedMips()) {
            totalRequestedMips += mips;
        }
        return totalRequestedMips;
    }

    @Override
    public double updateVmProcessing(final double currentTime, final List<Double> mipsShare) {
//                    CloudSimExample1.vmprocessingcount++;
//	                   System.out.println("Current time: "+currentTime + " vm id"+ getId()+ " host #"+ getHost().getId() +
//                                   " datacenter "+  getHost().getDatacenter().getName());
//	                   System.out.println("##############################################3");
//            if (currentTime > 10 )	{
//            int a;
//                    a=1;
//        }

        double time = super.updateVmProcessing(currentTime, mipsShare);
        if (currentTime > getPreviousTime() && (currentTime - 0.1) % getSchedulingInterval() == 0) {
            double utilization = getTotalUtilizationOfCpu(getCloudletScheduler().getPreviousTime());
            if (CloudSim.clock() != 0 || utilization != 0) {
                addUtilizationHistoryValue(utilization);
            }
            setPreviousTime(currentTime);
        }
        return time;
    }

    public List<CloudletPower> getVmCloudletList() {
        return vmCloudletList;
    }

    public void setVmCloudletList(List<CloudletPower> vmCloudletList) {
        this.vmCloudletList = vmCloudletList;
    }
    public void addCloudlettoVm(CloudletPower cloudlet){
        getVmCloudletList().add(cloudlet);
    }
    public void removeCloudletfromVm(CloudletPower cloudlet ){
        getVmCloudletList().remove(cloudlet);
//        cloudlet.get
//        for(Cloudlet cl : getVmCloudletList()){
//            if(cl.getCloudletId() == cloudlet.getCloudletId())      
//        }
    }
}
