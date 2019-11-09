package com.calvin.oohw14.elements;

import com.oocourse.uml2.models.elements.UmlAssociation;

public class MyUmlAssociation extends MyUmlElement {
    private String end1;
    private String end2;
    
    public MyUmlAssociation(UmlAssociation element) {
        super(element);
        this.end1 = element.getEnd1();
        this.end2 = element.getEnd2();
    }
    
    public String getEnd1() {
        return end1;
    }
    
    public String getEnd2() {
        return end2;
    }
}
