/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExteraCloudSim;

import Agents.NetworkAgent;
import Agents.ServerAgent;
import Resource.Resource;
import Test.CloudSimExample1;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.HostDynamicWorkload;
import org.cloudbus.cloudsim.HostStateHistoryEntry;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.ResCloudlet;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.lists.CloudletList;
import org.cloudbus.cloudsim.lists.PeList;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerVm;
import org.cloudbus.cloudsim.provisioners.BwProvisioner;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;
import simulation.AppConstants;
import simulation.Simulation;

/**
 *
 * @author Novin Pendar
 */
public class HostPower extends HostDynamicWorkload {
//public class HostPower extends Host {

    private int datacenterID;
    private ServerAgent serveragent;

    /**
     * The utilization mips.
     */
    private double utilizationMips;
    private static double bandwidthUtilization;

    /**
     * The previous utilization mips.
     */
    private double previousUtilizationMips;

    /**
     * The state history.
     */
    private final List<HostStateHistoryEntry> stateHistory = new LinkedList<HostStateHistoryEntry>();

    public HostPower(int id, RamProvisioner ramProvisioner, BwProvisioner bwProvisioner, long storage, List<? extends Pe> peList, VmScheduler vmScheduler) {
        super(id, ramProvisioner, bwProvisioner, storage, peList, vmScheduler);
        setUtilizationMips(0);
        setPreviousUtilizationMips(0);
        createServerAgent();
    }

    public HostPower(int datacenterId,
            int id,
            RamProvisioner ramProvisioner,
            BwProvisioner bwProvisioner,
            long storage,
            List<? extends Pe> peList,
            VmScheduler vmScheduler) {
        super(id, ramProvisioner, bwProvisioner, storage, peList, vmScheduler);
        this.datacenterID = datacenterId;
        setUtilizationMips(0);
        setPreviousUtilizationMips(0);
        createServerAgent();
    }

    public int getDatacenterID() {
        return datacenterID;
    }

    private void createServerAgent() {
        serveragent = new ServerAgent();
        this.serveragent.setHostMaxAvaiiableMips(getMaxAvailableMips());
        this.serveragent.setHostMaxCurrentUtilization(getMaxUtilization());
        this.serveragent.setHostPreviousUtilizationCpu(getPreviousUtilizationOfCpu());
        this.serveragent.setHostPreviousUtilizationMips(getPreviousUtilizationMips());

    }

    public double getBwUtilization() {
        double bwUtilization = 0;
        List<VmPower> vmlist = this.getVmList();
        for (VmPower vm : vmlist) {
            List<CloudletPower> cloudletlist = vm.getVmCloudletList();
            for (CloudletPower cloudlet : cloudletlist) {
                int id = cloudlet.getCloudletId();
                int status = vm.getCloudletScheduler().getCloudletStatus(id);
                if (status == Cloudlet.PAUSED) { // blocked for I/O
                    long sizeoffile = cloudlet.getSizeofFiles()[cloudlet.getIoNo()];
                    if (cloudlet.getDataStorageDatacenterId() == this.getDatacenterID()) {
                        bwUtilization = bwUtilization + sizeoffile * AppConstants.BASE_FILE_TRANSFER_TIME_IN_DC;
                    } else {
                        bwUtilization = bwUtilization + sizeoffile * AppConstants.BASE_FILE_TRANSFER_TIME_BETWEEN_DC;
                    }

                }
            }
        }
//        }

        return (bwUtilization / this.getBw());
    }
    
    private void setDynamicWTable(){
//        NetworkAgent networkagent = new NetworkAgent();
//        networkagent.
        
    }
    static boolean flag = true;

