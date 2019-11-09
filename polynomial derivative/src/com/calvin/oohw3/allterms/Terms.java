package com.calvin.oohw3.allterms;

import com.calvin.oohw3.allterms.terminterfaces.DerivativeInterface;
import com.calvin.oohw3.enums.TermType;

public class Terms implements DerivativeInterface {
    private TermType termType;
    private String value;
    
    // constructor
    public Terms(TermType ttp) {
        this.termType = ttp;
    }
    
    public TermType getTermType() {
        return termType;
    }
    
    public void setTermType(TermType ttp) {
        this.termType = ttp;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    // check if there is only some 0 or 1
    public boolean checkOnlyConst(String candidate, String cst) {
        for (int j = 0; j < candidate.length(); j++) {
            char c = candidate.charAt(j);
            if (!cst.contains(String.valueOf(c))) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String derivativeMth() {
        return "";
    }
}
