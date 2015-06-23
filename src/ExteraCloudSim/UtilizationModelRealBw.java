/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExteraCloudSim;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.security.auth.login.AppConfigurationEntry;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;
import simulation.AppConstants;
import simulation.Simulation;

/**
 *
 * @author Novin Pendar
 */
public class UtilizationModelRealBw implements UtilizationModel {
    /*
     @Override
     public double getUtilization(double time) {
     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     }
    
     */

    /**
     * The random generator.
     */
    private Random randomGenerator;

    /**
     * The history.
     */
    private Map<Double, Double> history;

    /**
     * Instantiates a new utilization model stochastic.
     */
    public UtilizationModelRealBw() {
        setHistory(new HashMap<Double, Double>());
        setRandomGenerator(new Random());
    }

    /**
     * Instantiates a new utilization model stochastic.
     *
     * @param seed the seed
     */
    public UtilizationModelRealBw(long seed) {
        setHistory(new HashMap<Double, Double>());
        setRandomGenerator(new Random(seed));
    }

    /*
     * (non-Javadoc)
     * @see cloudsim.power.UtilizationModel#getUtilization(double)
     */
/*
    @Override
    public double getUtilization(double time) {
        for (HostPower host : Simulation.COMPUTE_SERVER_LIST) {
            double bwUtilization = 0;
            List<VmPower> vmlist = host.getVmList();
            for (VmPower vm : vmlist) {
                List<CloudletPower> cloudletlist = vm.getVmCloudletList();
                for (CloudletPower cloudlet : cloudletlist) {
                    int id = cloudlet.getCloudletId();
                    int status = vm.getCloudletScheduler().getCloudletStatus(id);
                    if (status == Cloudlet.PAUSED) { // blocked for I/O
//                        long startTime = cloudlet.getStartTime();
//                        long[] timetoIO = cloudlet.getTimeofIO();
//                        long[] sizeofFiles = cloudlet.getSizeofFiles();
                        long sizeoffile = cloudlet.getSizeofFiles()[cloudlet.getIoNo()];
                        if (cloudlet.getDataStorageDatacenterId() == host.getDatacenterID()) {
                            bwUtilization = bwUtilization + sizeoffile * AppConstants.BASE_FILE_TRANSFER_TIME_IN_DC;
                        }else{
                            bwUtilization = bwUtilization + sizeoffile * AppConstants.BASE_FILE_TRANSFER_TIME_BETWEEN_DC;
                        }

//                        double cloudletCurrentTime = time - startTime;
//                        for (int i = 0; i < timetoIO.length; i++) {
//                            
//                        }
                    }
                }
            }
        }

        if (getHistory().containsKey(time)) {
            return getHistory().get(time);
        }

        double utilization = getRandomGenerator().nextDouble();
        getHistory().put(time, utilization);
        return utilization;
    }

    /**
     * Gets the history.
     *
     * @return the history
     */
    protected Map<Double, Double> getHistory() {
        return history;
    }

    /**
     * Sets the history.
     *
     * @param history the history
     */
    protected void setHistory(Map<Double, Double> history) {
        this.history = history;
    }

    /**
     * Save history.
     *
     * @param filename the filename
     * @throws Exception the exception
     */
    public void saveHistory(String filename) throws Exception {
        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(getHistory());
        oos.close();
    }

    /**
     * Load history.
     *
     * @param filename the filename
     * @throws Exception the exception
     */
    @SuppressWarnings("unchecked")
    public void loadHistory(String filename) throws Exception {
        FileInputStream fis = new FileInputStream(filename);
        ObjectInputStream ois = new ObjectInputStream(fis);
        setHistory((Map<Double, Double>) ois.readObject());
        ois.close();
    }

    /**
     * Sets the random generator.
     *
     * @param randomGenerator the new random generator
     */
    public void setRandomGenerator(Random randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    /**
     * Gets the random generator.
     *
     * @return the random generator
     */
    public Random getRandomGenerator() {
        return randomGenerator;
    }

    @Override
    public double getUtilization(double time) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
