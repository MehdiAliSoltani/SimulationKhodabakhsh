/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExteraCloudSim;

import Log.LogRecord;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.lists.VmList;
import org.cloudbus.cloudsim.power.PowerDatacenterBroker;
import simulation.AppConstants;
import simulation.Simulation;
import static simulation.Simulation.LOGRECORD;

/**
 *
 * @author Novin Pendar
 */
public class DatacenterBrokerPower extends DatacenterBroker {
//public class DatacenterBrokerPower extends PowerDatacenterBroker{

    public DatacenterBrokerPower(String name) throws Exception {
        super(name);
    }
    /*    
     protected void createVmsInDatacenterInHost(SdVm sdvm) {
     //        doPolicy();
     //        SdVm sdvm = (SdVm) vm1;
     int requestedVms = 0;
     int destinationDatacenter = 0;
     int vmId = extractVmId(sdvm.getId());
     destinationDatacenter = CloudRunner.datacenterIds[sdvm.getDestinationCloudId()];
     String datacenterName = CloudSim.getEntityName(destinationDatacenter);
     int requestedtime = CloudTable.getCloudTable(sdvm.getCloudId()).get(vmId).getRequestTime();
     if (!getVmsToDatacentersMap().containsKey(sdvm.getId())) { //if a given vm has'nt created yet then
            
     //            Log.printLine(CloudSim.clock() + ": " + getName() + ": Trying to Create VM #" + vmId
     //                    + " from cloud #" + sdvm.getCloudId() + " which requested in " + requestedtime + " in " + datacenterName);
     //here I should determine in which data center a given vm has to be created.				
     //datacenterId = the data center Id that has been selected by our algorithm
     sendNow(destinationDatacenter, Constants.PROCESS_CREATEVM_In_HOST, sdvm);
            

     requestedVms++;
     }
     setVmsRequested(requestedVms);
     setVmsAcks(0);
     }
     */

    @Override
    public void processEvent(SimEvent ev) {
        switch (ev.getTag()) {
            // Resource characteristics request
            case CloudSimTags.RESOURCE_CHARACTERISTICS_REQUEST:
                processResourceCharacteristicsRequest(ev);
                break;
            // Resource characteristics answer
            case CloudSimTags.RESOURCE_CHARACTERISTICS:
                processResourceCharacteristics(ev);
                break;
            // VM Creation answer
            case CloudSimTags.VM_CREATE_ACK:
                processVmCreate(ev);
                break;
            // A finished cloudlet returned
            case CloudSimTags.CLOUDLET_RETURN:
                processCloudletReturn(ev);
                break;
            // if the simulation finishes
            case CloudSimTags.END_OF_SIMULATION:
                shutdownEntity();
                break;
//            case CloudSimTags.VM_DESTROY_ACK:
//                processVmDestroy(ev);
//                break;
            // other unknown tags are processed by this method
            default:
                processOtherEvent(ev);
                break;
        }
    }

    @Override
    protected void processVmCreate(SimEvent ev) {
        int[] data = (int[]) ev.getData();
        int datacenterId = data[0];
        int vmId = data[1];
        int result = data[2];

        if (result == CloudSimTags.TRUE) {
            Simulation.NO_VM++;
            getVmsToDatacentersMap().put(vmId, datacenterId);
            getVmsCreatedList().add(VmList.getById(getVmList(), vmId));
            Log.printLine(CloudSim.clock() + ": " + getName() + ": VM #" + vmId + " VM Type: Number of Pe's "
                    + VmList.getById(getVmsCreatedList(), vmId).getNumberOfPes()
                    + " has been created in Datacenter #" + datacenterId + ", Host #"
                    + VmList.getById(getVmsCreatedList(), vmId).getHost().getId());
        } else {
            Log.printLine(CloudSim.clock() + ": " + getName() + ": Creation of VM #" + vmId
                    + " failed in Datacenter #" + datacenterId);
        }

        incrementVmsAcks();

        // all the requested VMs have been created
        if (getVmsCreatedList().size() == getVmList().size() - getVmsDestroyed()) {
            submitCloudlets();
        } else {
            // all the acks received, but some VMs were not created
            if (getVmsRequested() == getVmsAcks()) {
                // find id of the next datacenter that has not been tried
                for (int nextDatacenterId : getDatacenterIdsList()) {
                    if (!getDatacenterRequestedIdsList().contains(nextDatacenterId)) {
                        createVmsInDatacenter(nextDatacenterId);
                        return;
                    }
                }

                // all datacenters already queried
                if (getVmsCreatedList().size() > 0) { // if some vm were created
                    submitCloudlets();
                } else { // no vms created. abort
                    Log.printLine(CloudSim.clock() + ": " + getName()
                            + ": none of the required VMs could be created. Aborting");
                    finishExecution();
                }
            }
        }
    }

