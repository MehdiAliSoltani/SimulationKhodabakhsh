/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import Agents.AdmissionAgent;
import Agents.NetworkAgent;
import Agents.QueuingAgent;
import CloudManagement.CloudManagement;
import ExteraCloudSim.DatacenterBrokerPower;
import ExteraCloudSim.DatacenterPower;
import ExteraCloudSim.HostPower;
import ExteraCloudSim.VmPower;
import Resource.StorageHost;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import org.cloudbus.cloudsim.core.CloudSim;

/**
 *
 * @author Novin Pendar
 */
public class Simulation {

    public static int SENARIO = 1;
    public static int NO_VM = 0;
    public static int Vm_NO = 0;
//    public static List<HostPower> COMPUTE_SERVER_LIST = new ArrayList<HostPower>();  // list of hosts
    public static List<HostPower>[] COMPUTE_SERVER_LIST = new List[AppConstants.NUM_DATACENTER];// = new ArrayList<HostPower>();  // list of hosts
    public static StorageHost[] STORAGE_SERVER_LIST;

    public static List<VmPower> VMLIST = new ArrayList<VmPower>();
    public static int[][] VM_NUM_TYPE = new int[AppConstants.NUM_DATACENTER][AppConstants.VM_TYPE.length];

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // TODO code application logic here
        int num_user = 1; // number of cloud users
        Calendar calendar = Calendar.getInstance();
        boolean trace_flag = false; // mean trace events

        // Initialize the CloudSim library
        CloudSim.init(num_user, calendar, trace_flag);
        System.out.println("Clock:::" + CloudSim.clock());
//            createCloud();

        CloudManagement c = new CloudManagement("cloudcontroller");

        // Initilize Agents(AdmissionAgent, QueuingAgent)
//        AdmissionAgent admissionagent = new AdmissionAgent(DIRECTORY);
//        admissionagent.setHour(0);
//        
//        admissionagent.createQueue();
//        
// initial VM list 
//        for (int i = 0; i < AppConstants.VM_TYPE.length; i++) {
//            VMLIST =  new ArrayList<VmPower>();
//        }
//        HOSTLIST = new List[AppConstants.NUM_DATACENTER];  // list of hosts
//        VMLIST = new List[AppConstants.NUM_DATACENTER];
        /*           int numberofStorgehosts = 0;
         for (int i = 0; i < AppConstants.NUM_DATACENTER; i++) {
         //            HOSTLIST[i] = new ArrayList<HostPower>();
         //            VMLIST[i] = new ArrayList<VmPower>();
         Arrays.fill(VM_NUM_TYPE[i], 0);
         numberofStorgehosts =+ AppConstants.NUM_STORAGE_SERVERS[i];
 
         }
         //create storage servers
         STORAGE_SERVER_LIST = new StorageHost[numberofStorgehosts];

         CreateResources resource0 = new CreateResources(0);
         CreateResources resource1 = new CreateResources(1);
         //        resource0.getWorkload(0);
        
         DatacenterPower datacenterpower0 = resource0.createDatacenter("Datacenter_0");
         DatacenterPower datacenterpower1 = resource1.createDatacenter("Datacenter_1");
         NetworkAgent na = new NetworkAgent();
         DatacenterBrokerPower broker = resource0.createBroker();
         for (int i = 0; i < AppConstants.VM_TYPE.length; i++) {
         resource0.createVM(broker.getId(), AppConstants.VM_TYPE[i]);
         }
         for (int i = 0; i < AppConstants.VM_TYPE.length; i++) {
         resource1.createVM(broker.getId(), AppConstants.VM_TYPE[i]);
         }

         //        DatacenterBrokerPower broker1 = resource1.createBroker();
         //        System.out.println(""+broker.getId());
         broker.submitVmList(VMLIST);
         //        broker1.submitVmList(VMLIST[1]);
         */
        CloudSim.startSimulation();

        CloudSim.stopSimulation();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print("  " + VM_NUM_TYPE[i][j]);
            }
            System.out.println("\n");
        }
        System.out.println("");
        System.out.println("Number of created VM = " + NO_VM);
    }

    public static List<HostPower> getCOMPUTE_SERVER_LIST(int datacenterId) {
        return COMPUTE_SERVER_LIST[datacenterId];
    }

    public static void setCOMPUTE_SERVER_LIST(List<HostPower> HOSTLIST, int datacenterId) {
        Simulation.COMPUTE_SERVER_LIST[datacenterId] = HOSTLIST;
    }

    public static List<VmPower> getVMLIST() {
        return VMLIST;
    }

    public static void setVMLIST(List<VmPower> VMLIST) {
        Simulation.VMLIST = VMLIST;
    }

    public static HostPower getOneHost(int datacenterId, int hostId) {
        Iterator it = getCOMPUTE_SERVER_LIST(datacenterId).iterator();
        while (it.hasNext()) {
            HostPower host = (HostPower) it.next();
            if (host.getId() == hostId) {
                return host;
            }
        }
        return null;
    }
    /*
     public static void createCloud(){
     int numberofStorgehosts = 0;
     for (int i = 0; i < AppConstants.NUM_DATACENTER; i++) {
     //            HOSTLIST[i] = new ArrayList<HostPower>();
     //            VMLIST[i] = new ArrayList<VmPower>();
     Arrays.fill(VM_NUM_TYPE[i], 0);
     numberofStorgehosts =+ AppConstants.NUM_STORAGE_SERVERS[i];
 
     }
     //create storage servers
     STORAGE_SERVER_LIST = new StorageHost[numberofStorgehosts];

     CreateResources resource0 = new CreateResources(0);
     CreateResources resource1 = new CreateResources(1);
     //        resource0.getWorkload(0);
        
     DatacenterPower datacenterpower0 = resource0.createDatacenter("Datacenter_0");
     DatacenterPower datacenterpower1 = resource1.createDatacenter("Datacenter_1");
     NetworkAgent na = new NetworkAgent();
     DatacenterBrokerPower broker = resource0.createBroker();
     for (int i = 0; i < AppConstants.VM_TYPE.length; i++) {
     resource0.createVM(broker.getId(), AppConstants.VM_TYPE[i]);
     }
     for (int i = 0; i < AppConstants.VM_TYPE.length; i++) {
     resource1.createVM(broker.getId(), AppConstants.VM_TYPE[i]);
     }

     //        DatacenterBrokerPower broker1 = resource1.createBroker();
     //        System.out.println(""+broker.getId());
     broker.submitVmList(VMLIST);

     }
     */
}
