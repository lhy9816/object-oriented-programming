package com.calvin.oohw7;

import com.calvin.oohw7.enums.ElevIndex;

import java.util.ArrayList;
import java.util.concurrent.BlockingDeque;

public class TopScheduler extends Scheduler implements Runnable {
    private BlockingDeque<OriginalRequest> originalRequests;
    private ArrayList<ElevatorScheduler> elevatorSchedulers;
    private static int Atimes;
    private static int Btimes;
    private static int Ctimes;
    private static Graph graph = new Graph();
    
    public TopScheduler(BlockingDeque<OriginalRequest> originalRequests) {
        super(50);
        this.originalRequests = originalRequests;
        this.elevatorSchedulers = new ArrayList<ElevatorScheduler>();
        elevatorSchedulers.add(new ElevatorScheduler(6, 0.4, "A"));
        elevatorSchedulers.add(new ElevatorScheduler(8, 0.5, "B"));
        elevatorSchedulers.add(new ElevatorScheduler(7, 0.6, "C"));
    }
    
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            OriginalRequest ogr = null;
            try {
                ogr = originalRequests.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!distributeOriRequest(ogr)) {
                break;
            }
        }
    }
    
    private boolean distributeOriRequest(OriginalRequest ogr) {
        SplitRequest srq = ogr.getSplitRequests().get(0);
        if (srq.getLiftRequests().get(0).getId() == -1) {
            LiftRequest lrq = srq.getLiftRequests().get(0);
            BlockingDeque<LiftRequest> lrqA =
                    elevatorSchedulers.get(0).getLiftRequests();
            synchronizeElev(lrq, lrqA, elevatorSchedulers.get(0));
            BlockingDeque<LiftRequest> lrqB =
                    elevatorSchedulers.get(1).getLiftRequests();
            synchronizeElev(lrq, lrqB, elevatorSchedulers.get(1));
            BlockingDeque<LiftRequest> lrqC =
                    elevatorSchedulers.get(2).getLiftRequests();
            synchronizeElev(lrq, lrqC, elevatorSchedulers.get(2));
            return false;
        }
        if (srq.getLiftRequests().size() == 2) {
            // 需要换乘
            distributeLiftRequest(srq.getLiftRequests().get(0), false);
            distributeLiftRequest(srq.getLiftRequests().get(1), true);
        } else {
            distributeLiftRequest(srq.getLiftRequests().get(0), false);
        }
        return true;
    }
    
    private void synchronizeElev(LiftRequest lrq, BlockingDeque<LiftRequest>
            lrqDq, ElevatorScheduler evs) {
        synchronized (lrqDq) {
            evs.putRequest(lrq);
            lrqDq.notifyAll();
        }
    }
    
    public void distributeLiftRequest(LiftRequest lrq, boolean mutex) {
        if (mutex) {
            lrq.setMutex(true);
        } else {
            lrq.setMutex(false);
        }
        
        ElevIndex elevIndex = lrq.getElevIndex();
        if (elevIndex.equals(ElevIndex.A)) {
            BlockingDeque<LiftRequest> lrqA =
                    elevatorSchedulers.get(0).getLiftRequests();
            synchronizeElev(lrq, lrqA, elevatorSchedulers.get(0));
        } else if (elevIndex.equals(ElevIndex.B)) {
            BlockingDeque<LiftRequest> lrqB =
                    elevatorSchedulers.get(1).getLiftRequests();
            synchronizeElev(lrq, lrqB, elevatorSchedulers.get(1));
        } else {
            BlockingDeque<LiftRequest> lrqC =
                    elevatorSchedulers.get(2).getLiftRequests();
            synchronizeElev(lrq, lrqC, elevatorSchedulers.get(2));
        }
    }
    
    /*public synchronized OriginalRequest takeRequest() {
        // quite complex, needs further refinement
        return new OriginalRequest(new ArrayList<SplitRequest>());
    }*/
    
    public synchronized void putRequest(OriginalRequest or) {
        try {
            originalRequests.put(or);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    // start three elevators
    public void startElevatorSchedulersAndElevs() {
        for (ElevatorScheduler es : elevatorSchedulers) {
            /*Thread elevScheThread = new Thread(es);
            elevScheThread.start();*/
            es.getElevatorObserver().start();
        }
    }
}
