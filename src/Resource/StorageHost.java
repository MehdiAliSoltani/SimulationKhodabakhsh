/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Resource;

/**
 *
 * @author Mehdi
 */
public class StorageHost {
    private int datacenterId;
    private int dataStorageId;
    private long bandWidth;
    private int numberofConnectedHost;
    private int numberofFilesServiced;

    public StorageHost(int datacenterId, int dataStorageId, long bandWidth) {
        this.datacenterId = datacenterId;
        this.dataStorageId = dataStorageId;
        this.bandWidth = bandWidth;
    }

    public int getDatacenterId() {
        return datacenterId;
    }

    public void setDatacenterId(int datacenterId) {
        this.datacenterId = datacenterId;
    }

    public int getDataStorageId() {
        return dataStorageId;
    }

    public void setDataStorageId(int dataStorageId) {
        this.dataStorageId = dataStorageId;
    }

    public long getBandWidth() {
        return bandWidth;
    }

    public void setBandWidth(long bandWidth) {
        this.bandWidth = bandWidth;
    }

    public int getNumberofConnectedHost() {
        return numberofConnectedHost;
    }

    public void setNumberofConnectedHost(int numberofConnectedHost) {
        this.numberofConnectedHost = numberofConnectedHost;
    }

    public int getNumberofFilesServiced() {
        return numberofFilesServiced;
    }

    public void setNumberofFilesServiced(int numberofFilesServiced) {
        this.numberofFilesServiced = numberofFilesServiced;
    }
    
    
}
