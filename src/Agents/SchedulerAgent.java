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
import java.util.Random;
import org.cloudbus.cloudsim.Vm;
import simulation.AppConstants;
import simulation.CreateResources;
import simulation.Simulation;

/**
 *
 * @author Novin Pendar
 */
public class SchedulerAgent {

    private static final int ERROR_DATACENTER = Integer.MIN_VALUE;

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
        cloudletlist.clear();
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

    private int findHostClass(HostPower host) {
//        host.getUtilizationMips();
        return 0;
    }

    public int determinVmIdforCpuBound(CloudletPower cloudlet) {

        return 0;
    }

    public int determineVmId_RandomwithResourceConsidering(CloudletPower cloudlet) {
        int dataStorageDcId = cloudlet.getDataStorageDatacenterId();
        int dataStroageId = cloudlet.getDataStorageId();
        NetworkAgent.TableValues[] tableRow = getNetworkAgent().getWTableRow(dataStorageDcId, dataStroageId);

        Random r = new Random();
        int numberVm = Simulation.VMLIST.size();
        int vmId;
        List<Integer> selectedVmId = new ArrayList<Integer>();

        while (true) {
            while (true) {
                vmId = r.nextInt(numberVm);
                if (!selectedVmId.contains(vmId)) {
                    selectedVmId.add(vmId);
                    break;
                }
            }
            for (int datacenterId = 0; datacenterId < AppConstants.NUM_DATACENTER; datacenterId++) {
                List<HostPower> hostlist = Simulation.getCOMPUTE_SERVER_LIST(datacenterId);
                for (HostPower host : hostlist) {

                    vmId = isHostHasEnoughMips(host, cloudlet);
                    List<VmPower> vmlist = host.getVmList();
                    for (VmPower vm : vmlist) {
                        if (vm.getId() == vmId) {
//                            isHostHasEnoughMips(host, cloudlet);
                            for (int Index = 0; Index < tableRow.length; Index++) {
                                if (tableRow[Index].getDatacenterId() == datacenterId && tableRow[Index].getComputehostId() == host.getId()) {
                                    cloudlet.setDatacenterIdHostedCloudlet(datacenterId);
                                    cloudlet.setHost(host.getId());
                                    cloudlet.setAlpha(tableRow[Index].getAlpha());
                                    if (dataStorageDcId == datacenterId) {
                                        cloudlet.setInSource(true);
                                    } else {
                                        cloudlet.setInSource(false);
                                    }
                                    return vmId;

                                }

                            }
                        }

                    }
                }
            }
            if (selectedVmId.size() == numberVm) {
                break;
            }
        }

        return AppConstants.NOT_SUITABLE_VM_FOUND;

    }

    public int determineVmId_LowestLoad(CloudletPower cloudlet) {
        int dataStorageDcId = cloudlet.getDataStorageDatacenterId();
        int dataStroageId = cloudlet.getDataStorageId();
        NetworkAgent.TableValues[] tableRow = getNetworkAgent().getWTableRow(dataStorageDcId, dataStroageId);

        Random r = new Random();
        int numberVm = Simulation.VMLIST.size();
        int vmId;
        List<Integer> selectedVmId = new ArrayList<Integer>();

        while (true) {
            while (true) {
                vmId = r.nextInt(numberVm);
                if (!selectedVmId.contains(vmId)) {
                    selectedVmId.add(vmId);
                    break;
                }
            }
            for (int datacenterId = 0; datacenterId < AppConstants.NUM_DATACENTER; datacenterId++) {
                List<HostPower> hostlist = Simulation.getCOMPUTE_SERVER_LIST(datacenterId);
                for (HostPower host : hostlist) {
                    List<VmPower> vmlist = host.getVmList();
                    for (VmPower vm : vmlist) {
                        if (vm.getId() == vmId) {
                            if (isHostEnoughMips(host, cloudlet)) {
                                for (int Index = 0; Index < tableRow.length; Index++) {
                                    if (tableRow[Index].getDatacenterId() == datacenterId && tableRow[Index].getComputehostId() == host.getId()) {
                                        cloudlet.setDatacenterIdHostedCloudlet(datacenterId);
                                        cloudlet.setHost(host.getId());
                                        cloudlet.setAlpha(tableRow[Index].getAlpha());
                                        if (dataStorageDcId == datacenterId) {
                                            cloudlet.setInSource(true);
                                        } else {
                                            cloudlet.setInSource(false);
                                        }
                                        return vmId;

                                    }

                                }
                            }
                        }

                    }
                }
            }
            if (selectedVmId.size() == numberVm) {
                break;
            }
        }

        return AppConstants.NOT_SUITABLE_VM_FOUND;

    }

