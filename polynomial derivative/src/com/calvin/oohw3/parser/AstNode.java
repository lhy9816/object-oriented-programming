package com.calvin.oohw3.parser;

import com.calvin.oohw3.enums.TermType;

public class AstNode implements Cloneable {
    private TermType termType;
    private AstNode parentNode;
    private AstNode leftNode;
    private AstNode rightNode;
    private String value;
    private String nested;
    
    // for plus or sub
    private String operator;
    
    // note the begin & end idx in the input tarStr
    /*private int bgIdx;
    private int edIdx;*/
    
    // just for drawing
    private int nodeIdx;
    
    // constructor
    public AstNode(TermType ttp) {
        this.termType = ttp;
        nodeIdx = 0;
        nested = "";
        operator = "";
    }
    
    // get operator
    public String getOperator() {
        return operator;
    }
    
    // set operator
    public void setOperator(String operator) {
        this.operator = operator;
    }
    
    // get nested content
    public String getNested() {
        return nested;
    }
    
    // set nested content
    public void setNested(String nested) {
        this.nested = nested;
    }
    
    public int getNodeIdx() {
        return nodeIdx;
    }
    
    public void setNodeIdx(int nodeIdx) {
        this.nodeIdx = nodeIdx;
    }
    
    // check isLeaf method
    public boolean isLeaf() {
        return (leftNode == null && rightNode == null);
    }
    
    // has left / right node
    public boolean hasLeftNode() {
        return leftNode != null;
    }
    
    public boolean hasRightNode() {
        return rightNode != null;
    }
    
    // set & get methods
    // get parent node
    public AstNode getParentNode() {
        return this.parentNode;
    }
    
    public void setParentNode(AstNode node) {
        this.parentNode = node;
    }
    
    // get left node
    public AstNode getLeftNode() {
        return this.leftNode;
    }
    
    // get right node
    public AstNode getRightNode() {
        return this.rightNode;
    }
    
    // set left node
    public void setLeftNode(AstNode node) {
        this.leftNode = node;
    }
    
    // set right node
    public void setRightNode(AstNode node) {
        this.rightNode = node;
    }
    
    // remove left node
    public void removeLeftNode() {
        this.leftNode = null;
    }
    
    // remove right node
    public void removeRightNode() {
        this.rightNode = null;
    }
    
    // get termType
    public TermType getTermType() {
        return this.termType;
    }
    
    // set termType
    public void setTermType(TermType ttp) {
        this.termType = ttp;
    }
    
    // get value, for leaf: its element; for mid：its operation
    public String getValue() {
        return value;
    }
    
    // set value
    public void setValue(String value) {
        this.value = value;
    }
    
    // implement clone
    public Object clone() {
        AstNode astNode = null;
        try {
            astNode = (AstNode) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println("FAIL IN CLONE!");
            System.out.println(e);
        }
        return astNode;
    }
}
