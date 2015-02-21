/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudsimexamples.Datacenter;

import LPSolver.LPSolverInFederated.ObjectiveFunctionPolynomial1;
import cloudsimexamples.Cloud.CloudController;
import cloudsimexamples.Cloud.CloudRunner;
import cloudsimexamples.Cloud.CloudTable;
import cloudsimexamples.Cloud.CloudTableContents;
import cloudsimexamples.Cloud.Constants;
import cloudsimexamples.Cloud.Resolver;
import cloudsimexamples.Market.CloudCoordinator;
import cloudsimexamples.Runner.RunnerMain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.lists.VmList;

/**
 *
 * @author Mehdi
 */
public class FederatedDatacenterBroker extends DatacenterBroker {

    Resolver resolver = new Resolver();
    private CloudCoordinator coordinator;
    private int policy = RunnerMain.policy;
    private int destinationDatacenter;
    private int localDatacenter;
    private int cloudId;
    private static final int HOUR = 3600;
    private static final int MINUTE = 60;
    private List<SdVm> list = new ArrayList<SdVm>();
    private CloudController controller;

    public FederatedDatacenterBroker(int cloudId, String name, CloudCoordinator coordinator) throws Exception {
        super(name);
        setCoordinator(coordinator);
        this.cloudId = cloudId;
    }

    public CloudController getController() {
        return controller;
    }

    public void setController(CloudController controller) {
        this.controller = controller;
    }

    private void doPolicy() {
        switch (RunnerMain.policy) {
            case 1:
//                List<SdVm> list = new ArrayList<SdVm>();
                for (Vm vm : getVmList()) {
                    SdVm sdvm = (SdVm) vm;
                    list.add(sdvm);
                }
                createVmsInDatacenter();
                break;
            case 2:
//                List<SdVm> list = new ArrayList<SdVm>();
                for (Vm vm : getVmList()) {
                    SdVm sdvm = (SdVm) vm;
                    list.add(sdvm);
                }
                createVmsInDatacenter();
                break;
            case 3:
//                List<SdVm> list = new ArrayList<SdVm>();
                for (Vm vm : getVmList()) {
                    SdVm sdvm = (SdVm) vm;
                    list.add(sdvm);
                }
                createVmsInDatacenter();
                break;
            case 4:
//                List<SdVm> list = new ArrayList<SdVm>();
                for (Vm vm : getVmList()) {
                    SdVm sdvm = (SdVm) vm;
                    list.add(sdvm);
                }
                createVmsInDatacenter();
                break;
            case 5:
//                List<SdVm> list = new ArrayList<SdVm>();
                for (Vm vm : getVmList()) {
                    SdVm sdvm = (SdVm) vm;
                    list.add(sdvm);
                }
                createVmsInDatacenter();
                break;
            case 6:
//                List<SdVm> list = new ArrayList<SdVm>();
                for (Vm vm : getVmList()) {
                    SdVm sdvm = (SdVm) vm;
                    list.add(sdvm);
                }
                createVmsInDatacenter();
                break;

            default:
                throw new AssertionError();
        }
    }

    /**
     * Create the virtual machines in a datacenter.
     *
     * @param datacenterId Id of the chosen PowerDatacenter
     * @pre $none
     * @post $none
     */
    

    

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

    protected int selectDatacenter() {
        int datacenterId = 0;

        return datacenterId;
    }

    /**
     * Process the return of a request for the characteristics of a
     * PowerDatacenter.
     *
     * @param ev a SimEvent object
     * @pre ev != $null
     * @post $none
     */
    @Override
    protected void processResourceCharacteristics(SimEvent ev) {
        DatacenterCharacteristics characteristics = (DatacenterCharacteristics) ev.getData();

        getDatacenterCharacteristicsList().put(characteristics.getId(), characteristics);
        /*        
         FederatedDatacenter fdcenter = (FederatedDatacenter) CloudSim.getEntity(characteristics.getId());
         Log.printLine("Datacenter# " + characteristics.getId() + " datacenter name:" + fdcenter.getName()
         + "  for cloud# " + fdcenter.getCloudId());
         */

        if (getDatacenterCharacteristicsList().size() == getDatacenterIdsList().size()) {
            //create a list to insert data centers which is requested;
            setDatacenterRequestedIdsList(new ArrayList<Integer>());
            //getDatacenterIdsList().get(0) means in the first data center according Id e.g. datacenter_0
//                        createVmsInDatacenter(getDatacenterIdsList().get(0));
            doPolicy();
//            createVmsInDatacenter();


        }
    }

