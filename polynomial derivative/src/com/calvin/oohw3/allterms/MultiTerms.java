package com.calvin.oohw3.allterms;

import com.calvin.oohw3.enums.TermType;

public class MultiTerms extends CombinedTerms {
    private String operator;
    private String ori1;
    private String ori2;
    
    public MultiTerms(Terms t1, Terms t2, TermType ttp, String operator,
                      String o1, String o2) {
        super(t1, t2, ttp);
        this.operator = operator;
        this.ori1 = o1;
        this.ori2 = o2;
    }
    
    @Override
    public String derivativeMth() {
        // is required to return each string of sub derivative
        String s1 = getTerm1().derivativeMth();
        String s2 = getTerm2().derivativeMth();
        String ret;
        boolean checkS11 = checkOnlyConst(s1, "()1");
        boolean checkS21 = checkOnlyConst(s2, "()1");
        boolean checkS10 = checkOnlyConst(s1, "()0");
        boolean checkS20 = checkOnlyConst(s2, "()0");
        
        
        if (checkS10 && checkS20) {
            ret = "0";
        } else if (checkS11 && checkS21) {
            ret = String.format("(%s)+(%s)", ori1, ori2);
        } else if (checkS10 && checkS21) {
            ret = ori1;
        } else if (checkS11 && checkS20) {
            ret = ori2;
        } else if (checkS10) {
            ret = String.format("(%s)*(%s)", ori1, s2);
        } else if (checkS20) {
            ret = String.format("(%s)*(%s)", s1, ori2);
        } else if (checkS11) {
            ret = String.format("%s+(%s)*(%s)", ori2, ori1, s2);
        } else if (checkS21) {
            ret = String.format("(%s)*(%s)+%s", s1, ori2, ori1);
        } else {
            ret = String.format("(%s)*(%s)+(%s)*(%s)", s1, ori2, ori1, s2);
        }
        return ret;
    }
}
