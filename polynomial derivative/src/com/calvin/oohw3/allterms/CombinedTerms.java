package com.calvin.oohw3.allterms;

import com.calvin.oohw3.enums.TermType;

public class CombinedTerms extends Terms {
    private Terms term1;
    private Terms term2;
    
    public CombinedTerms(Terms t1, Terms t2, TermType ttp) {
        super(ttp);
        this.term1 = t1;
        this.term2 = t2;
    }
    
    // methods
    public Terms getTerm1() {
        return term1;
    }
    
    public Terms getTerm2() {
        return term2;
    }
    
    public void setTerm1(Terms t1) {
        this.term1 = t1;
    }
    
    public void setTerm2(Terms t2) {
        this.term2 = t2;
    }
}
