package com.calvin.oohw7;

import com.calvin.oohw7.enums.Direction;
import com.calvin.oohw7.enums.ElevIndex;
import com.calvin.oohw7.enums.Status;
import com.oocourse.elevator3.PersonRequest;

public class LiftRequest implements Cloneable {
    // variables
    private final int id;
    private final int fromFloor;
    private final int toFloor;
    private boolean mutex;
    private Status status;
    private Direction direction;
    private ElevIndex elevIndex;
    private LiftRequest nextLrq;
    
    // constructor
    public LiftRequest(PersonRequest pr, ElevIndex idx, LiftRequest nextLrq) {
        id = pr.getPersonId();
        fromFloor = changeMinusFloor(pr.getFromFloor());
        toFloor = changeMinusFloor(pr.getToFloor());
        status = Status.WAIT;
        direction = tellDirection(fromFloor, toFloor);
        elevIndex = idx;
        mutex = false;
        this.nextLrq = nextLrq;
    }
    
    public LiftRequest(int id, int fromFloor, int toFloor,
                       Status status, ElevIndex idx, LiftRequest nextLrq) {
        this.id = id;
        this.fromFloor = changeMinusFloor(fromFloor);
        this.toFloor = changeMinusFloor(toFloor);
        this.status = status;
        direction = tellDirection(fromFloor, toFloor);
        elevIndex = idx;
        mutex = false;
        this.nextLrq = nextLrq;
    }
    
    // public methods
    private static int changeMinusFloor(int tarFloor) {
        if (tarFloor < 0) {
            return tarFloor + 1;
        } else {
            return tarFloor;
        }
    }
    
    // recognize the direction
    private static Direction tellDirection(int fromFloor, int toFloor) {
        int delta = toFloor - fromFloor;
        if (delta > 0) {
            return Direction.UP;
        } else if (delta < 0) {
            return Direction.DOWN;
        } else {
            return Direction.HALT;
        }
    }
    
    public LiftRequest getNextLrq() {
        return nextLrq;
    }
    
    public void setNextLrq(LiftRequest nextLrq) {
        this.nextLrq = nextLrq;
    }
    
    public int getId() {
        return id;
    }
    
    public int getFromFloor() {
        return fromFloor;
    }
    
    public int getToFloor() {
        return toFloor;
    }
    
    public boolean isMutex() {
        return mutex;
    }
    
    public void setMutex(boolean mutex) {
        this.mutex = mutex;
    }
    
    // only use in ElevatorObserver, so don't need to be synchronized
    public Status getStatus() {
        return status;
    }
    
    public Direction getDirection() {
        return direction;
    }
    
    public ElevIndex getElevIndex() {
        return elevIndex;
    }
    
    // change status methods
    public void getReady() {
        status = Status.READY;
    }
    
    public void loseTarElevator() {
        status = Status.WAIT;
    }
    
    public void enterElevator() {
        status = Status.RUN;
    }
    
    public void leaveElevator() {
        status = Status.FINISH;
    }
    
    @Override
    public String toString() {
        return String.format("%d: FROM %d TO %d", id, fromFloor, toFloor);
    }
    
    @Override
    protected LiftRequest clone() throws CloneNotSupportedException {
        LiftRequest lr = null;
        try {
            lr = (LiftRequest) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return lr;
    }
}
