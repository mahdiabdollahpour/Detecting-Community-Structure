import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Created by ASUS on 03/07/2018.
 */
public class Utils {

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
            // System.out.println(n);
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
