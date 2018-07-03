import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by ASUS on 03/07/2018.
 */
public class UsualLPA {


    public static void detect(String addres, int siz) {


        Vector<Vector<int[]>> graph = Utils.readGraph(addres + "\\network.txt", siz);
        //     Vector<Vector<int[]>> graph = readGraph("mine.txt", 8);

        Utils.chapGraph(graph);
        System.gc();
        int n = graph.size();


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
            orders = Utils.random_shuffle(orders);
            num_passes++;
            changed = false;

            for (int counter = 0; counter < n; counter++) {
                int i = orders[counter];
                int[] labelScore = new int[n];
                for (int d = 0; d < n; d++) {
                    if (d != i) {
                        labelScore[labels[d]] ++;
                    }
                    //  System.out.println("the value : " + pw[i][d]);
                }


                int maxAmount = labelScore[labels[i]];
                ArrayList<Integer> maxLabels = new ArrayList<>();
                //  int maxLabel = labels[i];
                for (int i1 = 0; i1 < labelScore.length; i1++) {
                    if (labelScore[i1] > maxAmount) {
                        maxAmount = labelScore[i1];
                        maxLabels = new ArrayList<>();
                        maxLabels.add(i1);

                    } else if (labelScore[i1] == maxAmount) {
                        maxLabels.add(i1);
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


            }
            //  labels = newLabels.clone();
        }
        NormalWay.printComms(labels);

        Vector<Integer> v = new Vector<>();
        //new Vector(Arrays.asList(labels));
        v.add(0, 0);
        for (int i = 0; i < labels.length; i++) {
            v.add(labels[i]);
        }
        try {
            System.out.println(Utils.NMI(v, addres + "\\community.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
