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
        Future<NodeComputingResult> rootParentFuture = getRootParent().computeResult();
        Future<NodeComputingResult> parentFuture = getParent().computeResult();

        try {
            Bit previousCarry = previousResultFuture == null ? Bit.Zero : previousResultFuture.get().getGeneration();

            Bit rootParentPropagation = rootParentFuture.get().getPropagation();
            Bit sum = BitCalculator.xor(rootParentPropagation, previousCarry);

            return new NodeComputingResult(Bit.Zero, parentFuture.get().getGeneration(), sum);
        } catch (Exception e) {
            throw new ComputingException(e);
        }

    }

    private Node getRootParent(){
        int position = this.getPosition();
        int depth = meshNodes.getDepth();
        int width = meshNodes.getWidth();

        int rootParentPosition = position - (depth + 1) * width;

        return meshNodes.getNodeByPosition(rootParentPosition);
    }

    @Override
    public String toString(){

        return String.format("|{%d}{%d}{%d} C:%s S:%s T:%s| ",
                getPosition(),
                getParentPosition(),
                getPrevParentPosition(),
                getResult() == null ? "U" : getResult().getGeneration(),
                getResult() == null ? "U" : getResult().getSum(),
                getNodeName());
    }

}