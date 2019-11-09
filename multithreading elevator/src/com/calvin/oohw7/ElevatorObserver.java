package com.calvin.oohw7;

import com.calvin.oohw7.enums.Direction;
import com.calvin.oohw7.enums.Door;
import com.calvin.oohw7.enums.Movement;
import com.calvin.oohw7.enums.Status;
import com.oocourse.TimableOutput;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.TimeUnit;

public class ElevatorObserver extends Thread {
    
    private BlockingDeque<LiftRequest> liftRequests;
    private int capacity;
    private int numInElev;
    
    private String type;
    private ElevatorScheduler elevatorScheduler;
    
    private Direction runDirection;
    private Door door;
    private int curFloor;
    
    private TimeConsumeTable movementTable;
    
    public ElevatorObserver(ElevatorScheduler evs,
                            double movetime, int capacity, String type) {
        this.capacity = capacity;
        numInElev = 0;
        this.type = type;
        
        // reqDirection = Direction.HALT;
        runDirection = Direction.HALT;
        elevatorScheduler = evs;
        liftRequests = evs.getLiftRequests();
        door = Door.CLOSE;
        curFloor = 1;
        movementTable = new TimeConsumeTable(movetime);
        
        
    }
    
    // methods
    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                // complex logical judgment, see details in function comment
                // DebugHelper.debugPrintln(2, "curFloor: " + curFloor);
                if (checkInputFinished()) {
                    // System.out.println("I'M DONE, " + type);
                    closeDoor();
                    break;
                }
                if (containTarPeople()) {
                    // if someone is going out, set its state to FINISH
                    // DebugHelper.debugPrintln(3,"contain people in lift");
                    getOffElev();
                    closeDoor();
                }
                // traverse carryQueue and find the reqs with Status.WAIT
                // throw them into openDoorQueue and closeDoorQueue
                runDirection = tellDirection();
                loadOpenCloseQueue();
                
