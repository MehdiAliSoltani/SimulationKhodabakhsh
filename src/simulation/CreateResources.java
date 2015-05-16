/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import AppWorkload.Workload;
import ExteraCloudSim.CloudletPower;
import ExteraCloudSim.DatacenterBrokerPower;
import ExteraCloudSim.DatacenterPower;
import ExteraCloudSim.HostPower;
import ExteraCloudSim.VmPower;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerDynamicWorkload;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.UtilizationModelNull;
import org.cloudbus.cloudsim.UtilizationModelStochastic;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.VmSchedulerTimeSharedOverSubscription;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import simulation.Simulation;

/**
 *
 * @author Novin Pendar
 */
public class CreateResources {

    private static List<Cloudlet> cloudletList;
//    private int datacenterID;

    public DatacenterPower createDatacenter(String name, int datacenterId) {

        createHostList(AppConstants.NUM_COMPUTE_SERVERS[datacenterId], datacenterId);
        String arch = "x86"; // system architecture
        String os = "Linux"; // operating system
        String vmm = "Xen";
        double time_zone = 10.0; // time zone this resource located
        double cost = 3.0; // the cost of using processing in this resource
        double costPerMem = 0.05; // the cost of using memory in this resource
        double costPerStorage = 0.001; // the cost of using storage in this
        // resource
        double costPerBw = 0.0; // the cost of using bw in this resource
        double schedulinginterval = AppConstants.SCHEDULING_INTERVAL;
        LinkedList<Storage> storageList = new LinkedList<Storage>(); // we are not adding SAN
        // devices by now

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, Simulation.COMPUTE_SERVER_LIST[datacenterId], time_zone, cost, costPerMem,
                costPerStorage, costPerBw);

        // 6. Finally, we need to create a PowerDatacenter object.
        DatacenterPower datacenter = null;
        try {
//            datacenter = new DatacenterPower(this.datacenterID,
            datacenter = new DatacenterPower(
                    name,
                    characteristics,
                    new VmAllocationPolicySimple(Simulation.COMPUTE_SERVER_LIST[datacenterId]),
                    storageList,
                    schedulinginterval);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datacenter;
    }

    /**
     * Create Hosts
     */
    private void createHostList(int numberofhosts, int datacenterId) {
//        List<HostPower> hostlist = new ArrayList<HostPower>();
        List<Pe> peList = new ArrayList<Pe>();
        for (int i = 0; i < AppConstants.NUM_PE_per_CS; i++) {
            peList.add(new Pe(i, new PeProvisionerSimple(AppConstants.PE_MIPS)));
        }
        for (int hostID = 0; hostID < AppConstants.NUM_COMPUTE_SERVERS[datacenterId]; hostID++) {
            Simulation.COMPUTE_SERVER_LIST[datacenterId].add(new HostPower(datacenterId,
                    hostID,
                    new RamProvisionerSimple(AppConstants.CS_RAM),
                    new BwProvisionerSimple(AppConstants.CS_BAND_WIDTH),
                    AppConstants.STORAGE_per_CS,
                    peList,
                    //                    new VmSchedulerTimeShared(peList)
                    new VmSchedulerTimeSharedOverSubscription(peList)
            ));

        }
    }

    private int getVmId() {
        return Simulation.Vm_NO++;
    }

    private void incrementVmNumber(int vmtype, int datacenterId) {
        Simulation.VM_NUM_TYPE[datacenterId][vmtype]++;
    }

