package com.idea.binaryStringAdders;

import com.idea.arithmetic.BinaryString;
import com.idea.arithmetic.IBinaryStringAdder;

public class SequentialAdder implements IBinaryStringAdder {
    @Override
    public BinaryString add(BinaryString a, BinaryString b) {

        int desiredLength = calculateLongerLength(a, b) + 1;

        a = leftPadWithZeros(a, desiredLength);
        b = leftPadWithZeros(b, desiredLength);

        String result = addBitStrings(a, b);

        return new BinaryString(result);
    }

    // The main function that adds two bit sequences and returns the addition
    private String addBitStrings(BinaryString first, BinaryString second )
    {
        String result = "";  // To store the sum bits

        int carry = 0;  // Initialize carry
        int length = first.length();

        // Add all bits one by one
        for (int i = length - 1 ; i >= 0 ; i--)
        {
            int firstBit = first.charAt(i) - '0';
            int secondBit = second.charAt(i) - '0';

            // boolean expression for sum of 3 bits
            int sum = (firstBit ^ secondBit ^ carry)+'0';

            result = (char)sum + result;

            // boolean expression for 3-bit addition
            carry = (firstBit & secondBit) | (secondBit & carry) | (firstBit & carry);
        }

        // if overflow, then add a leading 1
        if (carry == 1)
            result = '1' + result;

        return result;
    }

    private int calculateLongerLength(BinaryString a, BinaryString b){
        return Integer.max(a.length(), b.length());
    }

    private BinaryString leftPadWithZeros(BinaryString stringToPad, int desiredLength){
        String paddedString = String.format("%0" + String.valueOf(desiredLength - stringToPad.length()) + "d%s", 0, stringToPad.toString());
        return new BinaryString(paddedString);
    }
}
