package com.calvin.oohw3.parser;

import com.calvin.oohw3.enums.ChildDirection;
import com.calvin.oohw3.enums.ErrorType;
import com.calvin.oohw3.OoMain;
import com.calvin.oohw3.enums.TermType;
import com.calvin.oohw3.enums.TokenType;

import java.math.BigInteger;

public class Parser {
    private Lexer lexer;
    private AsTree asTree;
    // the other things are for constructing an AST
    
    // constructor
    public Parser(String s) {
        lexer = new Lexer(s);
    }
    
    // embedded methods
    public Lexer getLexer() {
        return lexer;
    }
    
    public AsTree getAsTree() {
        return asTree;
    }
    
    // check if current required id is identical to achieved id
    public boolean checkType(Lexer l, TokenType id) {
        TokenType ttp = l.getCurToken();
        /*if (ttp.equals(TokenType.QUIT)) {
            OoMain.errorManipulate(ErrorType.QUITTOOEARLY);
            return false;
        }*/
        return ttp.equals(id);
    }
    
    public Boolean parsePowerFactor(AstNode astNode) {
        astNode.setTermType(TermType.XTERMS);
        astNode.setValue("x");
        lexer.getNextSymbol(false);
        TokenType tt = lexer.getCurToken();
        // check if there is a power idx
        if (tt.equals(TokenType.POWSY)) {
            lexer.getNextSymbol(false);
            boolean b1 = checkType(lexer, TokenType.NUMB);
            boolean b2 = checkExp(lexer.getCurNum());
            if (!b1 || !b2) {
                OoMain.errorManipulate(ErrorType.WRONGFORMATNUMBER);
            }
            String tmps = lexer.getCurNum().toString();
            astNode.setValue(astNode.getValue() + "^" + tmps);
            lexer.getNextSymbol(false);
            return true;
        }
        return false;
    }
    
    // parse cos function
    public void parseCosSinFactor(AstNode astnode, TokenType tp) {
        if (tp.equals(TokenType.COS)) {
            astnode.setTermType(TermType.COSTERMS);
            astnode.setNested("x");
            astnode.setValue("cos(x)");
        } else {
            astnode.setTermType(TermType.SINTERMS);
            astnode.setNested("x");
            astnode.setValue("sin(x)");
        }
        // check (
        lexer.getNextSymbol(false);
        if (!checkType(lexer, TokenType.LSBRASY)) {
            OoMain.errorManipulate(ErrorType.BRASYMISMATCH);
        }
        // record the begin idx of NESTED part
        lexer.getNextSymbol(false);
        int bgIdx = lexer.getPreStrIdx();
        AstNode nextnode = new AstNode(TermType.GENERALTERMS);
        asTree.setCurNode(nextnode);
        linkNodes(nextnode, astnode, ChildDirection.LEFT);
        boolean isNested = parseFactor(nextnode);
        if (isNested) {
            // LHY here it should return the NESTED in String !!!
            // record the end idx of NESTED part
            int edIdx = lexer.getPreStrIdx();
            String tarstr = lexer.getTarStr().substring(bgIdx, edIdx);
            tarstr = tarstr.trim();
            // astnode.setTermType(TermType.NESTEDTERMS);
            astnode.setValue(astnode.getValue().replace(
                    "x", tarstr));
            astnode.setNested(tarstr);
            // adjust the node sequence
            AstNode nestednode = new AstNode(TermType.NESTEDTERMS);
            nestednode.setValue(astnode.getValue());
            AstNode uppernode = astnode.getParentNode();
            AstNode innernode = (AstNode) astnode.getLeftNode().clone();
            linkNodes(nestednode, uppernode, ChildDirection.LEFT);
            linkNodes(astnode, nestednode, ChildDirection.LEFT);
            linkNodes(innernode, nestednode, ChildDirection.RIGHT);
        }
        astnode.removeLeftNode();
        if (!checkType(lexer, TokenType.RSBRASY)) {
            OoMain.errorManipulate(ErrorType.BRASYMISMATCH);
        }
        lexer.getNextSymbol(false);
        TokenType ttp = lexer.getCurToken();
        // check if there is a power idx
        if (ttp.equals(TokenType.POWSY)) {
            lexer.getNextSymbol(false);
            String s = lexer.getCurNum().toString();
            boolean b1 = checkType(lexer, TokenType.NUMB);
            if (!b1 || !checkExp(lexer.getCurNum())) {
                OoMain.errorManipulate(ErrorType.WRONGFORMATNUMBER);
            }
            astnode.setValue(astnode.getValue() + "^" + s);
            lexer.getNextSymbol(false);
        }
    }
    