    public void createVM(int userId, int vmtype, int datacenterId) {

        //VM Parameters
        long size = AppConstants.VM_IMAGE_SIZE[vmtype]; //image size (MB)
        int ram = AppConstants.VM_RAM[vmtype]; //vm memory (MB)
        int mips = AppConstants.VM_MIPS;
        long bw = AppConstants.VM_BAND_WIDTH[vmtype];
        int pesNumber = AppConstants.VM_PES[vmtype]; //number of cpus
        String vmm = "Xen"; //VMM name

        //create VMs
        int vmNum = AppConstants.NUM_of_VM_in_HOST[vmtype] * AppConstants.NUM_COMPUTE_SERVERS[datacenterId];
        for (int i = 0; i < vmNum; i++) {
            VmPower vmpower = new VmPower(getVmId(),
                    userId,
                    mips,
                    pesNumber,
                    ram,
                    bw,
                    size,
                    1,
                    vmm,
                    new CloudletSchedulerDynamicWorkload(mips, pesNumber), 0);
//                    new CloudletSchedule  rSpaceShared(), 0);
//                    new CloudletSchedulerTimeShared(), 0);
            Simulation.VMLIST.add(vmpower);
            incrementVmNumber(vmtype, datacenterId);
//            System.out.println(""+Simulation.VM_NUM_TYPE[this.datacenterID][vmtype]);
        }

    }

    public void TempcreateVM(int userId, int vmtype, int datacenterId) {

        //VM Parameters
        long size = AppConstants.VM_IMAGE_SIZE[vmtype]; //image size (MB)
        int ram = AppConstants.VM_RAM[vmtype]; //vm memory (MB)
        int mips = AppConstants.VM_MIPS;
        long bw = AppConstants.VM_BAND_WIDTH[vmtype];
        int pesNumber = AppConstants.VM_PES[vmtype]; //number of cpus
        String vmm = "Xen"; //VMM name

        //create VMs
        int vmNum = AppConstants.NUM_of_VM_in_HOST[vmtype] * AppConstants.NUM_COMPUTE_SERVERS[datacenterId];
        VmPower vmpower = new VmPower(getVmId(),
                userId,
                mips,
                pesNumber,
                ram,
                bw,
                size,
                1,
                vmm,
                new CloudletSchedulerDynamicWorkload(mips, pesNumber), 0);
//                    new CloudletSchedulerSpaceShared(), 0);
//                    new CloudletSchedulerTimeShared(), 0);
        Simulation.VMLIST.add(vmpower);
        incrementVmNumber(vmtype, datacenterId);
//            System.out.println(""+Simulation.VM_NUM_TYPE[this.datacenterID][vmtype]);

    }

    public DatacenterBrokerPower createBroker() {
        DatacenterBrokerPower broker = null;
        try {
            broker = new DatacenterBrokerPower("Broker");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return broker;
    }

    public CloudletPower createCloudlet(int userId, Workload request) {
        int dataStorageDatacenterId = request.getDatastorageDcId();
        int dataStorageId = request.getDataserverNode();
        long arrivalTime = request.getArrivalTime();
        int numberofFiles = request.getNumberofFiles();
        long[] sizeofFiles = request.getSizeofFile();
        long[] timeofIO = request.getTimetoIO();
        int cloudletId = request.getId();
        long cloudletLength = request.getSize();
        int pesNumber = request.getPes();
        long cloudletFileSize = request.getFilesize();
        long cloudletOutputSize = request.getOutputfilsize();
        CloudletPower cloudlet = new CloudletPower(dataStorageDatacenterId,
                dataStorageId,
                arrivalTime,
                numberofFiles,
                sizeofFiles,
                timeofIO,
                cloudletId,
                cloudletLength,
                pesNumber,
                cloudletFileSize,
                cloudletOutputSize,
                new UtilizationModelStochastic(),
                new UtilizationModelNull(),
                new UtilizationModelNull());
        cloudlet.setUserId(userId);

        return cloudlet;
    }

    private static List<Cloudlet> createCloudlet(int userId, int cloudlets, int idShift) {
        // Creates a container to store Cloudlets
        LinkedList<Cloudlet> list = new LinkedList<Cloudlet>();

        //cloudlet parameters
        long length = 40000;
        long fileSize = 300;
        long outputSize = 300;
        int pesNumber = 1;
        UtilizationModel utilizationModel = new UtilizationModelFull();

        Cloudlet[] cloudlet = new Cloudlet[cloudlets];

        for (int i = 0; i < cloudlets; i++) {
            cloudlet[i] = new Cloudlet(idShift + i, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
            // setting the owner of these Cloudlets
            cloudlet[i].setUserId(userId);

            list.add(cloudlet[i]);
        }

        return list;
    }

}
