/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AppWorkload;

import java.io.Serializable;
import java.util.Comparator;

/**
 *
 * @author Novin Pendar
 */
public class Workload implements Serializable{
    
    int id;
    int length;  // per million instruction
    int filesize;  // per Byte
    int outputfilsize;     //per Byte
    int pes; //number of pes that this request needs
    int datastorageDcId;
    int dataserverNode; // ID of data server which contains the given data 
    long arrivalTime;  //from ara of starting simulation
    int numberofFiles;
    long[] sizeofFile;// = new long[numberofFiles];
    long[] timetoIO;//at this time a cloudlet start to I/O
//    double CpuUtilization;

    public Workload(int id, int size, int filesize, int outputfilsize, int pes,int datastoragDcId, int dataserverNode, long arrivalTime, int numberofFiles, long[] sizeofFile,long[] timetoIO) {
        this.id = id;
        this.length = size;
        this.filesize = filesize;
        this.outputfilsize = outputfilsize;
        this.pes = pes;
        this.datastorageDcId = datastoragDcId;
        this.dataserverNode = dataserverNode;
        this.arrivalTime = arrivalTime;
        this.numberofFiles = numberofFiles;
        this.sizeofFile = sizeofFile;
        this.timetoIO = timetoIO;
//        this.CpuUtilization = Cputilization;
    }

    public Workload(int id, int size, int filesize, int outputfilsize, int pes, int dataserverNode, long arrivalTime) {
        this.id = id;
        this.length = size;
        this.filesize = filesize;
        this.outputfilsize = outputfilsize;
        this.pes = pes;
        this.dataserverNode = dataserverNode;
        this.arrivalTime = arrivalTime;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return length;
    }

    public int getFilesize() {
        return filesize;
    }

    public int getOutputfilsize() {
        return outputfilsize;
    }

    public int getPes() {
        return pes;
    }

    public int getDataserverNode() {
        return dataserverNode;
    }
    
/*    
static class CompareArrivalTime implements Comparator<Workload>{

        @Override
        public int compare(Workload o1, Workload o2) {
          if (o1.getArrivalTime() > o2.getArrivalTime())
            return 1;
          else if (o1.getArrivalTime() < o2.getArrivalTime())
            return -1;
          else 
              return 0;
        }
    
}    
    */

    public int getNumberofFiles() {
        return numberofFiles;
    }

    public long[] getSizeofFile() {
        return sizeofFile;
    }

    public int getLength() {
        return length;
    }

    public int getDatastorageDcId() {
        return datastorageDcId;
    }

    public long[] getTimetoIO() {
        return timetoIO;
    }
  
}
