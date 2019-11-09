package com.calvin.oohw14.elements;

import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlLifeline;

import java.util.HashSet;

public class MyUmlLifeline extends MyUmlElement {
    private Visibility visibility;
    private HashSet<String> inMessages;
    private String represent;
    
    public MyUmlLifeline(UmlLifeline element) {
        super(element);
        this.visibility = element.getVisibility();
        this.inMessages = new HashSet<>();
        this.represent = element.getRepresent();
    }
    
    public HashSet<String> getInMessages() {
        return inMessages;
    }
    
    public String getRepresent() {
        return represent;
    }
}