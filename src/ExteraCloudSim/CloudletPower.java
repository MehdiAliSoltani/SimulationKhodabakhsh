/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ExteraCloudSim;

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
    private long startTime;
    private int numberofFiles;
    private long[] sizeofFiles;
    private long[] timeofIO;
    private int ioNo; // indicate which files have been read before

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
    }

    public CloudletPower(int dataStorageDatacenterId, int dataStorageId, long arrivalTime, int numberofFiles, long[] sizeofFiles, long[] timeofIO, int cloudletId, long cloudletLength, int pesNumber, long cloudletFileSize, long cloudletOutputSize, UtilizationModel utilizationModelCpu, UtilizationModel utilizationModelRam, UtilizationModel utilizationModelBw, List<String> fileList) {
        super(cloudletId, cloudletLength, pesNumber, cloudletFileSize, cloudletOutputSize, utilizationModelCpu, utilizationModelRam, utilizationModelBw, fileList);
        this.dataStorageDatacenterId = dataStorageDatacenterId;
        this.dataStorageId = dataStorageId;
        this.arrivalTime = arrivalTime;
        this.numberofFiles = numberofFiles;
        this.sizeofFiles = sizeofFiles;
        this.timeofIO = timeofIO;
    }

    public CloudletPower(int dataStorageDatacenterId, int dataStorageId, long arrivalTime, int numberofFiles, long[] sizeofFiles, long[] timeofIO, int cloudletId, long cloudletLength, int pesNumber, long cloudletFileSize, long cloudletOutputSize, UtilizationModel utilizationModelCpu, UtilizationModel utilizationModelRam, UtilizationModel utilizationModelBw, boolean record) {
        super(cloudletId, cloudletLength, pesNumber, cloudletFileSize, cloudletOutputSize, utilizationModelCpu, utilizationModelRam, utilizationModelBw, record);
        this.dataStorageDatacenterId = dataStorageDatacenterId;
        this.dataStorageId = dataStorageId;
        this.arrivalTime = arrivalTime;
        this.numberofFiles = numberofFiles;
        this.sizeofFiles = sizeofFiles;
        this.timeofIO = timeofIO;
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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
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
    
    
}
