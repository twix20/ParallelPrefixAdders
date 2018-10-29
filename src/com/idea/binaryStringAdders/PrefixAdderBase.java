package com.idea.binaryStringAdders;

import com.idea.arithmetic.BinaryString;
import com.idea.arithmetic.Bit;
import com.idea.arithmetic.IBinaryStringAdder;
import com.idea.nodes.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class PrefixAdderBase implements IBinaryStringAdder {
    protected MeshNodes meshNodes;

    private ExecutorService executorService;

    public PrefixAdderBase(ExecutorService executorService) {

        this.executorService = executorService;
    }

    public abstract void insertNodeByPosition(int globalPosition, int position, int stage, int gapSize, Node parent);


    public BinaryString add(BinaryString a, BinaryString b){
        if(meshNodes == null)
            generateMesh(a, b);

        // Get result nodes
        List<ResultNode> allResultNodes = meshNodes.getResultMeshNodes();
        String r = "";


        try{
            //r = allResultNodes
            //        .parallelStream()
            //        .map(resultNode -> resultNode.getSum() ? "1" : "0")
            //        .reduce("", (acc, s) -> s + acc);

            r = executorService.submit(
                    () -> allResultNodes
                            .parallelStream()
                            .map(resultNode -> resultNode.getSum() ? "1" : "0")
                            .reduce("", (acc, s) -> s + acc)).get();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return new BinaryString(r);
    }

    private <T> T getFutureResultUncheckException(Future<T> future) {
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Collector<Object, StringBuilder, String> supplyReversingCollector(){
         return Collector.of(StringBuilder::new,
                (stringBuilder, str) -> stringBuilder.insert(0, str),
                StringBuilder::append,
                StringBuilder::toString);
    }


    public void generateMesh(BinaryString sA, BinaryString sB){
        meshNodes = new MeshNodes(sA, sB);

        int length = meshNodes.getLongerLength() + 1;
        int depth = meshNodes.getDepth();

        sA = alignBinaryString(sA, length);
        sB = alignBinaryString(sB, length);

        int posCounter = 0;
        for(int stage = 0; stage <= depth + 1; stage++){ // Iterate stages
            int gapSize = (int)Math.pow(2, stage - 1);

            for(int pos = 0; pos < length; pos++) { // Iterate stage positions
                Node parent;
                int parentPosition = posCounter - length;
                parent = meshNodes.getNodeByPosition(parentPosition);

                if(stage == 0){ // Create Red Squares
                    Bit aBit = sA.bitAt(sA.length() - 1 - pos);
                    Bit bBit = sB.bitAt(sB.length() - 1 - pos);
                    this.addInitialNode(stage, posCounter, aBit, bBit);
                }
                else if(stage == depth + 1) { //Create Yellow and Green circles

                    this.addResultNode(stage, posCounter, parent);
                }
                else {
                    this.insertNodeByPosition(posCounter, pos, stage, gapSize, parent);
                }

                posCounter++;
            }
        }
    }

    private BinaryString alignBinaryString(BinaryString stringToPad, int padToLength){
        if(stringToPad.length() < padToLength) {
            String paddedString = String.format("%0" + String.valueOf(padToLength - stringToPad.length()) + "d%s",0,stringToPad.toString());
            stringToPad = new BinaryString(paddedString);
        }
        return stringToPad;
    }

    protected void addInitialNode(int stage, int pos, Bit aBit, Bit bBit){
        Node nodeToInsert = new InitialNode(executorService, stage, pos);
        nodeToInsert.a = aBit == Bit.One;
        nodeToInsert.b = bBit == Bit.One;
        nodeToInsert.rootParent = nodeToInsert;

        meshNodes.insertNode(nodeToInsert);
    }
    protected void addWorkerNode(int stage, int pos, Node parent, Node prevParent){
        Node nodeToInsert = new WorkerNode(executorService, stage, pos);
        nodeToInsert.rootParent = parent.rootParent;
        nodeToInsert.parent = parent;
        nodeToInsert.prevParent = prevParent;

        meshNodes.insertNode(nodeToInsert);
    }
    protected void addHangingNode(int stage, int pos, Node parent){
        Node nodeToInsert = new HangingNode(executorService, stage, pos);
        nodeToInsert.rootParent = parent.rootParent;
        nodeToInsert.parent = parent;

        meshNodes.insertNode(nodeToInsert);
    }
    protected void addResultNode(int stage, int pos, Node parent){
        Node prevNode = meshNodes.getNodeByPosition(pos - 1);
        ResultNode prevResultNode = prevNode instanceof ResultNode ? (ResultNode)prevNode : ResultNode.FAKE_NODE;

        Node nodeToInsert = new ResultNode(executorService, stage, pos, prevResultNode);
        nodeToInsert.rootParent = parent.rootParent;
        nodeToInsert.parent = parent;

        meshNodes.insertNode(nodeToInsert);
    }
}
