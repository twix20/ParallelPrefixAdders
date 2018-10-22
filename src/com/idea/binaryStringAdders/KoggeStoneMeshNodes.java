package com.idea.binaryStringAdders;

import com.idea.arithmetic.BinaryString;
import com.idea.nodes.Node;

import java.util.concurrent.ExecutorService;

public class KoggeStoneMeshNodes extends MeshNodesV2 {

    public KoggeStoneMeshNodes(ExecutorService executorService, BinaryString sA, BinaryString sB) {
        super(executorService, sA, sB);
    }

    @Override
    public Node createNodeByPosition(int position)  {
        int depth = getDepth();
        int stage = calculateStageFromPosition(position);
        int gapSize = calculateGapSizeFromPosition(position);
        int meshWidth = getWidth();

        int parentPosition = position - meshWidth;
        int index = position - meshWidth * stage;
        if(stage <= depth && index >= gapSize ){// Create Yellow Circles
            int prevParentPosition = parentPosition - gapSize;

            return createWorkerNode(position, parentPosition, prevParentPosition);
        }else if(stage <= depth){ //Create Green Circles
            return createHangingNode(position, parentPosition);
        }

        return null;
    }
}
