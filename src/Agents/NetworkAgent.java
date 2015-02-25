/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import ExteraCloudSim.HostPower;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import simulation.AppConstants;
import simulation.Simulation;

/**
 *
 * @author Novin Pendar
 */
public class NetworkAgent {

    private static Map<Keys, TableValues[]> initWTable = new LinkedHashMap<Keys, TableValues[]>();
    private static Map<Keys, TableValues[]> WTable;
    int dataStorage;

    public NetworkAgent() {
        initializeWTable();

    }

    public void initializeWTable() {
        TableValues[] tablevalue;
        int tableIndex;
        double default_alpha_in_datacenter = 1;
        double default_alpha_between_datacenter;
//        List<HostPower> hostlist = Simulation.getCOMPUTE_SERVER_LIST();
 // this part temporarily added       
        List<HostPower> hostlist = new  ArrayList<HostPower>();
        for (int i = 0; i < AppConstants.NUM_DATACENTER; i++) {
            for (HostPower h : Simulation.getCOMPUTE_SERVER_LIST(i)) {
                hostlist.add(h);
            }
        }
        
        tablevalue = new TableValues[hostlist.size()];
        for (int datacenterId = 0; datacenterId < AppConstants.NUM_DATACENTER; datacenterId++) {
            for (int dataStorageId = 0; dataStorageId < AppConstants.NUM_STORAGE_SERVERS[datacenterId]; dataStorageId++) {
                tableIndex = 0;
                for (int i = 0; i < tablevalue.length; i++) {
//                    tablevalue[i] =null;
                    tablevalue[i] = new TableValues();
                }

                for (HostPower host : hostlist) {
                    int dcId = host.getDatacenterID();
                    switch (datacenterId) {
                        case 0:
                            if (datacenterId == dcId) {
                                default_alpha_in_datacenter = getRandomTrafficCoeff(
                                        AppConstants.staticCoefficientTraficInDatacenter_0.get(0),
                                        AppConstants.staticCoefficientTraficInDatacenter_0.get(1));
                            } else {
                                default_alpha_in_datacenter = getRandomTrafficCoeff(
                                        AppConstants.staticCoefficientTraficBetweenDatacenter_0.get(0),
                                        AppConstants.staticCoefficientTraficBetweenDatacenter_0.get(1));

                            }
                            break;
                        case 1:
                            if (datacenterId == dcId) {
                                default_alpha_in_datacenter = getRandomTrafficCoeff(
                                        AppConstants.staticCoefficientTraficInDatacenter_1.get(0),
                                        AppConstants.staticCoefficientTraficInDatacenter_1.get(1));
                            } else {
                                default_alpha_in_datacenter = getRandomTrafficCoeff(
                                        AppConstants.staticCoefficientTraficBetweenDatacenter_1.get(0),
                                        AppConstants.staticCoefficientTraficBetweenDatacenter_1.get(1));

                            }
                            break;

                    }

                    tablevalue[tableIndex].setDatacenterId(datacenterId);
                    tablevalue[tableIndex].setComputehostId(host.getId());
                    tablevalue[tableIndex].setAlpha(default_alpha_in_datacenter);
                    tableIndex++;
                }
                TableValues[] tv = new TableValues[tablevalue.length];
                System.arraycopy(tablevalue, 0, tv, 0, tablevalue.length);
                Keys key = new Keys(datacenterId, dataStorageId);
                initWTable.put(key, tv);
//                for (int i = 0; i < tablevalue.length; i++) {
//                    tablevalue[i] = null;
//                }

            }

        }
        /*        
         for (int i = 0; i < 2; i++) {
         for (int j = 0; j < 10; j++) {
         Keys k = new Keys(i, j);
         TableValues[] t = WTable.get(k);
         //                System.out.println("Key: dcId # " + i + "  DataStorageId # " + j);
         for (int l = 0; l < 200; l++) {
         //                    System.out.print(t[l].getDatacenterId() + "  " + t[l].getComputehostId() + "  " + t[l].getAlpha() + " | ");
         System.out.print(t[l].getAlpha() + "  ");
         }
         System.out.println("");
         //                System.out.println("");
         }
         }

         System.out.println("");
         */
        WTable = initWTable;
    }