    public int determineVmId_Random(CloudletPower cloudlet) {
        int dataStorageDcId = cloudlet.getDataStorageDatacenterId();
        int dataStroageId = cloudlet.getDataStorageId();
        NetworkAgent.TableValues[] tableRow = getNetworkAgent().getWTableRow(dataStorageDcId, dataStroageId);

        Random r = new Random();
        int numberVm = Simulation.VMLIST.size();
        int vmId = r.nextInt(numberVm);
        for (int datacenterId = 0; datacenterId < AppConstants.NUM_DATACENTER; datacenterId++) {
            List<HostPower> hostlist = Simulation.getCOMPUTE_SERVER_LIST(datacenterId);
            for (HostPower host : hostlist) {
                List<VmPower> vmlist = host.getVmList();
                for (VmPower vm : vmlist) {
                    if (vm.getId() == vmId) {
                        for (int Index = 0; Index < tableRow.length; Index++) {
                            if (tableRow[Index].getDatacenterId() == datacenterId && tableRow[Index].getComputehostId() == host.getId()) {
                                cloudlet.setDatacenterIdHostedCloudlet(datacenterId);
                                cloudlet.setHost(host.getId());
                                cloudlet.setAlpha(tableRow[Index].getAlpha());
                                if (dataStorageDcId == datacenterId) {
                                    cloudlet.setInSource(true);
                                } else {
                                    cloudlet.setInSource(false);
                                }
                                return vmId;
                            }

                        }
                    }

                }
            }
        }

        return AppConstants.NOT_SUITABLE_VM_FOUND;

    }

