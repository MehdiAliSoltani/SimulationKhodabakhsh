/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import AppWorkload.Workload;
import ExteraCloudSim.CloudletPower;
import ExteraCloudSim.HostPower;
import ExteraCloudSim.VmPower;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import simulation.AppConstants;
import simulation.CreateResources;
import simulation.Simulation;

/**
 *
 * @author Novin Pendar
 */
public class SchedulerAgent {

    private static final int ERROR_DATACENTER = Integer.MIN_VALUE;
    private static final int NOT_SUITABLE = -100;
    private AdmissionAgent admissionagent;
    private NetworkAgent networkAgent;
    List<CloudletPower> cloudletlist = new ArrayList<CloudletPower>();

    public SchedulerAgent(AdmissionAgent admissionagent, NetworkAgent networkAgent) {
        this.admissionagent = admissionagent;
        this.networkAgent = networkAgent;
    }

    /**
     *
     * @param userId
     * @param createResources
     * @return a list of CloudletPower
     */
//    public List<CloudletPower> createCloudletList(int userId, AdmissionAgent admissionagent) {
    public List<CloudletPower> createCloudletList(int userId) {
        QueuingAgent queuingAgent = getAdmissionagent().queuingagent;
//        QueuingAgent qa = new QueuingAgent();
//        Queue<Workload>[] sq = qa.getSystemQueue();
        CreateResources createResources = new CreateResources();
        int[] kq = determineNumberOfRequestsForQueue();
        for (int i = 0; i < AppConstants.NUM_QUEUE; i++) {
            if (queuingAgent.getSystemQueue(i).size() > 0) {
                for (int j = 0; j < kq[i]; j++) {
                    Workload request = queuingAgent.getElement(i);
                    if (request != null) {
                        cloudletlist.add(createResources.createCloudlet(userId, request));
//                        queuingAgent.removeElement(i);
                    } else {
                        break;
                    }
                }
            }
            queuingAgent.getSystemQueue(i);
        }
        return cloudletlist;
    }

    /**
     *
     * @return an array which consist number of requests for each queue
     */
    private int[] determineNumberOfRequestsForQueue() {
        ResourceAgent resourceAgent = new ResourceAgent();
        double approximation = resourceAgent.getApproximationNumberofPes();
        int[] k = new int[AppConstants.NUM_QUEUE];
        int K_Q = getK_Q(approximation);
        k[AppConstants.NUM_QUEUE - 1] = K_Q;
        for (int i = 0; i < k.length - 1; i++) {
            k[i] = getK_q(i + 1, K_Q);
        }
        return k;
    }

    private int getK_Q(double approximationNumPes) {
        int Q = AppConstants.NUM_QUEUE;

        double numerator = Math.pow(Q, Q - 1);
        double denumerator = fact(Q - 1);
        double num_div_denum = numerator / denumerator;
        int K_Q = (int) (approximationNumPes / (10 + log2(num_div_denum)));
        return K_Q;
    }

    private int getK_q(double q, int K_Q) {
        int k_q = (int) (K_Q + Math.floor(K_Q * log2(((double) AppConstants.NUM_QUEUE / q))));
        return k_q;
    }

    private double log2(double x) {
        return Math.log(x) / Math.log(2.0d);
    }

    private double fact(int n) {
        double f = 1;
        for (int i = n; i > 0; i--) {
            f = f * i;
        }
        return f;
    }

