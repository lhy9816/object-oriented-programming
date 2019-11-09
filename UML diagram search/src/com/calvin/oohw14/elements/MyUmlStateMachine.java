package com.calvin.oohw14.elements;

import com.oocourse.uml2.models.elements.UmlStateMachine;

import java.util.HashMap;

public class MyUmlStateMachine extends MyUmlElement {
    
    // private HashMap<String, MyUmlStates> states;
    private HashMap<String, MyUmlRegion> regions;
    private int statesNum;
    private int transNum;
    
    public MyUmlStateMachine(UmlStateMachine element) {
        super(element);
        // this.states = new HashMap<>();
        this.regions = new HashMap<>();
        this.statesNum = 0;
        this.transNum = 0;
    }
    
    public int getStatesNum() {
        return statesNum;
    }
    
    public int getTransNum() {
        return transNum;
    }
    
    public void addStatesNum(int increment) {
        statesNum += increment;
    }
    
    public void addTransNum(int increment) {
        transNum += increment;
    }
    
    // inner methods
    public HashMap<String, MyUmlRegion> getRegions() {
        return regions;
    }
}
