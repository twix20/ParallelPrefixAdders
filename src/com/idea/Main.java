package com.idea;

import com.idea.arithmetic.BinaryString;
import com.idea.binaryStringAdders.*;
import com.idea.nodes.Node;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "30000");

        BinaryString a = new BinaryString("1001");
        BinaryString b = new BinaryString("1100");

        ExecutorService executorService = Executors.newFixedThreadPool(3000);

        KoggeStoneMeshNodes mesh = new KoggeStoneMeshNodes(executorService, a ,b);
        PrefixAdderSolver koggeStoneSolver = new PrefixAdderSolver(mesh);
        BinaryString result = koggeStoneSolver.add(a, b);

        for(int i = 0; i < 4; i++){
            List<Node> nodesOnStage = mesh.getMeshNodesByStage(i);

            Collections.sort(nodesOnStage, new Comparator<Node>(){
                public int compare(Node s1, Node s2) {
                    return s1.getPosition() > s2.getPosition() ? -1 : 1;
                }
            });

            nodesOnStage.stream().forEach(n -> System.out.print(n.toString() + " "));
            System.out.println();
        }

        System.out.println(result);



        //KoggeStoneAdder koggeStoneAdder = new KoggeStoneAdder(executorService);
        //LadnerFischerAdder ladnerFischerAdder = new LadnerFischerAdder(executorService);
        //SequentialAdder sequentialAdder = new SequentialAdder();

        //koggeStoneAdder.generateMesh(a, b);
        //ladnerFischerAdder.generateMesh(a, b);

        //System.out.println(koggeStoneAdder.add(a, b));
        //System.out.println("after kogge: " + System.currentTimeMillis());
//        System.out.println(ladnerFischerAdder.add(a, b));
//        System.out.println(System.currentTimeMillis());
//        System.out.println(sequentialAdder.add(a, b));
//        System.out.println(System.currentTimeMillis());
    }

    // Binary to decimal: https://www.mathsisfun.com/binary-decimal-hexadecimal-converter.html
    //Correct results proven by wolfram alpha
    // 1#:
    //      a: 001111111111000000000000000111111111111111111110000000000000000000000001111110000000111111
    //      b: 0111111100000011111101011111010110000000000000000000000000000011111111111101011111111110001110
    //      r: 01000001100000010111101011111011101111111111111111110000000000011111111111111011101111111001101

    // 2#:
    //      a: 0000000000000000000000000000000000000000000111111111111111111111111111111111111111111111111111
    //      b: 1111111111111111111111111111111111111111111000000000000000000000000000000000000000000000000000
    //      r: 01111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111

    // 3#:
    //      a: 00011111111100001111111111000000111111111111111000000000000001111111111111111100000000000000010101111111111111000000000000000000000000000000000000000000000000000111111111111111111111111111111111111111111111111111
    //      b: 111111111111111111100001111111100011111111111111110000000111111111110101010101010101010101010111111111111111101010101111111111111011111111111111111111111111000000000000000000000000000000000000000000000000000
    //      r: 000100111111100001111111011010000111100011111110111111110000010111111111110100110101010101011000000111111111110111101010101111111111111011111111111111111111111111111111111111111111111111111111111111111111111111111
}
