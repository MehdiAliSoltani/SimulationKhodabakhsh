/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Novin Pendar
 */
public class AppConstants {

//    public final static int SIMULATION_LENGTH = 24 * 3600; // one day
    public final static int SSLNT = 0; // Selecting Server with the Lowest Network Traffic
    public final static int RANDOMSELECTION = 1;
    public final static int RANDOMSELECTIONWITHRESOURCECONSIDERING = 2;
    public final static int TURNSELECTION = 3;
    public final static String[] POLICY_NAME = {"SSLNT",
        "RANDOMSELECTION",
        "RANDOMSELECTIONWITHRESOURCECONSIDERING",
        "TURNSELECTION"};
    
    public final static String INPUT_DIRECTORY = "C:\\";
    public static final String WORKLOAD_DIRECTORY = "D:\\Workload";
    public final static String OUTPUT_DIRECTORY = WORKLOAD_DIRECTORY+ Simulation.SENARIO+"\\"+POLICY_NAME[Simulation.POLICY]+"\\";
    public static final String[] SCENARIOS = {"Senario1", "Senario2", "Senario3", "Senario4", "Senario5", "Senario6"};
    public static final int[] SCENARIO = {1, 2, 3, 4, 5, 6};

    public final static int SIMULATION_HOUR = 1; // one day
//    public final static int SIMULATION_LENGTH = SIMULATION_HOUR *  3600; // one day
    public final static int SIMULATION_LENGTH = SIMULATION_HOUR * 100; // one day
    public final static int HOUR_IN_SECOND = 3600;
    public final static int EVERY_FIVE = 5;

    public final static int NUM_of_REQUESTS_SECOND = 10; // per second

// request size (number of instructions)    
    public final static int L_MIN = 1000; // minimum number of instructions
    public final static int L_MAX = 100000; // maximum number of instructions

    public final static int L_Mean = 20000; // average number of instructions

// I/O file size    
    public static final int MIN_FILES_NEEDED = 1;
    public static final int MAX_FILES_NEEDED = 100;
    public static final int MIN_FILESIZE = 2000; //2K
    public static final int MAX_FILESIZE = 10000000; // 10MB
    public static final int MEAN_FILESIZE = 500000; // KB

// file size    
    public final static int FILE_SIZE_MIN = 5000;
    public final static int FILE_SIZE_MAX = Integer.MAX_VALUE;

// datacenter specification
    public final static int NUM_DATACENTER = 2;
    public final static int[] NUM_COMPUTE_SERVERS = {10, 10};
    public final static int[] NUM_STORAGE_SERVERS = {10, 10};
// networking    
    public final static List<Double> staticCoefficientTraficInDatacenter_0 = Arrays.asList(1.0, 1.5);
    public final static List<Double> staticCoefficientTraficBetweenDatacenter_0 = Arrays.asList(4.0, 4.7);
    public final static List<Double> staticCoefficientTraficInDatacenter_1 = Arrays.asList(1.0, 1.7);
    public final static List<Double> staticCoefficientTraficBetweenDatacenter_1 = Arrays.asList(4.2, 4.9);
    // basic size of file
    public final static int FILE_BASE_UNIT_ONE_K = 1024 * 8; //bit
    // in datacenter
    public final static long MAX_NETWORK_SPEED_IN_DC = 1000 * 1024 * 1024; // bit per second (Giga bit Ethernet)
    public final static double NETWORK_OVERHEAD_IN_DC = 0.40;
    public final static double NETWORK_OVERHEAD_IN_DC_WITH_OVERHEAD = MAX_NETWORK_SPEED_IN_DC * NETWORK_OVERHEAD_IN_DC;
    public final static double MAX_EFFICIENYC_IN_DC = 1 - NETWORK_OVERHEAD_IN_DC;
    public final static double BASE_FILE_TRANSFER_TIME_IN_DC = FILE_BASE_UNIT_ONE_K / (MAX_NETWORK_SPEED_IN_DC * MAX_EFFICIENYC_IN_DC);
    //between datacenter
    public final static long MAX_NETWORK_SPEED_BETWEEN_DC = 100 * 1024 * 1024; // 100 Mega bit per second (Internet)
    public final static List<Double> NETWORK_OVERHEAD_BETWEEN_DC = Arrays.asList(0.2, 0.5);

    public static double BASE_FILE_TRANSFER_TIME_BETWEEN_DC; // should initilize dynamicly at runtime 
//SAHRE OF BANDWIDTH
//    public static double SHARE_OF_BANDWIDTH_IN_DATACENTER_0 = NETWORK_OVERHEAD_IN_DC_WITH_OVERHEAD / (double) NUM_COMPUTE_SERVERS[0];
    public static double SHARE_OF_BANDWIDTH_IN_DATACENTER_0 = NETWORK_OVERHEAD_IN_DC_WITH_OVERHEAD;
    public static double SHARE_OF_BANDWIDTH_IN_DATACENTER_1 = NETWORK_OVERHEAD_IN_DC_WITH_OVERHEAD / (double) NUM_COMPUTE_SERVERS[1];

    public static double SHARE_OF_BANDWIDTH_BET_D0_D1 = MAX_NETWORK_SPEED_BETWEEN_DC / (double) NUM_COMPUTE_SERVERS[0];
    public static double SHARE_OF_BANDWIDTH_BET_D1_D0 = MAX_NETWORK_SPEED_BETWEEN_DC / (double) NUM_COMPUTE_SERVERS[1];
    public static double COEF_NETWORK_TRAFFIC = 1;
    public static double MAX_ALPHA = 10.0;
    public static double MIN_ALPHA = 1.0;

    // server specication   
    public final static int COMPUTE_HOST = 1;
    public final static int STORAGE_HOST = 2;
    public final static int NUM_CPU = 2;
    public final static int NUM_CORE_per_CPU = 10;
    public final static int NUM_PE_per_CS = 40;
    public final static int PE_MIPS = 10000; //mips

    public final static int STORAGE_per_CS = 10000000;
    public final static int STORAGE_per_SS = 100000000;
    public final static int CS_BAND_WIDTH = 10000000;
    public final static int CS_RAM = 65536;

// VM specification
    public final static int[] VM_TYPE = {0, 1, 2, 3};
    public final static int[] NUM_of_VM_in_HOST = {4, 3, 3, 2};
//    public final static int[] NUM_of_VM_in_HOST = {1, 1, 1, 1};
    public final static int[] VM_PES = {1, 2, 4, 8};
    public final static int[] VM_RAM = {2048, 3481, 7680, 10240};
    public final static int VM_MIPS = 10000;
    public final static int[] VM_IMAGE_SIZE = {10000, 15000, 20000, 25000, 30000};
    public final static int[] VM_BAND_WIDTH = {10000, 20000, 30000, 40000};
// Queue specification
    public final static int NUM_QUEUE = 4;

    public final static double SCHEDULING_INTERVAL = 1.0; //mili second
    public final static double SCHEDULING_PER_SECOND = 1.0; //mili second

    public static final int PROCESS_CREATEVM_In_HOST = 1358;
    public static final int NOT_SUITABLE_VM_FOUND = -100; // return when a suitable vm is not found
    public static final int CLASS_5 = 5;
    public static final int CLASS_4 = 4;
    public static final int CLASS_3 = 3;
    public static final int CLASS_2 = 2;
    public static final int CLASS_1 = 1;

}
