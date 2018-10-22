//package com.idea.binaryStringAdders;
//
//import com.idea.nodes.Node;
//
//import java.util.concurrent.ExecutorService;
//
//public class LadnerFischerAdder extends PrefixAdderBase {
//
//    public LadnerFischerAdder(ExecutorService executorService) {
//        super(executorService);
//    }
//
//    @Override
//    public void insertNodeByPosition(int globalPosition, int position, int stage, int gapSize, Node parent) {
//        int depth = meshNodes.getDepth();
//
//        if(stage <= depth && gapSize <= position % (gapSize*2)){ // Create Yellow Circles
//            int prevParentPosition = parent.getPosition() - (position % gapSize) - 1;
//            Node prevParent = meshNodes.getNodeByPosition(prevParentPosition);
//
//            addWorkerNode(stage, globalPosition, parent, prevParent);
//        }
//        else if(stage <= depth){ //Create Green Circles
//
//            addHangingNode(stage, globalPosition, parent);
//        }
//    }
//}
