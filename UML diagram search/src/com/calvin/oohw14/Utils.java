package com.calvin.oohw14;

import com.calvin.oohw14.elements.MyUmlAttribute;
import com.calvin.oohw14.elements.MyUmlClass;
import com.calvin.oohw14.elements.MyUmlClassOrInterface;
import com.calvin.oohw14.elements.MyUmlInterface;
// import com.calvin.oohw14.elements.MyUmlStates;
import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule009Exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
// import java.util.LinkedList;
import java.util.Stack;

public class Utils {
    private HashMap<String, MyUmlClass> classes;
    private HashMap<String, MyUmlInterface> interfaces;
    private HashMap<String, MyUmlAttribute> attributes;
    
    public Utils(HashMap<String, MyUmlClass> classes, HashMap<String,
            MyUmlInterface> interfaces,
                 HashMap<String, MyUmlAttribute> attributes) {
        this.classes = classes;
        this.interfaces = interfaces;
        this.attributes = attributes;
    }
    
    public void dfsInterface(String itfId, HashSet<String> interfaceIds,
                             ArrayList<String> interfaceList,
                             HashMap<String, MyUmlInterface> interfaces) {
        if (!interfaces.containsKey(itfId)) {
            return;
        }
        if (!interfaceIds.contains(itfId)) {
            interfaceIds.add(itfId);
            interfaceList.add(interfaces.get(
                    itfId).getName());
        }
        HashMap<String, MyUmlInterface> fatherItfs =
                interfaces.get(itfId).getFather();
        for (String fatherId : fatherItfs.keySet()) {
            dfsInterface(fatherId, interfaceIds, interfaceList, interfaces);
        }
    }
    
    public boolean dfsLoopInterface(String itfId, String tarId,
                                    HashSet<String> visitedItf,
                                    HashMap<String, MyUmlInterface> itfs) {
        boolean ret = false;
        for (String nextItfId : itfs.get(itfId).getFather().keySet()) {
            if (nextItfId.equals(tarId)) {
                return true;
            }
            if (visitedItf.contains(nextItfId)) {
                continue;
            } else {
                visitedItf.add(nextItfId);
            }
            ret = dfsLoopInterface(nextItfId, tarId, visitedItf, itfs);
            if (ret) {
                break;
            }
        }
        return ret;
    }
    
    public void dfsLoopInterface(String itfId, HashSet<String> visitItf,
                             HashSet<String> dupItfes,
                                 HashSet<String> curVisitItf,
                                 HashMap<String, MyUmlInterface> interfaces) {
        // 时间戳
        HashMap<String, Integer> dfnStamp = new HashMap<>();
        HashMap<String, Integer> lowStamp = new HashMap<>();
        Stack<String> stack = new Stack<>();
        
        
        if (!interfaces.containsKey(itfId)) {
            return;
        }
        if (!curVisitItf.contains(itfId)) {
            visitItf.add(itfId);
            curVisitItf.add(itfId);
        } else {
            dupItfes.add(itfId);
        }
        HashMap<String, MyUmlInterface> fatherItfs =
                interfaces.get(itfId).getFather();
        for (String fatherId : fatherItfs.keySet()) {
            dfsLoopInterface(fatherId, visitItf,
                    dupItfes, curVisitItf, interfaces);
        }
    }
    
    public boolean dfsDupInterface(String itfId, HashSet<String> curVisitItf,
             HashSet<String> dupClsItf, HashMap<String,
            MyUmlInterface> interfaces) {
        if (!interfaces.containsKey(itfId)) {
            return false;
        }
        if (dupClsItf.contains(itfId)) {
            return true;
        }
        if (!curVisitItf.contains(itfId)) {
            curVisitItf.add(itfId);
        } else {
            return true;
        }
        HashMap<String, MyUmlInterface> fatherItfs =
                interfaces.get(itfId).getFather();
        boolean ret = false;
        for (String fatherId : fatherItfs.keySet()) {
            ret = dfsDupInterface(fatherId, curVisitItf, dupClsItf, interfaces);
            if (ret) {
                return true;
            }
        }
        return ret;
    }
    
