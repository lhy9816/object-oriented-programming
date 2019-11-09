package com.calvin.oohw14;

import com.calvin.oohw14.elements.MyUmlAssociation;
import com.calvin.oohw14.elements.MyUmlAssociationEnd;
import com.calvin.oohw14.elements.MyUmlAttribute;
import com.calvin.oohw14.elements.MyUmlClass;
// import com.calvin.oohw14.elements.MyUmlClassOrInterface;
import com.calvin.oohw14.elements.MyUmlGeneralization;
import com.calvin.oohw14.elements.MyUmlInterface;
import com.calvin.oohw14.elements.MyUmlInterfaceRealization;
import com.calvin.oohw14.elements.MyUmlOperation;
import com.calvin.oohw14.elements.MyUmlParameter;
import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.common.AttributeQueryType;
// import com.oocourse.uml2.interact.exceptions.user.UmlRule002Exception;
// import com.oocourse.uml2.interact.exceptions.user.UmlRule008Exception;
// import com.oocourse.uml2.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml2.models.common.Direction;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAssociation;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInterface;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import java.util.List;

public class ClassDataBase {
    private ArrayList<UmlElement> umlElements;
    private HashMap<String, Integer> classDupMap;
    private HashMap<String, MyUmlClass> classes;
    private HashMap<String, MyUmlInterface> interfaces;
    private HashMap<String, MyUmlParameter> parameters;
    private HashMap<String, MyUmlAttribute> attributes;
    private HashMap<String, MyUmlOperation> operations;
    private HashMap<String, MyUmlAssociation> associations;
    private HashMap<String, MyUmlAssociationEnd> associationEnd;
    private HashMap<String, MyUmlGeneralization> generalizations;
    private HashMap<String, MyUmlInterfaceRealization> interfaceRealizations;
    private HashSet<String> dupInterfaces;
    private Utils utils;
    
    public ClassDataBase(ArrayList<UmlElement> elements) {
        // parse input
        this.umlElements = elements;
        this.classDupMap = new HashMap<>();
        this.classes = new HashMap<String, MyUmlClass>();
        this.interfaces = new HashMap<String, MyUmlInterface>();
        this.parameters = new HashMap<String, MyUmlParameter>();
        this.attributes = new HashMap<>();
        this.operations = new HashMap<>();
        this.associations = new HashMap<>();
        this.associationEnd = new HashMap<>();
        this.generalizations = new HashMap<>();
        this.interfaceRealizations = new HashMap<>();
        this.dupInterfaces = new HashSet<>();
        parseInput();
        linkClassesAndInterfaces();
        linkClassInterface();
        linkAssociation();
        linkAttrClass();
        linkParaOper();
        linkOperClass();
        this.utils = new Utils(classes, interfaces, attributes);
    }
    
    public void addDupClassNum(String className) {
        if (classDupMap.containsKey(className)) {
            classDupMap.put(className, classDupMap.get(className) + 1);
        } else {
            classDupMap.put(className, 1);
        }
    }
    
    public void parseInput() {
        for (int i = 0; i < umlElements.size(); ++i) {
            ElementType type = umlElements.get(i).getElementType();
            if (type.equals(ElementType.UML_CLASS)) {
                MyUmlClass umlClass = new
                        MyUmlClass((UmlClass) umlElements.get(i));
                classes.put(umlClass.getId(), umlClass);
                addDupClassNum(umlClass.getName());
            } else if (type.equals(ElementType.UML_INTERFACE)) {
                MyUmlInterface umlInterface =
                        new MyUmlInterface((UmlInterface) umlElements.get(i));
                interfaces.put(umlInterface.getId(), umlInterface);
            } else if (type.equals(ElementType.UML_OPERATION)) {
                MyUmlOperation umlOperation =
                        new MyUmlOperation((UmlOperation) umlElements.get(i));
                operations.put(umlOperation.getId(), umlOperation);
            } else if (type.equals(ElementType.UML_ATTRIBUTE)) {
                MyUmlAttribute umlAttribute =
                        new MyUmlAttribute((UmlAttribute) umlElements.get(i));
                attributes.put(umlAttribute.getId(), umlAttribute);
            } else if (type.equals(ElementType.UML_PARAMETER)) {
                MyUmlParameter umlParameter =
                        new MyUmlParameter((UmlParameter) umlElements.get(i));
                parameters.put(umlParameter.getId(), umlParameter);
            } else if (type.equals(ElementType.UML_ASSOCIATION)) {
                MyUmlAssociation umlAssociation =
                        new MyUmlAssociation((UmlAssociation)
                                umlElements.get(i));
                associations.put(umlAssociation.getId(), umlAssociation);
            } else if (type.equals(ElementType.UML_ASSOCIATION_END)) {
                MyUmlAssociationEnd umlAssociationEnd =
                        new MyUmlAssociationEnd(
                                (UmlAssociationEnd) umlElements.get(i));
                associationEnd.put(umlAssociationEnd.getId(),
                        umlAssociationEnd);
            } else if (type.equals(ElementType.UML_GENERALIZATION)) {
                MyUmlGeneralization umlGeneralization =
                        new MyUmlGeneralization(
                                (UmlGeneralization) umlElements.get(i));
                generalizations.put(
                        umlGeneralization.getId(), umlGeneralization);
            } else if (type.equals(ElementType.UML_INTERFACE_REALIZATION)) {
                MyUmlInterfaceRealization umlInterfaceRealization =
                        new MyUmlInterfaceRealization(
                                (UmlInterfaceRealization) umlElements.get(i));
                interfaceRealizations.put(
                        umlInterfaceRealization.getId(),
                        umlInterfaceRealization);
            }
        }
    }
    
