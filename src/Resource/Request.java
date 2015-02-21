/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resource;

/**
 *
 * @author Novin Pendar
 */
public class Request {

    private int requestId;
    private int length;  // per million instruction
    private int filesize;  // per Byte
    private int outputfilsize;     //per Byte
    private int pes; //number of pes that this request needs
    private int datastorageDcId;
    private int dataserverNode; // ID of data server which contains the given data 
    private long arrivalTime;  //from ara of starting simulation
    private int numberofFiles;
    private long[] sizeofFile;// = new long[numberofFiles];
    private long[] timetoIO;//at this time a cloudlet start to I/O

    public Request(int requestId, int length, int filesize, int outputfilsize, int pes, int datastorageDcId, int dataserverNode, long arrivalTime, int numberofFiles, long[] sizeofFile, long[] timetoIO) {
        this.requestId = requestId;
        this.length = length;
        this.filesize = filesize;
        this.outputfilsize = outputfilsize;
        this.pes = pes;
        this.datastorageDcId = datastorageDcId;
        this.dataserverNode = dataserverNode;
        this.arrivalTime = arrivalTime;
        this.numberofFiles = numberofFiles;
        this.sizeofFile = sizeofFile;
        this.timetoIO = timetoIO;
    }

    public int getRequestId() {
        return requestId;
    }

    public int getLength() {
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

    public int getDatastorageDcId() {
        return datastorageDcId;
    }

    public int getDataserverNode() {
        return dataserverNode;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public int getNumberofFiles() {
        return numberofFiles;
    }

    public long[] getSizeofFile() {
        return sizeofFile;
    }

    public long[] getTimetoIO() {
        return timetoIO;
    }

}
