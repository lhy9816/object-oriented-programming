package com.calvin.oohw3.allterms;

import com.calvin.oohw3.OoMain;
import com.calvin.oohw3.enums.ErrorType;
import com.calvin.oohw3.enums.TermType;
import com.calvin.oohw3.parser.AsTree;
import com.calvin.oohw3.parser.AstNode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkTerms {
    private AsTree asTree;
    private Terms rootTerm;
    
    // constructor
    public LinkTerms(AsTree ast) {
        this.asTree = ast;
    }
    
    // embedded methods
    public AsTree getAsTree() {
        return asTree;
    }
    
    // link
    public Terms linkTermTree(AstNode node) {
        Terms terms1 = null;
        Terms terms2 = null;
        Terms retTerm;
        if (!node.isLeaf()) {
            if (node.hasLeftNode()) {
                AstNode node1 = node.getLeftNode();
                terms1 = linkTermTree(node1);
            }
            if (node.hasRightNode()) {
                AstNode node2 = node.getRightNode();
                terms2 = linkTermTree(node2);
            }
            retTerm = switchLinkTerm(node, terms1, terms2);
        } else {
            retTerm = switchLeafTerm(node);
        }
        return retTerm;
    }
    
    // terms cases
    public Terms switchLinkTerm(AstNode node, Terms term1, Terms term2) {
        Terms retTerm;
        TermType ttp = node.getTermType();
        switch (ttp) {
            case ADDTERMS:
                retTerm = new AddTerms(term1, term2, ttp, node.getOperator());
                break;
            case MULTITERMS:
                AstNode node1 = node.getLeftNode();
                AstNode node2 = node.getRightNode();
                String ori1 = node1.getValue();
                String ori2 = node2.getValue();
                if (node1.getTermType().equals(TermType.NESTEDTERMS)) {
                    ori1 = node1.getLeftNode().getValue();
                }
                if (node2.getTermType().equals(TermType.NESTEDTERMS)) {
                    ori2 = node2.getLeftNode().getValue();
                }
                
                retTerm = new MultiTerms(term1, term2, ttp,
                        node.getValue(), ori1, ori2);
                break;
            case NESTEDTERMS:
                retTerm = new NestTerms(term1, term2, ttp);
                break;
            default:
                OoMain.errorManipulate(ErrorType.WRONGTERMTYPEINLINKTERMS);
                retTerm = new Terms(TermType.GENERALTERMS);
                break;
        }
        return retTerm;
    }
    
    public Terms switchLeafTerm(AstNode node) {
        Terms retTerm;
        TermType ttp = node.getTermType();
        switch (ttp) {
            case CONSTTERMS:
                retTerm = new ConstFactor(ttp);
                break;
            case XTERMS:
                int exp = parseCoefficient(node.getValue());
                retTerm = new PowerFactor(ttp, exp);
                break;
            case COSTERMS:
                int expCos = parseCoefficient(node.getValue());
                String contentCos = node.getNested();
                retTerm = new CosFactor(ttp, expCos, contentCos);
                break;
            case SINTERMS:
                int expSin = parseCoefficient(node.getValue());
                String contentSin = node.getNested();
                retTerm = new SinFactor(ttp, expSin, contentSin);
                break;
            default:
                OoMain.errorManipulate(ErrorType.WRONGTERMTYPEINLINKTERMS);
                retTerm = new Terms(TermType.GENERALTERMS);
                break;
        }
        return retTerm;
    }
    
    // parse power coefficient
    public int parseCoefficient(String s) {
        Pattern pattern = Pattern.compile("\\^(?<exp>[ \t]*[+-]?[0-9]+)$");
        Matcher matcher = pattern.matcher(s);
        // convert String exp to int exp
        int exp = 0;
        if (matcher.find()) {
            String tmps = matcher.group("exp");
            tmps = tmps.replace(" ", "");
            tmps = tmps.replace("\t", "");
            exp = Integer.parseInt(tmps);
        }
        return exp;
    }
    
    // begin link
    public void beginLink() {
        AstNode node = asTree.getRoot().getLeftNode();
        rootTerm = linkTermTree(node);
    }
    
    // begin derivative
    public String beginDerivative() {
        return rootTerm.derivativeMth();
    }
}
