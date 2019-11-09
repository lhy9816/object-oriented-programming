package com.calvin.oohw3.allterms;

import com.calvin.oohw3.enums.TermType;

public class PowerFactor extends Terms {
    private int powExp;
    
    public PowerFactor(TermType ttp, int exp) {
        super(ttp);
        this.powExp = exp;
    }
    
    @Override
    public String derivativeMth() {
        if (powExp == 0) {
            return "1";
        } else {
            String coef = String.valueOf(powExp);
            String newexp = String.valueOf(powExp - 1);
            String ret = String.format("%s*x^%s", coef, newexp);
            return ret;
        }
    }
}
