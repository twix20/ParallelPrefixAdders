package com.idea.prefixAdders;

import com.idea.nodes.Node;

public class KoggeStoneAdder extends PrefixAdderBase {
    @Override
    public void insertNodeByPosition(int globalPosition, int index, int stage, int gapSize, Node parent) {
        if(stage <= depth && gapSize <= index){ // Create Yellow Circles
            int prevParentPosition = parent.getIndex() - gapSize;
            Node prevParent = getNodeByPosition(prevParentPosition);

            addWorkerNode(stage, globalPosition, parent, prevParent);
        }
        else if(stage <= depth){ //Create Green Circles

            addHangingNode(stage, globalPosition, parent);
        }
    }
}
