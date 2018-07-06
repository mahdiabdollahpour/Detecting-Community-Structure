
import java.util.*;

/**
 * Created by ASUS on 31/05/2018.
 */


public class NormalWay {

    public static void main(String[] args) {
//            detect("N1000MU0.1",1000);
        detect("cases\\TestCase1-N1000-k15-mu45", 1000);
//        detect("cases\\TestCase2-N1000-k5-mu50", 1000);
//        detect("cases\\TestCase3-N1000-k5-mu55", 1000);
//        detect("cases\\TestCase4-N1000-k5-mu60", 1000);
//        detect("cases\\TestCase5-N1000-k20-mu10", 1000);
//        detect("cases\\TestCase6-N1000-k20-mu45", 1000);
//        detect("cases\\TestCase7-N1000-k20-mu50", 1000);
//        detect("cases\\TestCase8-N1000-k20-mu55", 1000);
//        detect("cases\\TestCase9-N10000-k20-mu45", 10000);
//        detect("cases\\TestCase10-N10000-k20-mu50", 10000);
//        detect("cases\\TestCase11-N50000-k20-mu45", 50000);
//        detect("cases\\TestCase12-N50000-k20-mu50", 50000);


    }

    public static void detect(String addres, int siz) {


        Vector<Vector<int[]>> graph = MyUtils.readGraph(addres + "\\network.txt", siz);
        //     Vector<Vector<int[]>> graph = readGraph("mine.txt", 8);
        MyUtils.chapGraph(graph);
        System.gc();
        int n = graph.size();
        float[][] featurePair = new float[3][n * (n - 1) / 2];
        float[][] featurePair2 = new float[3][n * (n - 1) / 2];
        int cnt = 0;//pair index
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (MyUtils.hasArc(graph, i, j)) {
                    featurePair[0][cnt] = 1;
                }

                ArrayList<Integer> arrayList = MyUtils.comNeighbors(graph, i, j);
                int a = arrayList.size();
                featurePair[1][cnt] = a;
                if (a > 1) {
                    featurePair[2][cnt] = MyUtils.commonEdgedInSet(graph, arrayList);
                }
                System.out.println("for " + (i + 1) + " , " + (j + 1));
                System.out.println(featurePair[0][cnt]);
                System.out.println(featurePair[1][cnt]);
                System.out.println(featurePair[2][cnt]);
                cnt++;
            }
        }
        System.out.println("CNT : " + cnt);


        int sumF1 = 0;
        int sumF2 = 0;
        int sumF3 = 0;
        for (int i = 0; i < n * (n - 1) / 2; i++) {
            sumF1 += featurePair[0][i];
            sumF2 += featurePair[1][i];
            sumF3 += featurePair[2][i];
        }
        for (int i = 0; i < n * (n - 1) / 2; i++) {
            featurePair2[0][i] = featurePair[0][i] / sumF1;
            featurePair2[1][i] = featurePair[1][i] / sumF2;
            if (sumF3 != 0) {
                featurePair2[2][i] = featurePair[2][i] / sumF3;
            }
        }
        System.out.println("SUMF3 :" + sumF3);

        for (int i = 0; i < n * (n - 1) / 2 && i < 30; i++) {
            System.out.print(featurePair[2][i] + " , ");
        }
        System.out.println();
        for (int i = 0; i < n * (n - 1) / 2 && i < 30; i++) {
            System.out.print(featurePair2[0][i] + " , ");
        }
        System.out.println();
        for (int i = 0; i < n * (n - 1) / 2 && i < 30; i++) {
            System.out.print(featurePair2[1][i] + " , ");
        }
        System.out.println();
        for (int i = 0; i < n * (n - 1) / 2 && i < 30; i++) {
            System.out.print(featurePair2[2][i] + " , ");
        }
        System.out.println();

        double FE1 = 0;
        double FE2 = 0;
        double FE3 = 0;

        for (int i = 0; i < n * (n - 1) / 2; i++) {
            double log1 = 0;
            double log2 = 0;
            double log3 = 0;

            if (featurePair2[0][i] != 0) {
                log1 = Math.log(featurePair2[0][i]) / Math.log(n);

            }
            if (featurePair2[1][i] != 0) {
                log2 = Math.log(featurePair2[1][i]) / Math.log(n);
            }
            if (featurePair2[2][i] != 0) {

                log3 = Math.log(featurePair2[2][i]) / Math.log(n);
            }
            FE1 += featurePair2[0][i] * log1;
            FE2 += featurePair2[1][i] * log2;
            FE3 += featurePair2[2][i] * log3;
        }
        FE1 *= -1;
        FE2 *= -1;
        FE3 *= -1;
        System.out.println("FE 1 : " + FE1);
        System.out.println("FE 2 : " + FE2);
        System.out.println("FE 3 : " + FE3);
        double w1;
        if (FE2 == 0) {
            w1 = Math.abs(FE1) * (100000 / FE1);
        } else {

            w1 = FE1 / FE2;
        }
        double w2;
        if (FE3 == 0) {
            w2 = Math.abs(FE1) * (100000 / FE1);
        } else {
            w2 = FE1 / FE3;
        }
        //     w1 = 1;
        //     w2 = 1;
        System.out.println("w1 : " + w1);
        System.out.println("w2 : " + w2);

        double[][] pw = new double[n][n];
        int cnt2 = 0;//pair index
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {

                pw[i][j] = featurePair[0][cnt2];


                pw[i][j] += featurePair[1][cnt2] * w1;

                pw[i][j] += featurePair[2][cnt2] * w2;
                pw[j][i] = pw[i][j];
                cnt2++;
            }
        }
        System.out.println("CNT 2 : " + cnt2);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                System.out.print("for " + (i + 1) + " & " + (j + 1) + " : " + pw[i][j] + " , ");
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
        long begin = System.currentTimeMillis();
        while (changed && num_passes < allPases) {
            orders = MyUtils.random_shuffle(orders);
            num_passes++;
            changed = false;
            //    int[] newLabels = new int[n];
            //   boolean[] skipThis = new boolean[n];
            for (int counter = 0; counter < n; counter++) {
                int i = orders[counter];
//                if (skipThis[i]) {
//                    continue;
//                }
//                skipThis[i] = true;
                int[] labelScore = new int[n];
                for (int d = 0; d < n; d++) {
                    if (d != i) {
                        labelScore[labels[d]] += pw[d][i];
                    }
                    //  System.out.println("the value : " + pw[i][d]);
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
        long end = System.currentTimeMillis();
        printComms(labels);

        Vector<Integer> v = new Vector<>();
        //new Vector(Arrays.asList(labels));
        v.add(0, 0);
        for (int i = 0; i < labels.length; i++) {
            v.add(labels[i]);
        }
        try {
            float nmi = MyUtils.NMI(v, addres + "\\community.txt");

            System.out.println(nmi);
            MyUtils.report(addres, "LPA_CNP_E_(not_sparce)", nmi, end - begin);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void printComms(int[] labels) {
        for (int i = 0; i < labels.length; i++) {
            System.out.println((i + 1) + " : " + (labels[i] + 1));
        }
    }


}

