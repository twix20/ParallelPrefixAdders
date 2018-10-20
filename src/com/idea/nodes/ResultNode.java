package com.idea.nodes;

import com.idea.arithmetic.Bit;
import com.idea.arithmetic.BitCalculator;

public class ResultNode extends Node {
    public static ResultNode FAKE_NODE;
    static {
        FAKE_NODE = new ResultNode(-1, -1, null);
       FAKE_NODE.setResult(new NodeComputingResult(Bit.Zero, Bit.Zero));
    }

    public ResultNode(int stage, int postion, ResultNode prevParent) {
        super(stage, postion);

        this.setPrevParent(prevParent);
    }

    @Override
    public void computeResult() {
        Bit previousCarry = this.getPrevParent().getResult().getGeneration(); // Previous result C-1

        Bit rootParentPropagation = getRootParent().getResult().getPropagation();
        Bit sum = BitCalculator.xor(rootParentPropagation, previousCarry);

        NodeComputingResult result = new NodeComputingResult(Bit.Zero, getParent().getResult().getGeneration());
        result.setSum(sum);

        this.setResult(result);
    }

    @Override
    public String toString(){
        int prevParentIndexIfExists = getPrevParent() == null ? -1 : getPrevParent().getIndex();

        return String.format("|{%1$d}{%2$d} C:%3$s S:%4$s T:%5$s| ",
                getIndex(),
                prevParentIndexIfExists,
                getResult().getGeneration(),
                getResult().getSum(),
                getNodeName());
    }

}