    /**
     * call this method at the first of time slot scheduling
     */
    public void setDynamicWTable() {

        for (int datacenterId = 0; datacenterId < AppConstants.NUM_DATACENTER; datacenterId++) {
            for (int dataStorageId = 0; dataStorageId < AppConstants.NUM_STORAGE_SERVERS[datacenterId]; dataStorageId++) {
                Keys key = new Keys(datacenterId, dataStorageId);
                TableValues[] tvalue = initWTable.get(key);
                TableValues[] tableValues = null;
                tableValues = new TableValues[tvalue.length];
                for (int i = 0; i < tvalue.length; i++) {
                    int csId = tvalue[i].getComputehostId();
                    int dcId = tvalue[i].getDatacenterId();
                    for (int dId = 0; dId < AppConstants.NUM_DATACENTER; dId++) {
                        for (HostPower hostpower : Simulation.getCOMPUTE_SERVER_LIST(dId)) {
                            if (hostpower.getDatacenterID() == dcId && hostpower.getId() == csId) {
                                double bwutilization = hostpower.getBwUtilization();
                                double alpha = tvalue[i].getAlpha() + tvalue[i].getAlpha() * bwutilization;
//                            tableValues = new TableValues[tvalue.length];
                                tableValues[i] = new TableValues();
                                tableValues[i].setAlpha(alpha);
                                tableValues[i].setComputehostId(csId);
                                tableValues[i].setDatacenterId(dcId);
                                break;
                            }

                        }
                    }

                }
                WTable.put(key, tableValues);
            }
        }

    }
    /*
     set elements in WTable
     */

    public void setWTableElement(int dataStorageDcId, int dataStorageId, int cSDcId, int hostId, double alpha) {
        Keys key = new Keys(dataStorageDcId, dataStorageId);
        TableValues[] tablevalue = WTable.get(key);
        for (int i = 0; i < tablevalue.length; i++) {
            if (tablevalue[i].getDatacenterId() == cSDcId && tablevalue[i].getComputehostId() == hostId) {
                tablevalue[i].setAlpha(alpha);
                break;
            }
            WTable.replace(key, tablevalue);
        }
    }

    public double getWTableElement(int dataStorageDcId, int dataStorageId, int cSDcId, int hostId, double alpha) {
        Keys key = new Keys(dataStorageDcId, dataStorageId);
        TableValues[] tablevalue = WTable.get(key);
        for (int i = 0; i < tablevalue.length; i++) {
            if (tablevalue[i].getDatacenterId() == cSDcId && tablevalue[i].getComputehostId() == hostId) {
                return tablevalue[i].getAlpha();

            }
        }

        return -1;
    }

    public double getInitWtable(int dataStorageDcId, int dataStorageId, int cSDcId, int hostId, double alpha) {
        Keys key = new Keys(dataStorageDcId, dataStorageId);
        TableValues[] tablevalue = initWTable.get(key);
        for (int i = 0; i < tablevalue.length; i++) {
            if (tablevalue[i].getDatacenterId() == cSDcId && tablevalue[i].getComputehostId() == hostId) {
                return tablevalue[i].getAlpha();

            }
        }

        return -1;

    }

    private double getRandomTrafficCoeff(double rangeMax, double rangeMin) {
//                double rangeMin = 1;
//        double rangeMax = 1.5;
        Random r = new Random();
//        double randomValue =
        return rangeMin + (rangeMax - rangeMin) * r.nextDouble();

    }
//    private double getRandomTrafficBetweenDatacenter(int datacenterId){
//         Random random = new Random();
//         return (random.nextDouble() * AppConstants.staticCoefficientTraficInDatacenter[datacenterId] ) +1 ;
//    }

    public TableValues[] getWTableRow(int dataStorageDcId, int dataStorageId) {
        Keys key = new Keys(dataStorageDcId, dataStorageId);
//        TableValues[] t = this.WTable.get(key);
        return this.WTable.get(key);
    }

    class Keys {

        private int datacenterId;
        private int storagehostId;

        public Keys(int datacenterId, int storagehostId) {
            this.datacenterId = datacenterId;
            this.storagehostId = storagehostId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Keys)) {
                return false;
            }
            Keys key = (Keys) o;
            return datacenterId == key.datacenterId && storagehostId == key.storagehostId;
        }

        @Override
        public int hashCode() {
            int result = datacenterId;
            result = 31 * result + storagehostId;
            return result;
        }

        public int getDatacenterId() {
            return datacenterId;
        }

        public void setDatacenterId(int datacenterId) {
            this.datacenterId = datacenterId;
        }

        public int getHostId() {
            return storagehostId;
        }

        public void setHostId(int hostId) {
            this.storagehostId = hostId;
        }

    }

    public class TableValues {

        private int datacenterId;
        private int computehostId;
        private double alpha;  // this is transmision cost between a compute host and storage cost.

        public int getDatacenterId() {
            return datacenterId;
        }

        public void setDatacenterId(int datacenterId) {
            this.datacenterId = datacenterId;
        }

        public int getComputehostId() {
            return computehostId;
        }

        public void setComputehostId(int computehostId) {
            this.computehostId = computehostId;
        }

        public double getAlpha() {
            return alpha;
        }

        public void setAlpha(double alpha) {
            this.alpha = alpha;
        }

    }

    public Map<Keys, TableValues[]> getInitWTable() {
        return initWTable;
    }

    public void setInitWTable(Map<Keys, TableValues[]> initWTable) {
        this.initWTable = initWTable;
    }

    public Map<Keys, TableValues[]> getWTable() {
        return WTable;
    }

    public void setWTable(Map<Keys, TableValues[]> WTable) {
        this.WTable = WTable;
    }

}
