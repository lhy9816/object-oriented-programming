package com.calvin.oohw14.elements;

import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlRegion;

import java.util.HashMap;

public class MyUmlRegion extends MyUmlElement {
    
    private Visibility visibility;
    private HashMap<String, MyUmlStates> states;
    private HashMap<String, MyUmlTransition> transitions;
    private HashMap<String, Integer> statesDupMap;
    private HashMap<String, Integer> transDupMap;
    private int dupPseudo;
    private int dupFinal;
    
    public MyUmlRegion(UmlRegion element) {
        super(element);
        this.visibility = element.getVisibility();
        this.states = new HashMap<>();
        this.transitions = new HashMap<>();
        this.statesDupMap = new HashMap<>();
        this.transDupMap = new HashMap<>();
        this.dupPseudo = 0;
        this.dupFinal = 0;
    }
    
    public void addDupPseudo(int increment) {
        dupPseudo += increment;
    }
    
    public void addDupFinal(int increment) {
        dupFinal += increment;
    }
    
    public void addStatesDupMap(String statesName) {
        if (!statesDupMap.containsKey(statesName)) {
            statesDupMap.put(statesName, 1);
        } else {
            statesDupMap.put(statesName, statesDupMap.get(statesName) + 1);
        }
    }
    
    public void addTransDupMap(String transName) {
        if (!transDupMap.containsKey(transName)) {
            transDupMap.put(transName, 1);
        } else {
            transDupMap.put(transName, transDupMap.get(transName) + 1);
        }
    }
    
    public HashMap<String, MyUmlStates> getStates() {
        return states;
    }
    
    public HashMap<String, MyUmlTransition> getTransitions() {
        return transitions;
    }
    
    public HashMap<String, Integer> getStatesDupMap() {
        return statesDupMap;
    }
    
    public HashMap<String, Integer> getTransDupMap() {
        return transDupMap;
    }
    
    public Visibility getVisibility() {
        return visibility;
    }
    
    public int getDupFinal() {
        return dupFinal;
    }
    
    public int getDupPseudo() {
        return dupPseudo;
    }
}
