/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ExteraCloudSim;

import java.util.ArrayList;
import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;

/**
 *
 * @author Novin Pendar
 */
public class CloudletPower extends Cloudlet {
    private int dataStorageDatacenterId;
    private int dataStorageId;
    private long arrivalTime;
    private double startTime;
    private int numberofFiles;
    private long[] sizeofFiles;
    private long[] timeofIO;
    private double totalWaitingTime;
    private double blockedTime; 
    private int ioNo; // indicate which files have been read before
    private double totalExecutionTime;
    private double totalCpuConsume;
    private double restartTime;
//    private long bandwidthUseDuringRun; // per second
    private boolean inSource;
    private int datacenterIdHostedCloudlet;
    private int host;
//    private long bandwidthUseApprox;
    private double alpha;
    private boolean ioCheck;
//    private double cloudletTimetoRunApprox;

    public CloudletPower(int dataStorageDatacenterId,
            int dataStorageId,
            long arrivalTime,
            int numberofFiles, 
            long[] sizeofFiles,
            long[] timeofIO,
            int cloudletId, 
            long cloudletLength,
            int pesNumber,
            long cloudletFileSize, 
            long cloudletOutputSize, 
            UtilizationModel utilizationModelCpu, 
            UtilizationModel utilizationModelRam, 
            UtilizationModel utilizationModelBw) {
        super(cloudletId,
                cloudletLength,
                pesNumber, 
                cloudletFileSize,
                cloudletOutputSize, 
                utilizationModelCpu,
                utilizationModelRam, 
                utilizationModelBw);
        this.dataStorageDatacenterId = dataStorageDatacenterId;
        this.dataStorageId = dataStorageId;
        this.arrivalTime = arrivalTime;
        this.numberofFiles = numberofFiles;
        this.sizeofFiles = sizeofFiles;
        this.timeofIO = timeofIO;
        setTotalWaitingTime(0);
        setTotalExecutionTime(0);
        setTotalCpuConsume(0);
        setIoCheck(false);
    }

    public CloudletPower(int dataStorageDatacenterId,
            int dataStorageId, 
            long arrivalTime, 
            int numberofFiles, 
            long[] sizeofFiles, 
            long[] timeofIO,
            int cloudletId,
            long cloudletLength,
            int pesNumber, 
            long cloudletFileSize, 
            long cloudletOutputSize,
            UtilizationModel utilizationModelCpu,
            UtilizationModel utilizationModelRam, 
            UtilizationModel utilizationModelBw, 
            boolean record, 
            List<String> fileList) {
        super(cloudletId,
                cloudletLength, 
                pesNumber, 
                cloudletFileSize,
                cloudletOutputSize,
                utilizationModelCpu,
                utilizationModelRam, 
                utilizationModelBw, 
                record,
                fileList);
        this.dataStorageDatacenterId = dataStorageDatacenterId;
        this.dataStorageId = dataStorageId;
        this.arrivalTime = arrivalTime;
        this.numberofFiles = numberofFiles;
        this.sizeofFiles = sizeofFiles;
        this.timeofIO = timeofIO;
        setTotalWaitingTime(0);
        setTotalExecutionTime(0);
        setTotalCpuConsume(0);
        setIoCheck(false);
    }

    public CloudletPower(int dataStorageDatacenterId, int dataStorageId, long arrivalTime, int numberofFiles, long[] sizeofFiles, long[] timeofIO, int cloudletId, long cloudletLength, int pesNumber, long cloudletFileSize, long cloudletOutputSize, UtilizationModel utilizationModelCpu, UtilizationModel utilizationModelRam, UtilizationModel utilizationModelBw, List<String> fileList) {
        super(cloudletId, cloudletLength, pesNumber, cloudletFileSize, cloudletOutputSize, utilizationModelCpu, utilizationModelRam, utilizationModelBw, fileList);
        this.dataStorageDatacenterId = dataStorageDatacenterId;
        this.dataStorageId = dataStorageId;
        this.arrivalTime = arrivalTime;
        this.numberofFiles = numberofFiles;
        this.sizeofFiles = sizeofFiles;
        this.timeofIO = timeofIO;
        setTotalWaitingTime(0);
        setTotalExecutionTime(0);
        setTotalCpuConsume(0);
        setIoCheck(false);
    }

