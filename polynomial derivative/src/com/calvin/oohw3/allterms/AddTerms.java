package com.calvin.oohw3.allterms;

import com.calvin.oohw3.enums.TermType;

public class AddTerms extends CombinedTerms {
    private String operator;
    
    public AddTerms(Terms t1, Terms t2, TermType ttp, String operator) {
        super(t1, t2, ttp);
        this.operator = operator;
    }
    
    @Override
    public String derivativeMth() {
        String s1 = getTerm1().derivativeMth();
        String s2 = getTerm2().derivativeMth();
        String ret;
        boolean checkS1 = checkOnlyConst(s1, "()0");
        boolean checkS2 = checkOnlyConst(s2, "()0");
        if (checkS1 && checkS2) {
            ret = "0";
        } else if (checkS1) {
            ret = String.format("%s", s2);
        } else if (checkS2) {
            ret = String.format("%s", s1);
        } else if (operator.equals("-")) {
            ret = String.format("%s%s(%s)", s1, operator, s2);
        } else {
            ret = String.format("%s%s%s", s1, operator, s2);
        }
        return ret;
    }
}
