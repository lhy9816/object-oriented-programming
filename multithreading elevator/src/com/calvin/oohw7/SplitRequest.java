package com.calvin.oohw7;

import java.util.ArrayList;

public class SplitRequest {
    private int capacity;
    private ArrayList<LiftRequest> liftRequests;
    
    public SplitRequest(ArrayList<LiftRequest> liftRequests) {
        this.liftRequests = liftRequests;
        capacity = liftRequests.size();
    }
    
    public void add(LiftRequest lr) {
        liftRequests.add(lr);
    }
    
    public ArrayList<LiftRequest> getLiftRequests() {
        return liftRequests;
    }
}
