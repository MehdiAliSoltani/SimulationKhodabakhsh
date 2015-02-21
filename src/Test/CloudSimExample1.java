package Test;

/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation
 *               of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009, The University of Melbourne, Australia
 */
import ExteraCloudSim.CloudletSchedulerSpaceSharedd;
import ExteraCloudSim.DatacenterBrokerPower;
import ExteraCloudSim.DatacenterPower;
import ExteraCloudSim.HostPower;
import ExteraCloudSim.VmPower;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerDynamicWorkload;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.UtilizationModelNull;
import org.cloudbus.cloudsim.UtilizationModelStochastic;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.VmSchedulerTimeSharedOverSubscription;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerVm;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerHpProLiantMl110G4Xeon3040;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

/**
 * A simple example showing how to create a datacenter with one host and run one
 * cloudlet on it.
 */
public class CloudSimExample1 {
    public static int vmprocessingcount=0;
    public static int hostprocessingcount=0;
    

    static double INTERVAL = 100;
    public static List<HostPower> hostList;
    /**
     * The cloudlet list.
     */
    private static List<Cloudlet> cloudletList;

    /**
     * The vmlist.
     */
    private static List<VmPower> vmlist;
//    private static List<PowerVm> vmlist;

    /**
     * Creates main() to run this example.
     *
     * @param args the args
     */
    @SuppressWarnings("unused")
    public static void main(String[] args) {

        Log.printLine("Starting CloudSimExample1...");

        try {
            // First step: Initialize the CloudSim package. It should be called
            // before creating any entities.
            int num_user = 1; // number of cloud users
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false; // mean trace events

            // Initialize the CloudSim library
            CloudSim.init(num_user, calendar, trace_flag);

            // Second step: Create Datacenters
            // Datacenters are the resource providers in CloudSim. We need at
            // list one of them to run a CloudSim simulation
            DatacenterPower datacenter0 = createDatacenter("Datacenter_0");
            DatacenterPower datacenter1 = createDatacenter("Datacenter_1");
            DatacenterPower datacenter2 = createDatacenter("Datacenter_2");
            DatacenterPower datacenter3 = createDatacenter("Datacenter_2");
            DatacenterPower datacenter4 = createDatacenter("Datacenter_2");

            // Third step: Create Broker
            DatacenterBrokerPower broker = createBroker();
            int brokerId = broker.getId();

            // Fourth step: Create one virtual machine
//            vmlist = new ArrayList<PowerVm>();
            vmlist = new ArrayList<VmPower>();

            // VM description
            int vmid = 0;
            int mips = 1000;
            long size = 10000; // image size (MB)
            int ram = 512; // vm memory (MB)
            long bw = 3000;
            int pesNumber = 2; // number of cpus
            String vmm = "Xen"; // VMM name

            // create VM
//            VmPower vm = null;
            VmPower vm = null;

            for (int i = 0; i < 1; i++) {
                vm = new VmPower(i, brokerId, mips, pesNumber, ram, bw, size,1,  vmm,
//                                                      new CloudletSchedulerSpaceSharedd(),0);
                        new CloudletSchedulerDynamicWorkload(mips, pesNumber)
                        , 0);
                vmlist.add(vm);
            }

            // add the VM to the vmList
//			vmlist.add(vm);
            // submit vm list to the broker
            broker.submitVmList(vmlist);
//            NewEntity en = new NewEntity(vmm);

            // Fifth step: Create one Cloudlet
            cloudletList = new ArrayList<Cloudlet>();

            // Cloudlet properties
            int id = 0;
            long length = 10000;
            long fileSize = 3000000;
            long outputSize = 3000000;
            UtilizationModel utilizationModel = new UtilizationModelFull();

            for (int i = 0; i < 10; i++) {
                Cloudlet cloudlet = new Cloudlet(i, 1000
//                        (long) (Math.random() * 10000) //                        1000
                        , pesNumber, fileSize, outputSize,
                        new UtilizationModelStochastic(),
                        new UtilizationModelNull(),
                        new UtilizationModelNull());
//                        utilizationModel, 
//                        utilizationModel, 
//                        utilizationModel);
                cloudlet.setUserId(brokerId);
                
                        
                cloudletList.add(cloudlet);
                
//                cloudlet.setVmId(i % 10);
            }
            // add the cloudlet to the list

            // submit cloudlet list to the broker
            broker.submitCloudletList(cloudletList);

            // Sixth step: Starts the simulation
            CloudSim.startSimulation();

            CloudSim.stopSimulation();

            //Final step: Print results when simulation is over
            List<Cloudlet> newList = broker.getCloudletReceivedList();
            printCloudletList(newList);
            System.out.println("vm processing "+ vmprocessingcount);
            System.out.println("host processing "+ hostprocessingcount);
            Log.printLine("CloudSimExample1 finished!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("Unwanted errors happen");
        }
    }

    private static DatacenterPower createDatacenter(String name) {
        hostList = new ArrayList<HostPower>();
        List<Pe> peList = new ArrayList<Pe>();

        int mips = 1000;
        peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
        peList.add(new Pe(1, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
        peList.add(new Pe(2, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
        peList.add(new Pe(3, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating

        // of machines
        int hostId = 0;
        int ram = 2048; // host memory (MB)
//        int ram = 3400; // host memory (MB)
        long storage = 1000000; // host storage
        int bw = 10000;

        hostList.add(
                new HostPower(
                        0,
                        new RamProvisionerSimple(ram),
                        new BwProvisionerSimple(bw),
                        storage,
                        peList,
                        new VmSchedulerTimeSharedOverSubscription(peList)
//                                        new VmSchedulerTimeShared(peList)
                        

                )
        ); // This is our machine
        hostList.add(
                new HostPower(
                        1,
                        new RamProvisionerSimple(ram),
                        new BwProvisionerSimple(bw),
                        storage,
                        peList,
//                                                new VmSchedulerTimeShared(peList)
                        new VmSchedulerTimeSharedOverSubscription(peList)
                //                        new PowerModelSpecPowerHpProLiantMl110G4Xeon3040()
                )
        ); // This is our machine
        hostList.add(
                new HostPower(
                        2,
                        new RamProvisionerSimple(ram),
                        new BwProvisionerSimple(bw),
                        storage,
                        peList,
//                                                new VmSchedulerTimeShared(peList)
                        new VmSchedulerTimeSharedOverSubscription(peList)
                //                        new PowerModelSpecPowerHpProLiantMl110G4Xeon3040()
                )
        ); // This is our machine
        hostList.add(
                new HostPower(
                        3,
                        new RamProvisionerSimple(ram),
                        new BwProvisionerSimple(bw),
                        storage,
                        peList,
                        new VmSchedulerTimeSharedOverSubscription(peList)
//                                        new VmSchedulerTimeShared(peList)
//                                        new PowerModelSpecPowerHpProLiantMl110G4Xeon3040()
                )
        ); // This is our machine
        hostList.add(
                new HostPower(
                       4,
                        new RamProvisionerSimple(ram),
                        new BwProvisionerSimple(bw),
                        storage,
                        peList,
                        new VmSchedulerTimeSharedOverSubscription(peList)
//                                        new VmSchedulerTimeShared(peList)
                //                        new PowerModelSpecPowerHpProLiantMl110G4Xeon3040()
                )
        ); // This is our machine

//        ); // This is our machine
        // 5. Create a DatacenterCharacteristics object that stores the
        // properties of a data center: architecture, OS, list of
        // Machines, allocation policy: time- or space-shared, time zone
        // and its price (G$/Pe time unit).
        String arch = "x86"; // system architecture
        String os = "Linux"; // operating system
        String vmm = "Xen";
        double time_zone = 10.0; // time zone this resource located
        double cost = 3.0; // the cost of using processing in this resource
        double costPerMem = 0.05; // the cost of using memory in this resource
        double costPerStorage = 0.001; // the cost of using storage in this
        // resource
        double costPerBw = 0.0; // the cost of using bw in this resource
        LinkedList<Storage> storageList = new LinkedList<Storage>(); // we are not adding SAN
        // devices by now

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem,
                costPerStorage, costPerBw);

        // 6. Finally, we need to create a PowerDatacenter object.
        DatacenterPower datacenter = null;
        try { 
            datacenter = new DatacenterPower(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datacenter;
    }

       // We strongly encourage users to develop their own broker policies, to
    // submit vms and cloudlets according
    // to the specific rules of the simulated scenario
    /**
     * Creates the broker.
     *
     * @return the datacenter broker
     */
    private static DatacenterBrokerPower createBroker() {
        DatacenterBrokerPower broker = null;
        try {
            broker = new DatacenterBrokerPower("Broker");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return broker;
    }

    /**
     * Prints the Cloudlet objects.
     *
     * @param list list of Cloudlets
     */
    private static void printCloudletList(List<Cloudlet> list) {
        int size = list.size();
        Cloudlet cloudlet;

        String indent = "    ";
        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent
                + "Data center ID" + indent + "VM ID" + indent + "Time" + indent
                + "Start Time" + indent + "Finish Time");

        DecimalFormat dft = new DecimalFormat("###.##");
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);
            Log.print(indent + cloudlet.getCloudletId() + indent + indent);

            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
                Log.print("SUCCESS");

                Log.printLine(indent + indent + cloudlet.getResourceId()
                        + indent + indent + indent + cloudlet.getVmId()
                        + indent + indent
                        + dft.format(cloudlet.getActualCPUTime()) + indent
                        + indent + dft.format(cloudlet.getExecStartTime())
                        + indent + indent
                        + dft.format(cloudlet.getFinishTime())
                //                         cloudlet.getCloudletTotalLength()
                );
            }
        }
    }

}
