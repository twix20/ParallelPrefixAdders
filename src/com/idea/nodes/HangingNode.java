package com.idea.nodes;

//Green node
public class HangingNode extends Node{

    public HangingNode(int stage, int position) {
        super(stage, position);
    }

    @Override
    public void computeResult() {
        NodeComputingResult parentResult = getParent().getResult();
        NodeComputingResult result = new NodeComputingResult(parentResult.getPropagation(), parentResult.getGeneration());

        this.setResult(result);
    }
}
