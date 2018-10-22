package com.idea.nodes;

import com.idea.arithmetic.Bit;
import com.idea.arithmetic.BitCalculator;
import com.idea.binaryStringAdders.MeshNodesV2;

import java.util.concurrent.ExecutorService;

//Red node
public class InitialNode extends Node{


    public InitialNode(MeshNodesV2 meshNodes, ExecutorService executor, int position, Bit inputA, Bit inputB) {
        super(meshNodes, executor, position);

        setA(inputA);
        setB(inputB);
    }

    @Override
    protected NodeComputingResult computeResultInternal() {
        Bit a = getA();
        Bit b = getB();

        Bit propagation = BitCalculator.xor(a, b);
        Bit generation = BitCalculator.and(a, b);

        //System.out.println("start: " + System.currentTimeMillis());

        return new NodeComputingResult(propagation, generation);
    }

}