    public void checkAttrEndDup() throws UmlRule002Exception {
        HashSet<AttributeClassInformation> dupAttrNameSet = new HashSet<>();
        HashSet<AttributeClassInformation> containsAtrNameSet = new HashSet<>();
        for (String classId : classes.keySet()) {
            MyUmlClass curClass = classes.get(classId);
            for (String attrId : curClass.getAttributes().keySet()) {
                AttributeClassInformation tmp = new AttributeClassInformation(
                        attributes.get(attrId).getName(), curClass.getName());
                if (containsAtrNameSet.contains(tmp)) {
                    dupAttrNameSet.add(tmp);
                } else {
                    containsAtrNameSet.add(tmp);
                }
            }
            for (String oppoEndClass : curClass.
                    getAssociatedOppoEndName().keySet()) {
                for (String oppoEndName : curClass.
                        getAssociatedOppoEndName().get(oppoEndClass)) {
                    if (oppoEndName == null) {
                        continue;
                    }
                    AttributeClassInformation tmp = new
                            AttributeClassInformation(
                            oppoEndName, curClass.getName());
                    if (containsAtrNameSet.contains(tmp)) {
                        dupAttrNameSet.add(tmp);
                    } else {
                        containsAtrNameSet.add(tmp);
                    }
                }
            }
        }
        if (!dupAttrNameSet.isEmpty()) {
            throw new UmlRule002Exception(dupAttrNameSet);
        }
    }
    
    public void checkLoopExtension() throws UmlRule008Exception {
        // 对每个类或者接口遍历
        HashSet<MyUmlClassOrInterface> loopClsItf = new HashSet<>();
        HashSet<String> dupClasses = new HashSet<>();
        HashSet<String> visitClass = new HashSet<>();
        for (String classId : classes.keySet()) {
            HashSet<String> curVisitClass = new HashSet<>();
            /*if (visitClass.contains(classId)) {
                continue;
            }*/
            String tmpId = classes.get(classId).getFather();
            while (classes.containsKey(tmpId) &&
                    !curVisitClass.contains(tmpId)) {
                if (tmpId.equals(classId)) {
                    dupClasses.add(classId);
                    loopClsItf.add(new MyUmlClassOrInterface(
                            classes.get(classId)));
                    break;
                } else {
                    curVisitClass.add(tmpId);
                }
                tmpId = classes.get(tmpId).getFather();
            }
        }
        HashSet<String> dupItfes = new HashSet<>();
        for (String itfId : interfaces.keySet()) {
            String tmpId = new String(itfId);
            boolean ret = dfsLoopInterface(
                    tmpId, tmpId, new HashSet<String>(), interfaces);
            if (ret) {
                dupItfes.add(itfId);
                loopClsItf.add(new MyUmlClassOrInterface(
                        interfaces.get(itfId)));
            }
        }
        if (!loopClsItf.isEmpty()) {
            throw new UmlRule008Exception(loopClsItf);
        }
    }
    
    public void checkDupRealization() throws UmlRule009Exception {
        HashSet<MyUmlClassOrInterface> dupClsItf = new HashSet<>();
        // 先遍历所有类 对每个类或者接口遍历
        HashSet<String> dupClsItfId = new HashSet<>();
    
        for (String itfId : interfaces.keySet()) {
            HashSet<String> curVisitClaItf = new HashSet<>();
            boolean res = false;
            res = dfsDupInterface(itfId,
                    curVisitClaItf, dupClsItfId, interfaces);
            if (res) {
                dupClsItfId.add(itfId);
                dupClsItf.add(new MyUmlClassOrInterface(interfaces.get(itfId)));
            }
        }
        
        for (String classId : classes.keySet()) {
            HashSet<String> curVisitClsItf = new HashSet<>();
            String tmpClassId = new String(classId);
            boolean res = false;
            // 子类递归到父类
            while (classes.containsKey(tmpClassId)) {
                // 自己实现的
                MyUmlClass tarClass = classes.get(tmpClassId);
                HashMap<String, MyUmlInterface> implementInterfaces =
                        tarClass.getInterfaces();
                for (String interfaceId : implementInterfaces.keySet()) {
                    // 接口的多继承
                    if (interfaceId.equals(MyString.DUPCLSIMPITF)) {
                        dupClsItfId.add(tarClass.getId());
                        dupClsItf.add(new MyUmlClassOrInterface(
                                classes.get(classId)));
                        res = true;
                        break;
                    }
                    res = dfsDupInterface(interfaceId,
                            curVisitClsItf, dupClsItfId, interfaces);
                    if (res) {
                        dupClsItfId.add(tarClass.getId());
                        dupClsItf.add(new MyUmlClassOrInterface(
                                classes.get(classId)));
                        break;
                    }
                }
                if (res) {
                    break;
                }
                tmpClassId = tarClass.getFather();
            }
        }
        
        if (!dupClsItf.isEmpty()) {
            throw new UmlRule009Exception(dupClsItf);
        }
    }
    
}
