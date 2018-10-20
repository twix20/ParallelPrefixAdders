package com.idea.prefixAdders;

import com.idea.arithmetic.BinaryString;
import com.idea.arithmetic.Bit;
import com.idea.arithmetic.IBinaryStringAdder;
import com.idea.nodes.*;

import java.util.List;

public abstract class PrefixAdderBase implements IBinaryStringAdder {
    protected MeshNodes meshNodes;

    public abstract void insertNodeByPosition(int globalPosition, int position, int stage, int gapSize, Node parent);

    public BinaryString add(BinaryString a, BinaryString b){
        if(meshNodes == null)
            generateMesh(a, b);

        // Perform add
        List<Node> firstStageNodes = meshNodes.getMeshNodesByStage(0);
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
        List<ResultNode> allResultNodes = meshNodes.getResultMeshNodes();
        String sum = "";
        for(int i = allResultNodes.size() - 1; i >= 0; i--){
            ResultNode n = allResultNodes.get(i);

            sum += Bit.getValue(n.getResult().getSum());
        }

        return new BinaryString(sum);
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
                    Bit aBit = Bit.values()[Character.getNumericValue(sA.charAt(sA.length() - 1 - pos))];
                    Bit bBit = Bit.values()[Character.getNumericValue(sB.charAt(sB.length() - 1 - pos))];
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
        Node nodeToInsert = new InitialNode(stage, pos);
        nodeToInsert.setA(aBit);
        nodeToInsert.setB(bBit);
        nodeToInsert.setRootParent(nodeToInsert);

        meshNodes.insertNode(nodeToInsert);
    }
    protected void addWorkerNode(int stage, int pos, Node parent, Node prevParent){
        Node nodeToInsert = new WorkerNode(stage, pos);
        nodeToInsert.setRootParent(parent.getRootParent());
        nodeToInsert.setParent(parent);
        nodeToInsert.setPrevParent(prevParent);

        parent.addChild(nodeToInsert);
        prevParent.addChild(nodeToInsert);

        meshNodes.insertNode(nodeToInsert);
    }
    protected void addHangingNode(int stage, int pos, Node parent){
        Node nodeToInsert = new HangingNode(stage, pos);
        nodeToInsert.setRootParent(parent.getRootParent());
        nodeToInsert.setParent(parent);

        parent.addChild(nodeToInsert);

        meshNodes.insertNode(nodeToInsert);
    }
    protected void addResultNode(int stage, int pos, Node parent){
        Node prevNode = meshNodes.getNodeByPosition(pos - 1);
        ResultNode prevResultNode = prevNode instanceof ResultNode ? (ResultNode)prevNode : ResultNode.FAKE_NODE;

        Node nodeToInsert = new ResultNode(stage, pos, prevResultNode);
        nodeToInsert.setRootParent(parent.getRootParent());
        nodeToInsert.setParent(parent);

        prevResultNode.addChild(nodeToInsert);
        parent.addChild(nodeToInsert);

        meshNodes.insertNode(nodeToInsert);
    }
}
