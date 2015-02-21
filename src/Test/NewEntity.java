/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Test;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import simulation.AppConstants;

/**
 *
 * @author Novin Pendar
 */
public class NewEntity extends SimEntity {

    public NewEntity(String name) {
        super(name);
    }

    @Override
    public void startEntity() {
        int destination = getId();
        int delay =0;
        Log.printLine(super.getName() + " is starting...");
                
//                scheduleNode snode = new scheduleNode(0, delay);
//        for (int i = 1; i < 10; i++) {
//            delay = AppConstants.HOUR * i;
            schedule(destination, delay, 1, 1);
//            System.out.println(""+delay);
//        }
                

    }
private static int count =0;
    @Override
    public void processEvent(SimEvent ev) {
        ev.getDestination();
        ev.getData();
         if(count++  < 100){
                schedule(getId(), 0.1, 0);
         System.out.println(CloudSim.clock() +" : ++++++++++++++++++++++++++++++++++++++++++++++ # " +count);
         }

    }

    @Override
    public void shutdownEntity() {
    ;
    }
     class scheduleNode {

        int requestId;
        double starttime;

        public scheduleNode(int requestId, double starttime) {
            this.requestId = requestId;
            this.starttime = starttime;
        }

        public int getRequestId() {
            return requestId;
        }

        public double getStarttime() {
            return starttime;
        }
    }
   
    
}