    @Override
    public double updateVmsProcessing(double currentTime) {
        this.serveragent.setHostMaxAvaiiableMips(getMaxAvailableMips());
        this.serveragent.setHostMaxCurrentUtilization(getMaxUtilization());
        this.serveragent.setHostPreviousUtilizationCpu(getPreviousUtilizationOfCpu());
        this.serveragent.setHostPreviousUtilizationMips(getPreviousUtilizationMips());
        
// this section should be removed;        
//        System.out.println("MaxAvailableMips # " + getMaxAvailableMips());
//        System.out.println("MaxUtilization# " + getMaxUtilization());
//        System.out.println("PreviousUtilizationMips# " + getPreviousUtilizationMips());
//        System.out.println("PreviousUtilizationCpu# " + getPreviousUtilizationOfCpu());
//        System.out.println("getBwProvisioner().getAvailableBw() # " + getBwProvisioner().getAvailableBw());
//        System.out.println("UtilizationMips# " + getUtilizationMips());
//        System.out.println("UtilizationOfRam# " + getUtilizationOfRam());
//        System.out.println("UtilizationOfCpu# " + getUtilizationOfCpu());
//        System.out.println("UtilizationOfCpu# "+ getRamProvisioner().);
        
        this.setBandwidthUtilization(getBwUtilization()); // set the bandwidth utilization for this host
        double smallerTime = super.updateVmsProcessing(currentTime);
        setPreviousUtilizationMips(getUtilizationMips());
        setUtilizationMips(0);
        double hostTotalRequestedMips = 0;
        List<VmPower> vmlist = this.getVmList();

        for (VmPower vm : vmlist) {
            getVmScheduler().deallocatePesForVm(vm);
        }

        for (VmPower vm : vmlist) {
            getVmScheduler().allocatePesForVm(vm, vm.getCurrentRequestedMips());
        }

        for (VmPower vm : vmlist) {
            double totalRequestedMips = vm.getCurrentRequestedTotalMips();
            double totalAllocatedMips = getVmScheduler().getTotalAllocatedMipsForVm(vm);
            // set vm agent attributes
            vm.getVmagent().setVmtotalAllocatedMips(totalAllocatedMips);
            vm.getVmagent().setVmtotalRequestedMips(totalRequestedMips);
            vm.getVmagent().setVmmipsUtilization(totalRequestedMips / vm.getMips() * 100);
/*
            if (currentTime > 0.1 & flag) {

                int idd = vm.getId();
                boolean b = false;
                List<Integer> l = new ArrayList<Integer>();
                for (int i = 2; i < 10; i += 2) {
                    b = vm.getCloudletScheduler().cloudletPause(i);
                    if (b) {
                        flag = false;
                        l.add(i);
                        System.out.println(" vm.getCloudletScheduler().getCloudletStatus  " + i + "  " + vm.getCloudletScheduler().getCloudletStatus(i));
                    }
                }
            }

            if (currentTime > 1) {
                for (int i = 0; i < 10; i++) {
                    vm.getCloudletScheduler().cloudletResume(i);

                    System.out.println(" vm.getCloudletScheduler().getCloudletStatus  " + i + "  " + vm.getCloudletScheduler().getCloudletStatus(i));

                }
            }
            System.out.println("getUtilizationOfBw "+getUtilizationOfBw());
            System.out.println("getUtilizationOfram "+getUtilizationOfRam());
            */
////////////////////////            
            if (!Log.isDisabled()) {
                Log.formatLine(
                        "%.2f: Datacenter # " + getDatacenter().getId() + " [Host #" + getId() + "] Total allocated MIPS for VM #" + vm.getId()
                        + " (Host #" + vm.getHost().getId()
                        + ") is %.2f, was requested %.2f out of total %.2f (%.2f%%)",
                        CloudSim.clock(),
                        totalAllocatedMips,
                        totalRequestedMips,
                        vm.getMips(),
                        totalRequestedMips / vm.getMips() * 100);

                List<Pe> pes = getVmScheduler().getPesAllocatedForVM(vm);
                StringBuilder pesString = new StringBuilder();
                for (Pe pe : pes) {
                    pesString.append(String.format(" PE #" + pe.getId() + ": %.2f.", pe.getPeProvisioner()
                            .getTotalAllocatedMipsForVm(vm)));
                }
                Log.formatLine(
                        "%.2f: [Host #" + getId() + "] MIPS for VM #" + vm.getId() + " by PEs ("
                        + getNumberOfPes() + " * " + getVmScheduler().getPeCapacity() + ")."
                        + pesString,
                        CloudSim.clock());
            }

            if (getVmsMigratingIn().contains(vm)) {
                Log.formatLine("%.2f: [Host #" + getId() + "] VM #" + vm.getId()
                        + " is being migrated to Host #" + getId(), CloudSim.clock());
            } else {
                if (totalAllocatedMips + 0.1 < totalRequestedMips) {
                    Log.formatLine("%.2f: [Host #" + getId() + "] Under allocated MIPS for VM #" + vm.getId()
                            + ": %.2f", CloudSim.clock(), totalRequestedMips - totalAllocatedMips);
                }

                vm.addStateHistoryEntry(
                        currentTime,
                        totalAllocatedMips,
                        totalRequestedMips,
                        (vm.isInMigration() && !getVmsMigratingIn().contains(vm)));

                if (vm.isInMigration()) {
                    Log.formatLine(
                            "%.2f: [Host #" + getId() + "] VM #" + vm.getId() + " is in migration",
                            CloudSim.clock());
                    totalAllocatedMips /= 0.9; // performance degradation due to migration - 10%
                }
            }

            setUtilizationMips(getUtilizationMips() + totalAllocatedMips);
            hostTotalRequestedMips += totalRequestedMips;
        }

        addStateHistoryEntry(
                currentTime,
                getUtilizationMips(),
                hostTotalRequestedMips,
                (getUtilizationMips() > 0));

        return smallerTime;
    }