    public MyUmlClass getClassByName(String className) {
        for (String classId : classes.keySet()) {
            if (classes.get(classId).getName().equals(className)) {
                return classes.get(classId);
            }
        }
        System.out.println("SEARCH FOR EMPTY CLASS!");
        return null;
    }
    
    public int getAttrNumByName(String className,
                                AttributeQueryType queryType) {
        MyUmlClass tarClass = getClassByName(className);
        if (queryType.equals(AttributeQueryType.SELF_ONLY)) {
            return tarClass.getAttributes().size();
        } else {
            int tmp = 0;
            while (classes.containsKey(tarClass.getId())) {
                tmp += tarClass.getAttributes().size();
                if (!classes.containsKey(tarClass.getFather())) {
                    break;
                }
                tarClass = classes.get(tarClass.getFather());
            }
            return tmp;
        }
    }
    
    public String getTopClassName(String className) {
        MyUmlClass tarClass = getClassByName(className);
        while (classes.containsKey(tarClass.getFather())) {
            tarClass = classes.get(tarClass.getFather());
        }
        return tarClass.getName();
    }
    
    public List<AttributeClassInformation> getNotHiddenClassAttr(
            String className) {
        MyUmlClass tarClass = getClassByName(className);
        ArrayList<AttributeClassInformation> notHiddenClassAttr
                = new ArrayList<>();
        while (classes.containsKey(tarClass.getId())) {
            for (String attrName : tarClass.getNotHiddenAttr()) {
                AttributeClassInformation aci = new AttributeClassInformation(
                        attrName, tarClass.getName());
                notHiddenClassAttr.add(aci);
            }
            if (!classes.containsKey(tarClass.getFather())) {
                break;
            }
            tarClass = classes.get(tarClass.getFather());
        }
        return notHiddenClassAttr;
    }
    
    public ArrayList<String> getImplementInterfaces(String className) {
        MyUmlClass tarClass = getClassByName(className);
        ArrayList<String> interfaceLists = new ArrayList<>();
        HashSet<String> interfaceIds = new HashSet<>();
        // 子类递归到父类
        while (classes.containsKey(tarClass.getId())) {
            // 自己实现的
            HashMap<String, MyUmlInterface> implementInterfaces =
                    tarClass.getInterfaces();
            for (String interfaceId : implementInterfaces.keySet()) {
                MyUmlInterface tarInterface = interfaces.get(interfaceId);
                // 接口的多继承
                utils.dfsInterface(interfaceId,
                        interfaceIds, interfaceLists, interfaces);
            }
            if (!classes.containsKey(tarClass.getFather())) {
                break;
            }
            tarClass = classes.get(tarClass.getFather());
        }
        return interfaceLists;
    }
    
    public int getClassAssoCount(String className) {
        MyUmlClass tarClass = getClassByName(className);
        int tmp = 0;
        while (classes.containsKey(tarClass.getId())) {
            tmp += tarClass.getAssociatedNum();
            if (!classes.containsKey(tarClass.getFather())) {
                break;
            }
            tarClass = classes.get(tarClass.getFather());
        }
        return tmp;
    }
    
    public ArrayList<String> getClassAssoClassList(String className) {
        MyUmlClass tarClass = getClassByName(className);
        HashSet<String> tarId = new HashSet<>();
        while (classes.containsKey(tarClass.getId())) {
            tarId.addAll(tarClass.getAssociatedSet());
            if (!classes.containsKey(tarClass.getFather())) {
                break;
            }
            tarClass = classes.get(tarClass.getFather());
        }
        
        ArrayList<String> tarList = new ArrayList<>();
        for (String id : tarId) {
            tarList.add(classes.get(id).getName());
        }
        return tarList;
    }
    
    public void linkClassesAndInterfaces() {
        for (String geneId : generalizations.keySet()) {
            String father = generalizations.get(geneId).getTarget();
            String child = generalizations.get(geneId).getSource();
            if (classes.containsKey(father)) {
                classes.get(father).setChild(child);
                classes.get(child).setFather(father);
            } else {
                interfaces.get(father).setChild(child);
                interfaces.get(child).addFather(father, interfaces.get(father));
            }
            
        }
    }
    
