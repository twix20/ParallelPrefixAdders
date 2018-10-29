package com.idea.nodes;

import com.idea.arithmetic.Bit;
import com.idea.arithmetic.BitCalculator;

import java.util.concurrent.ExecutorService;

//Red node
public class InitialNode extends Node{

    public InitialNode(ExecutorService executorService, int stage, int position) {
        super(executorService, stage, position);
    }

    @Override
    public boolean getPropagation() {
        return a ^ b;
    }

    @Override
    public boolean getGeneration() {
        return a & b;
    }
}
