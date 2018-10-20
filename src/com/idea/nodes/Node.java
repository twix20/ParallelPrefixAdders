package com.idea.nodes;

import com.idea.arithmetic.Bit;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static java.text.MessageFormat.format;

public abstract class Node  {
    private int stage;
    private int position;
    private ExecutorService executor;
    private Node rootParent;
    private Node parent;
    private Node prevParent;
    private NodeComputingResult result;
    private Bit a, b;

    public Node(ExecutorService executor, int stage, int position){
        this.executor = executor;
        this.stage = stage;
        this.position = position;
    }

    private synchronized NodeComputingResult getComputedResult() throws ComputingException {
        if(this.getResult() == null) {
            this.setResult(this.computeResultInternal());
        }

        return this.getResult();
    }

    protected abstract NodeComputingResult computeResultInternal() throws ComputingException;

    public final Future<NodeComputingResult> computeResult() {

        return getExecutorService().submit(() -> {
//            System.out.println(format("Submitted: {0} for {1}", Thread.currentThread().getId(), this.getClass().getSimpleName()));

            return this.getComputedResult();
        });
    }

    public String getNodeName(){
        return this.getClass().getName();
    }


    @Override
    public String toString(){

        int prevParentPos = getPrevParent() == null ? -1 : getPrevParent().getPosition();

        return String.format("|{%1$d}{%2$d} P:%3$s G:%4$s T:%5$s| ",
                getPosition(),
                prevParentPos,
                getResult().getPropagation(),
                getResult().getGeneration(),
                getNodeName());
    }

    //Getters and setters
    public int getStage() {
        return stage;
    }

    public int getPosition() {
        return position;
    }

    public Node getRootParent() {
        return rootParent;
    }

    public Node getParent() {
        return parent;
    }

    public Node getPrevParent() {
        return prevParent;
    }

    public NodeComputingResult getResult() {
        return result;
    }

    public void setResult(NodeComputingResult result) {
        this.result = result;
    }

    public Bit getA() {
        return a;
    }

    public Bit getB() {
        return b;
    }

    public void setRootParent(Node rootParent) {
        this.rootParent = rootParent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setPrevParent(Node prevParent) {
        this.prevParent = prevParent;
    }

    public void setA(Bit a) {
        this.a = a;
    }

    public void setB(Bit b) {
        this.b = b;
    }

    protected ExecutorService getExecutorService() { return executor; }
}