    public void linkClassInterface() {
        for (String interRealId : interfaceRealizations.keySet()) {
            String fatherInterface = interfaceRealizations.get(
                    interRealId).getTarget();
            String realizingClass = interfaceRealizations.get(
                    interRealId).getSource();
            if (classes.get(realizingClass).getInterfaces().
                    containsKey(fatherInterface)) {
                classes.get(realizingClass).getInterfaces().put(
                        MyString.DUPCLSIMPITF, interfaces.get(fatherInterface));
            }
            classes.get(realizingClass).getInterfaces().put(
                    fatherInterface, interfaces.get(fatherInterface));
        }
    }
    
    public void linkAssociation() {
        // 先把每个类的associated number加上
        for (String end : associationEnd.keySet()) {
            String reference = associationEnd.get(end).getReference();
            if (classes.containsKey(reference)) {
                classes.get(reference).addAssociatedNum();
            }
        }
        // 将每一个类都与连到对应的关联类上
        for (String assoId : associations.keySet()) {
            String end1Id = associations.get(assoId).getEnd1();
            String end2Id = associations.get(assoId).getEnd2();
            String element1Id = associationEnd.get(end1Id).getReference();
            String element2Id = associationEnd.get(end2Id).getReference();
            // 增加对关联对端的名称判重
            if (classes.containsKey(element1Id)) {
                MyUmlClass class1 = classes.get(element1Id);
                String end2Name = associationEnd.get(end2Id).getName();
                if (!class1.getAssociatedOppoEndName().
                        containsKey(element2Id)) {
                    ArrayList<String> tmp = new ArrayList<>();
                    tmp.add(end2Name);
                    class1.getAssociatedOppoEndName().put(element2Id, tmp);
                } else {
                    class1.getAssociatedOppoEndName().
                            get(element2Id).add(end2Name);
                }
            }
            if (classes.containsKey(element2Id)) {
                String end1Name = associationEnd.get(end1Id).getName();
                MyUmlClass class2 = classes.get(element2Id);
                if (class2.getAssociatedOppoEndName().containsKey(element1Id)) {
                    class2.getAssociatedOppoEndName().
                            get(element1Id).add(end1Name);
                } else {
                    ArrayList<String> tmp = new ArrayList<>();
                    tmp.add(end1Name);
                    class2.getAssociatedOppoEndName().put(element1Id, tmp);
                }
            }
            if (classes.containsKey(element1Id) &&
                    classes.containsKey(element2Id)) {
                MyUmlClass class1 = classes.get(element1Id);
                MyUmlClass class2 = classes.get(element2Id);
                if (!class1.getAssociatedSet().contains(element2Id)) {
                    class1.getAssociatedClasses().add(class2.getName());
                    class1.getAssociatedSet().add(element2Id);
                }
                if (!class2.getAssociatedSet().contains(element1Id)) {
                    class2.getAssociatedClasses().add(class1.getName());
                    class2.getAssociatedSet().add(element1Id);
                }
            }
        }
    }
    
    public void linkAttrClass() {
        for (String attrId : attributes.keySet()) {
            MyUmlAttribute curAttr = attributes.get(attrId);
            MyUmlClass tarClass = classes.get(curAttr.getParent());
            if (tarClass != null) {
                tarClass.getAttributes().put(attrId, curAttr);
                tarClass.addAttributeVisiMap(curAttr.getVisibility(),
                        curAttr.getId());
                if (curAttr.getVisibility() != Visibility.PRIVATE) {
                    tarClass.addNotHiddenAttr(curAttr.getName());
                }
            }
        }
    }
    
    public void linkParaOper() {
        for (String paraId : parameters.keySet()) {
            MyUmlParameter curPara = parameters.get(paraId);
            MyUmlOperation tarOper = operations.get(curPara.getParent());
            tarOper.getParameters().put(paraId, curPara);
            if (curPara.getDirection().equals(Direction.IN)) {
                tarOper.addInNum();
            } else if (curPara.getDirection().equals(Direction.RETURN)) {
                tarOper.addReturnNum();
            }
        }
    }
    
    public void linkOperClass() {
        for (String operId : operations.keySet()) {
            MyUmlOperation curOper = operations.get(operId);
            MyUmlClass tarClass = classes.get(curOper.getParent());
            if (tarClass != null) {
                tarClass.getOperations().put(operId, curOper);
                tarClass.addOperationVisiMap(curOper.getVisibility(),
                        curOper.getId());
                if (curOper.getInNum() > 0) {
                    tarClass.addInParaOperNum();
                }
                if (curOper.getReturnNum() > 0) {
                    tarClass.addRetParaOperNum();
                }
            }
        }
    }
    
    public HashMap<String, Integer> getClassDupMap() {
        return classDupMap;
    }
    
    public HashMap<String, MyUmlAttribute> getAttributes() {
        return attributes;
    }
    
    public HashMap<String, MyUmlClass> getClasses() {
        return classes;
    }
    
    public HashMap<String, MyUmlOperation> getOperations() {
        return operations;
    }
    
    public HashMap<String, MyUmlParameter> getParameters() {
        return parameters;
    }
    
    public Utils getUtils() {
        return utils;
    }
}
