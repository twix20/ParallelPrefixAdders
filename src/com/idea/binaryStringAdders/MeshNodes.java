package com.idea.binaryStringAdders;

import com.idea.arithmetic.BinaryString;
import com.idea.nodes.Node;
import com.idea.nodes.ResultNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MeshNodes {
    private int depth, longerLength;

    // Position, Node
    protected ConcurrentHashMap<Integer, Node> meshByPosition = new ConcurrentHashMap<>();
    protected ConcurrentHashMap<Integer, List<Node>> meshByStage = new ConcurrentHashMap<>();

    public MeshNodes(BinaryString sA, BinaryString sB){
        depth = calculateMeshDepth(sA, sB);

        for(int i = 0; i <= depth + 1; i++)
            meshByStage.put(i, new ArrayList<>());
    }

    public void insertNode(Node n){
        meshByPosition.put(n.position, n);

        int stage = n.stage;
        List<Node> nodesByStage = meshByStage.get(stage);
        nodesByStage.add(n);
    }

    public Node getNodeByPosition(int pos){
        if(meshByPosition.containsKey(pos))
            return meshByPosition.get(pos);

        return null;
    }

    public List<Node> getMeshNodesByStage(int stage){
        return meshByStage.get(stage);
    }

    public List<ResultNode> getResultMeshNodes(){
        return getMeshNodesByStage(depth + 1)
                .stream()
                .map(n -> (ResultNode)n)
                .collect(Collectors.toList());
    }

    private int calculateMeshDepth(BinaryString sA, BinaryString sB){
        longerLength = getLongerLength(sA, sB);

        return closestNextPowerOfTwo(longerLength);
    }

    private int getLongerLength(BinaryString sA, BinaryString sB){
        return Integer.max(sA.length(), sB.length());
    }

    private int closestNextPowerOfTwo(int a) {
        return Integer.BYTES * 8 - Integer.numberOfLeadingZeros(a - 1);
    }


    public int getDepth(){
        return this.depth;
    }

    public int getLongerLength(){
        return this.longerLength;
    }
}
