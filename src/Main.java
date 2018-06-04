import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created by ASUS on 31/05/2018.
 */


public class Main {


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
            while (line != null) {
                String[] parts = line.split(" ");
                int source = Integer.parseInt(parts[0]);
                int destination = Integer.parseInt(parts[1]);
                ((Vector) (graph.get(source))).add(new int[]{destination, 0});
                ((Vector) (graph.get(destination))).add(new int[]{source, 0});
                line = br.readLine();
            }
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
            if (h2.contains(a[0])) ;
            arrayList.add(a[0]);
        }
        return arrayList;
    }

    public static int commonEdgedInSet(Vector<Vector<int[]>> graph, ArrayList<Integer> arrayList) {
        int count = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            for (int j = i + 1; j < arrayList.size(); j++) {
                if (hasArc(graph, i, j)) {
                    count++;
                }
            }
        }
        return count;


    }

    public static void main(String[] args) {


        Vector<Vector<int[]>> graph = readGraph("network.txt", 100);
        int n = graph.size();
        double[][] featurePair = new double[n * (n - 1) / 2][3];
        double[][] featurePair2 = new double[n * (n - 1) / 2][3];
        int cnt = 0;//pair index
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (hasArc(graph, i, j)) {
                    featurePair[0][cnt] = 1;
                }
                ArrayList<Integer> arrayList = comNeighbors(graph, i, j);
                featurePair[1][cnt] = arrayList.size();
                featurePair[2][cnt] = commonEdgedInSet(graph, arrayList);
                cnt++;
            }
        }

        int sumF1 = 0;
        int sumF2 = 0;
        int sumF3 = 0;
        for (int i = 0; i < n * (n - 1) / 2; i++) {
            sumF1 += featurePair[0][1];
            sumF2 += featurePair[1][1];
            sumF3 += featurePair[2][1];
        }
        for (int i = 0; i < n * (n - 1) / 2; i++) {
            featurePair2[0][i] = featurePair[0][i] / sumF1;
            featurePair2[1][i] = featurePair[1][i] / sumF2;
            featurePair2[2][i] = featurePair[2][i] / sumF3;
        }

        double FE1 = 0;
        double FE2 = 0;
        double FE3 = 0;

        for (int i = 0; i < n * (n - 1) / 2; i++) {
            FE1 += featurePair2[0][i] * Math.log(featurePair2[0][i]);
            FE2 += featurePair2[1][i] * Math.log(featurePair2[1][i]);
            FE3 += featurePair2[2][i] * Math.log(featurePair2[2][i]);
        }
        FE1 *= -1;
        FE2 *= -1;
        FE3 *= -1;
        double w1 = FE1 / FE2;
        double w2 = FE1 / FE3;

        double[][] pw = new double[n][n];
        int cnt2 = 0;//pair index
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {

                pw[i][j] = featurePair[0][cnt2];

                // ArrayList<Integer> com = comNeighbors(graph, i, j);
                pw[i][j] += featurePair[1][cnt2] * w1;
                // int comNedges = commonEdgedInSet(graph, com);
                pw[i][j] += featurePair[2][cnt2] * w2;
                pw[j][i] = pw[i][j];
                cnt2++;
            }
        }
        int[] labels = new int[n];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = i;
        }
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 0; i < n; i++) {
                int[] labelScore = new int[n];
                HashSet<Integer> neighbors = neighbors(graph, i);
                Iterator<Integer> iterator = neighbors.iterator();
                while (iterator.hasNext()) {
                    int d = iterator.next();
                    labelScore[labels[d]] += pw[i][d];
                }
                int maxAmount = 0;
                int maxLabel = labels[i];
                for (int i1 = 0; i1 < labelScore.length; i1++) {
                    if (labelScore[i1] > maxAmount) {
                        maxAmount = labelScore[i1];
                        maxLabel = i1;
                        changed = true;
                    }
                }
                labels[i] = maxLabel;

            }


        }





    }

}

