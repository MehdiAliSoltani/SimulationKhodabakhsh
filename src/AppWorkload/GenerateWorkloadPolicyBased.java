/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AppWorkload;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import org.cloudbus.cloudsim.distributions.ExponentialDistr;
import org.cloudbus.cloudsim.distributions.ParetoDistr;
import org.cloudbus.cloudsim.distributions.UniformDistr;
import simulation.AppConstants;
import simulation.Simulation;

/**
 *
 * @author Novin Pendar
 */
public class GenerateWorkloadPolicyBased {

    static int request_ID = 0;

    private int NextRequestID() {
        request_ID += 1;
        return request_ID;
    }
    /*
     private int NumberofRequest() {
     if (Simulation.SENARIO == 1) {
     return AppConstants.NUM_REQUESTS_IN_SENARIO_1;
     } else if (Simulation.SENARIO == 2) {
     return AppConstants.NUM_REQUESTS_IN_SENARIO_2;
     }
     return 0;
     }*/

    /*  private int NumberofRequestperSecond() {
     if (Simulation.SENARIO == 1) {
     return AppConstants.NUM_of_REQUESTS_SECOND;
     } else if (Simulation.SENARIO == 2) {
     return AppConstants.NUM_of_REQUESTS_SENARIO_2_per_SECOND;
     }
     return 0;
     }
     */
    public void generateWorkload() {
        int cpub = 0, iob = 0;

//        List<Integer> pelist = new ArrayList<>();
        Random rand = new Random();
        int[] pelist = new int[AppConstants.VM_PES.length];
        for (int i = 0; i < 4; i++) {
            pelist[i] = AppConstants.VM_PES[i];
        }
        int lower, upper;
        lower = 0;
        upper = pelist.length;
        long startarrivaltime = 0, endarrivaltime = 0;
//        int numrequest = NumberofRequest();
        int requestID;
        int size = AppConstants.L_MIN;
        int filesize;
        boolean firsttimewritetofile = true;
        String filepath = AppConstants.WORKLOAD_DIRECTORY + Simulation.SENARIO + "\\";
        int part = 0;
        int fileNo = 0;
        ReadWritetoFile write = new ReadWritetoFile();
        List<Workload> workloadlist = new ArrayList<Workload>();
//        List<List<Workload>> listworkloadlist = new ArrayList<List<Workload>>();
        Workload request = null;
        ExponentialDistr expdistr_size = new ExponentialDistr(AppConstants.L_Mean); // generate size of requests
        ExponentialDistr expdistr_numberofrequest = new ExponentialDistr(AppConstants.NUM_of_REQUESTS_SECOND); // generate size of requests
//        ExponentialDistr expdistr_numberofrequest1 = new ExponentialDistr(NumberofRequestperSecond()); // generate size of requests
        ParetoDistr paretodist = new ParetoDistr(1, AppConstants.FILE_SIZE_MIN); //file size
        UniformDistr uniformdist_arrivaltime;
        UniformDistr uniformdist_numberofFiles = new UniformDistr(AppConstants.MIN_FILES_NEEDED, AppConstants.MAX_FILES_NEEDED);
        UniformDistr uniformdist_pe = new UniformDistr(lower, upper);
        UniformDistr uniformdist_dataserver_in_DC_0 = new UniformDistr(1, AppConstants.NUM_STORAGE_SERVERS[0]);
        UniformDistr uniformdist_dataserver_in_DC_1 = new UniformDistr(1, AppConstants.NUM_STORAGE_SERVERS[1]);
        for (int i = 0; i < AppConstants.SIMULATION_LENGTH / 100; i++) {

            for (int j = 0; j < 100; j++) { // 100 seconds 

                String filename = filepath + fileNo + ".bin";
                endarrivaltime = startarrivaltime + 100000;
                int num_req = (int) expdistr_numberofrequest.sample();
                uniformdist_arrivaltime = new UniformDistr(startarrivaltime, endarrivaltime); //***************
                startarrivaltime = endarrivaltime;
                while (num_req > 0) { // 1 second
                    boolean ioState = false;
                    Random r = new Random();
                    if (r.nextDouble() <= ioPersent()) {
                        do {// io bound generator
                            size = (int) expdistr_size.sample();
                        } while (size >= AppConstants.L_Mean);
                        ioState = true;
                    } else {//cpu bound generator
                        do {
                            size = (int) expdistr_size.sample();
                        } while (size < AppConstants.L_Mean);

                    }
                    if (size < AppConstants.L_MAX && size >= AppConstants.L_MIN) {
                        if (ioState) {
                            iob++;
                        } else {
                            cpub++;
                        }

                        requestID = NextRequestID();
                        filesize = (int) paretodist.sample();
                        long arrivaltime = (long) uniformdist_arrivaltime.sample();
                        num_req -= 1;
                        int peneeded = (int) uniformdist_pe.sample();
                        int storageserver, datastorageDcId;
                        int numberOfFilesNeeded;
                        if (ioState) {
                            do { // generate number of files for io bound requests
                                numberOfFilesNeeded = (int) uniformdist_numberofFiles.sample();
                            } while (numberOfFilesNeeded <= 10);
                        } else {
                            do { // generate number of files for cpu bound requests
                                numberOfFilesNeeded = (int) uniformdist_numberofFiles.sample();
                            } while (numberOfFilesNeeded > 10);
                        }
                        ExponentialDistr expDistsizeofFile = new ExponentialDistr(AppConstants.MEAN_FILESIZE);
                        ExponentialDistr expDisttimetoIO = new ExponentialDistr(size / 4);
                        long[] sizeOfFiles = new long[numberOfFilesNeeded];
                        long[] timetoIO = new long[numberOfFilesNeeded];
                        for (int k = 0; k < numberOfFilesNeeded; k++) {
                            sizeOfFiles[k] = (long) expDistsizeofFile.sample();
                            long ioTime;
                            do {
                                ioTime = (long) expDisttimetoIO.sample();
                            } while (ioTime > size);
                            timetoIO[k] = ioTime;
                        }
//                        long tot = 0;
//                        for (int k = 0; k < sizeOfFiles.length; k++) {
//                            tot += sizeOfFiles[k];
//                        }
//                        System.out.println("Size = "+size +" Number of file "+ numberOfFilesNeeded + " total size is "+ (tot /1000000) +"M");
                        Arrays.sort(timetoIO);
                        if (rand.nextInt(100) % 2 == 0) {
                            storageserver = (int) uniformdist_dataserver_in_DC_0.sample();
                            datastorageDcId = 0; // data storage is in datacenter #0
                        } else {
                            storageserver = (int) uniformdist_dataserver_in_DC_1.sample();
                            datastorageDcId = 1;  // data storage is in datacenter #1
                        }
                        request = new Workload(requestID, size, filesize, filesize, AppConstants.VM_PES[peneeded], datastorageDcId, storageserver, arrivaltime,
                                numberOfFilesNeeded, sizeOfFiles, timetoIO);
                        workloadlist.add(request);
                    }
                }
                Collections.sort(workloadlist, new Comparator<Workload>() {
                    @Override
                    public int compare(Workload o1, Workload o2) {
                        if (o1.getArrivalTime() > o2.getArrivalTime()) {
                            return 1;
                        } else if (o1.getArrivalTime() < o2.getArrivalTime()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });
                if (firsttimewritetofile) {
                    write.writeListfirstTime(workloadlist, filename);
                    firsttimewritetofile = false;
                } else {
                    write.AppendListtoFile(workloadlist, filename);
                }
                workloadlist.clear();

            }
            part += 1;
            if (part == 36) {
                fileNo += 1;
                firsttimewritetofile = true;
                part = 0;
            }
        }
        System.out.println("CPU " + cpub + " I/O " + iob);
    }

    private double ioPersent() {
        double iopercent = 0.3;
        switch (Simulation.SENARIO) {
            case 1:
                iopercent = 0.3;
                break;
            case 2:
                iopercent = 0.4;
                break;
            case 3:
                iopercent = 0.5;
                break;
            case 4:
                iopercent = 0.6;
                break;
            case 5:
                iopercent = 0.7;
                break;
            case 6:
                iopercent = 0.8;
                break;
        }
        return iopercent;
    }

    public static void main(String[] args) {
        System.out.println("Scenario #"+Simulation.SENARIO);
        GenerateWorkloadPolicyBased g = new GenerateWorkloadPolicyBased();
        g.generateWorkload();
        ReadWritetoFile r = new ReadWritetoFile();
    }
}
