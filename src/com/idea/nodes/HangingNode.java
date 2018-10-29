package com.idea.nodes;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

//Green node
public class HangingNode extends Node{

    public HangingNode(ExecutorService executorService, int stage, int position) {
        super(executorService, stage, position);
    }

    @Override
    public boolean getPropagation() {
        return parent.getPropagation();
    }

    @Override
    public boolean getGeneration() {
        return parent.getGeneration();
    }
}
