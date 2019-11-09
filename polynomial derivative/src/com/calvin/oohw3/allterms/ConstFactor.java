package com.calvin.oohw3.allterms;

import com.calvin.oohw3.enums.TermType;

public class ConstFactor extends Terms {
    public ConstFactor(TermType ttp) {
        super(ttp);
    }
    
    @Override
    public String derivativeMth() {
        return "0";
    }
}
