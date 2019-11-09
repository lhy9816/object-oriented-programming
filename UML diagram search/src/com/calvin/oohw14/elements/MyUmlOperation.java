package com.calvin.oohw14.elements;

import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlOperation;

import java.util.HashMap;

public class MyUmlOperation extends MyUmlElement {
    private Visibility visibility;
    private HashMap<String, MyUmlParameter> parameters;
    private int returnNum;
    private int inNum;
    
    public MyUmlOperation(UmlOperation element) {
        super(element);
        this.visibility = element.getVisibility();
        this.parameters = new HashMap<>();
        this.returnNum = 0;
        this.inNum = 0;
    }
    
    public Visibility getVisibility() {
        return visibility;
    }
    
    public HashMap<String, MyUmlParameter> getParameters() {
        return parameters;
    }
    
    public void addReturnNum() {
        returnNum++;
    }
    
    public void addInNum() {
        inNum++;
    }
    
    public int getInNum() {
        return inNum;
    }
    
    public int getReturnNum() {
        return returnNum;
    }
}
