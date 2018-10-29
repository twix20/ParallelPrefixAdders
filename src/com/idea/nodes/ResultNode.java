package com.idea.nodes;

import com.idea.arithmetic.Bit;
import com.idea.arithmetic.BitCalculator;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.text.MessageFormat.format;

public class ResultNode extends Node {
    public static ResultNode FAKE_NODE;
    static {
       FAKE_NODE = new ResultNode(Executors.newSingleThreadExecutor(), -1, -1, null);
    }

    public boolean getSum(){
        boolean prevCarry = ((ResultNode)prevParent).getCarry();

        return prevCarry ^ rootParent.getPropagation();
    }

    public boolean getCarry() {
        if(stage == -1)
            return  false;

        return getGeneration();
    }

    @Override
    public boolean getPropagation() {
        return false;
    }

    @Override
    public boolean getGeneration() {
        return parent.getGeneration();
    }

    public ResultNode(ExecutorService executorService, int stage, int position, ResultNode prevParent) {
        super(executorService, stage, position);

        this.prevParent = prevParent;
    }
}