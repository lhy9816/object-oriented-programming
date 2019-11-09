package com.calvin.oohw14.elements;

import com.oocourse.uml2.interact.common.OperationQueryType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MyUmlClass extends MyUmlElement {
    private Visibility visibility;
    private String father;
    private String child;
    private HashMap<String, MyUmlOperation> operations;
    private HashMap<String, MyUmlAttribute> attributes;
    private HashMap<Visibility, HashSet<String>> attributeVisiMap;
    private HashMap<String, HashMap<Visibility, Integer>> operationVisiMap;
    private HashSet<String> notHiddenAttr;       // violate attribute type set
    private ArrayList<String> associatedClasses;
    private HashSet<String> associatedSet;
    private HashMap<String, ArrayList<String>> associatedOppoEndName;
    private HashMap<String, MyUmlInterface> interfaces;
    private int retOperNum;
    private int inParaOperNum;
    private int associatedNum;
    
    public MyUmlClass(UmlClass element) {
        super(element);
        this.father = "";
        this.child = "";
        this.visibility = element.getVisibility();
        this.operations = new HashMap<>();
        this.attributes = new HashMap<>();
        this.attributeVisiMap = new HashMap<>();
        this.operationVisiMap = new HashMap<>();
        this.notHiddenAttr = new HashSet<>();
        this.associatedClasses = new ArrayList<>();
        this.associatedSet = new HashSet<>();
        this.associatedOppoEndName = new HashMap<>();
        this.interfaces = new HashMap<>();
        this.retOperNum = 0;
        this.inParaOperNum = 0;
        this.associatedNum = 0;
    }
    
    public void addAttributeVisiMap(Visibility visibility, String attrId) {
        if (attributeVisiMap.containsKey(visibility)) {
            attributeVisiMap.get(visibility).add(attrId);
        } else {
            HashSet<String> visibleMap = new HashSet<String>();
            visibleMap.add(attrId);
            attributeVisiMap.put(visibility, visibleMap);
        }
    }
    
    public void addOperationVisiMap(Visibility visibility, String operId) {
        MyUmlOperation tarOper = operations.get(operId);
        if (operationVisiMap.containsKey(tarOper.getName())) {
            if (operationVisiMap.get(tarOper.getName()).
                    containsKey(visibility)) {
                int oriCount = operationVisiMap.get(
                        tarOper.getName()).get(visibility);
                operationVisiMap.get(tarOper.getName()).put(
                        visibility, oriCount + 1);
            } else {
                operationVisiMap.get(tarOper.getName()).put(visibility, 1);
            }
        } else {
            HashMap<Visibility, Integer> visibleMap = new HashMap<>();
            visibleMap.put(visibility, 1);
            operationVisiMap.put(tarOper.getName(), visibleMap);
        }
    }
    
    public int getOperNumByType(OperationQueryType queryType) {
        if (queryType.equals(OperationQueryType.ALL)) {
            return operations.size();
        } else if (queryType.equals(OperationQueryType.PARAM)) {
            return inParaOperNum;
        } else if (queryType.equals(OperationQueryType.RETURN)) {
            return retOperNum;
        } else if (queryType.equals(OperationQueryType.NON_PARAM)) {
            return operations.size() - inParaOperNum;
        } else if (queryType.equals(OperationQueryType.NON_RETURN)) {
            return operations.size() - retOperNum;
        } else {
            System.out.println("Unknown QueryType!");
            return 0;
        }
    }
    
    public HashMap<Visibility, Integer> getOperVisibility(String operName) {
        if (!operationVisiMap.containsKey(operName)) {
            return new HashMap<Visibility, Integer>() {
                {
                    put(Visibility.PUBLIC, 0);
                    put(Visibility.PRIVATE, 0);
                    put(Visibility.PROTECTED, 0);
                    put(Visibility.PACKAGE, 0);
                }
            };
        }
        return operationVisiMap.get(operName);
    }
    
    public Visibility getAttrVisibility(String attrName) {
        for (String attrId : attributes.keySet()) {
            if (attributes.get(attrId).getName().equals(attrName)) {
                return attributes.get(attrId).getVisibility();
            }
        }
        return null;
    }
    
    // 类的属性可见性
    public void addNotHiddenAttr(String attr) {
        notHiddenAttr.add(attr);
    }
    
    public void addAssociatedNum() {
        associatedNum++;
    }
    
    // innate methods
    public String getChild() {
        return child;
    }
    
    public String getFather() {
        return father;
    }
    
    public void setChild(String child) {
        this.child = child;
    }
    
    public void setFather(String father) {
        this.father = father;
    }
    
    public Visibility getVisibility() {
        return visibility;
    }
    
    public HashMap<String, MyUmlInterface> getInterfaces() {
        return interfaces;
    }
    
    public HashMap<String, MyUmlOperation> getOperations() {
        return operations;
    }
    
    public HashMap<String, MyUmlAttribute> getAttributes() {
        return attributes;
    }
    
    public HashMap<Visibility, HashSet<String>> getAttributeVisiMap() {
        return attributeVisiMap;
    }
    
    public HashMap<String, HashMap<Visibility, Integer>> getOperationVisiMap() {
        return operationVisiMap;
    }
    
    public HashSet<String> getNotHiddenAttr() {
        return notHiddenAttr;
    }
    
    public ArrayList<String> getAssociatedClasses() {
        return associatedClasses;
    }
    
    public HashSet<String> getAssociatedSet() {
        return associatedSet;
    }
    
    public HashMap<String, ArrayList<String>> getAssociatedOppoEndName() {
        return associatedOppoEndName;
    }
    
    public int getAssociatedNum() {
        return associatedNum;
    }
    
    public void addInParaOperNum() {
        inParaOperNum++;
    }
    
    public void addRetParaOperNum() {
        retOperNum++;
    }
}
