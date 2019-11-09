package com.calvin.oohw3.allterms;

import com.calvin.oohw3.enums.TermType;

public class CosFactor extends TriFactor {
    public CosFactor(TermType ttp, int exp, String content) {
        super(ttp, exp, content);
    }
    
    @Override
    public String derivativeMth() {
        String cof;
        String nexp;
        String ret;
        int exp = getPowExp();
        String ct = getContent();
        if (exp == 0) {
            ret = String.format("-1*sin(%s)", ct);
        } else {
            cof = String.valueOf(exp);
            nexp = String.valueOf(exp - 1);
            ret = String.format("%s*cos(%s)^%s*-1*sin(%s)", cof, ct, nexp, ct);
        }
        if (checkOnlyConst(ct, " -0123456789")) {
            ret = "0";
        }
        return ret;
    }
}
