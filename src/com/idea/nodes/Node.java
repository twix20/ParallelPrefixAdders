package com.idea.nodes;

import com.idea.arithmetic.Bit;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static java.text.MessageFormat.format;

public abstract class Node  {
    public int stage;
    public int position;
    public ExecutorService executor;
    public Node rootParent;
    public Node parent;
    public Node prevParent;

    public boolean a, b;

    public Node(ExecutorService executor, int stage, int position){
        this.executor = executor;
        this.stage = stage;
        this.position = position;
    }

    public abstract boolean getPropagation();

    public abstract boolean getGeneration();

    public String getNodeName(){
        return this.getClass().getName();
    }
}
