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
public class Resource {
    int datacenterId;
    int hostId;
    int vmId;
    private double mips;
    private double ram;
    private double bw;

    public Resource(int datacenterId, int hostId, int vmId, double mips, double ram, double bw) {
        this.datacenterId = datacenterId;
        this.hostId = hostId;
        this.vmId = vmId;
        this.mips = mips;
        this.ram = ram;
        this.bw = bw;
    }

    
}