    @Override
    protected void processResourceCharacteristicsRequest(SimEvent ev) {
        setDatacenterIdsList(CloudSim.getCloudResourceList());
        setDatacenterCharacteristicsList(new HashMap<Integer, DatacenterCharacteristics>());

        Log.printLine(CloudSim.clock() + ": " + getName() + ": Cloud Resource List received with "
                + getDatacenterIdsList().size() + " resource(s)");

        for (Integer datacenterId : getDatacenterIdsList()) {
            sendNow(datacenterId, CloudSimTags.RESOURCE_CHARACTERISTICS, getId());
        }
    }

    @Override
    protected void submitCloudlets() {
        // determine VmId
        int vmIndex = 0;
//                List<Cloudlet> c = getCloudletList();
        List<CloudletPower> cloudletlist = getCloudletList();
        for (CloudletPower cloudlet : cloudletlist) {
            VmPower vm;
            // if user didn't bind this cloudlet and it has not been executed yet
            if (cloudlet.getVmId() == -1) {
                vm = (VmPower) getVmsCreatedList().get(vmIndex);
            } else { // submit to the specific vm
                vm = VmList.getById(getVmsCreatedList(), cloudlet.getVmId());
                if (vm == null) { // vm was not created
                    Log.printLine(CloudSim.clock() + ": " + getName() + ": Postponing execution of cloudlet "
                            + cloudlet.getCloudletId() + ": bount VM not available");
                    continue;
                }
            }

            Log.printLine(CloudSim.clock() + ": " + getName() + ": Sending cloudlet "
                    + cloudlet.getCloudletId() + " to VM #" + vm.getId());
            cloudlet.setVmId(vm.getId());
            sendNow(getVmsToDatacentersMap().get(vm.getId()), CloudSimTags.CLOUDLET_SUBMIT, cloudlet);
            cloudletsSubmitted++;
            vmIndex = (vmIndex + 1) % getVmsCreatedList().size();
            getCloudletSubmittedList().add(cloudlet);
            
            
            long[] timeToIo = cloudlet.getTimeofIO();
            cloudlet.setStartTime(CloudSim.clock());
            cloudlet.setRestartTime(CloudSim.clock());

            int cloudletDCId = cloudlet.getDatacenterIdHostedCloudlet();
            int cloudletHostId = cloudlet.getHost();
//            VmPower vm1 = (VmPower) Simulation.getOneHost(cloudletDCId, cloudletHostId).getVm(vmId, getId());
            vm.addCloudlettoVm(cloudlet);
            int vmId = vm.getId();
//        Log.printLine(CloudSim.clock() + ": " + getName() + " : Sending cloudlet #"
//                + cloudlet.getCloudletId() + " to VM #" + vmId);
            sendNow(getVmsToDatacentersMap().get(vmId), CloudSimTags.CLOUDLET_SUBMIT, cloudlet);
            for (int i = 0; i < 1; i++) {
                Random r = new Random();

//            send(getVmsToDatacentersMap().get(vmId),r.nextInt(5) , CloudSimTags.CLOUDLET_PAUSE, cloudlet);
//            send(getVmsToDatacentersMap().get(vmId),r.nextInt(20), CloudSimTags.CLOUDLET_RESUME, cloudlet);
//            send(getVmsToDatacentersMap().get(vmId),3 , CloudSimTags.CLOUDLET_PAUSE, cloudlet);
//            send(getVmsToDatacentersMap().get(vmId),17, CloudSimTags.CLOUDLET_RESUME, cloudlet);
//            System.out.println("Send at " + (CloudSim.clock()+ 400));
            }
//        long[] timetoIO = cloudlet.getTimeofIO();
            long[] files = cloudlet.getSizeofFiles();
//        long len = cloudlet.getCloudletLength();
            long totalSizeofFiles = 0;
            for (int i = 0; i < files.length; i++) {
                totalSizeofFiles += files[i];
            }

            final int BYTE = 8;
            long bandwidthUseApprox = 0;
            double totalWatingTimeforIO = ((totalSizeofFiles * BYTE) / (double) AppConstants.SHARE_OF_BANDWIDTH_BET_D0_D1) * cloudlet.getAlpha();

            LOGRECORD.put(cloudlet.getCloudletId(), new LogRecord(cloudlet.getCloudletLength(),
                    totalSizeofFiles,
                    cloudlet.getArrivalTime(),
                    cloudlet.getStartTime(),
                    cloudlet.isInSource(),
                    cloudlet.getHost(),
                    cloudlet.getStatus()));

//        System.out.println(" totalWatingTimeforIO " + totalWatingTimeforIO + "  totalSizeofFiles " + totalSizeofFiles + " Alpha()" + cloudlet.getAlpha());
            double cloudletTimetoRunApprox = cloudlet.getCloudletLength() / (double) AppConstants.PE_MIPS;
//        System.out.println("cloudletTimetoRunApprox "+cloudletTimetoRunApprox);
//        cloudlet.setCloudletTimetoRunApprox(cloudletTimetoRunApprox);
            Random r = new Random();
            r.setSeed(totalSizeofFiles);
            double startIO = r.nextDouble() * cloudletTimetoRunApprox;
// start to I/O
            send(getVmsToDatacentersMap().get(vmId), startIO, CloudSimTags.CLOUDLET_PAUSE, cloudlet);
// finish I/O
            send(getVmsToDatacentersMap().get(vmId), startIO + totalWatingTimeforIO, CloudSimTags.CLOUDLET_RESUME, cloudlet);

        }

        // remove submitted cloudlets from waiting list
        for (Cloudlet cloudlet : getCloudletSubmittedList()) {
            getCloudletList().remove(cloudlet);
        }
    }

