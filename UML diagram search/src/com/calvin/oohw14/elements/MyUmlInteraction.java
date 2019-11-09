package com.calvin.oohw14.elements;

import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlInteraction;

import java.util.HashMap;
import java.util.HashSet;

public class MyUmlInteraction extends MyUmlElement {
    private Visibility visibility;
    private HashMap<String, MyUmlLifeline> lifelines;
    private HashSet<String> attributes;
    private HashMap<String, MyUmlMessage> messages;
    private HashMap<String, Integer> lifelineDupMap;
    
    public MyUmlInteraction(UmlInteraction element) {
        super(element);
        this.visibility = element.getVisibility();
        this.lifelines = new HashMap<>();
        this.attributes = new HashSet<>();
        this.messages = new HashMap<>();
        this.lifelineDupMap = new HashMap<>();
    }
    
    public void addLifelineDupMap(String lifelineName) {
        if (!lifelineDupMap.containsKey(lifelineName)) {
            lifelineDupMap.put(lifelineName, 1);
        } else {
            lifelineDupMap.put(lifelineName,
                    lifelineDupMap.get(lifelineName) + 1);
        }
    }
    
    public void addAttributes(String attributeId) {
        attributes.add(attributeId);
    }
    
    public HashSet<String> getAttributes() {
        return attributes;
    }
    
    public HashMap<String, MyUmlLifeline> getLifelines() {
        return lifelines;
    }
    
    public void addMessages(String messageId, MyUmlMessage message) {
        messages.put(messageId, message);
    }
    
    public HashMap<String, MyUmlMessage> getMessages() {
        return messages;
    }
    
    public HashMap<String, Integer> getLifelineDupMap() {
        return lifelineDupMap;
    }
}
