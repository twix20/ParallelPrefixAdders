package com.idea.nodes;

import com.idea.arithmetic.Bit;
import com.idea.arithmetic.BitCalculator;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.text.MessageFormat.format;

public class ResultNode extends Node {
    public static ResultNode FAKE_NODE;
    static {
       FAKE_NODE = new ResultNode(Executors.newSingleThreadExecutor(), -1, -1, null);
       FAKE_NODE.setResult(new NodeComputingResult(Bit.Zero, Bit.Zero));
    }

    public ResultNode(ExecutorService executorService, int stage, int postion, ResultNode prevParent) {
        super(executorService, stage, postion);

        this.setPrevParent(prevParent);
    }

    @Override
    protected NodeComputingResult computeResultInternal() throws ComputingException {
        Future<NodeComputingResult> previousParentFuture = getPrevParent().computeResult();
        Future<NodeComputingResult> rootParentFuture = getRootParent().computeResult();
        Future<NodeComputingResult> parentFuture = getParent().computeResult();

        try {
            Bit previousCarry = previousParentFuture.get().getGeneration();
            Bit rootParentPropagation = rootParentFuture.get().getPropagation();
            Bit sum = BitCalculator.xor(rootParentPropagation, previousCarry);

            NodeComputingResult result = new NodeComputingResult(Bit.Zero, parentFuture.get().getGeneration());
            result.setSum(sum);

            return result;
        } catch (Exception e) {
            throw new ComputingException(e);
        }

    }

    @Override
    public String toString(){
        int prevParentPositionIfExists = getPrevParent() == null ? -1 : getPrevParent().getPosition();

        return String.format("|{%1$d}{%2$d} C:%3$s S:%4$s T:%5$s| ",
                getPosition(),
                prevParentPositionIfExists,
                getResult().getGeneration(),
                getResult().getSum(),
                getNodeName());
    }

}