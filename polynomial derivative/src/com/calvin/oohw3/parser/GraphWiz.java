package com.calvin.oohw3.parser;

import com.calvin.oohw3.enums.TermType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GraphWiz {
    private AsTree asTree;
    private int nodeIdx;
    private BufferedWriter out;
    
    // constructor
    public GraphWiz(AsTree tree) {
        this.asTree = tree;
        this.nodeIdx = 1;
    }
    
    // main
    public void beginDraw() {
        try {
            out = new BufferedWriter(new FileWriter("AST.dot"));
            out.write("graph g {\n");
            AstNode node = asTree.getRoot().getLeftNode();
            traverse(node);
            out.write("}");
            out.close();
            System.out.println("文件创建成功!");
        } catch (IOException e) {
            System.out.println("画图的时候存在错误！");
            System.out.println(e);
        }
    }
    
    // toolkit
    public void traverse(AstNode node) throws IOException {
        // pre-traverse
        AstNode father = node;
        if (father.getNodeIdx() == 0) {
            father.setNodeIdx(nodeIdx++);
        }
        if (!father.isLeaf()) {
            AstNode left = node.getLeftNode();
            AstNode right = node.getRightNode();
            if (left.getNodeIdx() == 0) {
                left.setNodeIdx(nodeIdx++);
            }
            String value = getNodeValue(node);
            String linkFn = String.format("%d", father.getNodeIdx());
            String linkLn = String.format("%d", left.getNodeIdx());
            String linkFather = String.format("%s",
                    value);
            out.write(linkFn + "--" + linkLn + ";\n");
            if (right != null) {
                if (right.getNodeIdx() == 0) {
                    right.setNodeIdx(nodeIdx++);
                }
                String linkRn = String.format("%d", right.getNodeIdx());
                out.write(linkFn + "--" + linkRn + ";\n");
            }
            out.write(linkFn + "[shape=circle, label=\"" +
                    linkFather + "\"];\n");
            traverse(left);
            if (right != null) {
                traverse(right);
            }
        } else {
            String linkFn = String.format("%d", father.getNodeIdx());
            String linkFather = String.format("%s",
                    getNodeValue(father));
            out.write(linkFn + "[shape=circle, label=\"" +
                    linkFather + "\"];\n");
        }
    }
    
    public String convertNode(String s) {
        if (s.equals("+")) {
            return "plus";
        } else if (s.equals("-")) {
            return "sub";
        } else if (s.equals("*")) {
            return "multi";
        } else if (s.contains("^")) {
            String tmps = s;
            if (s.contains("(")) {
                tmps = tmps.replace("(", "B");
                tmps = tmps.replace(")", "B");
            }
            return tmps.replace("^", "pow");
        } else if (s.contains("(")) {
            String tmps = s;
            tmps = tmps.replace("(", "B");
            tmps = tmps.replace(")", "B");
            return tmps;
        } else {
            return s;
        }
    }
    
    public String getNodeValue(AstNode node) {
        TermType ttp = node.getTermType();
        if (ttp.equals(TermType.ADDTERMS)) {
            return node.getOperator();
        } else if (ttp.equals(TermType.MULTITERMS)) {
            return "*";
        } else if (ttp.equals(TermType.NESTEDTERMS)) {
            return "NESTED";
        } else {
            return node.getValue();
        }
    }
}
