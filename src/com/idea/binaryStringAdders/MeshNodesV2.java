package com.idea.binaryStringAdders;

import com.idea.arithmetic.BinaryString;
import com.idea.arithmetic.Bit;
import com.idea.nodes.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public abstract class MeshNodesV2 {
    private int depth;
    private ExecutorService executorService;
    private BinaryString paddedStringA;
    private BinaryString paddedStringB;

    // Position, Node
    protected Map<Integer, Node> meshByPosition = new HashMap<>();
    protected Map<Integer, List<Node>> meshByStage = new HashMap<>();

    public MeshNodesV2(ExecutorService executorService, BinaryString sA, BinaryString sB){
        int length = getLongerLength(sA, sB) + 1;

        this.executorService = executorService;
        this.paddedStringA = paddBinaryString(sA, length);
        this.paddedStringB = paddBinaryString(sB, length);

        depth = calculateMeshDepth();

        for(int i = 0; i <= depth + 1; i++)
            meshByStage.put(i, new ArrayList<>());

        generateInitialMeshNodes();
        generateResultMeshNodes();
    }

    public abstract Node createNodeByPosition(int position);

    public void insertNode(Node n){
        int position = n.getPosition();
        int stage = calculateStageFromPosition(position);

        //System.out.println(String.format("Inserting node %s Stage:%d", n.toString(), stage));

        meshByPosition.put(position, n);

        List<Node> nodesByStage = meshByStage.get(stage);
        nodesByStage.removeIf(node -> node.getPosition() == position);
        nodesByStage.add(n);
    }

    public Node getNodeByPosition(int pos){
        if(meshByPosition.containsKey(pos)) {
            //System.out.println(String.format("Node at %d exists.", pos));
            return meshByPosition.get(pos);
        }
        else {
            //System.out.println(String.format("Node at %d doesnt exist. Created new", pos));
            insertNode(createNodeByPosition(pos));
            return getNodeByPosition(pos);
        }
    }

    public List<Node> getMeshNodesByStage(int stage){
        return meshByStage.get(stage);
    }

    public List<ResultNode> getResultMeshNodes(){
        return getMeshNodesByStage(getDepth() + 1)
                .stream()
                .map(n -> (ResultNode)n)
                .collect(Collectors.toList());
    }

    private int calculateMeshDepth(){
        return closestNextPowerOfTwo(getWidth() - 1);
    }

    private void generateResultMeshNodes(){
        int depth = getDepth() + 1;
        int meshWidth = getWidth();

        for(int i = 0; i < meshWidth; i++){
            int position = depth * meshWidth + i;

            //System.out.println(position);
            Node node = createResultNode(position);
            insertNode(node);
        }
    }

    private void generateInitialMeshNodes(){
        int meshWidth = getWidth();

        for(int i = 0; i < meshWidth; i++){
            Bit inputA = paddedStringA.bitAt(meshWidth - i - 1);
            Bit inputB = paddedStringB.bitAt(meshWidth - i - 1);

            InitialNode initialNode = createInitialNode(i, inputA, inputB);
            insertNode(initialNode);
        }
    }

    private int closestNextPowerOfTwo(int a) {
        return Integer.BYTES * 8 - Integer.numberOfLeadingZeros(a - 1);
    }

    private BinaryString paddBinaryString(BinaryString stringToPad, int padToLength){
        if(stringToPad.length() < padToLength) {
            String paddedString = String.format("%0" + String.valueOf(padToLength - stringToPad.length()) + "d%s",0,stringToPad.toString());
            stringToPad = new BinaryString(paddedString);
        }
        return stringToPad;
    }


    public int calculateStageFromPosition(int position){
        return position / getWidth();
    }

    public int calculateGapSizeFromPosition(int position) {
        int stage = calculateStageFromPosition(position);
        int gapSize = (int)Math.pow(2, stage - 1);

        return gapSize;
    }

    public int getDepth() {
        return depth;
    }

    public int getLongerLength(BinaryString a, BinaryString b){
        return Integer.max(a.length(), b.length());
    }

    public int getWidth(){ return paddedStringA.length();};



    protected InitialNode createInitialNode(int position, Bit aBit, Bit bBit){
        //System.out.println(String.format("Creating initial node at %d with a:%s b:%s", position, aBit, bBit));
        return new InitialNode(this, executorService, position, aBit, bBit);
    }
    protected WorkerNode createWorkerNode(int position, int parentPosition, int prevParentPosition){
        return new WorkerNode(this, executorService, position, parentPosition, prevParentPosition);
    }
    protected HangingNode createHangingNode(int position, int parentPosition){
        return new HangingNode(this, executorService, position, parentPosition);
    }
    protected ResultNode createResultNode(int pos){
        int meshWidth = getWidth();
        int parentPosition = pos - meshWidth;

        if(pos %  meshWidth == 0){
            return new ResultNode(this, executorService, pos, parentPosition);
        }
        else{
            int prevResultNodePosition = pos - 1;
            return new ResultNode(this, executorService, pos, parentPosition, prevResultNodePosition);
        }
    }


}