    // parse factor
    // the return value indicates whether it is a nested factor
    public boolean parseFactor(AstNode astNode) {
        TokenType tt = lexer.getCurToken();
        // the content within a factor
        int bgIdx = lexer.getPreStrIdx();
        if (bgIdx < 0) {
            bgIdx = 0;
        }
        int edIdx;
        if (tt.equals(TokenType.NUMB)) {
            // signed number
            astNode.setTermType(TermType.CONSTTERMS);
            astNode.setValue(lexer.getCurNum().toString());
            lexer.getNextSymbol(false);
        } else if (tt.equals(TokenType.XS)) {
            final boolean isNested = parsePowerFactor(astNode);
            edIdx = lexer.getPreStrIdx();
            astNode.setNested(lexer.getTarStr().substring(bgIdx, edIdx));
            astNode.setValue(lexer.getTarStr().substring(bgIdx, edIdx));
            if (isNested) {
                return true;
            }
        } else if (tt.equals(TokenType.COS) || tt.equals(TokenType.SIN)) {
            parseCosSinFactor(astNode, tt);
            edIdx = lexer.getPreStrIdx();
            String nest = lexer.getTarStr().substring(bgIdx, edIdx);
            // remove sin(, )  /  cos(, )
            String trimSpace = nest.replace(" ", "");
            astNode.setValue(trimSpace);
            astNode.setNested(trimSpace.substring(
                    4, findLastBrassy(trimSpace, ')')));
            return true;
        } else if (tt.equals(TokenType.LSBRASY)) {
            astNode.setTermType(TermType.ADDTERMS);
            lexer.getNextSymbol(false);
            parseExpression(astNode, true);
            if (!checkType(lexer, TokenType.RSBRASY)) {
                OoMain.errorManipulate(ErrorType.BRASYMISMATCH);
            }
            lexer.getNextSymbol(false);
            edIdx = lexer.getPreStrIdx();
            astNode.setNested(lexer.getTarStr().substring(bgIdx, edIdx));
            astNode.setValue(lexer.getTarStr().substring(bgIdx, edIdx));
            return true;
        } else {
            OoMain.errorManipulate(ErrorType.WRONGGRAMMAR);
        }
        edIdx = lexer.getPreStrIdx();
        astNode.setNested(lexer.getTarStr().substring(bgIdx, edIdx));
        return false;
    }
    
    // parse item
    public void parseItem(AstNode astNode, boolean firstCall) {
        TokenType tt = lexer.getCurToken();
        
        // create left node for iterating
        AstNode leftNode = new AstNode(TermType.GENERALTERMS);
        AstNode parentNode = astNode;
        parentNode.setValue("*");
        linkNodes(leftNode, parentNode, ChildDirection.LEFT);
        
        // item starts with + / -, add one factor 0 before whole AST
        if (firstCall) {
            if (tt.equals(TokenType.PLUSSY)) {
                lexer.getNextSymbol(false);
            } else if (checkType(lexer, TokenType.SUBSY)) {
                // add 1* after - to create a new factor
                lexer.changeTarStr("1*");
                // reget curToken
                lexer.retract();
                lexer.getNextSymbol(false);
            }
        }
        // the content within a factor
        int bgIdx = lexer.getPreStrIdx();
        asTree.setCurNode(leftNode);
        parseFactor(leftNode);
        int edIdx = lexer.getPreStrIdx();
        leftNode.setValue(lexer.getTarStr().substring(bgIdx, edIdx));
        
        // create right node for iterating
        AstNode rightNode = new AstNode(TermType.MULTITERMS);
        linkNodes(rightNode, parentNode, ChildDirection.RIGHT);
        asTree.setCurNode(rightNode);
        tt = lexer.getCurToken();
        if (tt.equals(TokenType.MULTSY)) {
            lexer.getNextSymbol(false);
            bgIdx = lexer.getPreStrIdx();
            parseItem(rightNode, false);
            edIdx = lexer.getPreStrIdx();
            rightNode.setValue(lexer.getTarStr().substring(bgIdx, edIdx));
        } else { // LHY
            rightNode.setTermType(TermType.CONSTTERMS);
            rightNode.setValue("1");
            rightNode.setNested("1");
        }
    }
    
