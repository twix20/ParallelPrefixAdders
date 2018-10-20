package com.idea.nodes;

import com.idea.arithmetic.Bit;

import java.util.ArrayList;
import java.util.List;

public abstract class Node  {
    private int stage, index;
    private Node rootParent;
    private Node parent;
    private Node prevParent;
    private List<Node> children;
    private NodeComputingResult result;
    private Bit a, b;

    public Node(int stage, int index){
        this.children = new ArrayList<Node>();

        this.stage = stage;
        this.index = index;
    }

    public abstract void computeResult();

    public boolean isComputed() {
        return this.getResult() != null;
    }

    public synchronized void tryToComputeResult(){

        //Check if result has already been computed
        if(isComputed())
        {
            return;
        }

        // Check if all parents are computed to proceed
        if(!areParentsComputed()) return;

        computeResult();

        computeChildren();
    }

    public void computeChildren(){
        getChildren().parallelStream().forEach((node) -> {
            node.tryToComputeResult();
        });
    }


    public String getNodeName(){
        return this.getClass().getName();
    }

    public void addChild(Node child){
        this.children.add(child);
    }

    @Override
    public String toString(){

        int prevParentPos = getPrevParent() == null ? -1 : getPrevParent().getIndex();

        return String.format("|{%1$d}{%2$d} P:%3$s G:%4$s T:%5$s| ",
                getIndex(),
                prevParentPos,
                getResult().getPropagation(),
                getResult().getGeneration(),
                getNodeName());
    }

    private Boolean areParentsComputed(){
        if(parent != null && !parent.isComputed()){
            return false;
        }

        if(prevParent != null && !prevParent.isComputed()){
            return false;
        }

        return true;
    }

    //Getters and setters
    public int getStage() {
        return stage;
    }

    public int getIndex() {
        return index;
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

    public List<Node> getChildren() {
        return children;
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
}
