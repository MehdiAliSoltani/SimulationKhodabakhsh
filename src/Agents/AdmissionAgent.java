/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import AppWorkload.ReadWritetoFile;
import AppWorkload.Workload;
import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import simulation.AppConstants;
import simulation.Simulation;

/**
 *
 * @author Novin Pendar
 */
public class AdmissionAgent {

    List<List<Workload>> list_of_list_of_workload;
    QueuingAgent queuingagent = new QueuingAgent();
    String directory;
    int hour;

    public AdmissionAgent(String directory) {
        this.directory = directory;
    }
    /*
     private List<List<Workload>> readWorkload(int hour) {
     ReadWritetoFile r = new ReadWritetoFile();
     String filename = this.directory + Simulation.SENARIO + "\\" + hour + ".bin";
     this.list_of_list_of_workload = r.readWorkloadFiles(filename);
     return list_of_list_of_workload;
     }
     */

    public void readWorkload() {
        ReadWritetoFile r = new ReadWritetoFile();
        String filename = this.directory + Simulation.SENARIO + "\\" + this.hour + ".bin";
        this.list_of_list_of_workload = r.readWorkloadFiles(filename);
    }
    /*
     private  List<List<Workload>> readWorkloads(int hour) {
     ReadWritetoFile r = new ReadWritetoFile();
     double time = System.currentTimeMillis();
     String filename = this.directory + Simulation.SENARIO + "\\" + hour + ".bin";
     List<List<Workload>> list_of_list_of_workload = r.readWorkloadFiles(filename);
     System.out.println(System.currentTimeMillis() - time);
     return list_of_list_of_workload;
     }
     */
    /*
     private void fillQueue() {
     //        System.gc();
     List<List<Workload>> list_of_list_workload = readWorkload(this.hour);
     for (List<Workload> listworkload : list_of_list_workload) {
     for (Workload request : listworkload) {
     int length = request.getSize();
     int queueNumber = (int) (Math.floor(length / 1000));
     if (queueNumber > 99) {
     queueNumber = 99;
     }
     try {
     queuingagent.addQueue(request, queueNumber);
     } catch (ArrayIndexOutOfBoundsException e) {
     System.out.println("length = " + length + " queueNumber = " + queueNumber);
     }
     }
     }
     //        System.out.println("");
     //        queuingagent.
     }
     */

//    private void getNumofElemets() throws IOException {
//        int no =0;
//        for (List<Workload> listworkload : this.list_of_list_of_workload) {
//            for (Workload request : listworkload) {
//                no++;
//            }
//        }
//        System.out.println("************** "+no );
//        System.in.read();
//    }
//    static boolean flag1= true;
    public void fillQueue(double starttime) {
//        if(flag1) {
//            try {
//                getNumofElemets();
//            } catch (IOException ex) {
//                Logger.getLogger(AdmissionAgent.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            flag1 =false;
//        }
//        System.gc();
        boolean flag = true;
        Iterator<List<Workload>> topIt = this.list_of_list_of_workload.iterator();
        while (topIt.hasNext()) {
            List<Workload> topList = topIt.next();
            Iterator<Workload> downIt = topList.iterator();
            while (downIt.hasNext()) {
                Workload request = downIt.next();
                double arrivaltime = request.getArrivalTime() / 100000.0;
//                System.out.println("request.arriavleTime: " + request.getArrivalTime());
                if (arrivaltime < starttime + AppConstants.SCHEDULING_INTERVAL) {

                    int length = request.getSize();
                    int pes = request.getPes();

//                    int queueNumber = (int) (Math.floor(length / 1000));
                    int queueNumber = 0;
                    for (int i = 0; i < AppConstants.VM_PES.length; i++) {
                        queueNumber = Arrays.binarySearch(AppConstants.VM_PES, pes);
                    }
//                    int queueNumber = pes-1;
/*                    if (queueNumber > 99) {
                     queueNumber = 99;
                     }
                     */
                    try {
                        queuingagent.addQueue(request, queueNumber);
                        downIt.remove();
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println(e);
                    }
                } else {
                    flag = false;
                    break;

                }
            } // inner while
            if (!flag) {
                break;
            }
            topIt.remove();
        } // outer while
        /*
         for (List<Workload> listworkload : this.list_of_list_of_workload) {
         for (Workload request : listworkload) {
         double arrivaltime = request.getArrivalTime() / 100000.0;
         //                System.out.println("request.arriavleTime: " + request.getArrivalTime());
         if (arrivaltime < starttime + AppConstants.SCHEDULING_INTERVAL) {

         int length = request.getSize();
         int pes = request.getPes();

         //                    int queueNumber = (int) (Math.floor(length / 1000));
         int queueNumber = 0;
         for (int i = 0; i < AppConstants.VM_PES.length; i++) {
         queueNumber = Arrays.binarySearch(AppConstants.VM_PES, pes);
         }
         //                    int queueNumber = pes-1;
         /*                    if (queueNumber > 99) {
         queueNumber = 99;
         }
         */
        /*
         try {
         queuingagent.addQueue(request, queueNumber);
         this.list_of_list_of_workload.remove(request);
         } catch (ArrayIndexOutOfBoundsException e) {
         System.out.println(e);
         }
         } else {
         flag = false;
         break;

         }
         } //
         if (!flag) {
         break;
         }
         }
         */
//        System.out.println("");
//        queuingagent.
    }

    public void createQueue() {
//        fillQueue();
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

}