    // parse expression
    public void parseExpression(AstNode astNode, boolean firstCall) {
        TokenType tt = lexer.getCurToken();
        boolean hasBrassy = false;
        
        /*if (tt.equals(TokenType.LSBRASY)) {
            hasBrassy = true;
            lexer.getNextSymbol(false);
            tt = lexer.getCurToken();
        }*/
        
        // create left node for iterating
        AstNode leftNode = new AstNode(TermType.MULTITERMS);
        AstNode fatherNode = astNode;
        linkNodes(leftNode, fatherNode, ChildDirection.LEFT);
        // expr starts with + / -, add one item 0 before whole AST
        if (firstCall) {
            if (tt.equals(TokenType.PLUSSY)) {
                lexer.getNextSymbol(false);
            } else if (tt.equals(TokenType.SUBSY)) {
                // add 1* after - to create a new factor
                lexer.changeTarStr("1*");
            }
        }
        asTree.setCurNode(leftNode);
        parseItem(leftNode, true);
        
        // create right node for iterating
        AstNode rightNode = new AstNode(TermType.ADDTERMS);
        linkNodes(rightNode, fatherNode, ChildDirection.RIGHT);
        asTree.setCurNode(rightNode);
        tt = lexer.getCurToken();
        if (tt.equals(TokenType.PLUSSY) || tt.equals(TokenType.SUBSY)
                || tt.equals(TokenType.NUMB)) {
            if (tt.equals(TokenType.PLUSSY)) {
                fatherNode.setOperator("+");
            } else if (tt.equals(TokenType.SUBSY)) {
                fatherNode.setOperator("-");
            } else {
                char c = lexer.getTarStr().charAt(lexer.getPreStrIdx());
                if (c == '+') {
                    fatherNode.setOperator("+");
                    lexer.setStrIdx(lexer.getPreStrIdx() + 1);
                } else if (c == '-') {
                    fatherNode.setOperator("-");
                    lexer.setStrIdx(lexer.getPreStrIdx() + 1);
                } else {
                    OoMain.errorManipulate(ErrorType.LACKOPERATION);
                }
            }
            lexer.getNextSymbol(false);
            parseExpression(rightNode, false);
        }  else { // LHY
            fatherNode.setOperator("+");
            rightNode.setTermType(TermType.CONSTTERMS);
            rightNode.setValue("0");
            rightNode.setNested("0");
        }
        
        /*if (hasBrassy) {
            if (!checkType(lexer, TokenType.RSBRASY)) {
                OoMain.errorManipulate(ErrorType.BRASYMISMATCH);
            }
            lexer.getNextSymbol(false);
        }*/
    }
    
    // begin parse
    public void beginParse() {
        asTree = new AsTree();
        AstNode node = new AstNode(TermType.ADDTERMS);
        linkNodes(node, asTree.getRoot(), ChildDirection.LEFT);
        asTree.setCurNode(node);
        lexer.getNextSymbol(false);
        parseExpression(node, true);
        if (!lexer.getCurToken().equals(TokenType.QUIT)) {
            OoMain.errorManipulate(ErrorType.QUITTOOEARLY);
        }
        
        // beginDraw
        /*GraphWiz graphWiz = new GraphWiz(asTree);
        graphWiz.beginDraw();*/
    }
    
    // toolkit
    // link two astNodes
    public void linkNodes(AstNode child, AstNode father, ChildDirection dirt) {
        child.setParentNode(father);
        if (dirt == ChildDirection.LEFT) {
            father.setLeftNode(child);
        } else {
            father.setRightNode(child);
        }
    }
    
    // check 0 < exp <= 10000
    public boolean checkExp(BigInteger bi) {
        boolean b1 = true;
        //bi.compareTo(BigInteger.ZERO) > 0;
        boolean b2 = bi.compareTo(new BigInteger("10000")) <= 0;
        if (b1 && b2) {
            return true;
        } else {
            return false;
        }
    }
    
    public int findLastBrassy(String s, char c) {
        for (int i = s.length() - 1; i > 3; i--) {
            if (s.charAt(i) == c) {
                return i;
            }
        }
        return 0;
    }
}
    