    public void doCloudlet(CloudletPower cloudlet, int vmId) {
        cloudlet.setVmId(vmId);
        long[] timeToIo = cloudlet.getTimeofIO();
        cloudlet.setStartTime(CloudSim.clock());
        cloudlet.setRestartTime(CloudSim.clock());

        int cloudletDCId = cloudlet.getDatacenterIdHostedCloudlet();
        int cloudletHostId = cloudlet.getHost();
        VmPower vm = (VmPower) Simulation.getOneHost(cloudletDCId, cloudletHostId).getVm(vmId, getId());
        vm.addCloudlettoVm(cloudlet);

//        Log.printLine(CloudSim.clock() + ": " + getName() + " : Sending cloudlet #"
//                + cloudlet.getCloudletId() + " to VM #" + vmId);
        sendNow(getVmsToDatacentersMap().get(vmId), CloudSimTags.CLOUDLET_SUBMIT, cloudlet);
//        for (int i = 0; i < 1; i++) {
//            Random r = new Random();

//            send(getVmsToDatacentersMap().get(vmId),r.nextInt(5) , CloudSimTags.CLOUDLET_PAUSE, cloudlet);
//            send(getVmsToDatacentersMap().get(vmId),r.nextInt(20), CloudSimTags.CLOUDLET_RESUME, cloudlet);
//            send(getVmsToDatacentersMap().get(vmId),3 , CloudSimTags.CLOUDLET_PAUSE, cloudlet);
//            send(getVmsToDatacentersMap().get(vmId),17, CloudSimTags.CLOUDLET_RESUME, cloudlet);
//            System.out.println("Send at " + (CloudSim.clock()+ 400));
//        }
//        long[] timetoIO = cloudlet.getTimeofIO();
        long[] files = cloudlet.getSizeofFiles();
//        long len = cloudlet.getCloudletLength();
        long totalSizeofFiles = 0;
        for (int i = 0; i < files.length; i++) {
            totalSizeofFiles += files[i];
        }

        final int BYTE = 8;
        long bandwidthUseApprox = 0;
        double totalWatingTimeforIO = ((totalSizeofFiles * BYTE) / (double) AppConstants.SHARE_OF_BANDWIDTH_BET_D0_D1) * cloudlet.getAlpha();

        LOGRECORD.put(cloudlet.getCloudletId(), new LogRecord(cloudlet.getCloudletLength(),
                totalSizeofFiles,
                cloudlet.getArrivalTime()/100000,
                cloudlet.getStartTime(),
                cloudlet.isInSource(),
                cloudlet.getHost(),
                cloudlet.getStatus()));

//        System.out.println(" totalWatingTimeforIO " + totalWatingTimeforIO + "  totalSizeofFiles " + totalSizeofFiles + " Alpha()" + cloudlet.getAlpha());
        double cloudletTimetoRunApprox = cloudlet.getCloudletLength() / (double) AppConstants.PE_MIPS;
//        System.out.println("cloudletTimetoRunApprox "+cloudletTimetoRunApprox);
//        cloudlet.setCloudletTimetoRunApprox(cloudletTimetoRunApprox);
        Random r = new Random();
        r.setSeed(totalSizeofFiles);
        double startIO = r.nextDouble() * cloudletTimetoRunApprox;
// start to I/O
//        send(getVmsToDatacentersMap().get(vmId), startIO, CloudSimTags.CLOUDLET_PAUSE, cloudlet);
        send(getVmsToDatacentersMap().get(vmId), 0.01, CloudSimTags.CLOUDLET_PAUSE, cloudlet);
// finish I/O
//        send(getVmsToDatacentersMap().get(vmId), startIO + totalWatingTimeforIO, CloudSimTags.CLOUDLET_RESUME, cloudlet);
        send(getVmsToDatacentersMap().get(vmId), 0.01 + totalWatingTimeforIO, CloudSimTags.CLOUDLET_RESUME, cloudlet);
        /*
         double cloudletTimetoRunApprox = cloudlet.getCloudletLength() / (double) AppConstants.PE_MIPS; //calculate the approximation of cloudlet running 
         try {
         bandwidthUseApprox = (long) (totalSizeofFiles / (double) cloudletTimetoRunApprox);
         } catch (ArithmeticException e) {
         bandwidthUseApprox = 1000;
         System.out.println("Divide by zero");
         }

         cloudlet.setBandwidthUseApprox(bandwidthUseApprox);
         cloudlet.setBandwidthUseDuringRun(bandwidthUseApprox);
         // increment total bandwidth in use by all cloudlets in datacenter

         if (!cloudlet.isInSource()) {// this cloudlet is outsourced and uses the between datacenters bandwidth
         Simulation.increaseTotalBandwidthUsedBetweenDatacenter(bandwidthUseApprox);
         } else {
         Simulation.increaseToatalBandwidthUsedDatacenter(cloudlet.getDatacenterIdHostedCloudlet(), bandwidthUseApprox);
         }
         */
    }