    public int determineVmId_SSLNT(CloudletPower cloudlet) {
        int vmId = 0;
        int dataStorageDcId = cloudlet.getDataStorageDatacenterId();
        int dataStroageId = cloudlet.getDataStorageId();
//        System.out.println("@@ dataStorageDcId "+ dataStorageDcId + " @@ dataStroageId "+dataStroageId);
        int pesNeeded = cloudlet.getNumberOfPes();
        NetworkAgent.TableValues[] tableRow = getNetworkAgent().getWTableRow(dataStorageDcId, dataStroageId);
        Arrays.sort(tableRow, new Comparator<NetworkAgent.TableValues>() {

            @Override
            public int compare(NetworkAgent.TableValues o1, NetworkAgent.TableValues o2) {
                if (o1.getAlpha() > o2.getAlpha()) {
                    return 1;
                } else if (o1.getAlpha() < o2.getAlpha()) {
                    return -1;
                } else {
                    return 0;
                }

            }
        });
        double min;
        int tableIndex = 0;

        //tableRow sorted ascending according to alpha parameter
        boolean flag = false;
        double meanIn = 0;
        double meanOut = 0;
        int countIn = 0;
        int countOut = 0;
        // this part calculate the mean of alpha parameter in datacenter which contain data of cloudlet
        for (int i = 0; i < tableRow.length; i++) {
            if (tableRow[i].getDatacenterId() == dataStorageDcId) {
                meanIn = meanIn + tableRow[i].getAlpha();
                countIn++;
            } else {
                meanOut = meanOut + tableRow[i].getAlpha();
                countOut++;
            }
        }
        meanIn = meanIn / countIn;
        meanOut = meanOut / countOut;
        List<SelectedHost> selectedHost = new ArrayList<SelectedHost>();
        int hostDatacenter = 0;
        for (int i = 0; i < tableRow.length; i++) {
            int dcId = tableRow[i].getDatacenterId();
            int csId = tableRow[i].getComputehostId();
            double alpha = tableRow[i].getAlpha();
//            if (tableRow[i].getAlpha() < meanIn && dcId == dataStorageDcId) { // means first select appropriate hosts those are in the same datacenter as cloudlet
            HostPower hostPower = Simulation.getOneHost(dcId, csId);    //select the host from host list in datacenter             
            hostDatacenter = hostPower.getDatacenterID();
            vmId = isHostHasEnoughMips(hostPower, cloudlet);
            if (vmId != AppConstants.NOT_SUITABLE_VM_FOUND) {
                flag = true;
                cloudlet.setDatacenterIdHostedCloudlet(hostDatacenter);
                cloudlet.setHost(hostPower.getId());
                cloudlet.setAlpha(alpha);
                break;
            } else {
                continue;
            }
        }
        if (flag) {

            if (dataStorageDcId == hostDatacenter) {
                cloudlet.setInSource(true);
            } else {
                cloudlet.setInSource(false);
            }
            return vmId;
        } else {
            return AppConstants.NOT_SUITABLE_VM_FOUND;
        }
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
        boolean find = false;
        int VmId = AppConstants.NOT_SUITABLE_VM_FOUND;
        List<VmPower> vmPowerList = host.getVmList();
        Iterator it = vmPowerList.iterator();
        while (it.hasNext()) { // trying to find a vm with the same pes as cloudlet requires
            VmPower vm = (VmPower) it.next();
            double vmMips = vm.getMips();
            if (vm.getNumberOfPes() == cloudlet.getNumberOfPes()) { // means a cloudlet with the multiple pes should be run on the vm with the same pes
//                double currentAvailibleMips = vm.getCurrentRequestedTotalMips();// * vm.getNumberOfPes();
//                double currentFreeMips = vmMips * vm.getNumberOfPes() - vm.getCurrentRequestedTotalMips();
                double vmUtilization = vm.getCurrentRequestedTotalMips() / (vm.getNumberOfPes() * vmMips);
                if (vmUtilization < 0.50) {
                    VmId = vm.getId();
                    find = true;
                }
            }
        }
        if (!find) { // operate First Fit if an appropriate Vm did not find
            Iterator itt = vmPowerList.iterator();
            while (itt.hasNext()) {
                VmPower vm = (VmPower) itt.next();
                double vmMips = vm.getMips();
                double vmUtilization = vm.getCurrentRequestedTotalMips() / (vm.getNumberOfPes() * vmMips);
                if (vmUtilization < 0.50) {
                    VmId = vm.getId();
                    find = true;
                }

            }
        }
        return VmId;
    }

    private boolean isHostEnoughMips(HostPower host, CloudletPower cloudlet) {

        boolean find = false;
        int VmId = AppConstants.NOT_SUITABLE_VM_FOUND;
        List<VmPower> vmPowerList = host.getVmList();
        Iterator it = vmPowerList.iterator();
        while (it.hasNext()) { // trying to find a vm with the same pes as cloudlet requires
            VmPower vm = (VmPower) it.next();
            double vmMips = vm.getMips();
            if (vm.getNumberOfPes() == cloudlet.getNumberOfPes()) { // means a cloudlet with the multiple pes should be run on the vm with the same pes
//                double currentAvailibleMips = vm.getCurrentRequestedTotalMips();// * vm.getNumberOfPes();
//                double currentFreeMips = vmMips * vm.getNumberOfPes() - vm.getCurrentRequestedTotalMips();
                double vmUtilization = vm.getCurrentRequestedTotalMips() / (vm.getNumberOfPes() * vmMips);
                if (vmUtilization < 0.50) {
                    VmId = vm.getId();
                    find = true;
                }
            }
        }
        return find;
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
