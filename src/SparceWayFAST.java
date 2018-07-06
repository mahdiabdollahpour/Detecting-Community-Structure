/**
 * Created by ASUS on 01/07/2018.
 */

import java.util.*;


public class SparceWayFAST {


    static SparceMatrix f1;
    static SparceMatrix f2;
    static SparceMatrix f3;

    static int sumF1;
    static int sumF2;
    static int sumF3;
    static double w1;
    static double w2;


    public static void main(String[] args) {

        detect("cases\\TestCase2-N1000-k5-mu50", 1000);

    }

    public static void detect(String addres, int siz) {

        Vector<Vector<int[]>> graph = MyUtils.readGraph(addres + "\\network.txt", siz);
        //     Vector<Vector<int[]>> graph = readGraph("mine.txt", 8);

        MyUtils.chapGraph(graph);
        System.gc();
        int n = graph.size();
        f1 = new SparceMatrix(n);
        f2 = new SparceMatrix(n);
        f3 = new SparceMatrix(n);

        int cnt = 0;//pair index

        for (int i = 0; i < graph.size(); i++) {
            Vector<int[]> arrayList = graph.get(i);
            for (int i1 = 0; i1 < arrayList.size(); i1++) {
                int des = arrayList.get(i1)[0];
                if (i < des) {
                    //featurePair[0][cnt] = 1;
                    f1.addEntry(i, des, 1);
                    ArrayList<Integer> arrayList2 = MyUtils.comNeighbors(graph, i, des);
                    if (arrayList2.size() != 0) {
                        f2.addEntry(i, des, arrayList2.size());
                        int a = MyUtils.commonEdgedInSet(graph, arrayList2);
                        if (a != 0) {
                            f3.addEntry(i, des, a);
                        }
                    }
                }

            }
        }


//        sumF1 = 0;
//        sumF2 = 0;
//        sumF3 = 0;
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
            for (int counter = 0; counter < n; counter++) {
                int i = orders[counter];

                double[] labelScore = new double[n];

                Vector<int[]> vector = graph.get(i);
                for (int i1 = 0; i1 < vector.size(); i1++) {
                    int des = vector.get(i1)[0];
                    if (des != i) {
                        labelScore[labels[des]] += getP(Math.min(des, i), Math.max(des, i));
                    }

                }


                double maxAmount = labelScore[labels[i]];
                ArrayList<Integer> maxLabels = new ArrayList<>();
                //  int maxLabel = labels[i];
                for (int i1 = 0; i1 < labelScore.length; i1++) {
                    if (labelScore[i1] > maxAmount) {

                        maxAmount = labelScore[i1];
                        maxLabels = new ArrayList<>();
                        maxLabels.add(i1);
                    } else if (labelScore[i1] == maxAmount) {
                        maxLabels.add(i1);
                        //   changed = true;
                    }
                }
                int old = 0;
                if (!maxLabels.contains(labels[i])) {
                    old = labels[i];
                    labels[i] = maxLabels.get((int) (Math.random() * maxLabels.size()));
                    changed = true;
                    System.out.println(num_passes + ". label of " + (i + 1) + " goes from " + (old + 1) + " to " + (labels[i] + 1));
                } else {

                    //   System.out.println(num_passes + ". label of " + (i + 1) + " stays " + (labels[i] + 1));
                }

            }

        }
        long end = System.currentTimeMillis();

        NormalWay.printComms(labels);

        Vector<Integer> v = new Vector<>();
        v.add(0, 0);
        for (int i = 0; i < labels.length; i++) {
            v.add(labels[i]);
        }
        try {
            float nmi = MyUtils.NMI(v, addres + "\\community.txt");

            System.out.println(nmi);
            MyUtils.report(addres, "SparceWay_FAST", nmi, end - begin);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    static double getP(int i, int j) {
        System.out.println("p requested");

        double r = f1.getEntry(i, j, true) + f2.getEntry(i, j, true) * w1 + f3.getEntry(i, j, true) * w2;
        System.out.println("p given is : " + r);
        return r;
    }


}



