package com.calvin.oohw14.elements;

import com.oocourse.uml2.models.elements.UmlInterfaceRealization;

public class MyUmlInterfaceRealization extends MyUmlElement {
    
    private String source;
    private String target;
    
    public MyUmlInterfaceRealization(UmlInterfaceRealization element) {
        super(element);
        this.source = element.getSource();
        this.target = element.getTarget();
    }
    
    public String getSource() {
        return source;
    }
    
    public String getTarget() {
        return target;
    }
}