                // go up / down for only one floor
                moveElevator();
                // program has finished
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void putRequest(LiftRequest lrq) {
        try {
            liftRequests.put(lrq);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private boolean containTarPeople() {
        boolean ret = false;
        try {
            synchronized (liftRequests) {
                while (liftRequests.size() == 0) {
                    liftRequests.wait();
                }
                for (LiftRequest lrq : liftRequests) {
                    if (lrq.getStatus().equals(Status.RUN) &&
                            lrq.getToFloor() == curFloor) {
                        ret = true;
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ret;
    }
    
    // methods
    public BlockingDeque<LiftRequest> getLiftRequests() {
        return liftRequests;
    }
    
    private Direction tellDirection() {
        boolean hasRun = false;
        boolean hasReady = false;
        boolean hasWait = false;
        boolean hasItem = false;
        LiftRequest l1 = null;
        try {
            synchronized (liftRequests) {
                while (liftRequests.size() == 0) {
                    liftRequests.wait();
                }
                for (LiftRequest lrq : liftRequests) {
                    if (!lrq.getStatus().equals(Status.FINISH)) {
                        hasItem = true;
                    }
                    Status st = lrq.getStatus();
                    if (st.equals(Status.RUN)) {
                        hasRun = true;
                    }
                    if (st.equals(Status.READY)) {
                        hasReady = true;
                        l1 = lrq;
                    }
                    if (st.equals(Status.WAIT)) {
                        hasWait = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!hasItem) {
            return Direction.HALT;
        } else if (hasRun) {
            return runDirection;
        } else {
            if (hasReady) {
                l1.loseTarElevator();
            }
            LiftRequest l2 = setOneReady();
            l2.getReady();
            if (l2 != null) {
                if (l2.getFromFloor() > curFloor) {
                    return Direction.UP;
                } else if (l2.getFromFloor() < curFloor) {
                    return Direction.DOWN;
                } else {
                    return l2.getDirection();
                }
            } else {
                return Direction.HALT;
            }
        }
    }
    
    public LiftRequest setOneReady() {
        int ups = 0;
        int downs = 0;
        int min = 100;
        int max = 0;
        LiftRequest tmpLrqMin = null;
        LiftRequest tmpLrqMax = null;
        try {
            synchronized (liftRequests) {
                while (liftRequests.size() == 0) {
                    liftRequests.wait();
                }
                for (LiftRequest lrq : liftRequests) {
                    if (lrq.getDirection().equals(Direction.DOWN) &&
                            !lrq.isMutex()) {
                        downs++;
                        if (lrq.getFromFloor() > max) {
                            max = lrq.getFromFloor();
                            tmpLrqMax = lrq;
                        }
                    } else if (lrq.getDirection().equals(Direction.UP)
                            && !lrq.isMutex()) {
                        ups++;
                        if (lrq.getFromFloor() < min) {
                            min = lrq.getFromFloor();
                            tmpLrqMin = lrq;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tmpLrqMax == null) {
            tmpLrqMax = liftRequests.peek();
        }
        if (tmpLrqMin == null) {
            tmpLrqMin = liftRequests.peek();
        }
        if (runDirection.equals(Direction.UP)) {
            return tmpLrqMax;
        } else if (runDirection.equals(Direction.DOWN)) {
            return tmpLrqMin;
        } else {
            if (ups <= downs) {
                // tmpLrqMax.getReady();
                return tmpLrqMax;
            } else {
                // tmpLrqMin.getReady();
                return tmpLrqMin;
            }
        }
    }
    
    /*private boolean initialControl() {
        // if there are still people in the elevator
        // meaning that the main request is still alive
        if (containTarPeople()) {
            // if someone is going out, set its state to FINISH(remove it is ok)
            // DebugHelper.debugPrintln(3,"contain people in lift");
            getOffElev();
        }
        BlockingDeque<LiftRequest> lrqs = elevatorScheduler.getLiftRequests();
        // begin synchronization
        synchronized (lockCarry) {
            scheWorking = true;
            lockCarry.notifyAll();
            elevWorking = false;
            try {
                while (!elevWorking) {
                    lockCarry.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (checkInputFinished()) {
            closeDoor();
            return false;
        }
        int idx = hasMainRequest();
        //System.out.println("idx: " + idx);
        if (idx >= 0) {
            reqDirection = carryQueue.get(idx).getDirection();
            setRunDirection(idx);
        } else {
            reqDirection = Direction.HALT;
            runDirection = Direction.HALT;
        }
        return true;
    }*/
    
    private boolean checkInputFinished() {
        boolean flag = false;
        try {
            synchronized (liftRequests) {
                while (liftRequests.size() == 0) {
                    liftRequests.wait();
                }
                for (LiftRequest liftRequest : liftRequests) {
                    // find end flag
                    if (-1 == liftRequest.getId()) {
                        flag = true;
                    }
                }
                for (LiftRequest liftRequest : liftRequests) {
                    if (!liftRequest.getStatus().equals(Status.FINISH)) {
                        flag = false;
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    public void loadOpenCloseQueue() {
        try {
            // synchronized (lockCarry) {
            // iterate carryQueue to renew open and close queue
            synchronized (liftRequests) {
                while (liftRequests.size() == 0) {
                    liftRequests.wait();
                }
                for (LiftRequest lr : liftRequests) {
                    if (canLoad(lr)) {
                        openDoor();
                        getOnElev(lr);
                        lr.enterElevator();
                    }
                }
            }
            closeDoor();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean canLoad(LiftRequest lr) {
        boolean b1 = lr.getStatus().equals(Status.WAIT)
                || lr.getStatus().equals(Status.READY);
        boolean b2 = lr.getDirection().equals(runDirection);
        boolean b3 = lr.getFromFloor() == curFloor
                && lr.getDirection().equals(Direction.UP);
        boolean b4 = lr.getFromFloor() == curFloor
                && lr.getDirection().equals(Direction.DOWN);
        boolean b5 = numInElev < capacity;
        boolean b6 = !lr.isMutex();
        if (b1 && b2 && (b3 || b4) && b5 && b6) {
            return true;
        } else {
            return false;
        }
        
    }
    
    // interface for schedule lift queue
    // sleep needs to be synchronized
    private synchronized void sleepMethods(Movement mv) {
        try {
            TimeUnit.MILLISECONDS.sleep((long) (
                    movementTable.getTime(mv) * 1000));
        } catch (InterruptedException e) {
            System.out.println("Error when sleeps!");
        }
    }
    
    // move elevator between floor
    public void moveElevator() {
        try {
            /*while (isElevStateMutex()) {
                wait();
            }
            setElevStateMutex(true);*/
            Direction dirt;
            dirt = runDirection;
            
            /*setElevStateMutex(false);
            notifyAll();*/
            if (dirt.equals(Direction.UP)) {
                goUp();
                arrive();
            } else if (dirt.equals(Direction.DOWN)) {
                goDown();
                arrive();
            } else {
                // do nothing
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // arrive, go up and down, can be interrupted !
    private void goUp() {
        sleepMethods(Movement.UPWARD);
        curFloor += 1;
    }
    
    private void goDown() {
        sleepMethods(Movement.DOWNWARD);
        curFloor -= 1;
    }
    
    private void arrive() {
        TimableOutput.println(String.format(
                "ARRIVE-%d-%s", recoverMinusFloor(curFloor), type));
    }
    
    // open and close the door, cannot be interrupted !
    public void openDoor() {
        if (door.equals(Door.CLOSE)) {
            TimableOutput.println(String.format(
                    "OPEN-%d-%s", recoverMinusFloor(curFloor), type));
            door = Door.OPEN;
            sleepMethods(Movement.OPEN);
        }
    }
    
    public void closeDoor() {
        if (door.equals(Door.OPEN)) {
            sleepMethods(Movement.CLOSE);
            TimableOutput.println(String.format(
                    "CLOSE-%d-%s", recoverMinusFloor(curFloor), type));
            door = Door.CLOSE;
        }
    }
    
    public void getOnElev(LiftRequest lrq) {
        try {
            // traverse opencloseQueue
            int id = lrq.getId();
            int from = lrq.getFromFloor();
            openDoor();
            
            TimableOutput.println(
                    String.format("IN-%d-%d-%s",
                            id, recoverMinusFloor(from), type));
            numInElev++;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public void getOffElev() {
        // use state == FINISH to note that this instruction has finished
        try {
            synchronized (liftRequests) {
                while (liftRequests.size() == 0) {
                    liftRequests.wait();
                }
                for (LiftRequest lrq : liftRequests) {
                    int id = lrq.getId();
                    int to = lrq.getToFloor();
                    if (lrq.getStatus().equals(Status.RUN) && to == curFloor) {
                        openDoor();
                        lrq.leaveElevator();
                        liftRequests.remove(lrq);
                        numInElev--;
                        TimableOutput.println(
                                String.format("OUT-%d-%d-%s",
                                        id, recoverMinusFloor(to), type));
                        if (lrq.getNextLrq() != null) {
                            lrq.getNextLrq().setMutex(false);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public int recoverMinusFloor(int tarFloor) {
        if (tarFloor <= 0) {
            return tarFloor - 1;
        } else {
            return tarFloor;
        }
    }
}
