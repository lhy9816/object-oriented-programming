package com.calvin.oohw14.elements;

import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.elements.UmlElement;

public class MyUmlElement {
    private String parent;
    private String id;
    private String name;
    private ElementType type;
    
    public MyUmlElement(UmlElement element) {
        this.parent = element.getParentId();
        this.id = element.getId();
        this.name = element.getName();
        this.type = element.getElementType();
    }
    
    public ElementType getType() {
        return type;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getParent() {
        return parent;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
