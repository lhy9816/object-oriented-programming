package com.calvin.oohw3.parser;

import com.calvin.oohw3.enums.ErrorType;
import com.calvin.oohw3.OoMain;
import com.calvin.oohw3.enums.TokenType;

import java.math.BigInteger;

public class Lexer {
    private char curChar;
    private TokenType curToken;
    private String tarStr;
    private int strIdx;
    private int preStrIdx;
    private String strToken;
    private BigInteger curNum;
    
    // constructor
    public Lexer(String s) {
        this.curChar = '\0';
        this.curToken = TokenType.INIT;
        this.tarStr = s;
        this.strIdx = 0;
        this.preStrIdx = this.strIdx;
        this.strToken = "";
        this.curNum = BigInteger.ZERO;
    }
    
    // methods
    // check length of input
    public boolean reachInputLength() {
        return this.tarStr.length() <= this.strIdx;
    }
    
    // get next char
    private void getNextChar() {
        if (strIdx < tarStr.length()) {
            this.curChar = this.tarStr.charAt(strIdx++);
        } else {
            // for quit, set '#' as quit char
            this.curChar = '#';
        }
    }
    
    // retract char
    public void retract() {
        if (curChar != '#') {
            this.strIdx--;
            this.curChar = '\0';
        }
    }
    
    // get stridx
    public int getStrIdx() {
        return strIdx;
    }
    
    // set stridx
    public void setStrIdx(int strIdx) {
        this.strIdx = strIdx;
    }
    
    // get prestridx
    public int getPreStrIdx() {
        return preStrIdx;
    }
    
    // append char in strToken
    public void catToken() {
        this.strToken += this.curChar;
    }
    
    // get current token type
    public TokenType getCurToken() {
        return curToken;
    }
    
    // get current num
    public BigInteger getCurNum() {
        return curNum;
    }
    
    // get tarStr
    protected String getTarStr() {
        return tarStr;
    }
    
    // set current num ( for -sin(x) )
    public void setCurNum(BigInteger bi) {
        this.curNum = bi;
        retract();
        setCurToken(TokenType.NUMB);
    }
    
    // set current tokenType
    public void setCurToken(TokenType tp) {
        this.curToken = tp;
    }
    
    // set tarStr
    private void setTarStr(String s) {
        this.tarStr = s;
    }
    
    // for outer class to change tarStr
    public void changeTarStr(String s) {
        String tmpS1 = tarStr.substring(0, strIdx);
        String tmpS2 = tarStr.substring(strIdx);
        tarStr = tmpS1 + s + tmpS2;
    }
    
    // judging methods
    public boolean isSpace() {
        return this.curChar == ' ';
    }
    
    public boolean isNumber() {
        return this.curChar >= '0' && this.curChar <= '9';
    }
    
    public boolean isLetter() {
        return this.curChar >= 'a' && this.curChar <= 'z';
    }
    
    public boolean isPlus() {
        return this.curChar == '+';
    }
    
    public boolean isSub() {
        return this.curChar == '-';
    }
    
    public boolean isMulti() {
        return this.curChar == '*';
    }
    
    public boolean isPower() {
        return this.curChar == '^';
    }
    
    public boolean isXs() {
        return this.curChar == 'x';
    }
    
    public boolean isLeftBrassy() {
        return this.curChar == '(';
    }
    
    public boolean isRightBrassy() {
        return this.curChar == ')';
    }
    
    public boolean isCos() {
        if (this.strIdx <= this.tarStr.length() - 3) {
            if (tarStr.substring(strIdx - 1, strIdx + 2).equals("cos")) {
                strIdx += 2;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    public boolean isSin() {
        if (this.strIdx <= this.tarStr.length() - 3) {
            if (tarStr.substring(strIdx - 1, strIdx + 2).equals("sin")) {
                strIdx += 2;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    // interface for getSymbol
    public void getNextSymbol(boolean flag) {
        this.preStrIdx = this.strIdx;
        this.curToken = getSymbol();
        if (flag) {
            if (!curToken.equals(TokenType.NUMB)) {
                OoMain.errorManipulate(ErrorType.SPACEINSIGNEDNUM);
            }
        } else {
            while (curToken.equals(TokenType.SPACE)) {
                this.curToken = getSymbol();
            }
        }
        /*if (curToken.equals(TokenType.QUIT)) {
            return "END";
        } else if(curToken.equals(TokenType.WA)) {
            return "FUCK";
        } else {
            return "OK";
        }*/
    }
    
    // get symbol and reserve
    private TokenType getSymbol() {
        this.strToken = "";
        if (reachInputLength()) {
            return TokenType.QUIT;
        }
        this.getNextChar();
        // begin identify
        if (isSpace()) {
            return TokenType.SPACE;
        } else if (isNumber()) {
            catToken();
            getNextChar();
            while (isNumber()) {
                catToken();
                getNextChar();
            }
            retract();
            this.curNum = new BigInteger(this.strToken);
            return TokenType.NUMB;
        } else if (isLetter()) {
            if (isXs()) {
                return TokenType.XS;
            } else if (isCos()) {
                return TokenType.COS;
            } else if (isSin()) {
                return TokenType.SIN;
            } else {
                return TokenType.WA;
            }
        } else if (isPlus() || isSub()) {
            return checkPosNeg();
        } else if (isMulti()) {
            return TokenType.MULTSY;
        } else if (isPower()) {
            return TokenType.POWSY;
        } else if (isLeftBrassy()) {
            return TokenType.LSBRASY;
        } else if (isRightBrassy()) {
            return TokenType.RSBRASY;
        } else {
            OoMain.errorManipulate(ErrorType.INVALIDCHAR);
            return TokenType.WA;
        }
    }
    
    public TokenType checkPosNeg() {
        if (isPlus()) {
            // judge it's a signed number or operator
            getNextChar();
            if (isNumber()) {
                catToken();
                getNextChar();
                while (this.isNumber()) {
                    catToken();
                    getNextChar();
                }
                retract();
                this.curNum = new BigInteger(this.strToken);
                return TokenType.NUMB;
            } else {
                retract();
                return TokenType.PLUSSY;
            }
        } else {
            // judge whether it's a number or operator
            catToken();
            getNextChar();
            if (isNumber()) {
                catToken();
                getNextChar();
                while (isNumber()) {
                    catToken();
                    getNextChar();
                }
                retract();
                this.curNum = new BigInteger(strToken);
                return TokenType.NUMB;
            } else {
                retract();
                return TokenType.SUBSY;
            }
        }
    }
}
