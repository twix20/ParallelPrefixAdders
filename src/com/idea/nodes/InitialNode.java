package com.idea.nodes;

import com.idea.arithmetic.Bit;
import com.idea.arithmetic.BitCalculator;

import java.util.concurrent.ExecutorService;

//Red node
public class InitialNode extends Node{

    public InitialNode(ExecutorService executorService, int stage, int position) {
        super(executorService, stage, position);
    }

    @Override
    protected NodeComputingResult computeResultInternal() {

        // TODO Get real input data not "1" and "1"

        Bit propagation = BitCalculator.xor(Bit.One, Bit.One);
        Bit generation = BitCalculator.and(Bit.One, Bit.One);

        System.out.println("start: " + System.currentTimeMillis());

        return new NodeComputingResult(propagation, generation);
    }

}
