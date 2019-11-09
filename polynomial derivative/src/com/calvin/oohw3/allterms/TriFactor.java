package com.calvin.oohw3.allterms;

import com.calvin.oohw3.enums.TermType;

public class TriFactor extends Terms {
    private int powExp;
    private String content;
    
    public TriFactor(TermType ttp, int exp, String content) {
        super(ttp);
        this.powExp = exp;
        this.content = content;
    }
    
    // methods
    public int getPowExp() {
        return powExp;
    }
    
    public void setPowExp(int exp) {
        this.powExp = exp;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
}
