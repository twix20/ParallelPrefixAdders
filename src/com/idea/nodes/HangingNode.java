package com.idea.nodes;

import com.idea.binaryStringAdders.MeshNodesV2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

//Green node
public class HangingNode extends Node {


    public HangingNode(MeshNodesV2 meshNodes, ExecutorService executor, int position, int parentPosition) {
        super(meshNodes, executor, position, parentPosition);
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
