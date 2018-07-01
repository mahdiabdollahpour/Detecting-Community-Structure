/**
 * Created by ASUS on 01/07/2018.
 */

import java.util.*;

/**
 * Created by ASUS on 31/05/2018.
 */


public class Sparce {


    static String address = "N1000MU.5";
    static int size = 1000;
    //    static ArrayList<node> feature1Pair;
//    static ArrayList<node> feature2Pair;
//    static ArrayList<node> feature3Pair;
//    static int[] start1;
//    static int[] start2;
//    static int[] start3;
//    static int[] end1;
//    static int[] end2;
//    static int[] end3;
//
    static SparceMateix f1;
    static SparceMateix f2;
    static SparceMateix f3;

    static int sumF1;
    static int sumF2;
    static int sumF3;
    static double w1;
    static double w2;


    public static void main(String[] args) {


        Vector<Vector<int[]>> graph = Main.readGraph(address + "\\network.txt", size);
        //     Vector<Vector<int[]>> graph = readGraph("mine.txt", 8);

        Main.chapGraph(graph);
        System.gc();
        int n = graph.size();
//        float[][] featurePair = new float[3][n * (n - 1) / 2];
//        float[][] featurePair2 = new float[3][n * (n - 1) / 2];
        f1 = new SparceMateix(n);
        f2 = new SparceMateix(n);
        f3 = new SparceMateix(n);

        int cnt = 0;//pair index
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (Main.hasArc(graph, i, j)) {
                    //featurePair[0][cnt] = 1;

                    f1.addEntry(i, j, 1);
                }

                ArrayList<Integer> arrayList = Main.comNeighbors(graph, i, j);
                // featurePair[1][cnt] = arrayList.size();
                if (arrayList.size() != 0) {
                    f2.addEntry(i, j, arrayList.size());
                }
                // featurePair[2][cnt] = Main.commonEdgedInSet(graph, arrayList);
                int a = Main.commonEdgedInSet(graph, arrayList);
                if (a != 0) {

                    f3.addEntry(i, j, a);
                }
//                System.out.println("for " + (i + 1) + " , " + (j + 1));
//                System.out.println(featurePair[0][cnt]);
//                System.out.println(featurePair[1][cnt]);
//                System.out.println(featurePair[2][cnt]);
                cnt++;
            }

        }

        System.out.println("CNT : " + cnt);


        sumF1 = 0;
        sumF2 = 0;
        sumF3 = 0;
//        for (int i = 0; i < n * (n - 1) / 2; i++) {
//            sumF1 += feature1Pair.;
//            sumF2 += featurePair[1][i];
//            sumF3 += featurePair[2][i];
//        }
        sumF1 = (int) f1.sumAll();
        sumF2 = (int) f2.sumAll();
        sumF3 = (int) f3.sumAll();


        f1.divideby(sumF1);
        f2.divideby(sumF2);
        f3.divideby(sumF3);


        System.out.println("SUMF1 :" + sumF1);
        System.out.println("SUMF2 :" + sumF2);
        System.out.println("SUMF3 :" + sumF3);


        double FE1 = 0;
        double FE2 = 0;
        double FE3 = 0;


        for (int i = 0; i < f1.matrix.size(); i++) {
            FE1 += f1.valueOf(i) * Math.log(f1.valueOf(i));
        }
        for (int i = 0; i < f2.matrix.size(); i++) {
            FE2 += f2.valueOf(i) * Math.log(f2.valueOf(i));
        }
        for (int i = 0; i < f3.matrix.size(); i++) {
            FE3 += f3.valueOf(i) * Math.log(f3.valueOf(i));
        }

        FE1 *= -1;
        FE2 *= -1;
        FE3 *= -1;
        System.out.println("FE 1 : " + FE1);
        System.out.println("FE 2 : " + FE2);
        System.out.println("FE 3 : " + FE3);

        if (FE2 == 0) {
            w1 = Math.abs(FE1) * (100000 / FE1);
        } else {

            w1 = FE1 / FE2;
        }
        if (FE3 == 0) {
            w2 = Math.abs(FE1) * (100000 / FE1);
        } else {
            w2 = FE1 / FE3;
        }
        //     w1 = 1;
        //     w2 = 1;
        System.out.println("w1 : " + w1);
        System.out.println("w2 : " + w2);

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                System.out.print("for " + (i + 1) + " & " + (j + 1) + " : " + getP(i, j) + " , ");
            }
            System.out.println();
        }
        int[] labels = new int[n];
        int[] orders = new int[n];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = i;
            orders[i] = i;
        }
        final int allPases = 150;
        int num_passes = 0;
        boolean changed = true;
        while (changed && num_passes < allPases) {
            orders = Main.random_shuffle(orders);
            num_passes++;
            changed = false;
            for (int counter = 0; counter < n; counter++) {
                int i = orders[counter];
//                if (skipThis[i]) {
//                    continue;
//                }
//                skipThis[i] = true;
                int[] labelScore = new int[n];
                for (int d = 0; d < n; d++) {
                    if (d != i) {
                        labelScore[labels[d]] += getP(d, i);
                    }

                }


                int maxAmount = labelScore[labels[i]];
                ArrayList<Integer> maxLabels = new ArrayList<>();
                //  int maxLabel = labels[i];
                for (int i1 = 0; i1 < labelScore.length; i1++) {
                    if (labelScore[i1] > maxAmount) {
                        // System.out.println("diff : " + (labelScore[i1] - maxAmount));
                        maxAmount = labelScore[i1];
                        maxLabels = new ArrayList<>();
                        maxLabels.add(i1);
                        //  maxLabel = i1;
                        //   changed = true;
                        //      System.out.println("hi");
                    } else if (labelScore[i1] == maxAmount) {
                        maxLabels.add(i1);
                        //   changed = true;
                    }
                }
                int old = 0;
                if (!maxLabels.contains(labels[i])) {
                    old = labels[i];
                    labels[i] = maxLabels.get((int) (Math.random() * maxLabels.size()));
                    //  labels[i] = newLabels[i];// just testing it in this way => bad thing to do
                    changed = true;
                    System.out.println(num_passes + ". label of " + (i + 1) + " goes from " + (old + 1) + " to " + (labels[i] + 1));
                } else {

                    System.out.println(num_passes + ". label of " + (i + 1) + " stays " + (labels[i] + 1));
                }
//                for (int j = 0; j < n; j++) {
//                    if (labels[j] == newLabels[i]) {
//                        skipThis[j] = true;
//                    }
//                }


            }
            //  labels = newLabels.clone();
        }
        Main.printComms(labels);

        Vector<Integer> v = new Vector<>();
        //new Vector(Arrays.asList(labels));
        v.add(0, 0);
        for (int i = 0; i < labels.length; i++) {
            v.add(labels[i]);
        }
        try {
            System.out.println(Main.NMI(v, address + "\\community.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    static double getP(int i, int j) {
        System.out.println("p requested");

        double r = f1.getEntry(i, j) * sumF1 + f2.getEntry(i, j) * sumF2 * w1 + f3.getEntry(i, j) * sumF3 * w2;
        System.out.println("p given");
        return r;
    }


}



