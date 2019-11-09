package com.calvin.oohw3.parser;

import com.calvin.oohw3.enums.TermType;

public class AsTree {
    // root node the entrance of whole AST
    private AstNode root;
    private AstNode curNode;
    
    // constructor
    public AsTree() {
        root = new AstNode(TermType.GENERALTERMS);
        curNode = root;
    }
    
    // get current node
    public AstNode getCurNode() {
        return curNode;
    }
    
    // set current node
    public void setCurNode(AstNode node) {
        curNode = node;
    }
    
    // get root node
    public AstNode getRoot() {
        return root;
    }
}
