package com.idea.binaryStringAdders;

import com.idea.arithmetic.BinaryString;
import com.idea.arithmetic.Bit;
import com.idea.arithmetic.BitCalculator;
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

        Bit carry = Bit.Zero;  // Initialize carry
        int length = first.length();

        // Add all bits one by one
        for (int i = length - 1 ; i >= 0 ; i--)
        {
            Bit firstBit = first.bitAt(i);
            Bit secondBit = second.bitAt(i);

            // boolean expression for sum of 3 bits
            Bit sum = BitCalculator.xor(firstBit, secondBit);
            sum = BitCalculator.xor(sum, carry);

            result = Bit.getValue(sum) + result;

            // boolean expression for 3-bit addition
            Bit temp = BitCalculator.or(BitCalculator.and(firstBit, secondBit), BitCalculator.and(secondBit, carry));
            carry = BitCalculator.or(temp, BitCalculator.and(firstBit, carry));
        }

        // if overflow, then add a leading 1
        if (carry == Bit.One)
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
