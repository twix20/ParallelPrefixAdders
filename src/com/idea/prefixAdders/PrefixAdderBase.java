package com.idea.prefixAdders;

import com.idea.arithmetic.BinaryString;
import com.idea.arithmetic.Bit;
import com.idea.arithmetic.IBinaryStringAdder;
import com.idea.nodes.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class PrefixAdderBase implements IBinaryStringAdder {
    protected int depth;
    protected Map<Integer, Node> meshNodes = new HashMap<>();

    public abstract void insertNodeByPosition(int globalPosition, int index, int stage, int gapSize, Node parent);

    public BinaryString add(BinaryString a, BinaryString b){
        generateMesh(a, b);

        // Perform add
        List<Node> firstStageNodes = getMeshNodesByStage(0);
        firstStageNodes.parallelStream().forEach((node) -> {
            //try {
            //    Thread.sleep(500);
            //} catch (InterruptedException e) {
            //    // TODO Auto-generated catch block
            //    e.printStackTrace();
            //}
            node.tryToComputeResult();
        });

        // Gather result
        List<ResultNode> allResultNodes = getResultMeshNodes();
        String sum = "";
        for(int i = allResultNodes.size() - 1; i >= 0; i--){
            ResultNode n = allResultNodes.get(i);

            sum += Bit.getValue(n.getResult().getSum());
        }

        return new BinaryString(sum);
    }

    private void generateMesh(BinaryString sA, BinaryString sB){
        int indexCounter = 0;

        depth = calculateMeshDepth(sA, sB);
        int length = getLongerLength(sA, sB) + 1;

        sA = alignBinaryString(sA, length);
        sB = alignBinaryString(sB, length);

        for(int stage = 0; stage <= depth + 1; stage++){ // Iterate stages
            int gapSize = (int)Math.pow(2, stage - 1);

            for(int pos = 0; pos < length; pos++) { // Iterate stage positions
                Node parent;
                int parentPosition = indexCounter - length;
                parent = getNodeByPosition(parentPosition);

                if(stage == 0){ // Create Red Squares
                    Bit aBit = Bit.values()[Character.getNumericValue(sA.charAt(sA.length() - 1 - pos))];
                    Bit bBit = Bit.values()[Character.getNumericValue(sB.charAt(sB.length() - 1 - pos))];
                    this.addInitialNode(stage, indexCounter, aBit, bBit);
                }
                else if(stage == depth + 1) { //Create Yellow and Green circles

                    this.addResultNode(stage, indexCounter, parent);
                }
                else {
                    this.insertNodeByPosition(indexCounter, pos, stage, gapSize, parent);
                }

                indexCounter++;
            }
        }
    }

    private int calculateMeshDepth(BinaryString sA, BinaryString sB){
        int longerLength = getLongerLength(sA, sB);

        return closestNextPowerOfTwo(longerLength);
    }

    private int getLongerLength(BinaryString sA, BinaryString sB){
        return Integer.max(sA.length(), sB.length());
    }

    private int closestNextPowerOfTwo(int a) {
        return Integer.BYTES * 8 - Integer.numberOfLeadingZeros(a - 1);
    }

    public BinaryString alignBinaryString(BinaryString stringToPad, int padToLength){
        if(stringToPad.length() < padToLength) {
            String paddedString = String.format("%0" + String.valueOf(padToLength - stringToPad.length()) + "d%s",0,stringToPad.toString());
            stringToPad = new BinaryString(paddedString);
        }
        return stringToPad;
    }

    public Node getNodeByPosition(int pos){
        if(meshNodes.containsKey(pos))
            return meshNodes.get(pos);

        return null;
    }

    public List<Node> getMeshNodesByStage(int stage){
        List<Node> nodesAtStage = new ArrayList<>();

        for (Node n : meshNodes.values()) {
            if(n.getStage() == stage){
                nodesAtStage.add(n);
            }
        }

        return nodesAtStage;
    }

    public List<ResultNode> getResultMeshNodes(){
        return getMeshNodesByStage(depth + 1)
                .stream()
                .map(n -> (ResultNode)n)
                .collect(Collectors.toList());
    }

    protected void addInitialNode(int stage, int pos, Bit aBit, Bit bBit){
        Node nodeToInsert = new InitialNode(stage, pos);
        nodeToInsert.setA(aBit);
        nodeToInsert.setB(bBit);
        nodeToInsert.setRootParent(nodeToInsert);

        meshNodes.put(nodeToInsert.getIndex(), nodeToInsert);
    }
    protected void addWorkerNode(int stage, int pos, Node parent, Node prevParent){
        Node nodeToInsert = new WorkerNode(stage, pos);
        nodeToInsert.setRootParent(parent.getRootParent());
        nodeToInsert.setParent(parent);
        nodeToInsert.setPrevParent(prevParent);

        parent.addChild(nodeToInsert);
        prevParent.addChild(nodeToInsert);

        meshNodes.put(nodeToInsert.getIndex(), nodeToInsert);
    }
    protected void addHangingNode(int stage, int pos, Node parent){
        Node nodeToInsert = new HangingNode(stage, pos);
        nodeToInsert.setRootParent(parent.getRootParent());
        nodeToInsert.setParent(parent);

        parent.addChild(nodeToInsert);
        meshNodes.put(nodeToInsert.getIndex(), nodeToInsert);
    }
    protected void addResultNode(int stage, int pos, Node parent){
        Node prevNode = getNodeByPosition(pos - 1);
        ResultNode prevResultNode = prevNode instanceof ResultNode ? (ResultNode)prevNode : ResultNode.FAKE_NODE;

        Node nodeToInsert = new ResultNode(stage, pos, prevResultNode);
        nodeToInsert.setRootParent(parent.getRootParent());
        nodeToInsert.setParent(parent);

        prevResultNode.addChild(nodeToInsert);
        parent.addChild(nodeToInsert);

        meshNodes.put(nodeToInsert.getIndex(), nodeToInsert);
    }
}
