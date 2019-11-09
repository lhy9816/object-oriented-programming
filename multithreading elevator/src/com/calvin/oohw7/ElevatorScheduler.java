package com.calvin.oohw7;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class ElevatorScheduler extends Scheduler implements Runnable {
    private BlockingDeque<LiftRequest> liftRequests;
    private final ElevatorObserver elevatorObserver;
    
    public ElevatorScheduler(int capacity, double movetime, String type) {
        // 该电梯电梯的总容量
        super(capacity);
        // this.liftRequests = liftRequests;
        liftRequests = new LinkedBlockingDeque<>();
        elevatorObserver = new ElevatorObserver(this, movetime, capacity, type);
    }
    
    // 将elevator scheduler 写在elevator里面，elevator会调用里面的takeRequest方法，
    // 取得对应elevatorscheduler里面的
    @Override
    public void run() {
        LiftRequest lrq = null;
        try {
            while (!Thread.interrupted()) {
                synchronized (elevatorObserver.getLiftRequests()) {
                    while (liftRequests.size() <= 0) {
                        liftRequests.wait();
                    }
                    lrq = liftRequests.peek();
                    if (lrq != null) {
                        if (lrq.getId() == -1) {
                            if (liftRequests.size() == 1) {
                                elevatorObserver.getLiftRequests().notifyAll();
                                break;
                            }
                        }
                        /*if (!lrq.isMutex()) {*/
                        //if (elevatorObserver.getLiftRequests().size() == 0) {
                        elevatorObserver.getLiftRequests().notifyAll();
                        /*} else {
                        elevatorObserver.putRequest(lrq);
                        }*/
                        /*if (lrq.getId() == -1) {
                        break;
                        }*/
                        /*} else {
                            // 这个指令是分段指令，前面的指令还没有进行完毕，将它抛到队列后面去
                            lrq = liftRequests.takeFirst();
                            liftRequests.putLast(lrq);
                        }*/
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public ElevatorObserver getElevatorObserver() {
        return elevatorObserver;
    }
    
    public BlockingDeque<LiftRequest> getLiftRequests() {
        return liftRequests;
    }
    
    public synchronized void putRequest(LiftRequest lrq) {
        try {
            liftRequests.put(lrq);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
