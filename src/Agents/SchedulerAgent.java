/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import AppWorkload.Workload;
import ExteraCloudSim.CloudletPower;
import ExteraCloudSim.HostPower;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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

        while (true) {
            for (int i = 0; i < tableRow.length; i++) {
                int dcId = tableRow[i].getDatacenterId();
                int csId =tableRow[i].getComputehostId();
                List<HostPower> hostlist = new ArrayList<HostPower>(Simulation.getCOMPUTE_SERVER_LIST(i));
//                hostlist.get(i).get
            }
            break;
        }
        return 0;
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

}
