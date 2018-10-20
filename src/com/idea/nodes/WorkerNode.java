package com.idea.nodes;

import com.idea.arithmetic.Bit;
import com.idea.arithmetic.BitCalculator;

//Yellow circle
public class WorkerNode extends Node{

    public WorkerNode(int stage, int index) {
        super(stage, index);
    }

    @Override
    public void computeResult() {
        NodeComputingResult parentResult = getParent().getResult();
        NodeComputingResult prevParentResult = getPrevParent().getResult();

        Bit newPropagation = BitCalculator.and(parentResult.getPropagation(), prevParentResult.getPropagation());
        Bit newGeneration = BitCalculator.or(BitCalculator.and(parentResult.getPropagation(), prevParentResult.getGeneration()), parentResult.getGeneration());

        this.setResult(new NodeComputingResult(newPropagation, newGeneration));
    }

}