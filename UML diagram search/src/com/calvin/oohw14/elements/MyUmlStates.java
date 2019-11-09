package com.calvin.oohw14.elements;

import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlFinalState;
import com.oocourse.uml2.models.elements.UmlPseudostate;
import com.oocourse.uml2.models.elements.UmlState;

import java.util.HashMap;

public class MyUmlStates extends MyUmlElement {
    
    private Visibility visibility;
    private HashMap<String, MyUmlStates> brothers;
    
    public MyUmlStates(UmlState element) {
        super(element);
        this.visibility = element.getVisibility();
        this.brothers = new HashMap<>();
    }
    
    public MyUmlStates(UmlPseudostate element) {
        super(element);
        this.setName("Pseudo" + this.getId());
        this.visibility = element.getVisibility();
        this.brothers = new HashMap<>();
    }
    
    public MyUmlStates(UmlFinalState element) {
        super(element);
        this.setName("Finals" + this.getId());
        this.visibility = element.getVisibility();
        this.brothers = new HashMap<>();
    }
    
    public HashMap<String, MyUmlStates> getBrothers() {
        return brothers;
    }
    
    public Visibility getVisibility() {
        return visibility;
    }
}
