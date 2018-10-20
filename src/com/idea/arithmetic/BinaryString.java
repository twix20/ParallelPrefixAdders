package com.idea.arithmetic;

public class BinaryString {
    private String originalString;

    public BinaryString(String bitString) {
        originalString = bitString;
    }

    public int length(){
        return originalString.length();
    }

    public Bit bitAt(int index){
        return originalString.charAt(index) == '1' ? Bit.One : Bit.Zero;
    }

    private boolean isValid(){
        return originalString.chars().allMatch(c -> c == '1' || c == '0');
    }

    @Override
    public String toString() {
        return originalString;
    }
}
