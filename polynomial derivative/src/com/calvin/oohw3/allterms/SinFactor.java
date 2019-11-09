package com.calvin.oohw3.allterms;

import com.calvin.oohw3.enums.TermType;

public class SinFactor extends TriFactor {
    public SinFactor(TermType ttp, int exp, String content) {
        super(ttp, exp, content);
    }
    
    @Override
    public String derivativeMth() {
        String cof;
        String newexp;
        String ret;
        int exp = getPowExp();
        String ctt = getContent();
        if (exp == 0) {
            ret = String.format("cos(%s)", ctt);
        } else {
            cof = String.valueOf(exp);
            newexp = String.valueOf(exp - 1);
            ret = String.format("%s*sin(%s)^%s*cos(%s)", cof, ctt, newexp, ctt);
        }
        if (checkOnlyConst(ctt, " -0123456789")) {
            ret = "0";
        }
        return ret;
    }
}