    /**
     * Gets the completed vms.
     *
     * @return the completed vms
     */
    public List<Vm> getCompletedVms() {
        List<Vm> vmsToRemove = new ArrayList<Vm>();
        for (Vm vm : getVmList()) {
            if (vm.isInMigration()) {
                continue;
            }
            if (vm.getCurrentRequestedTotalMips() == 0) {
                vmsToRemove.add(vm);
            }
        }
        return vmsToRemove;
    }

    /**
     * Gets the max utilization among by all PEs.
     *
     * @return the utilization
     */
    public double getMaxUtilization() {
        return PeList.getMaxUtilization(getPeList());
    }

    /**
     * Gets the max utilization among by all PEs allocated to the VM.
     *
     * @param vm the vm
     * @return the utilization
     */
    public double getMaxUtilizationAmongVmsPes(Vm vm) {
        return PeList.getMaxUtilizationAmongVmsPes(getPeList(), vm);
    }

    /**
     * Gets the utilization of memory.
     *
     * @return the utilization of memory
     */
    public double getUtilizationOfRam() {
        return getRamProvisioner().getUsedRam();
    }

    /**
     * Gets the utilization of bw.
     *
     * @return the utilization of bw
     */
    public double getUtilizationOfBw() {
        return getBwProvisioner().getUsedBw();
    }

    /**
     * Get current utilization of CPU in percentage.
     *
     * @return current utilization of CPU in percents
     */
    public double getUtilizationOfCpu() {
        double utilization = getUtilizationMips() / getTotalMips();
        if (utilization > 1 && utilization < 1.01) {
            utilization = 1;
        }
        return utilization;
    }

    /**
     * Gets the previous utilization of CPU in percentage.
     *
     * @return the previous utilization of cpu
     */
    public double getPreviousUtilizationOfCpu() {
        double utilization = getPreviousUtilizationMips() / getTotalMips();
        if (utilization > 1 && utilization < 1.01) {
            utilization = 1;
        }
        return utilization;
    }

    /**
     * Get current utilization of CPU in MIPS.
     *
     * @return current utilization of CPU in MIPS
     */
    public double getUtilizationOfCpuMips() {
        return getUtilizationMips();
    }

    /**
     * Gets the utilization mips.
     *
     * @return the utilization mips
     */
    public double getUtilizationMips() {
        return utilizationMips;
    }

    /**
     * Sets the utilization mips.
     *
     * @param utilizationMips the new utilization mips
     */
    protected void setUtilizationMips(double utilizationMips) {
        this.utilizationMips = utilizationMips;
    }

    /**
     * Gets the previous utilization mips.
     *
     * @return the previous utilization mips
     */
    public double getPreviousUtilizationMips() {
        return previousUtilizationMips;
    }

    /**
     * Sets the previous utilization mips.
     *
     * @param previousUtilizationMips the new previous utilization mips
     */
    protected void setPreviousUtilizationMips(double previousUtilizationMips) {
        this.previousUtilizationMips = previousUtilizationMips;
    }

    /**
     * Gets the state history.
     *
     * @return the state history
     */
    public List<HostStateHistoryEntry> getStateHistory() {
        return stateHistory;
    }

    /**
     * Adds the state history entry.
     *
     * @param time the time
     * @param allocatedMips the allocated mips
     * @param requestedMips the requested mips
     * @param isActive the is active
     */
    public void addStateHistoryEntry(double time, double allocatedMips, double requestedMips, boolean isActive) {

        HostStateHistoryEntry newState = new HostStateHistoryEntry(
                time,
                allocatedMips,
                requestedMips,
                isActive);
        if (!getStateHistory().isEmpty()) {
            HostStateHistoryEntry previousState = getStateHistory().get(getStateHistory().size() - 1);
            if (previousState.getTime() == time) {
                getStateHistory().set(getStateHistory().size() - 1, newState);
                return;
            }
        }
        getStateHistory().add(newState);
    }

    public static double getBandwidthUtilization() {
        return bandwidthUtilization;
    }

    public static void setBandwidthUtilization(double bandwidthUtilization) {
        HostPower.bandwidthUtilization = bandwidthUtilization;
    }

    public ServerAgent getServeragent() {
        return serveragent;
    }
    
}
