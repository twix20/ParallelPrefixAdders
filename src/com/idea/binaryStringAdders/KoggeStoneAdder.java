package com.idea.binaryStringAdders;

import com.idea.nodes.Node;

public class KoggeStoneAdder extends PrefixAdderBase {
    @Override
    public void insertNodeByPosition(int globalPosition, int position, int stage, int gapSize, Node parent) {
        int depth = meshNodes.getDepth();
        if(stage <= depth && gapSize <= position){ // Create Yellow Circles
            int prevParentPosition = parent.getPosition() - gapSize;
            Node prevParent = meshNodes.getNodeByPosition(prevParentPosition);

            addWorkerNode(stage, globalPosition, parent, prevParent);
        }
        else if(stage <= depth){ //Create Green Circles

            addHangingNode(stage, globalPosition, parent);
        }
    }
}
