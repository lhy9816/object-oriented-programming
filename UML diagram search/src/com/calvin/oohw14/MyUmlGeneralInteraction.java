package com.calvin.oohw14;

import com.calvin.oohw14.elements.MyUmlClass;
import com.calvin.oohw14.elements.MyUmlStateMachine;
import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.common.AttributeQueryType;
import com.oocourse.uml2.interact.common.OperationQueryType;
import com.oocourse.uml2.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml2.interact.format.UmlGeneralInteraction;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyUmlGeneralInteraction implements UmlGeneralInteraction {
    
    private ClassDataBase classDataBase;
    private StateDataBase stateDataBase;
    private ColabDataBase colabDabaBase;
    private HashMap<String, ArrayList<UmlElement>> databases;
    private GeneralTypes generalTypes;
    
    public MyUmlGeneralInteraction(UmlElement[] elements) {
        generalTypes = new GeneralTypes();
        databases = allocateInput(elements);
        classDataBase = new ClassDataBase(databases.get("Class"));
        stateDataBase = new StateDataBase(databases.get("State"));
        colabDabaBase = new ColabDataBase(databases.get("Colab"));
    }
    
    private HashMap<String, ArrayList<UmlElement>> allocateInput(
            UmlElement[] elements) {
        HashMap<String, ArrayList<UmlElement>> dbs = new HashMap<>();
        dbs.put("Class", new ArrayList<>());
        dbs.put("State", new ArrayList<>());
        dbs.put("Colab", new ArrayList<>());
        for (int i = 0; i < elements.length; ++i) {
            ElementType type = elements[i].getElementType();
            if (generalTypes.getClassType().contains(type)) {
                dbs.get("Class").add(elements[i]);
            } else if (generalTypes.getStateType().contains(type)) {
                dbs.get("State").add(elements[i]);
            } else {
                dbs.get("Colab").add(elements[i]);
            }
        }
        return dbs;
    }
    
    // class orders
    
    public int getClassCount() {
        return classDataBase.getClasses().size();
    }
    
    public int getClassOperationCount(String className,
                                      OperationQueryType queryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        throwClassExps(className);
        MyUmlClass tarClass = classDataBase.getClassByName(className);
        return tarClass.getOperNumByType(queryType);
    }
    
    public int getClassAttributeCount(String className,
                                      AttributeQueryType queryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        throwClassExps(className);
        return classDataBase.getAttrNumByName(className, queryType);
    }
    
    public int getClassAssociationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        throwClassExps(className);
        
        return classDataBase.getClassAssoCount(className);
    }
    
    public List<String> getClassAssociatedClassList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        throwClassExps(className);
        
        return classDataBase.getClassAssoClassList(className);
    }
    
    public Map<Visibility, Integer> getClassOperationVisibility(
            String className, String operationName)
            throws ClassNotFoundException, ClassDuplicatedException {
        throwClassExps(className);
        MyUmlClass tarClass = classDataBase.getClassByName(className);
        return tarClass.getOperVisibility(operationName);
    }
    
    public Visibility getClassAttributeVisibility(
            String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        throwClassExps(className);
        MyUmlClass tarClass = classDataBase.getClassByName(className);
        Visibility findType = null;
        while (classDataBase.getClasses().containsKey(tarClass.getId())) {
            Visibility curType = tarClass.getAttrVisibility(attributeName);
            if (curType != null && findType != null) {
                throw new AttributeDuplicatedException(className,
                        attributeName);
            }
            if (curType != null) {
                findType = curType;
            }
            tarClass = classDataBase.getClasses().get(tarClass.getFather());
            if (tarClass == null) {
                break;
            }
        }
        if (findType == null) {
            throw new AttributeNotFoundException(className, attributeName);
        }
        return findType;
    }
    
    public String getTopParentClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        throwClassExps(className);
        return classDataBase.getTopClassName(className);
    }
    
    public List<String> getImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        throwClassExps(className);
        return classDataBase.getImplementInterfaces(className);
    }
    
    public List<AttributeClassInformation> getInformationNotHidden(
            String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        throwClassExps(className);
        return classDataBase.getNotHiddenClassAttr(className);
    }
    
    public void throwClassExps(String className) throws
            ClassNotFoundException, ClassDuplicatedException {
        if (!classDataBase.getClassDupMap().containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (classDataBase.getClassDupMap().get(className) > 1) {
            throw new ClassDuplicatedException(className);
        }
    }
    
    // statemachine orders
    public int getStateCount(String stateMachineName) throws
            StateMachineNotFoundException, StateMachineDuplicatedException {
        throwStateMachineExps(stateMachineName);
        return stateDataBase.getStateMachineByName(stateMachineName).
                getStatesNum();
    }
    
    public int getTransitionCount(String stateMachineName) throws
            StateMachineNotFoundException, StateMachineDuplicatedException {
        throwStateMachineExps(stateMachineName);
        MyUmlStateMachine tarStateMachine = stateDataBase.
                getStateMachineByName(stateMachineName);
        return tarStateMachine.getTransNum();
    }
    
    public int getSubsequentStateCount(String stateMachineName,
                                       String stateName)
            throws StateMachineNotFoundException,
            StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        throwStateMachineExps(stateMachineName);
        return stateDataBase.getSubsequentStateCount(
                stateMachineName, stateName);
    }
    
    public void throwStateMachineExps(String stateMachineName) throws
            StateMachineNotFoundException, StateMachineDuplicatedException {
        if (!stateDataBase.getStateMachineDupMap().
                containsKey(stateMachineName)) {
            throw new StateMachineNotFoundException(stateMachineName);
        }
        if (stateDataBase.getStateMachineDupMap().get(stateMachineName) > 1) {
            throw new StateMachineDuplicatedException(stateMachineName);
        }
    }
    
    // judge plausible orders
    // 无重名成员
    public void checkForUml002() throws UmlRule002Exception {
        classDataBase.getUtils().checkAttrEndDup();
    }
    
    // 无循环继承
    public void checkForUml008() throws UmlRule008Exception {
        classDataBase.getUtils().checkLoopExtension();
    }
    
    // 无重复实现接口
    public void checkForUml009() throws UmlRule009Exception {
        classDataBase.getUtils().checkDupRealization();
    }
    
    // interaction orders
    public int getParticipantCount(String interactionName) throws
            InteractionNotFoundException,
            InteractionDuplicatedException {
        throwInteractionExps(interactionName);
        return colabDabaBase.getInteractionByName(
                    interactionName).getAttributes().size();
    }
    
    public int getMessageCount(String interactionName)
            throws InteractionNotFoundException,
            InteractionDuplicatedException {
        throwInteractionExps(interactionName);
        return colabDabaBase.getInteractionByName(
                interactionName).getMessages().size();
    }
   
    public int getIncomingMessageCount(String interactionName,
                                       String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        throwInteractionExps(interactionName);
        return colabDabaBase.getInMessageCount(interactionName, lifelineName);
    }
    
    public void throwInteractionExps(String interactionName) throws
            InteractionNotFoundException, InteractionDuplicatedException {
        if (!colabDabaBase.getInteractionDupMap().
                containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        }
        if (colabDabaBase.getInteractionDupMap().get(interactionName) > 1) {
            throw new InteractionDuplicatedException(interactionName);
        }
    }
}
