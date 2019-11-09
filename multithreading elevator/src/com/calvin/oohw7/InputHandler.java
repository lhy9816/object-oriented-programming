package com.calvin.oohw7;

import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.ElevatorInput;

public class InputHandler extends Thread {
    // private Object lockInput;
    // private boolean mutexInput;
    private ElevatorInput elevatorInput = new ElevatorInput(System.in);
    private TopScheduler topScheduler;
    
    public InputHandler(TopScheduler topScheduler) {
        // this.lockInput = lockInput;
        // this.mutexInput = mutexInput;
        this.topScheduler = topScheduler;
    }
    
    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                PersonRequest request = elevatorInput.nextPersonRequest();
                // when request == null
                // it means there are no more lines in stdin
                if (request == null) {
                    break;
                } else {
                    // create request
                    OriginalRequest originalRequest =
                            new OriginalRequest(request);
                    // add to lift-queue and avoid conflicts between lrq.add and
                    // lrq.checkEmpty & lrq.checkFinished (in SchedulerServer)!!
                    
                    topScheduler.putRequest(originalRequest);
                    
                }
            }
            elevatorInput.close();
            topScheduler.putRequest(new OriginalRequest(-1));
            /*synchronized (lock) {
                liftRequestQueue.add(haltRequest);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Add to Lift-queue interrupted!");
        }
    }
}
