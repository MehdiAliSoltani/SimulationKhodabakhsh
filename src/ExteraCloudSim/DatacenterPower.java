/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExteraCloudSim;

import Test.CloudSimExample1;
import java.util.List;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;
import simulation.AppConstants;

/**
 *
 * @author Novin Pendar
 */
public class DatacenterPower extends Datacenter {

    private int datacenterID;
    public DatacenterPower(String name, DatacenterCharacteristics characteristics, VmAllocationPolicy vmAllocationPolicy, List<Storage> storageList, double schedulingInterval) throws Exception {
        super(name, characteristics, vmAllocationPolicy, storageList, schedulingInterval);
    }

    public DatacenterPower(int datacenterID, String name, DatacenterCharacteristics characteristics, VmAllocationPolicy vmAllocationPolicy, List<Storage> storageList, double schedulingInterval) throws Exception {
        super(name, characteristics, vmAllocationPolicy, storageList, schedulingInterval);
        this.datacenterID = datacenterID;
    }

    @Override
    public void processEvent(SimEvent ev) {
        int srcId = -1;
        switch (ev.getTag()) {
            // Resource characteristics inquiry
            case CloudSimTags.RESOURCE_CHARACTERISTICS:
                srcId = ((Integer) ev.getData()).intValue();
                sendNow(srcId, ev.getTag(), getCharacteristics());
                break;

            // Resource dynamic info inquiry
            case CloudSimTags.RESOURCE_DYNAMICS:
                srcId = ((Integer) ev.getData()).intValue();
                sendNow(srcId, ev.getTag(), 0);
                break;

            case CloudSimTags.RESOURCE_NUM_PE:
                srcId = ((Integer) ev.getData()).intValue();
                int numPE = getCharacteristics().getNumberOfPes();
                sendNow(srcId, ev.getTag(), numPE);
                break;

            case CloudSimTags.RESOURCE_NUM_FREE_PE:
                srcId = ((Integer) ev.getData()).intValue();
                int freePesNumber = getCharacteristics().getNumberOfFreePes();
                sendNow(srcId, ev.getTag(), freePesNumber);
                break;

            // New Cloudlet arrives
            case CloudSimTags.CLOUDLET_SUBMIT:
                processCloudletSubmit(ev, false);
                break;

            // New Cloudlet arrives, but the sender asks for an ack
            case CloudSimTags.CLOUDLET_SUBMIT_ACK:
                processCloudletSubmit(ev, true);
                break;

            // Cancels a previously submitted Cloudlet
            case CloudSimTags.CLOUDLET_CANCEL:
                processCloudlet(ev, CloudSimTags.CLOUDLET_CANCEL);
                break;

            // Pauses a previously submitted Cloudlet
            case CloudSimTags.CLOUDLET_PAUSE:
                processCloudlet(ev, CloudSimTags.CLOUDLET_PAUSE);
                break;

            // Pauses a previously submitted Cloudlet, but the sender
            // asks for an acknowledgement
            case CloudSimTags.CLOUDLET_PAUSE_ACK:
                processCloudlet(ev, CloudSimTags.CLOUDLET_PAUSE_ACK);
                break;

            // Resumes a previously submitted Cloudlet
            case CloudSimTags.CLOUDLET_RESUME:
                processCloudlet(ev, CloudSimTags.CLOUDLET_RESUME);
                break;

            // Resumes a previously submitted Cloudlet, but the sender
            // asks for an acknowledgement
            case CloudSimTags.CLOUDLET_RESUME_ACK:
                processCloudlet(ev, CloudSimTags.CLOUDLET_RESUME_ACK);
                break;

            // Moves a previously submitted Cloudlet to a different resource
            case CloudSimTags.CLOUDLET_MOVE:
                processCloudletMove((int[]) ev.getData(), CloudSimTags.CLOUDLET_MOVE);
                break;

            // Moves a previously submitted Cloudlet to a different resource
            case CloudSimTags.CLOUDLET_MOVE_ACK:
                processCloudletMove((int[]) ev.getData(), CloudSimTags.CLOUDLET_MOVE_ACK);
                break;

            // Checks the status of a Cloudlet
            case CloudSimTags.CLOUDLET_STATUS:
                processCloudletStatus(ev);
                break;

            // Ping packet
            case CloudSimTags.INFOPKT_SUBMIT:
                processPingRequest(ev);
                break;

            case CloudSimTags.VM_CREATE:
                processVmCreate(ev, false);
                break;

            case CloudSimTags.VM_CREATE_ACK:
                processVmCreate(ev, true);
                break;

            case CloudSimTags.VM_DESTROY:
                processVmDestroy(ev, false);
                break;

            case CloudSimTags.VM_DESTROY_ACK:
                processVmDestroy(ev, true);
                break;

            case CloudSimTags.VM_MIGRATE:
                processVmMigrate(ev, false);
                break;

            case CloudSimTags.VM_MIGRATE_ACK:
                processVmMigrate(ev, true);
                break;

            case CloudSimTags.VM_DATA_ADD:
                processDataAdd(ev, false);
                break;

            case CloudSimTags.VM_DATA_ADD_ACK:
                processDataAdd(ev, true);
                break;

            case CloudSimTags.VM_DATA_DEL:
                processDataDelete(ev, false);
                break;

            case CloudSimTags.VM_DATA_DEL_ACK:
                processDataDelete(ev, true);
                break;

            case CloudSimTags.VM_DATACENTER_EVENT:
                updateCloudletProcessing();
                checkCloudletCompletion();
                break;
//            case AppConstants.PROCESS_CREATEVM_In_HOST:
//                processVmCreateInHost(ev, true);
//                break;

            // other unknown tags are processed by this method
            default:
                processOtherEvent(ev);
                break;
        }
    }
/*
    protected void processVmCreateInHost(SimEvent ev, boolean ack) {
//        SdVm vm = (SdVm) ev.getData();
        Vm vm = (Vm) ev.getData();
        boolean result;
        double currenttime = CloudSim.clock();
        HostPower hostpower = null;// = doShrinking(currenttime, vm);
        if (hostpower == null) {
            result = false;
        } else {
            result = getVmAllocationPolicy().allocateHostForVm(vm, hostpower);
        }

        if (ack) {
            int[] data = new int[3];
            data[0] = getId();
            data[1] = vm.getId();

            if (result) {
                data[2] = CloudSimTags.TRUE;
            } else {
                data[2] = CloudSimTags.FALSE;
            }

            sendNow(vm.getUserId(), CloudSimTags.VM_CREATE_ACK, data);
        }

        if (result) {
            double amount = 0.0;
            if (getDebts().containsKey(vm.getUserId())) {
                amount = getDebts().get(vm.getUserId());
            }
            amount += getCharacteristics().getCostPerMem() * vm.getRam();
            amount += getCharacteristics().getCostPerStorage() * vm.getSize();

            getDebts().put(vm.getUserId(), amount);

            getVmList().add(vm);

            vm.updateVmProcessing(CloudSim.clock(), getVmAllocationPolicy().getHost(vm).getVmScheduler().getAllocatedMipsForVm(vm));
        }

    }
*/
    @Override
    protected void processVmCreate(SimEvent ev, boolean ack) {
        Vm vm = (Vm) ev.getData();
//        Host h = CloudSimExample1.hostList.get(2);
//        boolean result = getVmAllocationPolicy().allocateHostForVm(vm,h);
        boolean result = getVmAllocationPolicy().allocateHostForVm(vm);
//        System.out.println("****************************************************************");
//        System.out.println("****************************************************************");
//        System.out.println("****************************************************************");
        if (ack) {
            int[] data = new int[3];
            data[0] = getId();
            data[1] = vm.getId();

            if (result) {
                data[2] = CloudSimTags.TRUE;
            } else {
                data[2] = CloudSimTags.FALSE;
            }
            send(vm.getUserId(), CloudSim.getMinTimeBetweenEvents(), CloudSimTags.VM_CREATE_ACK, data);
        }

        if (result) {
            getVmList().add(vm);

            if (vm.isBeingInstantiated()) {
                vm.setBeingInstantiated(false);
            }

            vm.updateVmProcessing(CloudSim.clock(), getVmAllocationPolicy().getHost(vm).getVmScheduler()
                    .getAllocatedMipsForVm(vm));
        }

    }

}
