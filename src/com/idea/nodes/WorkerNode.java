package com.idea.nodes;

import com.idea.arithmetic.Bit;
import com.idea.arithmetic.BitCalculator;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

//Yellow circle
public class WorkerNode extends Node {

    public WorkerNode(ExecutorService executorService, int stage, int position) {
        super(executorService, stage, position);
    }

    @Override
    public boolean getPropagation() {
        return parent.getPropagation() & prevParent.getPropagation();
    }

    @Override
    public boolean getGeneration() {
        return (parent.getPropagation() & prevParent.getGeneration()) | parent.getGeneration();
    }
}