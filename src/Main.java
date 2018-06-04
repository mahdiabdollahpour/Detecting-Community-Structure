import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;

/**
 * Created by ASUS on 31/05/2018.
 */


public class Main {


    public static Vector readGraph(String addr) {
        Vector<Vector<int[]>> graph = new Vector<Vector<int[]>>();
        int n = 0;//nodes’number
        for (int i = 0; i < n; i++) {
            graph.add(new Vector<int[]>());
        }
        BufferedReader br = null;
        String line = "";
        try {
            br = new BufferedReader(new FileReader("network.txt"));
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


        Vector<Vector<int[]>> graph = readGraph("network.txt");
        int n = graph.size();
        double[][] featurePair = new double[n * (n - 1) / 2][3];
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (hasArc(graph, i, j)) {
                    featurePair[0][cnt] = 1;
                }
                ArrayList arrayList = comNeighbors(graph, i, j);
                featurePair[1][cnt] = arrayList.size();
                featurePair[2][cnt] = commonEdgedInSet(graph, arrayList);


            }
            cnt++;
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
            featurePair[0][i] = featurePair[0][i] / sumF1;
            featurePair[1][i] = featurePair[1][i] / sumF2;
            featurePair[2][i] = featurePair[2][i] / sumF3;
        }

        double FE1 = 0;
        double FE2 = 0;
        double FE3 = 0;

        for (int i = 0; i < n * (n - 1) / 2; i++) {
            FE1 += featurePair[0][i] * Math.log(featurePair[0][i]);
            FE2 += featurePair[1][i] * Math.log(featurePair[1][i]);
            FE3 += featurePair[2][i] * Math.log(featurePair[2][i]);
        }
        FE1 *= -1;
        FE2 *= -1;
        FE3 *= -1;
        double w1 = FE1 / FE2;
        double w2 = FE1 / FE3;





    }

}

