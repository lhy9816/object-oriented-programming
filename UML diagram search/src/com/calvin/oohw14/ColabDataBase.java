package com.calvin.oohw14;

import com.calvin.oohw14.elements.MyUmlEndpoint;
import com.calvin.oohw14.elements.MyUmlInteraction;
import com.calvin.oohw14.elements.MyUmlLifeline;
import com.calvin.oohw14.elements.MyUmlMessage;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlEndpoint;
import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ColabDataBase {
    private ArrayList<UmlElement> umlElements;
    private HashMap<String, MyUmlInteraction> interactions;
    private HashMap<String, MyUmlLifeline> lifelines;
    private HashMap<String, MyUmlMessage> messages;
    private HashMap<String, Integer> interactionDupMap;
    private HashSet<String> attributes;
    
    public ColabDataBase(ArrayList<UmlElement> elements) {
        this.umlElements = elements;
        this.interactionDupMap = new HashMap<>();
        this.interactions = new HashMap<>();
        this.lifelines = new HashMap<>();
        this.messages = new HashMap<>();
        this.attributes = new HashSet<>();
        parseInput();
        linkMessageLifelineAndInteraction();
        linkLifelineInteraction();
    }
    
    public void parseInput() {
        for (int i = 0; i < umlElements.size(); ++i) {
            ElementType type = umlElements.get(i).getElementType();
            if (type.equals(ElementType.UML_INTERACTION)) {
                MyUmlInteraction umlInteraction =
                        new MyUmlInteraction((UmlInteraction)
                                umlElements.get(i));
                interactions.put(umlInteraction.getId(), umlInteraction);
                addDupInteractionNum(umlInteraction.getName());
            } else if (type.equals(ElementType.UML_LIFELINE)) {
                MyUmlLifeline umlLifeline =
                        new MyUmlLifeline((UmlLifeline) umlElements.get(i));
                lifelines.put(umlLifeline.getId(), umlLifeline);
            } else if (type.equals(ElementType.UML_MESSAGE)) {
                MyUmlMessage umlMessage =
                        new MyUmlMessage((UmlMessage) umlElements.get(i));
                messages.put(umlMessage.getId(), umlMessage);
            } else if (type.equals(ElementType.UML_ENDPOINT)) {
                MyUmlEndpoint umlEndpoint =
                        new MyUmlEndpoint((UmlEndpoint) umlElements.get(i));
            }
        }
    }
    
    public MyUmlInteraction  getInteractionByName(String interactionName) {
        for (String interactionId : interactions.keySet()) {
            if (interactions.get(interactionId).getName().
                    equals(interactionName)) {
                return interactions.get(interactionId);
            }
        }
        System.out.println("SEARCH FOR EMPTY INTERACTION!");
        return null;
    }
    
    public void linkMessageLifelineAndInteraction() {
        for (String messageId : messages.keySet()) {
            MyUmlMessage curMessage = messages.get(messageId);
            MyUmlLifeline tarLifeline = lifelines.get(curMessage.getTarget());
            MyUmlInteraction tarInteraction = interactions.
                    get(curMessage.getParent());
            tarInteraction.addMessages(messageId, curMessage);
            if (tarLifeline == null) {
                continue;
            }
            tarLifeline.getInMessages().add(curMessage.getId());
        }
    }
    
    public void linkLifelineInteraction() {
        for (String lifelineId : lifelines.keySet()) {
            MyUmlLifeline curLifeline = lifelines.get(lifelineId);
            MyUmlInteraction tarInteraction = interactions.
                    get(curLifeline.getParent());
            String linkedAttribute = curLifeline.getRepresent();
            tarInteraction.getLifelines().put(lifelineId, curLifeline);
            tarInteraction.addLifelineDupMap(lifelines.
                    get(lifelineId).getName());
            tarInteraction.addAttributes(linkedAttribute);
        }
    }
    
    public void addDupInteractionNum(String interactionName) {
        if (interactionDupMap.containsKey(interactionName)) {
            interactionDupMap.put(interactionName,
                    interactionDupMap.get(interactionName) + 1);
        } else {
            interactionDupMap.put(interactionName, 1);
        }
    }
    
    public int getInMessageCount(String interactionName,
                                 String lifelineName) throws
            LifelineNotFoundException, LifelineDuplicatedException {
        for (String interactionId : interactions.keySet()) {
            if (interactions.get(interactionId).getName().
                    equals(interactionName)) {
                MyUmlInteraction tarInteraction =
                        interactions.get(interactionId);
                if (!tarInteraction.getLifelineDupMap().
                        containsKey(lifelineName)) {
                    throw new LifelineNotFoundException(
                            interactionName, lifelineName);
                }
                if (tarInteraction.getLifelineDupMap().get(lifelineName) > 1) {
                    throw new LifelineDuplicatedException(
                            interactionName, lifelineName);
                }
                for (String lifelineId : tarInteraction.
                        getLifelines().keySet()) {
                    if (tarInteraction.getLifelines().get(lifelineId).
                            getName().equals(lifelineName)) {
                        return tarInteraction.getLifelines().
                                get(lifelineId).getInMessages().size();
                    }
                }
            }
        }
        System.out.println("MISSING REQUIRED MESSAGECOUNT!");
        return -1;
    }
    
    // innate methods
    
    public HashMap<String, Integer> getInteractionDupMap() {
        return interactionDupMap;
    }
    
    public HashMap<String, MyUmlInteraction> getInteractions() {
        return interactions;
    }
    
    public HashMap<String, MyUmlLifeline> getLifelines() {
        return lifelines;
    }
    
    public HashMap<String, MyUmlMessage> getMessages() {
        return messages;
    }
}
