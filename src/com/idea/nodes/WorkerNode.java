package com.idea.nodes;

import com.idea.arithmetic.Bit;
import com.idea.arithmetic.BitCalculator;
import com.idea.binaryStringAdders.MeshNodesV2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

//Yellow circle
public class WorkerNode extends Node {


    public WorkerNode(MeshNodesV2 meshNodes, ExecutorService executor, int position, int parentPosition, int prevParentPosition) {
        super(meshNodes, executor, position, parentPosition, prevParentPosition);
    }

    @Override
    protected NodeComputingResult computeResultInternal() throws ComputingException {
        Future<NodeComputingResult> previousParentFuture = getPrevParent().computeResult();
        Future<NodeComputingResult> parentFuture = getParent().computeResult();

        try {
            NodeComputingResult parentResult = parentFuture.get();
            NodeComputingResult prevParentResult = previousParentFuture.get();

            Bit newPropagation = BitCalculator.and(parentResult.getPropagation(), prevParentResult.getPropagation());
            Bit newGeneration = BitCalculator.or(BitCalculator.and(parentResult.getPropagation(), prevParentResult.getGeneration()), parentResult.getGeneration());

            return new NodeComputingResult(newPropagation, newGeneration);
        } catch (Exception e) {
            throw new ComputingException(e);
        }

    }

}