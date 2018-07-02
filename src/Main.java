import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

/**
 * Created by ASUS on 31/05/2018.
 */


public class Main {
    static String address = "N100000MU.5";
    static int size = 100000;

    public static Vector<Vector<int[]>> readGraph(String addr, int n) {
        Vector<Vector<int[]>> graph = new Vector<Vector<int[]>>();
        // int n = 0;//nodes’number
        for (int i = 0; i < n; i++) {
            graph.add(new Vector<int[]>());
        }
        BufferedReader br = null;
        String line = "";
        try {
            br = new BufferedReader(new FileReader(addr));
            line = br.readLine();
//            System.out.println(br.lines().count());
            int cnt = 0;
            while (line != null) {
                System.out.println(line);
                String[] parts = line.split(" ");
                int source = Integer.parseInt(parts[0]) - 1;
                int destination = Integer.parseInt(parts[1]) - 1;
                ((graph.get(source))).add(new int[]{destination, 0});
                cnt++;
                //     ((graph.get(destination))).add(new int[]{source, 0});
                line = br.readLine();
            }
            System.out.println(n);
            System.out.println(cnt);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graph;
    }

    public static boolean hasArc(Vector<Vector<int[]>> graph, int i, int j) {
        Vector<int[]> v = graph.get(i);
        for (int i1 = 0; i1 < v.size(); i1++) {
            int[] a = v.get(i1);
            if (a[0] == j) {
                return true;
            }
        }
        return false;

    }

    public static void chapGraph(Vector<Vector<int[]>> graph) {
        for (int i = 0; i < graph.size(); i++) {
            Vector<int[]> v = graph.get(i);
            System.out.print((i + 1) + " has edges to :");
            for (int i1 = 0; i1 < v.size(); i1++) {
                System.out.print((v.get(i1)[0] + 1) + " , ");
            }
            System.out.println();
            System.out.println("---------------");
        }
    }

    public static HashSet<Integer> neighbors(Vector<Vector<int[]>> graph, int i) {
        HashSet<Integer> hashSet = new HashSet<>();
        Vector<int[]> v = graph.get(i);
        for (int i1 = 0; i1 < v.size(); i1++) {
            int[] a = v.get(i1);
            hashSet.add(a[0]);
        }
        return hashSet;
    }

    public static ArrayList<Integer> comNeighbors(Vector<Vector<int[]>> graph, int i, int j) {
        HashSet<Integer> h2 = neighbors(graph, j);
        ArrayList<Integer> arrayList = new ArrayList<>();
        Vector<int[]> v = graph.get(i);
        for (int i1 = 0; i1 < v.size(); i1++) {
            int[] a = v.get(i1);
            if (h2.contains(a[0])) {
                if (!arrayList.contains(a[0])) {
                    arrayList.add(a[0]);
                }
            }
        }
        System.out.println("common neighbors of " + (i + 1) + " and " + (j + 1) + " are " + arrayList);
        return arrayList;
    }

    public static int commonEdgedInSet(Vector<Vector<int[]>> graph, ArrayList<Integer> arrayList) {
        int count = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            for (int j = i + 1; j < arrayList.size(); j++) {
                if (hasArc(graph, arrayList.get(i), arrayList.get(j))) {
                    System.out.println("we have arc from " + (arrayList.get(i) + 1) + " to " + (arrayList.get(j) + 1));
                    count++;
                }
            }
        }
        System.out.println("edges in " + arrayList + " are " + count);
        return count;
    }

    public static void main(String[] args) {


        Vector<Vector<int[]>> graph = readGraph(address + "\\network.txt", size);
        //     Vector<Vector<int[]>> graph = readGraph("mine.txt", 8);

        chapGraph(graph);
        System.gc();
        int n = graph.size();
        float[][] featurePair = new float[3][n * (n - 1) / 2];
        float[][] featurePair2 = new float[3][n * (n - 1) / 2];
        int cnt = 0;//pair index
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (hasArc(graph, i, j)) {
                    featurePair[0][cnt] = 1;
                }

                ArrayList<Integer> arrayList = comNeighbors(graph, i, j);
                featurePair[1][cnt] = arrayList.size();
                featurePair[2][cnt] = commonEdgedInSet(graph, arrayList);
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
        while (changed && num_passes < allPases) {
            orders = random_shuffle(orders);
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
        printComms(labels);

        Vector<Integer> v = new Vector<>();
        //new Vector(Arrays.asList(labels));
        v.add(0, 0);
        for (int i = 0; i < labels.length; i++) {
            v.add(labels[i]);
        }
        try {
            System.out.println(NMI(v, address + "\\community.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void printComms(int[] labels) {
        for (int i = 0; i < labels.length; i++) {
            System.out.println((i + 1) + " : " + (labels[i] + 1));
        }
    }

    public static int[] random_shuffle(int[] input) {
        for (int i = 0; i < input.length; i++) {
            int random = i + (int) (Math.random() * ((input.length - 1 - i) + 1));
            int temp = input[random];
            input[random] = input[i];
            input[i] = temp;
        }
        return input;
    }

    public static float NMI(Vector<Integer> Prediction, String TrueCommunityPathTXT) throws Exception {
        Vector<Integer> TrueLabel = new Vector<Integer>();
        int countGuess = 0, countGold = 0;
        float NMI = 0, up = 0, down = 0;
        int n = 0;
        TrueLabel.add(0);
        BufferedReader br = new BufferedReader(new FileReader(TrueCommunityPathTXT));
        String line = br.readLine();
        while (line != null) {
            String[] parts = line.split(" ");
            TrueLabel.add(Integer.parseInt(parts[1]));
            System.out.println(n);
            n++;
            line = br.readLine();
        }
        br.close();
        if (n != Prediction.size() - 1) {
            System.out.println("size does not macth");
            System.out.println(Prediction.size() + " , " + n);
            return -1;
        }
        Hashtable<Integer, Integer> temp = new Hashtable<Integer, Integer>();
        int k = 1;
        for (int i = 1; i <= n; i++) {
            if (temp.containsKey((Integer) Prediction.get(i)))
                Prediction.set(i, temp.get(Prediction.get(i)));
            else {
                temp.put(Prediction.get(i), k);
                Prediction.set(i, temp.get(Prediction.get(i)));
                k++;
            }
        }
        for (int i = 1; i <= n; i++) {
            if (Prediction.get(i) > countGuess)
                countGuess = Prediction.get(i);
            if (TrueLabel.get(i) > countGold)
                countGold = TrueLabel.get(i);
        }
        float NRow[] = new float[countGold];
        float NCol[] = new float[countGuess];
        float matrix[][] = new float[countGold][countGuess];
        for (int i = 0; i < countGold; i++)
            matrix[i] = new float[countGuess];
        for (int i = 1; i <= n; i++) {
            matrix[TrueLabel.get(i) - 1][Prediction.get(i) - 1]++;
            NRow[TrueLabel.get(i) - 1]++;
            NCol[Prediction.get(i) - 1]++;
        }
        for (int i = 0; i < countGold; i++)
            if (NRow[i] != 0)
                down += NRow[i] * Math.log(NRow[i] / (float) (n));
        for (int i = 0; i < countGuess; i++)
            if (NCol[i] != 0)
                down += NCol[i] * Math.log(NCol[i] / (float) (n));
        for (int i = 0; i < countGold; i++)
            for (int j = 0; j < countGuess; j++)
                if (matrix[i][j] != 0)
                    up += matrix[i][j] * Math.log((matrix[i][j] * (float) (n) / ((NRow[i] * NCol[j]))));
        up *= (float) -2;
        NMI = up / down;
        return NMI;
    }


}

