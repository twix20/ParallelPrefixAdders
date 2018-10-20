package com.idea.nodes;

import com.idea.arithmetic.Bit;
import com.idea.arithmetic.BitCalculator;

//Red node
public class InitialNode extends Node{

    public InitialNode(int stage, int position) {
        super(stage, position);
    }

    @Override
    public void computeResult() {

        Bit propagation = BitCalculator.xor(getA(), getB());
        Bit generation = BitCalculator.and(getA(), getB());

        NodeComputingResult result = new NodeComputingResult(propagation, generation);

        this.setResult(result);
    }
}
