package com.calvin.oohw14;

import com.oocourse.uml2.models.common.ElementType;

import java.util.HashSet;

public class GeneralTypes {
    private final HashSet<ElementType> classType = new HashSet<>();
    private final HashSet<ElementType> stateType = new HashSet<>();
    private final HashSet<ElementType> colabType = new HashSet<>();
    
    public GeneralTypes() {
        classType.add(ElementType.UML_CLASS);
        classType.add(ElementType.UML_ATTRIBUTE);
        classType.add(ElementType.UML_OPERATION);
        classType.add(ElementType.UML_PARAMETER);
        classType.add(ElementType.UML_ASSOCIATION);
        classType.add(ElementType.UML_ASSOCIATION_END);
        classType.add(ElementType.UML_GENERALIZATION);
        classType.add(ElementType.UML_INTERFACE_REALIZATION);
        classType.add(ElementType.UML_INTERFACE);
        stateType.add(ElementType.UML_STATE_MACHINE);
        stateType.add(ElementType.UML_REGION);
        stateType.add(ElementType.UML_PSEUDOSTATE);
        stateType.add(ElementType.UML_FINAL_STATE);
        stateType.add(ElementType.UML_STATE);
        stateType.add(ElementType.UML_TRANSITION);
        stateType.add(ElementType.UML_EVENT);
        stateType.add(ElementType.UML_OPAQUE_BEHAVIOR);
        colabType.add(ElementType.UML_INTERACTION);
        colabType.add(ElementType.UML_LIFELINE);
        colabType.add(ElementType.UML_MESSAGE);
        colabType.add(ElementType.UML_ENDPOINT);
    }
    
    public HashSet<ElementType> getClassType() {
        return classType;
    }
    
    public HashSet<ElementType> getColabType() {
        return colabType;
    }
    
    public HashSet<ElementType> getStateType() {
        return stateType;
    }
}
