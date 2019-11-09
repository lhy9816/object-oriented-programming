package com.calvin.oohw14.elements;

import com.oocourse.uml2.models.common.Aggregation;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;

public class MyUmlAssociationEnd {
    
    private String parent;
    private String id;
    private String name;
    private ElementType type;
    private String reference;
    private Aggregation aggregation;
    private Visibility visibility;
    
    public MyUmlAssociationEnd(UmlAssociationEnd element) {
        this.parent = element.getParentId();
        this.id = element.getId();
        this.name = element.getName();
        this.type = element.getElementType();
        this.reference = element.getReference();
        this.aggregation = element.getAggregation();
        this.visibility = element.getVisibility();
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
    
    public Aggregation getAggregation() {
        return aggregation;
    }
    
    public String getReference() {
        return reference;
    }
    
    public Visibility getVisibility() {
        return visibility;
    }
}

