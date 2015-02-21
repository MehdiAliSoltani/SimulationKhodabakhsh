/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package CloudManagement;

import AppWorkload.Workload;

/**
 *
 * @author Novin Pendar
 */
public class CloudTableRow {
    
        
//    int cloudletId;
    int requestedLength;
    int actualLength;  // per million instruction
//    int datacenterId;
//    int hostedServerId;
//    int status;
//    int filesize;  // per Byte
//    int outputfilsize;     //per Byte
//    int pes; //number of pes that this request needs
//    int dataServerNode; // ID of data server which contains the given data 
    long arrivalTime;  //from ara of starting simulation
    long startTime;
    long finishTime;
        
    
    int numberofFiles;
    long[] sizeofFile;// = new long[numberofFiles];
    long[] timeToIO;//at this time a cloudlet start to I/O

    
}