    @Override
    public void processCloudletReturn(SimEvent ev) {

        CloudletPower cloudlet = (CloudletPower) (Cloudlet) ev.getData();
        int cloudletDCId = cloudlet.getDatacenterIdHostedCloudlet();
        int cloudletHostId = cloudlet.getHost();
        cloudlet.addTotalCpuConsume(CloudSim.clock() - cloudlet.getRestartTime());
        long[] files = cloudlet.getSizeofFiles();
        long totalfilesize = 0;
        for (int i = 0; i < files.length; i++) {
            totalfilesize += files[i];

        }

//        LogRecord 
        /*
         LOGRECORD.put(cloudlet.getCloudletId(),
         new LogRecord(cloudlet.getCloudletLength(),
         totalfilesize,
         cloudlet.getArrivalTime() / 100000,
         cloudlet.getStartTime(),
         cloudlet.getTotalCpuConsume(),
         cloudlet.getTotalWaitingTime(),
         cloudlet.getTotalExecutionTime(),
         cloudlet.isInSource(),
         cloudlet.getHost()
         ));
         */
        LOGRECORD.get(cloudlet.getCloudletId()).setCpuUsageTime(cloudlet.getTotalCpuConsume());
        LOGRECORD.get(cloudlet.getCloudletId()).setCompelitionTime(cloudlet.getTotalExecutionTime());
        LOGRECORD.get(cloudlet.getCloudletId()).setWaitingTime(cloudlet.getTotalWaitingTime());
        LOGRECORD.get(cloudlet.getCloudletId()).setStatus(cloudlet.getStatus());

//        System.out.println(cloudlet.getCloudletTimetoRunApprox() + "********");
        VmPower vm = (VmPower) Simulation.getOneHost(cloudletDCId, cloudletHostId).getVm(cloudlet.getVmId(), getId());
//        List<CloudletPower> l = vm.getVmCloudletList();
        vm.removeCloudletfromVm(cloudlet);
//     l = vm.getVmCloudletList();

        // decrease total bandwidth used by this cloudlet 
        /*
         long jk;
         if (cloudlet.isInSource()) {
         jk = Simulation.getToatalBandwidthUsedDatacenter(cloudlet.getDatacenterIdHostedCloudlet());
         long kk = cloudlet.getBandwidthUseApprox();
         Simulation.decreaseToatalBandwidthUsedDatacenter(cloudlet.getDatacenterIdHostedCloudlet(), cloudlet.getBandwidthUseApprox());
         jk = Simulation.getToatalBandwidthUsedDatacenter(cloudlet.getDatacenterIdHostedCloudlet());
         //            System.out.println("");
         } else {
         Simulation.decreaseTotalBandwidthUsedBetweenDatacenter(cloudlet.getBandwidthUseApprox());
         }

         */
/// decrese total bandwidth utilized by this cloudlet form host        
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"+ Simulation.getOneHost(cloudletDCId, cloudletHostId).getId()+ 
//                " cloudletDCId= "+ cloudletDCId+ " DataStorageDatacenterId= "+cloudlet.getDataStorageDatacenterId());
//        System.out.println( Simulation.getOneHost(cloudletDCId, cloudletHostId).getInHouseTotalBandwidthUsed() + " "+ cloudlet.isInSource());
//        System.out.println( Simulation.getOneHost(cloudletDCId, cloudletHostId).getOutHouseTotalBandwidthUsed()+ " "+ cloudlet.isInSource());
//        System.out.println("totalfilesize "+totalfilesize);
//        if (cloudlet.getDataStorageDatacenterId() == cloudletDCId) {
        if (cloudlet.isInSource()) {
            Simulation.getOneHost(cloudletDCId, cloudletHostId).decreseInHouseTotalBandwidthUsed(totalfilesize);
        } else {
            Simulation.getOneHost(cloudletDCId, cloudletHostId).decreseOutHouseTotalBandwidthUsed(totalfilesize);
        }

//        System.out.println( Simulation.getOneHost(cloudletDCId, cloudletHostId).getOutHouseTotalBandwidthUsed());
//        System.out.println( Simulation.getOneHost(cloudletDCId, cloudletHostId).getInHouseTotalBandwidthUsed());
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
////////////////*******************************************************************        
        getCloudletReceivedList().add(cloudlet);
        /*
         Log.printLine(CloudSim.clock() + ": " + getName() + ": Cloudlet " + cloudlet.getCloudletId()
         + " received" + " start time " + cloudlet.getExecStartTime() + " length " + cloudlet.getCloudletLength()
         + " actual cpu " + cloudlet.getActualCPUTime() + " status "
         + cloudlet.getStatus() + " pes " + cloudlet.getNumberOfPes() + " finish time " + cloudlet.getFinishTime()
         + " real start time " + cloudlet.getStartTime());
         System.out.println(" total waiting time " + cloudlet.getTotalWaitingTime() + " total execution time "
         + cloudlet.getTotalExecutionTime()
         + " total cpu time " + cloudlet.getTotalCpuConsume());
         */
//        System.out.println(cloudlet.getCloudletHistory());
////******************************************************************************************        
        cloudletsSubmitted--;
        if (getCloudletList().size() == 0 && cloudletsSubmitted == 0) { // all cloudlets executed
            Log.printLine(CloudSim.clock() + ": " + getName() + ": All Cloudlets executed. Finishing...");
            clearDatacenters();
            finishExecution();
        } else { // some cloudlets haven't finished yet
            if (getCloudletList().size() > 0 && cloudletsSubmitted == 0) {
                // all the cloudlets sent finished. It means that some bount
                // cloudlet is waiting its VM be created
                clearDatacenters();
                createVmsInDatacenter(0);
            }

        }
    }

}
