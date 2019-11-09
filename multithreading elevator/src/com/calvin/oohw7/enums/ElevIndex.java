package com.calvin.oohw7.enums;

public enum ElevIndex {
    A(0), B(1), C(2);
    
    private int value = 0;
    
    private ElevIndex(int value) {
        this.value = value;
    }
    
    public static ElevIndex valueOf(int value) {
        switch (value) {
            case 0:
                return A;
            case 1:
                return B;
            case 2:
                return C;
            default:
                return null;
        }
    }
    
    public int value() {
        return this.value;
    }
}
