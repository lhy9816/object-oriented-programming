package com.calvin.oohw14.elements;

import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlInterface;

import java.util.HashMap;

public class MyUmlInterface extends MyUmlElement {
    
    private Visibility visibility;
    private HashMap<String, MyUmlInterface> father;
    private String child;
    
    public MyUmlInterface(UmlInterface element) {
        super(element);
        this.visibility = element.getVisibility();
        this.father = new HashMap<>();
        this.child = "";
    }
    
    public String getChild() {
        return child;
    }
    
    public HashMap<String, MyUmlInterface> getFather() {
        return father;
    }
    
    public void addFather(String fatherId, MyUmlInterface fatherItf) {
        father.put(fatherId, fatherItf);
    }
    
    public void setChild(String child) {
        this.child = child;
    }
}
