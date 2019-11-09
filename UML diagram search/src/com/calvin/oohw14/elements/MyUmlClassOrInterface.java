package com.calvin.oohw14.elements;

import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlClassOrInterface;

import java.util.Map;

public class MyUmlClassOrInterface implements UmlClassOrInterface {
    private Visibility visibility;
    private ElementType elementType;
    private String id;
    private String name;
    private String parent;
    
    public MyUmlClassOrInterface(MyUmlElement element) {
        this.visibility = Visibility.PUBLIC;
        this.elementType = element.getType();
        this.id = element.getId();
        this.name = element.getName();
        this.parent = element.getParent();
    }
    
    @Override
    public Visibility getVisibility() {
        return visibility;
    }
    
    @Override
    public ElementType getElementType() {
        return elementType;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getParentId() {
        return parent;
    }
    
    @Override
    public Map<String, Object> toJson() {
        return null;
    }
}
