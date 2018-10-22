package com.idea.binaryStringAdders;

import com.idea.arithmetic.BinaryString;
import com.idea.arithmetic.Bit;
import com.idea.arithmetic.IBinaryStringAdder;
import com.idea.nodes.NodeComputingResult;
import com.idea.nodes.ResultNode;

import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class PrefixAdderSolver implements IBinaryStringAdder {
    MeshNodesV2 meshNodes;


    public PrefixAdderSolver(MeshNodesV2 meshNodes){
        this.meshNodes = meshNodes;
    }

    @Override
    public BinaryString add(BinaryString a, BinaryString b) {
        // Get result nodes
        List<ResultNode> allResultNodes = meshNodes.getResultMeshNodes();

        // Get final result futures
        List<Future<NodeComputingResult>> futures = allResultNodes.stream().map(ResultNode::computeResult).collect(Collectors.toList());

        String result = futures.stream()
                .map(this::getFutureResultUncheckException)
                .map(NodeComputingResult::getSum)
                .map(Bit::getValue)
                .map(bigInt -> bigInt.toString())
                .collect(supplyReversingCollector());

        return new BinaryString(result);
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
}
