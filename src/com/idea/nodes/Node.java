package com.idea.nodes;

import com.idea.arithmetic.Bit;
import com.idea.binaryStringAdders.MeshNodesV2;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static java.text.MessageFormat.format;

public abstract class Node  {
    protected Object lock = new Object();

    private int position;
    private NodeComputingResult result;
    private ExecutorService executor;
    protected MeshNodesV2 meshNodes;
    private int parentPosition;
    private int prevParentPosition;
    private Bit a, b;

    public Node(MeshNodesV2 meshNodes, ExecutorService executor, int position, int parentPosition, int prevParentPosition){
        this.meshNodes = meshNodes;
        this.executor = executor;
        this.position = position;
        this.parentPosition = parentPosition;
        this.prevParentPosition = prevParentPosition;
    }

    public Node(MeshNodesV2 meshNodes, ExecutorService executor, int position, int parentPosition){
        this(meshNodes, executor, position, parentPosition, -1);
    }

    public Node(MeshNodesV2 meshNodes, ExecutorService executor, int position){
        this(meshNodes, executor, position, -1, -1);
    }


    private synchronized NodeComputingResult getComputedResult() throws ComputingException {
        if (this.getResult() == null) {

            NodeComputingResult r = this.computeResultInternal();

            //this.setResult(new NodeComputingResult(Bit.Zero, Bit.Zero));
            this.setResult(r);
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
        return this.getClass().getSimpleName();
    }

    public Node getParent() {
        return this.meshNodes.getNodeByPosition(this.getParentPosition());
    }

    public Node getPrevParent() { return this.meshNodes.getNodeByPosition(this.prevParentPosition);}


    @Override
    public String toString(){

        return String.format("|{%1$d}{%2$d}{%3$d}P:%4$s G:%5$s T:%6$s| ",
                getPosition(),
                getParentPosition(),
                getPrevParentPosition(),
                getResult() == null ? "" : getResult().getPropagation(),
                getResult() == null ? "" : getResult().getGeneration(),
                getNodeName());
    }

    //Getters and setters
    public int getPosition() {
        return position;
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

    public void setA(Bit a) {
        this.a = a;
    }

    public void setB(Bit b) {
        this.b = b;
    }

    protected ExecutorService getExecutorService() { return executor; }

    public int getParentPosition() {
        return parentPosition;
    }

    public int getPrevParentPosition() {
        return prevParentPosition;
    }

}
