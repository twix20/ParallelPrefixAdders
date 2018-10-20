package com.idea.nodes;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

//Green node
public class HangingNode extends Node{

    public HangingNode(ExecutorService executorService, int stage, int position) {
        super(executorService, stage, position);
    }

    @Override
    protected NodeComputingResult computeResultInternal() throws ComputingException {
        Future<NodeComputingResult> parentResultFuture = getParent().computeResult();
        try {
            NodeComputingResult parentResult = parentResultFuture.get();
            return new NodeComputingResult(parentResult.getPropagation(), parentResult.getGeneration());
        } catch (Exception e) {
            throw new ComputingException(e);
        }

    }

}
