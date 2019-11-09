package com.calvin.oohw14.elements;

import com.oocourse.uml2.models.common.Direction;
import com.oocourse.uml2.models.common.NameableType;
import com.oocourse.uml2.models.elements.UmlParameter;

public class MyUmlParameter extends MyUmlElement {
    private NameableType dataType;
    private Direction direction;
    
    public MyUmlParameter(UmlParameter element) {
        super(element);
        this.direction = element.getDirection();
        this.dataType = element.getType();
    }
    
    public Direction getDirection() {
        return direction;
    }
}
