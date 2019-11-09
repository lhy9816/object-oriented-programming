package com.calvin.oohw14.elements;

import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlMessage;

public class MyUmlMessage extends MyUmlElement {
    private Visibility visibility;
    private String source;
    private String target;
    
    public MyUmlMessage(UmlMessage element) {
        super(element);
        this.visibility = element.getVisibility();
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
