package com.calvin.oohw14.elements;

import com.oocourse.uml2.models.common.NameableType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAttribute;

public class MyUmlAttribute extends MyUmlElement {
    private NameableType dataType;
    private Visibility visibility;
    
    public MyUmlAttribute(UmlAttribute element) {
        super(element);
        this.dataType = element.getType();
        this.visibility = element.getVisibility();
    }
    
    public Visibility getVisibility() {
        return visibility;
    }
    
    public NameableType getDataType() {
        return dataType;
    }
}
