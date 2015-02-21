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
public class GenerateWorkload {

    static int request_ID = 0;

    private int NextRequestID() {
        request_ID += 1;
        return request_ID;
    }

    private int NumberofRequest() {
        if (Simulation.SENARIO == 1) {
            return AppConstants.NUM_REQUESTS_IN_SENARIO_1;
        } else if (Simulation.SENARIO == 2) {
            return AppConstants.NUM_REQUESTS_IN_SENARIO_2;
        }
        return 0;
    }

    private int NumberofRequestperSecond() {
        if (Simulation.SENARIO == 1) {
            return AppConstants.NUM_of_REQUESTS_SENARIO_1_per_SECOND;
        } else if (Simulation.SENARIO == 2) {
            return AppConstants.NUM_of_REQUESTS_SENARIO_2_per_SECOND;
        }
        return 0;
    }

    public void generateWorkload() {
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
        int numrequest = NumberofRequest();
        int requestID;
        int size;
        int filesize;
        boolean firsttimewritetofile = true;
        String filepath = AppConstants.DIRECTORY + Simulation.SENARIO + "\\";
        int part = 0;
        int fileNo = 0;
        ReadWritetoFile write = new ReadWritetoFile();
        List<Workload> workloadlist = new ArrayList<Workload>();
//        List<List<Workload>> listworkloadlist = new ArrayList<List<Workload>>();
        Workload request = null;
        ExponentialDistr expdistr_size = new ExponentialDistr(AppConstants.L_Mean); // generate size of requests
        ExponentialDistr expdistr_numberofrequest = new ExponentialDistr(NumberofRequestperSecond()); // generate size of requests
//        ExponentialDistr expdistr_numberofrequest1 = new ExponentialDistr(NumberofRequestperSecond()); // generate size of requests
        ParetoDistr paretodist = new ParetoDistr(1, AppConstants.FILE_SIZE_MIN);
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
                    size = (int) expdistr_size.sample();
                    if (size < AppConstants.L_MAX && size >= AppConstants.L_MIN) {
                        requestID = NextRequestID();
                        filesize = (int) paretodist.sample();
                        long arrivaltime = (long) uniformdist_arrivaltime.sample();
                        num_req -= 1;
                        int peneeded = (int) uniformdist_pe.sample();
                        int storageserver;
                        int datastorageDcId;
                        int numberOfFilesNeeded = (int) uniformdist_numberofFiles.sample();
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
                        Arrays.sort(timetoIO);
                        if (rand.nextInt(100) % 2 == 0) {
                            storageserver = (int) uniformdist_dataserver_in_DC_0.sample();
                            datastorageDcId = 0; // data storage is in datacenter #0
                        } else {
                            storageserver = (int) uniformdist_dataserver_in_DC_1.sample();
                            datastorageDcId = 1;  // data storage is in datacenter #1
                        }
//                        System.out.println("Arrival time: "+arrivaltime);
                        request = new Workload(requestID, size, filesize, filesize, AppConstants.VM_PES[peneeded], storageserver, datastorageDcId, arrivaltime,
                                numberOfFilesNeeded, sizeOfFiles, timetoIO);
                        /*   request = new Workload(requestID,
                         size,
                         filesize,
                         filesize,
                         AppConstants.VM_PES[peneeded],
                         storageserver,
                         arrivaltime);*/
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

    }

    public static void main(String[] args) {
        GenerateWorkload g = new GenerateWorkload();
        g.generateWorkload();
        ReadWritetoFile r = new ReadWritetoFile();
//        long t1 = System.currentTimeMillis();
//        System.out.println(t1);
//        List<Object> o = r.readfromFile("d:\\Workload\\1.bin");

//        System.out.println(System.currentTimeMillis()-t1);
//        System.out.println("Size:"+o.size());
    }
}
