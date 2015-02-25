/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import AppWorkload.Workload;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import simulation.AppConstants;

/**
 *
 * @author Novin Pendar
 */
public class QueuingAgent {

    private static Queue<Workload>[] systemQueue = new Queue[AppConstants.NUM_QUEUE];

    public QueuingAgent() {
        for (int i = 0; i < AppConstants.NUM_QUEUE; i++) {
            systemQueue[i] = new LinkedBlockingQueue<Workload>();
        }
    }
    

    public Queue[] getSystemQueue() {
        return systemQueue;
    }

    public Queue getSystemQueue(int queueNumber) {
        return systemQueue[queueNumber];
    }

    public void addQueue(Workload workload, int queueNumber) {
       systemQueue[queueNumber].add(workload);
//        try{
//     
//    }catch(ArrayIndexOutOfBoundsException e){
//        System.out.println("in class QueuingAgent,  method addQueue Array Index Out of Bound, Index");
//    }
    }

    public Workload getElement(int queueNumber) {
        try {
//            return systemQueue[queueNumber].peek();
            Workload w = systemQueue[queueNumber].poll();
//            System.out.println("systemQueue[queueNumber].size "+ systemQueue[queueNumber].size());
            return w;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("class Queuing Agent, method getElement,  Index out of bound.");
            return null;
        } 
    }

    public Workload removeElement(int queueNumber) {
        try {
            return systemQueue[queueNumber].remove();
            
        } catch (IndexOutOfBoundsException e) {
            System.out.println("class Queuing Agent, method getElement,  Index out of bound.");
        } catch (NoSuchElementException e) {
            System.out.println("class Queuing Agent, method removeElement,  No Such a Element.");
        } finally {
            return null;
        }

    }

    public int numberofElements(int queueNumber) {
        return systemQueue[queueNumber].size();
    }

    public int numberofActiveQueue() {
        int numberofactivequeue = 0;
        for (int i = 0; i < systemQueue.length; i++) {
            if (systemQueue[i].size() != 0) {
                numberofactivequeue++;
            }
        }
        return numberofactivequeue;
    }
}
