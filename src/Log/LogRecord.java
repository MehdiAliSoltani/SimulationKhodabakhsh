/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Log;

/**
 *
 * @author Novin Pendar
 */
public class LogRecord {
//    private int cloudletId;
    private long cloudletLength;
    private long ioFileSize;
    private double arrivalTime;
    private double startTime;
    private double cpuUsageTime;
    private double waitingTime;
    private double compelitionTime;
    private boolean outsourced;
    private int computeServerId;
    private int status;

//    private int dataStorageDataCenterId;
//    private int vmId;
//    private float computeServerUtilization;
//    private double cloudletTimetoRunApprox;
    public LogRecord(long cloudletLength, long ioFileSize, double arrivalTime, double startTime, boolean outsourced, int computeServerId, int status) {
        this.cloudletLength = cloudletLength;
        this.ioFileSize = ioFileSize;
        this.arrivalTime = arrivalTime;
        this.startTime = startTime;
        this.outsourced = outsourced;
        this.computeServerId = computeServerId;
        this.status =status;
    }
    

    
    public LogRecord(long cloudletLength, long ioFileSize, double arrivalTime, double startTime, double cpuUsageTime, 
            double waitingTime, double compelitionTime, boolean outsourced, int computeServerId) {
        this.cloudletLength = cloudletLength;
        this.ioFileSize = ioFileSize;
        this.arrivalTime = arrivalTime;
        this.startTime = startTime;
        this.cpuUsageTime = cpuUsageTime;
        this.waitingTime = waitingTime;
        this.compelitionTime = compelitionTime;
        this.outsourced = outsourced;
        this.computeServerId = computeServerId;
    }
    /*
    public LogRecord(long cloudletLength, long ioFileSize, double arrivalTime, double startTime, double cpuUsageTime, 
            double waitingTime, double compelitionTime, boolean outsourced, int computeServerId, double cloudletTimetoRunApprox) {
        this.cloudletLength = cloudletLength;
        this.ioFileSize = ioFileSize;
        this.arrivalTime = arrivalTime;
        this.startTime = startTime;
        this.cpuUsageTime = cpuUsageTime;
        this.waitingTime = waitingTime;
        this.compelitionTime = compelitionTime;
        this.outsourced = outsourced;
        this.computeServerId = computeServerId;
        this.cloudletTimetoRunApprox = cloudletTimetoRunApprox;
    }
  */  

//    public int getCloudletId() {
//        return cloudletId;
//    }
//
//    public void setCloudletId(int cloudletId) {
//        this.cloudletId = cloudletId;
//    }

    public long getCloudletLength() {
        return cloudletLength;
    }

    public void setCloudletLength(long cloudletLength) {
        this.cloudletLength = cloudletLength;
    }

    public long getIoFileSize() {
        return ioFileSize;
    }

    public void setIoFileSize(long ioFileSize) {
        this.ioFileSize = ioFileSize;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getCpuUsageTime() {
        return cpuUsageTime;
    }

    public void setCpuUsageTime(double cpuUsageTime) {
        this.cpuUsageTime = cpuUsageTime;
    }

    public double getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(double waitingTime) {
        this.waitingTime = waitingTime;
    }

    public double getCompelitionTime() {
        return compelitionTime;
    }

    public void setCompelitionTime(double compelitionTime) {
        this.compelitionTime = compelitionTime;
    }

    public boolean isOutsourced() {
        return outsourced;
    }

    public void setOutsourced(boolean outsourced) {
        this.outsourced = outsourced;
    }

    public int getComputeServerId() {
        return computeServerId;
    }

    public void setComputeServerId(int computeServerId) {
        this.computeServerId = computeServerId;
    }
    

//    public double getCloudletTimetoRunApprox() {
//        return cloudletTimetoRunApprox;
//    }
//
//    public void setCloudletTimetoRunApprox(double cloudletTimetoRunApprox) {
//        this.cloudletTimetoRunApprox = cloudletTimetoRunApprox;
//    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    
    
    
    
}
