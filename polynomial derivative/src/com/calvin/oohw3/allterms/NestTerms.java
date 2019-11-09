package com.calvin.oohw3.allterms;

import com.calvin.oohw3.enums.TermType;

public class NestTerms extends CombinedTerms {
    
    public NestTerms(Terms t1, Terms t2, TermType ttp) {
        super(t1, t2, ttp);
    }
    
    @Override
    public String derivativeMth() {
        String s1 = getTerm1().derivativeMth();
        String s2 = getTerm2().derivativeMth();
        String ret = String.format("((%s)*(%s))", s1, s2);
        return ret;
    }
}
