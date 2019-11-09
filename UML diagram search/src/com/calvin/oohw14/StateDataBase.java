package com.calvin.oohw14;

import com.calvin.oohw14.elements.MyUmlRegion;
import com.calvin.oohw14.elements.MyUmlStateMachine;
import com.calvin.oohw14.elements.MyUmlStates;
import com.calvin.oohw14.elements.MyUmlTransition;
import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlFinalState;
import com.oocourse.uml2.models.elements.UmlPseudostate;
import com.oocourse.uml2.models.elements.UmlRegion;
import com.oocourse.uml2.models.elements.UmlState;
import com.oocourse.uml2.models.elements.UmlStateMachine;
import com.oocourse.uml2.models.elements.UmlTransition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class StateDataBase {
    private ArrayList<UmlElement> umlElements;
    private HashMap<String, Integer> stateMachineDupMap;
    private HashMap<String, MyUmlStateMachine> stateMachines;
    private HashMap<String, MyUmlStates> states;
    private HashMap<String, MyUmlTransition> transitions;
    private HashMap<String, MyUmlRegion> regions;
    
    public StateDataBase(ArrayList<UmlElement> elements) {
        this.umlElements = elements;
        this.stateMachineDupMap = new HashMap<>();
        this.stateMachines = new HashMap<>();
        this.states = new HashMap<>();
        this.transitions = new HashMap<>();
        this.regions = new HashMap<>();
        parseInput();
        linkAmongStates();
        linkStatesRegions();
        linkTransRegions();
        linkRegionMachines();
    }
    
    public void addDupStateMachineNum(String stateMachineName) {
        if (stateMachineDupMap.containsKey(stateMachineName)) {
            stateMachineDupMap.put(stateMachineName,
                    stateMachineDupMap.get(stateMachineName) + 1);
        } else {
            stateMachineDupMap.put(stateMachineName, 1);
        }
    }
    
    public void parseInput() {
        for (int i = 0; i < umlElements.size(); ++i) {
            ElementType type = umlElements.get(i).getElementType();
            if (type.equals(ElementType.UML_STATE_MACHINE)) {
                MyUmlStateMachine umlStateMachine =
                        new MyUmlStateMachine((UmlStateMachine)
                                umlElements.get(i));
                stateMachines.put(umlStateMachine.getId(), umlStateMachine);
                addDupStateMachineNum(umlStateMachine.getName());
            } else if (type.equals(ElementType.UML_REGION)) {
                MyUmlRegion umlRegion =
                        new MyUmlRegion((UmlRegion) umlElements.get(i));
                regions.put(umlRegion.getId(), umlRegion);
            } else if (type.equals(ElementType.UML_TRANSITION)) {
                MyUmlTransition umlTransition =
                        new MyUmlTransition((UmlTransition) umlElements.get(i));
                transitions.put(umlTransition.getId(), umlTransition);
            } else if (type.equals(ElementType.UML_STATE)) {
                MyUmlStates umlStates =
                        new MyUmlStates((UmlState) umlElements.get(i));
                states.put(umlStates.getId(), umlStates);
            } else if (type.equals(ElementType.UML_PSEUDOSTATE)) {
                MyUmlStates umlStates =
                        new MyUmlStates((UmlPseudostate) umlElements.get(i));
                states.put(umlStates.getId(), umlStates);
            } else if (type.equals(ElementType.UML_FINAL_STATE)) {
                MyUmlStates umlStates =
                        new MyUmlStates((UmlFinalState) umlElements.get(i));
                states.put(umlStates.getId(), umlStates);
            }
        }
    }
    
    public MyUmlStateMachine getStateMachineByName(String stateMachineName) {
        for (String stateMachineId : stateMachines.keySet()) {
            if (stateMachines.get(stateMachineId).getName().
                    equals(stateMachineName)) {
                return stateMachines.get(stateMachineId);
            }
        }
        System.out.println("SEARCH FOR EMPTY STATEMACHINE!");
        return null;
    }
    
    public void linkStatesRegions() {
        for (String stateId : states.keySet()) {
            MyUmlStates curState = states.get(stateId);
            MyUmlRegion tarRegion = regions.get(curState.getParent());
            tarRegion.getStates().put(stateId, curState);
            tarRegion.addStatesDupMap(curState.getName());
            if (curState.getType().equals(ElementType.UML_PSEUDOSTATE)) {
                tarRegion.addDupPseudo(1);
            }
            if (curState.getType().equals(ElementType.UML_FINAL_STATE)) {
                tarRegion.addDupFinal(1);
            }
        }
    }
    
    public void linkTransRegions() {
        for (String transId : transitions.keySet()) {
            MyUmlTransition curTrans = transitions.get(transId);
            MyUmlRegion tarRegion = regions.get(curTrans.getParent());
            tarRegion.getTransitions().put(transId, curTrans);
            tarRegion.addTransDupMap(curTrans.getName());
        }
    }
    
    public void linkRegionMachines() {
        for (String regionId : regions.keySet()) {
            MyUmlRegion curRegion = regions.get(regionId);
            MyUmlStateMachine tarStateMachine =
                    stateMachines.get(curRegion.getParent());
            tarStateMachine.getRegions().put(regionId, curRegion);
            tarStateMachine.addStatesNum(curRegion.getStates().size());
            if (curRegion.getDupPseudo() > 1) {
                tarStateMachine.addStatesNum(- (curRegion.getDupPseudo() - 1));
            }
            if (curRegion.getDupFinal() > 1) {
                tarStateMachine.addStatesNum(- (curRegion.getDupFinal() - 1));
            }
            tarStateMachine.addTransNum(curRegion.getTransitions().size());
        }
    }
    
    public void linkAmongStates() {
        for (String transId : transitions.keySet()) {
            MyUmlTransition curTrans = transitions.get(transId);
            MyUmlStates srcStates = states.get(curTrans.getSource());
            MyUmlStates tarStates = states.get(curTrans.getTarget());
            srcStates.getBrothers().put(tarStates.getId(), tarStates);
        }
    }
    
    // methods for UmlGeneralInteraction
    public int getSubsequentStateCount(String stateMachineName,
                                       String stateName) throws
            StateNotFoundException, StateDuplicatedException {
        MyUmlStateMachine tarStateMachine =
                getStateMachineByName(stateMachineName);
        MyUmlStates tarState = null;
        for (String curRegionId : tarStateMachine.getRegions().keySet()) {
            MyUmlRegion tarRegion = regions.get(curRegionId);
            for (String curStateId : tarRegion.getStates().keySet()) {
                if (states.get(curStateId).getName().equals(stateName)) {
                    if (tarState == null) {
                        tarState = states.get(curStateId);
                    } else {
                        throw new StateDuplicatedException(
                                stateMachineName, stateName);
                    }
                }
            }
        }
        if (tarState == null) {
            throw new StateNotFoundException(stateMachineName, stateName);
        }
        return bfsSubsequentState(tarState, states);
    }
    
    public int bfsSubsequentState(MyUmlStates umlState,
                                  HashMap<String, MyUmlStates> states) {
        LinkedList<String> queueStates = new LinkedList<>();
        HashSet<String> visitedStates = new HashSet<>();
        queueStates.add(umlState.getId());
        int i = 0;
        int dupPseudo = 0;
        int dupFinal = 0;
        while (!queueStates.isEmpty()) {
            String curStateId = queueStates.pop();
            if (i > 0) {
                visitedStates.add(curStateId);
            }
            for (String brotherStateId : states.get(curStateId).
                    getBrothers().keySet()) {
                if (!visitedStates.contains(brotherStateId)) {
                    if (dupPseudo > 0 && states.get(brotherStateId).getType().
                            equals(ElementType.UML_PSEUDOSTATE) ||
                            (dupFinal > 0 && states.get(brotherStateId).
                                    getType().equals(
                                            ElementType.UML_FINAL_STATE))) {
                        continue;
                    }
                    if (states.get(brotherStateId).getType().
                            equals(ElementType.UML_PSEUDOSTATE)) {
                        dupPseudo++;
                    }
                    if (states.get(brotherStateId).getType().
                            equals(ElementType.UML_FINAL_STATE)) {
                        dupFinal++;
                    }
                    queueStates.add(brotherStateId);
                }
            }
            i++;
        }
        return visitedStates.size();
    }
    
    // inner methods
    
    public HashMap<String, Integer> getStateMachineDupMap() {
        return stateMachineDupMap;
    }
    
    public HashMap<String, MyUmlRegion> getRegions() {
        return regions;
    }
    
    public HashMap<String, MyUmlStateMachine> getStateMachines() {
        return stateMachines;
    }
    
    public HashMap<String, MyUmlStates> getStates() {
        return states;
    }
    
    public HashMap<String, MyUmlTransition> getTransitions() {
        return transitions;
    }
}