    /**
     * Process a request for the characteristics of a PowerDatacenter.
     *
     * @param ev a SimEvent object
     * @pre ev != $null
     * @post $none
     */
    /**
     * register all datacenters which define in simulation
     *
     * @param ev
     * @author mehdi
     */
    @Override
    protected void processResourceCharacteristicsRequest(SimEvent ev) {
        setDatacenterIdsList(CloudSim.getCloudResourceList());
//                for(Integer i:getDatacenterIdsList())
//                System.out.println("setDatacenterIdsList:"+i.toString());
        setDatacenterCharacteristicsList(new HashMap<Integer, DatacenterCharacteristics>());
//******************************
//        Log.printLine(CloudSim.clock() + ": " + getName() + ": Cloud Federation Resource List received with "
//                + getDatacenterIdsList().size() + " resource(s)");
//******************************        
// for each data center method processResourceCharacteristics(ev) is called.
        for (Integer datacenterId : getDatacenterIdsList()) {
            sendNow(datacenterId, CloudSimTags.RESOURCE_CHARACTERISTICS, getId());
//                        System.out.println("datacenterId:"+datacenterId);
//                        System.out.println("getId:"+getId());
        }
    }

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
            case CloudSimTags.VM_DESTROY_ACK:
                processVmDestroy(ev);
                break;
            // other unknown tags are processed by this method
            default:
                processOtherEvent(ev);
                break;
        }
    }
