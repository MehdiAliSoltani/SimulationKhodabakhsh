/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CloudManagement;

import Agents.AdmissionAgent;
import Agents.NetworkAgent;
import Agents.SchedulerAgent;
import ExteraCloudSim.CloudletPower;
import ExteraCloudSim.DatacenterBrokerPower;
import ExteraCloudSim.DatacenterPower;
import ExteraCloudSim.HostPower;
import ExteraCloudSim.VmPower;
import Resource.StorageHost;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import simulation.AppConstants;
import simulation.CreateResources;
import simulation.Simulation;
import static simulation.Simulation.POLICY;
import static simulation.Simulation.STORAGE_SERVER_LIST;
import static simulation.Simulation.VMLIST;
import static simulation.Simulation.VM_NUM_TYPE;

/**
 *
 * @author Novin Pendar
 */
public class CloudManagement extends SimEntity {

//   private CreateResources createresource;
    private final static int SCHEDULE_EVERY_HOUR = 0;
    private final static int SCHEDULE_EVERY_FIVE_MINUTE = 1;
    private final static int SCHEDULE_EVERY_SECOND = 2;
    private final static int NUMBER_SCHEDULE_EVERY_SECOND = 1;
    private AdmissionAgent admissionagent;// = new AdmissionAgent(AppConstants.WORKLOAD_DIRECTORY);
    CreateResources resource = new CreateResources();
    DatacenterBrokerPower broker;
//    PowerDatacenterBroker broker;
    /**
     * *
     */
    DatacenterPower datacenterpower0;
    DatacenterPower datacenterpower1;
//    PowerDatacenterNonPowerAware datacenterpower0;
//    PowerDatacenterNonPowerAware datacenterpower1;
    SchedulerAgent scheduler;// = new SchedulerAgent();
    private Semaphore mutex = new Semaphore(1);
    private NetworkAgent networkAgent;
    public static List<Double>[] Utilization;
    public static int NumberofServer = 0;
    
    public CloudManagement(String name) {
        super(name);

        createCloud();
    }

