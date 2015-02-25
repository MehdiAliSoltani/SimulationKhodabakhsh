/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import AppWorkload.Workload;
import ExteraCloudSim.CloudletPower;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import simulation.AppConstants;
import simulation.CreateResources;

/**
 *
 * @author Novin Pendar
 */
public class SchedulerAgent {

    List<CloudletPower> cloudletlist = new ArrayList<CloudletPower>();

    /**
     *
     * @param userId
     * @param createResources
     * @return a list of CloudletPower
     */
    public List<CloudletPower> createCloudletList(int userId, AdmissionAgent admissionagent) {
        QueuingAgent queuingAgent = admissionagent.queuingagent;
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

    public static void main(String[] args) {
        SchedulerAgent s = new SchedulerAgent();
        System.out.println("------");
        int[] k = s.determineNumberOfRequestsForQueue();
        for (int i = 0; i < k.length; i++) {
            System.out.println(k[i]);
        }
    }

}
