package com.calvin.oohw7;

import com.calvin.oohw7.enums.Movement;

import java.util.HashMap;

public class TimeConsumeTable {
    private HashMap<Movement, Double> movementTable;
    
    public TimeConsumeTable(double move) {
        movementTable = new HashMap<Movement, Double>();
        movementTable.put(Movement.UPWARD, move);
        movementTable.put(Movement.DOWNWARD, move);
        movementTable.put(Movement.OPEN, 0.2);
        movementTable.put(Movement.CLOSE, 0.2);
    }
    
    public double getTime(Movement mv) {
        return movementTable.get(mv);
    }
}