    private void createCloud() {
//        Simulation.COMPUTE_SERVER_LIST  = 
        try {
            broker = new DatacenterBrokerPower("Broker");
        } catch (Exception ex) {
            Logger.getLogger(CloudManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        int numberofStorgehosts = 0;
        for (int i = 0; i < AppConstants.NUM_DATACENTER; i++) {
            Simulation.COMPUTE_SERVER_LIST[i] = new ArrayList<HostPower>();
            Arrays.fill(VM_NUM_TYPE[i], 0);
            numberofStorgehosts = +AppConstants.NUM_STORAGE_SERVERS[i];
           
            
        }
        //create storage servers
        STORAGE_SERVER_LIST = new StorageHost[numberofStorgehosts];

//        CreateResources resource1 = new CreateResources();
        datacenterpower0 = resource.createDatacenter("Datacenter_0", 0);
        datacenterpower1 = resource.createDatacenter("Datacenter_1", 1);
//        NetworkAgent na = new NetworkAgent();
//        for (int i = 0; i < AppConstants.VM_TYPE.length; i++) {
////            resource0.createVM(this.broker.getId(), AppConstants.VM_TYPE[i]);
//            resource.createVM(this.broker.getId(), AppConstants.VM_TYPE[i], 0);
//        }
//        for (int i = 0; i < AppConstants.VM_TYPE.length; i++) {
////            resource1.createVM(this.broker.getId(), AppConstants.VM_TYPE[i]);
//            resource.createVM(this.broker.getId(), AppConstants.VM_TYPE[i], 1);
//        }
//        this.broker.submitVmList(VMLIST);
        
        for (int i = 0; i < AppConstants.NUM_DATACENTER; i++) {
         NumberofServer += AppConstants.NUM_COMPUTE_SERVERS[i]    ;
        }
        Utilization =new List[NumberofServer];
        for (int i = 0; i < Utilization.length; i++) {
            Utilization[i] = new ArrayList<Double>();
        }
        
    }

    private void createVms() {
        for (int i = 0; i < AppConstants.VM_TYPE.length; i++) {
//            resource0.createVM(this.broker.getId(), AppConstants.VM_TYPE[i]);
            resource.createVM(this.broker.getId(), AppConstants.VM_TYPE[i], 0);
        }
        for (int i = 0; i < AppConstants.VM_TYPE.length; i++) {
//            resource1.createVM(this.broker.getId(), AppConstants.VM_TYPE[i]);
            resource.createVM(this.broker.getId(), AppConstants.VM_TYPE[i], 1);
        }
        this.broker.submitVmList(VMLIST);

    }
    /*
     private void TempcreateVms() {
     for (int i = 0; i < AppConstants.VM_TYPE.length; i++) {
     //            resource0.createVM(this.broker.getId(), AppConstants.VM_TYPE[i]);
     resource.TempcreateVM(this.broker.getId(), AppConstants.VM_TYPE[i], 0);
     }
     for (int i = 0; i < AppConstants.VM_TYPE.length; i++) {
     //            resource1.createVM(this.broker.getId(), AppConstants.VM_TYPE[i]);
     resource.TempcreateVM(this.broker.getId(), AppConstants.VM_TYPE[i], 1);
     }
     this.broker.submitVmList(VMLIST);

     }
     */

    @Override
    public void startEntity() {
        createAgents();
//          createCloud();
//        pause(1);
        int destination = getId();
        int delay;
        System.out.println("Start Cloud Management...");
        for (int hour = 0; hour < AppConstants.SIMULATION_HOUR; hour++) {
            delay = hour * AppConstants.HOUR_IN_SECOND;
//            if(delay == 0) delay =1;
            ScheduleNode snode = new ScheduleNode(delay, hour);
            schedule(destination, delay, SCHEDULE_EVERY_HOUR, snode);
            
        }

    }
    boolean flag = true;
    static boolean f = true;

    @Override
    public void processEvent(SimEvent ev) {
        int destination = getId();
        Object obj = ev.getData();
        ScheduleNode snode = (ScheduleNode) obj;
        double starttime = snode.getStarttime();

        switch (ev.getTag()) {
            case SCHEDULE_EVERY_HOUR:
                System.gc();
                System.out.println("Start Loading Workload for Hour "+ starttime);
                int hour = snode.getRequestId();
                admissionagent.setHour(hour);
                admissionagent.readWorkload();
//                admissionagent.createQueue();
                for (int i = 0; i < 12; i++) {
                    double delay = starttime + (i * AppConstants.EVERY_FIVE * 60);
                    ScheduleNode snodeFive = new ScheduleNode(delay, 0);
                    schedule(destination, delay - starttime, SCHEDULE_EVERY_FIVE_MINUTE, snodeFive);
                }
//                }
                //load workload into queue
                break;
            case SCHEDULE_EVERY_FIVE_MINUTE:
                for (int i = 0; i < AppConstants.EVERY_FIVE * 60 * NUMBER_SCHEDULE_EVERY_SECOND; i++) {
                    double delay = starttime + (i * AppConstants.SCHEDULING_PER_SECOND);
                    ScheduleNode snodeSecond = new ScheduleNode(delay, 0);
                    schedule(destination, delay - starttime, SCHEDULE_EVERY_SECOND, snodeSecond);
                }
                // load balancing 
                // server consolidation
                // turn(on/off) idel server
                break;
            case SCHEDULE_EVERY_SECOND:

                if (starttime == 0 && flag) {
                    try {
                        mutex.acquire();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(CloudManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    flag = false;
                    mutex.release();

                    createVms();
//                    TempcreateVms();
                    CloudSim.resumeSimulation();

                } else {
                    if (starttime > 0.2) {

//                        getNetworkAgent().setDynamicWTable();
//                        if(starttime > 100)
//                        getNetworkAgent().setWTable();
                        //ResourceAgent resourceAgent
                        admissionagent.fillQueue(starttime);
//                    List<CloudletPower> cloudletlist = scheduler.createCloudletList(this.broker.getId(), admissionagent);
                        List<CloudletPower> cloudletlist = scheduler.createCloudletList(this.broker.getId());

//////////////////////////////////////////
//////////////////////////////////////////                        
                        switch (POLICY) {
                            case AppConstants.SSLNT:
                                Iterator it = cloudletlist.iterator();
                                while (it.hasNext()) {
                                    CloudletPower cloudlet = (CloudletPower) it.next();
                                    int vmId = scheduler.determineVmId_SSLNT(cloudlet);
                                    if (vmId != AppConstants.NOT_SUITABLE_VM_FOUND) {
                                        broker.doCloudlet(cloudlet, vmId);
//                                System.out.println(CloudSim.clock()+ " : Cloudlet # "+cloudlet.getCloudletId()+" submited to Vm # "+ vmId );
                                    } else {
                                        System.out.println("Cloudlet is failed");
                                    }
                                }
                            break;
                            case AppConstants.RANDOMSELECTION :
                                Iterator randomIt = cloudletlist.iterator();
                                while (randomIt.hasNext()) {
                                    CloudletPower cloudlet = (CloudletPower) randomIt.next();
                                    int vmId = scheduler.determineVmId_Random(cloudlet);
                                    if (vmId != AppConstants.NOT_SUITABLE_VM_FOUND) {
                                        broker.doCloudlet(cloudlet, vmId);
//                                System.out.println(CloudSim.clock()+ " : Cloudlet # "+cloudlet.getCloudletId()+" submited to Vm # "+ vmId );
                                    } else {
                                        System.out.println("Cloudlet is failed");
                                    }
                                }
                            break;
                            case AppConstants.RANDOMSELECTIONWITHRESOURCECONSIDERING :
                                Iterator randomItR = cloudletlist.iterator();
                                while (randomItR.hasNext()) {
                                    CloudletPower cloudlet = (CloudletPower) randomItR.next();
                                    int vmId = scheduler.determineVmId_RandomwithResourceConsidering(cloudlet);
                                    if (vmId != AppConstants.NOT_SUITABLE_VM_FOUND) {
                                        broker.doCloudlet(cloudlet, vmId);
//                                System.out.println(CloudSim.clock()+ " : Cloudlet # "+cloudlet.getCloudletId()+" submited to Vm # "+ vmId );
                                    } else {
                                        System.out.println("Cloudlet is failed");
                                    }
                                }
                            break;

                        }

                        getNetworkAgent().setWTable();
                        getServersUtilization();
                        
                        //get the requests from the queue
                        //initialize vm list
                        //submit vm list
                        System.out.println("start time " + starttime);
                    /*    for (HostPower host : Simulation.getCOMPUTE_SERVER_LIST(0)) {
                            System.out.println(host.getMaxUtilization());
                            double vmUtilization = 0;
                            List<VmPower> vmPowerList = host.getVmList();
                            List<CloudletPower> cloudlet = null;
                            for (VmPower vm : vmPowerList) {
                                cloudlet = vm.getVmCloudletList();
                                vmUtilization = vm.getCurrentRequestedTotalMips();
                            }
                            System.out.print("Host #" + host.getId() + " utilization " + (vmUtilization / (40 * AppConstants.VM_MIPS) * 100));
                            for (CloudletPower c : cloudlet) {
                                System.out.print("  " + c.getStatus());
                            }
                            System.out.println("");

                        }*/

                    }
                }
                break;
        }
    }

    private void getServersUtilization(){
        int serverNo =0;
        for (int i = 0; i < AppConstants.NUM_DATACENTER; i++) {
            for(HostPower host :Simulation.getCOMPUTE_SERVER_LIST(i) ){
                List<VmPower> vmlist = host.getVmList();
                double  vmUtilization = 0;
                for(VmPower vm : vmlist){
                    vmUtilization += vm.getCurrentRequestedTotalMips();
                }   
//                System.out.println(vmUtilization/ (40 * AppConstants.VM_MIPS));
                this.Utilization[serverNo++].add((vmUtilization / (40 * AppConstants.VM_MIPS) * 100));
            }
        }
    }
    @Override
    public void shutdownEntity() {
        System.out.println("Simulation Terminated");
    }

    public NetworkAgent getNetworkAgent() {
        return networkAgent;
    }

    private void createAgents() {
        admissionagent = new AdmissionAgent(AppConstants.WORKLOAD_DIRECTORY);
        networkAgent = new NetworkAgent(); // initialize network table 
        scheduler = new SchedulerAgent(admissionagent, networkAgent);

    }

    class ScheduleNode {

        double starttime;
        int RequestId;

        public ScheduleNode(double starttime, int requestId) {
            this.starttime = starttime;
            this.RequestId = requestId;
        }

        public double getStarttime() {
            return starttime;
        }

        public int getRequestId() {
            return RequestId;
        }

    }

}
