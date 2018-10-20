package com.idea.nodes;

import com.idea.arithmetic.Bit;

public class NodeComputingResult {
    private Bit propagation;
    private Bit generation;
    private Bit sum;

    public NodeComputingResult(Bit propagation, Bit generation){
        this.propagation = propagation;
        this.generation = generation;
    }

    public Bit getPropagation() {
        return propagation;
    }

    public Bit getGeneration() {
        return generation;
    }

    public Bit getSum() {
        return sum;
    }

    public void setSum(Bit sum) {
        this.sum = sum;
    }
}
