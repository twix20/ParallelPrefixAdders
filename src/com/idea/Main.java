package com.idea;

import com.idea.arithmetic.BinaryString;
import com.idea.prefixAdders.IPrefixAdder;
import com.idea.prefixAdders.KoggeStoneAdder;
import com.idea.prefixAdders.LadnerFischerAdder;

public class Main {

    public static void main(String[] args) {
	    // write your code here
        //IPrefixAdder adder = new KoggeStoneAdder();
        IPrefixAdder adder = new LadnerFischerAdder();

        BinaryString a = new BinaryString("0000000000000000000000000000000000000000000111111111111111111111111111111111111111111111111111");
        BinaryString b = new BinaryString("1111111111111111111111111111111111111111111000000000000000000000000000000000000000000000000000");

        BinaryString result = adder.add(a, b);

        System.out.println(result);
    }

    // Binary to decimal: https://www.mathsisfun.com/binary-decimal-hexadecimal-converter.html
    //Correct results proven by wolfram alpha
    // 1#:
    //      a: 001111111111000000000000000111111111111111111110000000000000000000000001111110000000111111
    //      b: 0111111100000011111101011111010110000000000000000000000000000011111111111101011111111110001110
    //      r: 01000001100000010111101011111011101111111111111111110000000000011111111111111011101111111001101

    // 2#:
    //      a: 0000000000000000000000000000000000000000000111111111111111111111111111111111111111111111111111
    //      b: 1111111111111111111111111111111111111111111000000000000000000000000000000000000000000000000000
    //      r: 01111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111
}
