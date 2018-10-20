package com.idea.nodes;

import com.idea.arithmetic.Bit;
import com.idea.arithmetic.BitCalculator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

//Yellow circle
public class WorkerNode extends Node {

    public WorkerNode(ExecutorService executorService, int stage, int position) {
        super(executorService, stage, position);
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