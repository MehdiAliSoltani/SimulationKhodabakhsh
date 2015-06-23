/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import CloudManagement.CloudManagement;
import ExteraCloudSim.HostPower;
import ExteraCloudSim.VmPower;
import Log.LogRecord;
import Log.ReadWriteFile;
import Resource.StorageHost;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.core.CloudSim;

/**
 *
 * @author Novin Pendar
 */
public class Simulation {
    
    public static int SENARIO = 4;
    public static int POLICY = AppConstants.SSLNT;
//    public static int POLICY =AppConstants.RANDOMSELECTION;
//    public static int POLICY = AppConstants.RANDOMSELECTIONWITHRESOURCECONSIDERING;

    public static Map<Integer, LogRecord> LOGRECORD = new HashMap<Integer, LogRecord>();
    public static long[] ToatalBandwidthUsedDatacenter = new long[AppConstants.NUM_DATACENTER]; // for total system in second
    public static long TotalBandwidthUsedBetweenDatacenter;
    
            
    public static int NO_VM = 0;
    public static int Vm_NO = 0;
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

        CloudManagement c = new CloudManagement("cloudcontroller");

        CloudSim.terminateSimulation(AppConstants.SIMULATION_LENGTH);
        CloudSim.startSimulation();

        CloudSim.stopSimulation();
        ReadWriteFile writeLogtoExcel = new ReadWriteFile();
        String outputFileName = AppConstants.OUTPUT_DIRECTORY + AppConstants.POLICY_NAME[POLICY] + ".xls";
        writeLogtoExcel.writeToExcel(LOGRECORD, outputFileName);
        int numberCloudlet = 0;
        long totalSizeofFiles = 0;
        double totalCpuUsage = 0;
        double totalWaitingTime = 0;
        double totalCompelitionTime = 0;
        int success = 0, unsuccess = 0, numberInsourced = 0;
        for (Map.Entry<Integer, LogRecord> map : LOGRECORD.entrySet()) {
            numberCloudlet++;
            totalSizeofFiles += map.getValue().getIoFileSize();
            totalCpuUsage += map.getValue().getCpuUsageTime();
            totalWaitingTime += map.getValue().getWaitingTime();
            totalCompelitionTime += map.getValue().getCompelitionTime();
            if (!map.getValue().isOutsourced()) {
                numberInsourced++;
            }
            if (map.getValue().getStatus() == Cloudlet.SUCCESS) {
                success++;
            } else {
                unsuccess++;
            }
        }

        String summeryFileName = AppConstants.OUTPUT_DIRECTORY + "Summery_" + AppConstants.POLICY_NAME[POLICY] + ".xls";
        writeLogtoExcel.writeSummeryToExcel(numberCloudlet,
                totalSizeofFiles,
                totalCpuUsage,
                totalWaitingTime,
                totalCompelitionTime,
                numberInsourced,
                success,
                unsuccess,
                summeryFileName);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print("  " + VM_NUM_TYPE[i][j]);
            }
            System.out.println("\n");
        }
        System.out.println("");
        System.out.println("Number of created VM = " + NO_VM);

        for (int i = 0; i < CloudManagement.NumberofServer; i++) {
            for (int j = 0; j < CloudManagement.Utilization[i].size(); j++) {
                System.out.print(CloudManagement.Utilization[i].get(j)+ " ");
            }
            System.out.println("");
        }
    
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

    public static long getToatalBandwidthUsedDatacenter(int datacenterId) {
        return ToatalBandwidthUsedDatacenter[datacenterId];
    }

    public static void setToatalBandwidthUsedDatacenter(long[] ToatalBandwidthUsedDatacenter) {
        Simulation.ToatalBandwidthUsedDatacenter = ToatalBandwidthUsedDatacenter;
    }

    public static void increaseToatalBandwidthUsedDatacenter(int datacenterId, long ToatalBandwidthUsedDatacenter) {
        Simulation.ToatalBandwidthUsedDatacenter[datacenterId] += ToatalBandwidthUsedDatacenter;
    }

    public static void decreaseToatalBandwidthUsedDatacenter(int datacenterId, long ToatalBandwidthUsedDatacenter) {
        Simulation.ToatalBandwidthUsedDatacenter[datacenterId] -= ToatalBandwidthUsedDatacenter;
    }

    public static long getTotalBandwidthUsedBetweenDatacenter() {
        return TotalBandwidthUsedBetweenDatacenter;
    }

    public static void setTotalBandwidthUsedBetweenDatacenter(long TotalBandwidthUsedBetweenDatacenter) {
        Simulation.TotalBandwidthUsedBetweenDatacenter = TotalBandwidthUsedBetweenDatacenter;
    }

    public static void decreaseTotalBandwidthUsedBetweenDatacenter(long TotalBandwidthUsedBetweenDatacenter) {
        Simulation.TotalBandwidthUsedBetweenDatacenter -= TotalBandwidthUsedBetweenDatacenter;
    }

    public static void increaseTotalBandwidthUsedBetweenDatacenter(long TotalBandwidthUsedBetweenDatacenter) {
        Simulation.TotalBandwidthUsedBetweenDatacenter += TotalBandwidthUsedBetweenDatacenter;
    }
    

}
 