    public int determineVmId(CloudletPower cloudlet) {
        int dataStorageDcId = cloudlet.getDataStorageDatacenterId();
        int dataStroageId = cloudlet.getDataStorageId();
        int pesNeeded = cloudlet.getNumberOfPes();
        NetworkAgent.TableValues[] tableRow = getNetworkAgent().getWTableRow(dataStorageDcId, dataStroageId);
        Arrays.sort(tableRow, new Comparator<NetworkAgent.TableValues>() {

            @Override
            public int compare(NetworkAgent.TableValues o1, NetworkAgent.TableValues o2) {
                if (o1.getAlpha() > o2.getAlpha()) {
                    return -1;
                } else if (o1.getAlpha() < o2.getAlpha()) {
                    return 1;
                } else {
                    return 0;
                }

            }
        });
        double min;
        int tableIndex = 0;

        //tableRow sorted ascending according to alpha parameter
        boolean flag = true;
        double mean = 0;
        int count = 0;
        // this part calculate the mean of alpha parameter in datacenter which contain data of cloudlet
        for (int i = 0; i < tableRow.length; i++) {
            if (tableRow[i].getDatacenterId() == dataStorageDcId) {
                mean = mean + tableRow[i].getAlpha();
                count++;
            }
        }
        mean = mean / count;
//        List<Integer,Integer> appropriateList = new ArrayList<Integer,Integer>();
//        List<HostPower> hostPowerList;
//        for (int i = 0; i < AppConstants.NUM_DATACENTER; i++) {
//            hostPowerList.addAll(Simulation.getCOMPUTE_SERVER_LIST(i));
//        }
        List<SelectedHost> selectedHost = new ArrayList<SelectedHost>();

        for (int i = 0; i < tableRow.length; i++) {
            int dcId = tableRow[i].getDatacenterId();
            int csId = tableRow[i].getComputehostId();
            if (tableRow[i].getAlpha() < mean && dcId == dataStorageDcId) { // means first select appropriate hosts those are in the same datacenter as cloudlet
                HostPower hostPower = Simulation.getOneHost(dcId, csId);    //select the host from host list in datacenter             
                int vmId = isHostHasEnoughMips(hostPower, cloudlet);
                return vmId;
//                if (hostPower.) {
//                    selectedHost.add(new SelectedHost(dataStroageId, csId));
//                }
               
            }else{
                 tableRow[i].setDatacenterId(ERROR_DATACENTER);
            }
            

        }
        Iterator it = selectedHost.iterator();
        while (it.hasNext()) {
            SelectedHost shost = (SelectedHost) it.next();
            HostPower hostpower = Simulation.getOneHost(shost.getHostId(), shost.getHostId());

        }

        for (int i = 0; i < selectedHost.size(); i++) {
            Simulation.getOneHost(dataStroageId, count);
        }

        return 0;
    }

    /**
     * this method checks Vms on this host weather have enough resource for the
     * cloudlet
     *
     * @param host
     * @param cloudlet
     * @return
     */
    private int isHostHasEnoughMips(HostPower host, CloudletPower cloudlet) {
        double maxAvailibleMips = host.getMaxAvailableMips();
        double cloudletMipsNeeded = cloudlet.getNumberOfPes();

        List<VmPower> vmPowerList = host.getVmList();
        Iterator it = vmPowerList.iterator();
        while (it.hasNext()) {
            VmPower vm = (VmPower) it.next();
            double vmMips = vm.getMips();
            if (vm.getNumberOfPes() == cloudlet.getNumberOfPes()) { // means a cloudlet with the multiple pes should be run on the vm with the same pes
                double currentAvailibleMips = vm.getCurrentRequestedTotalMips() * vm.getNumberOfPes();
                double currentFreeMips = vmMips * vm.getNumberOfPes() - currentAvailibleMips;
                double vmUtilization = vm.getCurrentRequestedTotalMips() / (vm.getNumberOfPes() * vmMips);
                if (currentFreeMips >= (cloudlet.getNumberOfPes() * vmMips * 0.5)) {
                 return vm.getId();   
                }
            }

        }
//        double approximatePesAvailible = maxAvailibleMips;
        return NOT_SUITABLE;
    }

    public AdmissionAgent getAdmissionagent() {
        return admissionagent;
    }

    public NetworkAgent getNetworkAgent() {
        return networkAgent;
    }

    public static void main(String[] args) {
//        SchedulerAgent s = new SchedulerAgent();
//        System.out.println("------");
//        int[] k = s.determineNumberOfRequestsForQueue();
//        for (int i = 0; i < k.length; i++) {
//            System.out.println(k[i]);
//        }
    }

    class SelectedHost {

        int datacenterId;
        int hostId;

        public SelectedHost(int datacenterId, int hostId) {
            this.datacenterId = datacenterId;
            this.hostId = hostId;
        }

        public int getDatacenterId() {
            return datacenterId;
        }

        public int getHostId() {
            return hostId;
        }

    }

}