//@Override

    protected void processVmDestroy(SimEvent ev) {
        int[] data = (int[]) ev.getData();
        int datacenterId = data[0];
        int vmId = data[1];
        int result = data[2];
        if (result == CloudSimTags.TRUE) {
            System.out.println(CloudSim.clock() + " VM" + vmId + " termin");
        }

    }

    @Override
    protected void processVmCreate(SimEvent ev) {
        int[] data = (int[]) ev.getData();
        int datacenterId = data[0];
        int vmId = data[1];
        int result = data[2];
        int vmIdno;
        int requesttime;
        int deadline;
        int deadlinetime;
        double now = CloudSim.clock();
        SdVm sdvm = null;
        for (Vm vm : getVmList()) {
            if (vm.getId() == vmId) {
                sdvm = (SdVm) vm;
                break;
            }
        }
        double clock = CloudSim.clock();
        vmIdno = extractVmId(vmId);
        if (result == CloudSimTags.TRUE) {
            getVmsToDatacentersMap().put(vmId, datacenterId);
            getVmsCreatedList().add(VmList.getById(getVmList(), vmId));
            removeElementfromQueue(vmIdno);
            CloudTable.getCloudTable(cloudId).get(vmIdno).setStartTime(CloudSim.clock());
            resolver.setStatus(cloudId, vmIdno, Constants.VM_STATUS_RUNNING, (int) CloudSim.clock(), this.controller);
            
            //            CloudTable.getCloudTable(cloudId).get(extractVmId(vmId)).setStatus(Constants.VM_STATUS_RUNNING);
            int hostId = VmList.getById(getVmsCreatedList(), vmId).getHost().getId();
            resolver.setDatacenterHost(cloudId, vmIdno, clock, datacenterId, hostId);

//            Log.printLine(CloudSim.clock() + ": " + getName() + ": VM #" + vmIdno
//                    + " from cloud #" + sdvm.getCloudId() + " has been created in Datacenter #" + datacenterId + ", Host #"
//                    + VmList.getById(getVmsCreatedList(), vmId).getHost().getId());



            int duration = CloudTable.getCloudTable(cloudId).get(vmIdno).getDuraion() * HOUR;
            int finishtime = (int) (duration);
            schedule(datacenterId, finishtime, CloudSimTags.VM_DESTROY, sdvm);

            getVmList().remove(sdvm);

        } else {
            switch (RunnerMain.policy) {
                case 1: //policy 1 :Fully-In-House no federation
                    requesttime = CloudTable.getCloudTable(cloudId).get(vmIdno).getRequestTime();
                    deadline = CloudTable.getCloudTable(cloudId).get(vmIdno).getDeadline();
                    deadlinetime = requesttime + deadline;
//                    cloudsimtime = CloudSim.clock();
                    if (CloudSim.clock() <= deadlinetime) {
                        int tries = sdvm.getTries();
                        if (tries <= Constants.VM_CREATION_MAX_TRIES) {
                            sdvm.setTries(tries + 1);
                            createVmsInDatacenter(sdvm);
                        } else {
                            resolver.setStatus(cloudId, vmIdno, Constants.VM_STATUS_DELAYED, (int) clock, null);
//            Queue<CloudTable.Requset>[] l1 = CloudTable.getQueue(0);                            
//                            CloudTable.getCloudTable(cloudId).get(vmIdno).setStatus(Constants.VM_STATUS_DELAYED);
//                            Log.printLine(CloudSim.clock() + ": " + getName() + ": Creation of VM #" + vmIdno
//                                    + " has been delayed #");
                            getVmList().remove(sdvm);
                        }
                    } else {
                        removeElementfromQueue(vmIdno);
                        resolver.setStatus(cloudId, vmIdno, Constants.VM_STATUS_REJECTED, (int) clock, null);
//                        CloudTable.getCloudTable(cloudId).get(vmIdno).setStatus(Constants.VM_STATUS_REJECTED);
//                        Log.printLine(CloudSim.clock() + ": " + getName() + ": Creation of VM #" + vmIdno
//                                + " failed in Datacenter #" + datacenterId + " due to meeting deadline");
                        getVmList().remove(sdvm);
                    }
                    break;

                case 2://policy 2: Fully-In-House-Shrink no federation
                    requesttime = CloudTable.getCloudTable(cloudId).get(vmIdno).getRequestTime();
                    deadline = CloudTable.getCloudTable(cloudId).get(vmIdno).getDeadline();
                    deadlinetime = requesttime + deadline;
//                    cloudsimtime = CloudSim.clock();
                    if (CloudSim.clock() <= deadlinetime) {

                        if ((now + (2 * MINUTE)) >= deadlinetime && sdvm.getTries() != Integer.MAX_VALUE) { // means this try should be last try to create vm 
                            //here request shrink datacenter
                            sdvm.setTries(Integer.MAX_VALUE);
                            createVmsInDatacenterInHost(sdvm);
                            break;
                        }
//                        Log.printLine(CloudSim.clock() + ": " + " trying again to create vm# " + vmIdno);
                        if (sdvm.getTries() <= Constants.VM_CREATION_MAX_TRIES) {
                            sdvm.setTries(sdvm.getTries() + 1);
                            createVmsInDatacenter(sdvm);
                        } else {
                            resolver.setStatus(cloudId, vmIdno, Constants.VM_STATUS_DELAYED, (int) clock, null);
//                            CloudTable.getCloudTable(cloudId).get(vmIdno).setStatus(Constants.VM_STATUS_DELAYED);
//                            Log.printLine(CloudSim.clock() + ": " + getName() + ": Creation of VM #" + vmIdno
//                                    + " has been delayed #");
                            getVmList().remove(sdvm);
                        }
                    } else {// if meet deadline reject the given request.
                        removeElementfromQueue(vmIdno);
                        resolver.setStatus(cloudId, vmIdno, Constants.VM_STATUS_REJECTED, (int) clock, null);
//                        CloudTable.getCloudTable(cloudId).get(vmIdno).setStatus(Constants.VM_STATUS_REJECTED);
//                        Log.printLine(CloudSim.clock() + ": " + getName() + ": Creation of VM #" + vmIdno
//                                + " failed in Datacenter #" + datacenterId);
                        getVmList().remove(sdvm); // remove the rejected request from it's particular queue.
                    }
                    break;
                case 3: //policy 3 :Fisst Locally then Federation
                    requesttime = CloudTable.getCloudTable(cloudId).get(vmIdno).getRequestTime();
                    deadline = CloudTable.getCloudTable(cloudId).get(vmIdno).getDeadline();
                    deadlinetime = requesttime + deadline;
                    if (now <= deadlinetime && now > 3600) {
                        setFederatedinQueue(vmIdno, cloudId, true);
                        List<ObjectiveFunctionPolynomial1> lpresult = new ArrayList<ObjectiveFunctionPolynomial1>();
                        lpresult.clear();
                        lpresult = getController().solveLP((int) now);
                        if (!lpresult.isEmpty()) {
                            for (ObjectiveFunctionPolynomial1 obj : lpresult) {
                                if (obj.getRequestId() == vmIdno && obj.getResult() == 1) {
                                    outSourceProcess(cloudId, obj.getF(), vmIdno);
                                    removeElementfromQueue(vmIdno);
                                    RunnerMain.outsources.add(new RunnerMain.Outsource(vmIdno, cloudId, obj.getF()));
                                    for (int y = 0; y < Constants.QUEUE_NUMBER; y++) {
                                        for (CloudTable.Requset r : CloudTable.getQueue(cloudId, y)) {
                                            if (r.isFederated()) {
                                                r.setFederated(false);
                                            }
                                        }
                                    }
                                    getVmList().remove(sdvm);
                                    break;
                                } else {
                                    resolver.setStatus(cloudId, vmIdno, Constants.VM_STATUS_DELAYED, (int) clock, null);
                                    setFederatedinQueue(vmIdno, cloudId, false);
                                    getVmList().remove(sdvm);
                                    break;
                                }
                            }
                        } else {
                            resolver.setStatus(cloudId, vmIdno, Constants.VM_STATUS_DELAYED, (int) clock, null);
                            setFederatedinQueue(vmIdno, cloudId, false);
                            getVmList().remove(sdvm);
                            break;
                        }
                    } else {
                        removeElementfromQueue(vmIdno);
                        resolver.setStatus(cloudId, vmIdno, Constants.VM_STATUS_REJECTED, (int) clock, null);
                        getVmList().remove(sdvm); // remove the rejected request from it's particular queue.
//                        RunnerMain.Rejected[cloudId]++;
                    }
                    break;
                case 4: //policy 4 :Fisst Locally then Federation else if deadline will be reached shrink local
                    requesttime = CloudTable.getCloudTable(cloudId).get(vmIdno).getRequestTime();
                    deadline = CloudTable.getCloudTable(cloudId).get(vmIdno).getDeadline();
                    deadlinetime = requesttime + deadline;
                    if (now <= deadlinetime && now > 3600) {

                        if ((now + (2 * MINUTE)) >= deadlinetime && sdvm.getTries() != Integer.MAX_VALUE) { // means this try should be last try to create vm 
                            //here request shrink datacenter
                            sdvm.setTries(Integer.MAX_VALUE);
                            createVmsInDatacenterInHost(sdvm);
                            break;
                        }
                        setFederatedinQueue(vmIdno, cloudId, true);
                        List<ObjectiveFunctionPolynomial1> lpresult = new ArrayList<ObjectiveFunctionPolynomial1>();
                        lpresult.clear();
                        lpresult = getController().solveLP((int) now);
                        if (!lpresult.isEmpty()) {
                            for (ObjectiveFunctionPolynomial1 obj : lpresult) {
                                if (obj.getRequestId() == vmIdno && obj.getResult() == 1) {
                                    outSourceProcess(cloudId, obj.getF(), vmIdno);
                                    removeElementfromQueue(vmIdno);
                                    RunnerMain.outsources.add(new RunnerMain.Outsource(vmIdno, cloudId, obj.getF()));
                                    for (int y = 0; y < Constants.QUEUE_NUMBER; y++) {
                                        for (CloudTable.Requset r : CloudTable.getQueue(cloudId, y)) {
                                            if (r.isFederated()) {
                                                r.setFederated(false);
                                            }
                                        }
                                    }
                                    getVmList().remove(sdvm);
                                    break;
                                } else {
                                    resolver.setStatus(cloudId, vmIdno, Constants.VM_STATUS_DELAYED, (int) clock, null);
                                    setFederatedinQueue(vmIdno, cloudId, false);
                                    getVmList().remove(sdvm);
                                    break;
                                }
                            }
                        } else {
//                        if ((now + (2 * MINUTE)) >= deadlinetime && sdvm.getTries() != Integer.MAX_VALUE) { // means this try should be last try to create vm 
//                            //here request shrink datacenter
//                            sdvm.setTries(Integer.MAX_VALUE);
//                            createVmsInDatacenterInHost(sdvm);
//                            break;
//                        }
                            resolver.setStatus(cloudId, vmIdno, Constants.VM_STATUS_DELAYED, (int) clock, null);
                            setFederatedinQueue(vmIdno, cloudId, false);
                            getVmList().remove(sdvm);
                            break;
                        }
                    } 
                    else { //if reached deadline but has not rejected yet.
                        removeElementfromQueue(vmIdno);
                        resolver.setStatus(cloudId, vmIdno, Constants.VM_STATUS_REJECTED, (int) clock, null);
                        getVmList().remove(sdvm); // remove the rejected request from it's particular queue.
//                        RunnerMain.Rejected[cloudId]++;
                    }
                    break;
                case 5: //policy 5 : Federation consideration 
                    requesttime = CloudTable.getCloudTable(cloudId).get(vmIdno).getRequestTime();
                    deadline = CloudTable.getCloudTable(cloudId).get(vmIdno).getDeadline();
                    deadlinetime = requesttime + deadline;
                    if (now <= deadlinetime && now > 3600) {
                        setFederatedinQueue(vmIdno, cloudId, true);
                        List<ObjectiveFunctionPolynomial1> lpresult = new ArrayList<ObjectiveFunctionPolynomial1>();
                        lpresult.clear();
                        lpresult = getController().solveLP((int) now);
                        if (!lpresult.isEmpty()) {
                            for (ObjectiveFunctionPolynomial1 obj : lpresult) {
                                if (obj.getRequestId() == vmIdno && obj.getResult() == 1) {
                                    outSourceProcess(cloudId, obj.getF(), vmIdno);
                                    removeElementfromQueue(vmIdno);
                                    RunnerMain.outsources.add(new RunnerMain.Outsource(vmIdno, cloudId, obj.getF()));
                                    for (int y = 0; y < Constants.QUEUE_NUMBER; y++) {
                                        for (CloudTable.Requset r : CloudTable.getQueue(cloudId, y)) {
                                            if (r.isFederated()) {
                                                r.setFederated(false);
                                            }
                                        }
                                    }
                                    getVmList().remove(sdvm);
                                    break;
                                } else {
                                    resolver.setStatus(cloudId, vmIdno, Constants.VM_STATUS_DELAYED, (int) clock, null);
                                    setFederatedinQueue(vmIdno, cloudId, false);
                                    getVmList().remove(sdvm);
                                    break;
                                }
                            }
                        } else {
                            resolver.setStatus(cloudId, vmIdno, Constants.VM_STATUS_DELAYED, (int) clock, null);
                            setFederatedinQueue(vmIdno, cloudId, false);
                            getVmList().remove(sdvm);
                            break;
                        }
                    } 
                    else { //if reached deadline but has not rejected yet.
                        removeElementfromQueue(vmIdno);
                        resolver.setStatus(cloudId, vmIdno, Constants.VM_STATUS_REJECTED, (int) clock, null);
                        getVmList().remove(sdvm); // remove the rejected request from it's particular queue.
//                        RunnerMain.Rejected[cloudId]++;
                    }
                    break;
                case 6: //policy 6: Federation consideration else local else shrink local
                    requesttime = CloudTable.getCloudTable(cloudId).get(vmIdno).getRequestTime();
                    deadline = CloudTable.getCloudTable(cloudId).get(vmIdno).getDeadline();
                    deadlinetime = requesttime + deadline;
                    if (now <= deadlinetime && now > 3600) {

                        if ((now + (2 * MINUTE)) >= deadlinetime && sdvm.getTries() != Integer.MAX_VALUE) { // means this try should be last try to create vm 
                            //here request shrink datacenter
                            sdvm.setTries(Integer.MAX_VALUE);
                            createVmsInDatacenterInHost(sdvm);
                            break;
                        }
                        setFederatedinQueue(vmIdno, cloudId, true);
                        List<ObjectiveFunctionPolynomial1> lpresult = new ArrayList<ObjectiveFunctionPolynomial1>();
                        lpresult.clear();
                        lpresult = getController().solveLP((int) now);
                        if (!lpresult.isEmpty()) {
                            for (ObjectiveFunctionPolynomial1 obj : lpresult) {
                                if (obj.getRequestId() == vmIdno && obj.getResult() == 1) {
                                    outSourceProcess(cloudId, obj.getF(), vmIdno);
                                    removeElementfromQueue(vmIdno);
                                    RunnerMain.outsources.add(new RunnerMain.Outsource(vmIdno, cloudId, obj.getF()));
                                    for (int y = 0; y < Constants.QUEUE_NUMBER; y++) {
                                        for (CloudTable.Requset r : CloudTable.getQueue(cloudId, y)) {
                                            if (r.isFederated()) {
                                                r.setFederated(false);
                                            }
                                        }
                                    }
                                    getVmList().remove(sdvm);
                                    break;
                                } else {
                                    resolver.setStatus(cloudId, vmIdno, Constants.VM_STATUS_DELAYED, (int) clock, null);
                                    setFederatedinQueue(vmIdno, cloudId, false);
                                    getVmList().remove(sdvm);
                                    break;
                                }
                            }
                        } else {
//                        if ((now + (2 * MINUTE)) >= deadlinetime && sdvm.getTries() != Integer.MAX_VALUE) { // means this try should be last try to create vm 
//                            //here request shrink datacenter
//                            sdvm.setTries(Integer.MAX_VALUE);
//                            createVmsInDatacenterInHost(sdvm);
//                            break;
//                        }
                            resolver.setStatus(cloudId, vmIdno, Constants.VM_STATUS_DELAYED, (int) clock, null);
                            setFederatedinQueue(vmIdno, cloudId, false);
                            getVmList().remove(sdvm);
                            break;
                        }
                    } 
                    else { //if reached deadline but has not rejected yet.
                        removeElementfromQueue(vmIdno);
                        resolver.setStatus(cloudId, vmIdno, Constants.VM_STATUS_REJECTED, (int) clock, null);
                        getVmList().remove(sdvm); // remove the rejected request from it's particular queue.
//                        RunnerMain.Rejected[cloudId]++;
                    }
                    break;
//                            if(now >15000)
//                                System.out.println("");
//                          Queue<CloudTable.Requset>[] l1 = CloudTable.getQueue(0);

                default:
                    throw new AssertionError();
            }

        }
        incrementVmsAcks();
        // all the requested VMs have been created
        if (getVmsCreatedList().size() == getVmList().size() - getVmsDestroyed()) {
            submitCloudlets();
//            Queue<CloudTable.Requset>[] l1 = CloudTable.getQueue(0);
//            System.out.println("");
        } else {
            /*
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
             */
        }
    }

    private void setFederatedinQueue(int vmIdno, int cloudId, boolean state) {
        for (int y = 0; y < Constants.QUEUE_NUMBER; y++) {
            for (CloudTable.Requset r : CloudTable.getQueue(cloudId, y)) {
                if (r.getRequestID() == vmIdno) {
                    r.setFederated(state);
                }
            }
        }

    }

    public void outSourceProcess(int sourcecloudId, int destinationcloudId, int requestIdinsource) {
        CloudTable.getCloudTable(sourcecloudId).get(requestIdinsource).setOutsourcedcloudId(destinationcloudId);
        CloudTableContents cloudtablecontent = new CloudTableContents();
        cloudtablecontent = setcontent(CloudTable.getCloudTable(sourcecloudId).get(requestIdinsource));
        CloudTable.getCloudTable(sourcecloudId).get(requestIdinsource).setStatus(Constants.VM_STATUS_OUTSOURCED);
        CloudTable.getCloudTable(destinationcloudId).put(requestIdinsource, cloudtablecontent);
        CloudTable.getCloudTable(destinationcloudId).get(requestIdinsource).setStatus(Constants.VM_STATUS_ARRIVING);
        addElementtoQueue(destinationcloudId, requestIdinsource);
    }

    private CloudTableContents setcontent(CloudTableContents cloudtablecontent) {
        CloudTableContents content = new CloudTableContents();

        content.setCpuconsumption(cloudtablecontent.getCpuconsumption());
        content.setDatacenterNumber(cloudtablecontent.getDatacenterNumber());
        content.setDeadline(cloudtablecontent.getDeadline());
        content.setDuraion(cloudtablecontent.getDuraion());
        content.setFinishTime(cloudtablecontent.getFinishTime());
        content.setHostNumber(cloudtablecontent.getHostNumber());
        content.setOutsourcedcloudId(cloudtablecontent.getOutsourcedcloudId());
        content.setRamconsumption(cloudtablecontent.getRamconsumption());
        content.setRequestTime(cloudtablecontent.getRequestTime());
        content.setShrink(cloudtablecontent.isShrink());
        content.setSourcecloudId(cloudtablecontent.getSourcecloudId());
        content.setStartTime(cloudtablecontent.getStartTime());
        content.setStatus(cloudtablecontent.getStatus());
        content.setVmType(cloudtablecontent.getVmType());
        content.setCost(cloudtablecontent.getCost());
        content.setRevenue(cloudtablecontent.getRevenue());
        content.setSlapenalty(cloudtablecontent.getSlapenalty());
        return content;

    }

    private int extractVmId(int vmId) {
        String Id = Integer.toString(vmId);// = (String)vmId;
        Id = Id.substring(1, Id.length());
        vmId = Integer.parseInt(Id);
        return vmId;
    }

    private void removeElementfromQueue(int vmId) {
        int IdNo = CloudTable.getCloudTable(cloudId).get(vmId).getVmType();
        for (int y = 0; y < Constants.QUEUE_NUMBER; y++) {
            for (CloudTable.Requset r : CloudTable.getQueue(cloudId, y)) {
                if (r.getRequestID() == vmId) {
                    CloudTable.getQueue(cloudId, y).remove(r);
                }
            }
        }
    }

    private boolean isElementinQueue(int vmId) {
        int IdNo = CloudTable.getCloudTable(cloudId).get(vmId).getVmType();
        for (int y = 0; y < Constants.QUEUE_NUMBER; y++) {
            for (CloudTable.Requset r : CloudTable.getQueue(cloudId, y)) {
                if (r.getRequestID() == vmId) {
                    return true;
                }
            }
        }
        return false;
    }

    private void ElementinQueue(int vmId) {
        int IdNo = CloudTable.getCloudTable(cloudId).get(vmId).getVmType();
        for (int y = 0; y < Constants.QUEUE_NUMBER; y++) {
            for (CloudTable.Requset r : CloudTable.getQueue(cloudId, y)) {
                System.out.println(r.getRequestID());
            }
        }
    }

    private void addElementtoQueue(int descloudId, int requestId) {

        int queueId = CloudTable.getCloudTable(descloudId).get(requestId).getVmType();
        CloudTable.getQueue(cloudId, queueId).add(new CloudTable.Requset(requestId, Integer.MAX_VALUE, false));
    }

    /**
     * Sets the datacenter characteristics list.
     *
     * @param datacenterCharacteristicsList the datacenter characteristics list
     */
    public CloudCoordinator getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(CloudCoordinator coordinator) {
        this.coordinator = coordinator;
    }
    /*
     protected void createVmsInDatacenter(int datacenterId) {
     // send as much vms as possible for this datacenter before trying the next one
     int requestedVms = 0;
     String datacenterName = CloudSim.getEntityName(datacenterId);
                
     List<Vm> list = getVmList();
     SdVm sd;
     for(Vm vv : list)
     sd = (SdVm)vv;
     for (Vm vm : getVmList()) {
     //Call Cloud CloudCoordinator to  place a vm in federated cloud  
     //                    getCoordinator().allocateResourceVm(null, WAITING);
     if (!getVmsToDatacentersMap().containsKey(vm.getId())) { //if a given vm has'nt created yet then
     Log.printLine(CloudSim.clock() + ": " + getName() + ": Trying to Create VM #" + vm.getId()
     + " in " + datacenterName);
     //here I should determine in which data center a given vm has to be created.				
     //datacenterId = the data center Id that has been selected by our algorithm
     sendNow(datacenterId, CloudSimTags.VM_CREATE_ACK, vm);
     //                            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||"+requestedVms);
     requestedVms++;
     }
     }
     //datacenterId of the data center which requested will be added to the list
     getDatacenterRequestedIdsList().add(datacenterId);
     //                System.out.println("		getDatacenterRequestedIdsList().add(datacenterId)"+datacenterId);

     setVmsRequested(requestedVms);
     setVmsAcks(0);
     }*/
}
/*
 switch (RunnerMain.policy) {
 case 1: //policy 1 :Fully-In-House no federation
 requesttime = CloudTable.getCloudTable(cloudId).get(vmIdno).getRequestTime();
 deadline = CloudTable.getCloudTable(cloudId).get(vmIdno).getDeadline();
 deadlinetime = requesttime + deadline;
 //                    cloudsimtime = CloudSim.clock();
 if (CloudSim.clock() <= deadlinetime) {
 int tries = sdvm.getTries();
 if (tries <= Constants.VM_CREATION_MAX_TRIES) {
 sdvm.setTries(tries + 1);
 createVmsInDatacenter(sdvm);
 } else {
 resolver.setStatus(cloudId, vmIdno, Constants.VM_STATUS_DELAYED, (int) clock);
 //                            CloudTable.getCloudTable(cloudId).get(vmIdno).setStatus(Constants.VM_STATUS_DELAYED);
 //                            Log.printLine(CloudSim.clock() + ": " + getName() + ": Creation of VM #" + vmIdno
 //                                    + " has been delayed #");
 getVmList().remove(sdvm);
 }
 } else {
 removeElementfromQueue(vmIdno);
 resolver.setStatus(cloudId, vmIdno, Constants.VM_STATUS_REJECTED, (int) clock);
 //                        CloudTable.getCloudTable(cloudId).get(vmIdno).setStatus(Constants.VM_STATUS_REJECTED);
 //                        Log.printLine(CloudSim.clock() + ": " + getName() + ": Creation of VM #" + vmIdno
 //                                + " failed in Datacenter #" + datacenterId + " due to meeting deadline");
 getVmList().remove(sdvm);
 }
 break;

 case 2://policy 2: Fully-In-House-Shrink no federation
 requesttime = CloudTable.getCloudTable(cloudId).get(vmIdno).getRequestTime();
 deadline = CloudTable.getCloudTable(cloudId).get(vmIdno).getDeadline();
 deadlinetime = requesttime + deadline;
 //                    cloudsimtime = CloudSim.clock();
 if (CloudSim.clock() <= deadlinetime) {

 if ((now + (2 * MINUTE)) >= deadlinetime && sdvm.getTries() != Integer.MAX_VALUE) { // means this try should be last try to create vm 
 //here request shrink datacenter
 sdvm.setTries(Integer.MAX_VALUE);
 createVmsInDatacenterInHost(sdvm);
 break;
 }
 //                        Log.printLine(CloudSim.clock() + ": " + " trying again to create vm# " + vmIdno);
 if (sdvm.getTries() <= Constants.VM_CREATION_MAX_TRIES) {
 sdvm.setTries(sdvm.getTries() + 1);
 createVmsInDatacenter(sdvm);
 } else {
 resolver.setStatus(cloudId, vmIdno, Constants.VM_STATUS_DELAYED, (int) clock);
 //                            CloudTable.getCloudTable(cloudId).get(vmIdno).setStatus(Constants.VM_STATUS_DELAYED);
 //                            Log.printLine(CloudSim.clock() + ": " + getName() + ": Creation of VM #" + vmIdno
 //                                    + " has been delayed #");
 getVmList().remove(sdvm);
 }
 } else {// if meet deadline reject the given request.
 removeElementfromQueue(vmIdno);
 resolver.setStatus(cloudId, vmIdno, Constants.VM_STATUS_REJECTED, (int) clock);
 //                        CloudTable.getCloudTable(cloudId).get(vmIdno).setStatus(Constants.VM_STATUS_REJECTED);
 //                        Log.printLine(CloudSim.clock() + ": " + getName() + ": Creation of VM #" + vmIdno
 //                                + " failed in Datacenter #" + datacenterId);
 getVmList().remove(sdvm); // remove the rejected request from it's particular queue.
 }
 break;
 case 3: //policy 3 :Fully-In-House 
 requesttime = CloudTable.getCloudTable(cloudId).get(vmIdno).getRequestTime();
 deadline = CloudTable.getCloudTable(cloudId).get(vmIdno).getDeadline();
 deadlinetime = requesttime + deadline;
 if (now <= deadlinetime) {
 setFederatedinQueue(vmIdno, cloudId, true);
 Queue<CloudTable.Requset>[] l1 = CloudTable.getQueue(cloudId);
 List<ObjectiveFunctionPolynomial1> lpresult = new ArrayList<ObjectiveFunctionPolynomial1>();
 lpresult.clear();
 lpresult = getController().solveLP((int) now);
 for (ObjectiveFunctionPolynomial1 obj : lpresult) {
 if (obj.getRequestId() == vmIdno && obj.getResult() == 1) {
 outSourceProcess(cloudId, obj.getF(), vmIdno);
 removeElementfromQueue(vmIdno);
 getVmList().remove(sdvm);
 break;
 } else {
 resolver.setStatus(cloudId, vmIdno, Constants.VM_STATUS_DELAYED, (int) clock);
 setFederatedinQueue(vmIdno, cloudId, false);
 getVmList().remove(sdvm);
 break;
 }
 }
 } else {
 removeElementfromQueue(vmIdno);
 resolver.setStatus(cloudId, vmIdno, Constants.VM_STATUS_REJECTED, (int) clock);
 getVmList().remove(sdvm); // remove the rejected request from it's particular queue.

 }
 /*if (CloudSim.clock() <= deadlinetime) {
 int tries = sdvm.getTries();
 if (tries <= Constants.VM_CREATION_MAX_TRIES) {
 sdvm.setTries(tries + 1);

 createVmsInDatacenter(sdvm);
 } else {
 resolver.setStatus(cloudId, vmIdno, Constants.VM_STATUS_DELAYED, (int) clock);
 getVmList().remove(sdvm);
 }
 } else {
 removeElementfromQueue(vmIdno);
 resolver.setStatus(cloudId, vmIdno, Constants.VM_STATUS_REJECTED, (int) clock);
 getVmList().remove(sdvm);
 }*/
//                    break;
//                default:
//                    throw new AssertionError();
//            } 