    public CloudletPower(int dataStorageDatacenterId, int dataStorageId, long arrivalTime, int numberofFiles, long[] sizeofFiles, long[] timeofIO, int cloudletId, long cloudletLength, int pesNumber, long cloudletFileSize, long cloudletOutputSize, UtilizationModel utilizationModelCpu, UtilizationModel utilizationModelRam, UtilizationModel utilizationModelBw, boolean record) {
        super(cloudletId, cloudletLength, pesNumber, cloudletFileSize, cloudletOutputSize, utilizationModelCpu, utilizationModelRam, utilizationModelBw, record);
        this.dataStorageDatacenterId = dataStorageDatacenterId;
        this.dataStorageId = dataStorageId;
        this.arrivalTime = arrivalTime;
        this.numberofFiles = numberofFiles;
        this.sizeofFiles = sizeofFiles;
        this.timeofIO = timeofIO;
        setTotalWaitingTime(0);
        setTotalExecutionTime(0);
        setTotalCpuConsume(0);
        setIoCheck(false);
    }

    public int getDataStorageDatacenterId() {
        return dataStorageDatacenterId;
    }

    public void setDataStorageDatacenterId(int dataStorageDatacenterId) {
        this.dataStorageDatacenterId = dataStorageDatacenterId;
    }

    public int getDataStorageId() {
        return dataStorageId;
    }

    public void setDataStorageId(int dataStorageId) {
        this.dataStorageId = dataStorageId;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public int getNumberofFiles() {
        return numberofFiles;
    }

    public void setNumberofFiles(int numberofFiles) {
        this.numberofFiles = numberofFiles;
    }

    public long[] getSizeofFiles() {
        return sizeofFiles;
    }

    public void setSizeofFiles(long[] sizeofFiles) {
        this.sizeofFiles = sizeofFiles;
    }

    public long[] getTimeofIO() {
        return timeofIO;
    }

    public void setTimeofIO(long[] timeofIO) {
        this.timeofIO = timeofIO;
    }

    public int getIoNo() {
        return ioNo;
    }

    public void setIoNo(int ioNo) {
        this.ioNo = ioNo;
    }
    public void incrementIoNo(){
        this.ioNo++;
    }

    public double getTotalWaitingTime() {
        return totalWaitingTime;
    }

    public void addWaitingTime(double waitingTime) {
        this.totalWaitingTime = getTotalWaitingTime() + waitingTime;
    }

    public void setTotalWaitingTime(double totalWaitingTime) {
        this.totalWaitingTime = totalWaitingTime;
    }

    public double getBlockedTime() {
        return blockedTime;
    }

    public void setBlockedTime(double blockedTime) {
        this.blockedTime = blockedTime;
    }

    public double getTotalExecutionTime() {
        return getFinishTime() - getStartTime();
    }

    public void setTotalExecutionTime(double totalExecutionTime) {
        this.totalExecutionTime = totalExecutionTime;
    }

    public double getTotalCpuConsume() {
        return totalCpuConsume;
    }

    public void addTotalCpuConsume(double cpuConsume) {
        this.totalCpuConsume = getTotalCpuConsume() + cpuConsume;
    }

    public void setTotalCpuConsume(double totalCpuConsume) {
        this.totalCpuConsume = totalCpuConsume;
    }

    public double getRestartTime() {
        return restartTime;
    }

    public void setRestartTime(double restartTime) {
        this.restartTime = restartTime;
    }

    
    public boolean isInSource() {
        return inSource;
    }

    public void setInSource(boolean inSource) {
        this.inSource = inSource;
    }

    public int getDatacenterIdHostedCloudlet() {
        return datacenterIdHostedCloudlet;
    }

    public void setDatacenterIdHostedCloudlet(int datacenterIdHostedCloudlet) {
        this.datacenterIdHostedCloudlet = datacenterIdHostedCloudlet;
    }

    

    public int getHost() {
        return host;
    }

    public void setHost(int host) {
        this.host = host;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public boolean isIoCheck() {
        return ioCheck;
    }

    public void setIoCheck(boolean ioCheck) {
        this.ioCheck = ioCheck;
    }

    
}
