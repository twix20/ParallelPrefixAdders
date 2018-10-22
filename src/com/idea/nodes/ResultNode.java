package com.idea.nodes;

import com.idea.arithmetic.Bit;
import com.idea.arithmetic.BitCalculator;
import com.idea.binaryStringAdders.MeshNodesV2;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.text.MessageFormat.format;

public class ResultNode extends Node {
    public ResultNode(MeshNodesV2 meshNodes, ExecutorService executor, int position, int parentPosition, int prevParentPosition) {
        super(meshNodes, executor, position, parentPosition, prevParentPosition);
    }

    public ResultNode(MeshNodesV2 meshNodes, ExecutorService executor, int position, int parentPosition) {
        super(meshNodes, executor, position, parentPosition);
    }

    @Override
    protected NodeComputingResult computeResultInternal() throws ComputingException {
        Future<NodeComputingResult> previousResultFuture = getPrevParentPosition() == -1 ? null : getPrevParent().computeResult();
        Future<NodeComputingResult> parentFuture = getParent().computeResult();

        try {
            Bit previousCarry = previousResultFuture == null ? Bit.Zero : previousResultFuture.get().getGeneration();
            Bit parentPropagation = parentFuture.get().getPropagation();
            Bit sum = BitCalculator.xor(parentPropagation, previousCarry);

            return new NodeComputingResult(Bit.Zero, parentFuture.get().getGeneration(), sum);
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