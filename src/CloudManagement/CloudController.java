/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CloudManagement;

import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import simulation.AppConstants;

/**
 *
 * @author Novin Pendar
 */
public class CloudController extends SimEntity {

    private final static int SCHEDULE_one_HOUR = 0;

    public CloudController(String name) {
        super(name);
    }
//
    @Override
    public void startEntity() {
        int destination = getId();
        int delay;
        for (int hour = 0; hour < AppConstants.SIMULATION_HOUR; hour++) {
            delay = hour * AppConstants.HOUR;
            ScheduleNode snode = new ScheduleNode(delay, hour);
            schedule(destination, delay, SCHEDULE_one_HOUR, snode);
        }

    }

    @Override
    public void processEvent(SimEvent ev) {
        int destination = getId();
        Object obj = ev.getData();
        ScheduleNode snode = (ScheduleNode) obj;
        double starttime = snode.getStarttime();
        int hour = snode.getStarthour();
        
        switch (ev.getTag()) {
            case SCHEDULE_one_HOUR:
                //
                break;
            case AppConstants.SCHEDULING_INTERVAL:
                break;
        }
    }

    @Override
    public void shutdownEntity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    class ScheduleNode {

        double starttime;
        int starthour;

        public ScheduleNode(double starttime, int starthour) {
            this.starttime = starttime;
            this.starthour = starthour;
        }

        public double getStarttime() {
            return starttime;
        }

        public int getStarthour() {
            return starthour;
        }

    }

}
