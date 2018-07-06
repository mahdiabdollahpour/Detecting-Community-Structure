import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by ASUS on 03/07/2018.
 */
public class UsualLPA {
    public static void main(String[] args) {
//          detect("N100000MU.5", 100000);
//        detect("cases\\TestCase1-N1000-k15-mu45", 1000);
//        detect("cases\\TestCase2-N1000-k5-mu50", 1000);
//        detect("cases\\TestCase4-N1000-k5-mu60", 1000);
//        detect("cases\\TestCase5-N1000-k20-mu10", 1000);
//        detect("cases\\TestCase6-N1000-k20-mu45", 1000);
//        detect("cases\\TestCase7-N1000-k20-mu50", 1000);
//        detect("cases\\TestCase8-N1000-k20-mu55", 1000);
//        detect("cases\\TestCase9-N10000-k20-mu45", 10000);
//        detect("cases\\TestCase10-N10000-k20-mu50", 10000);
//        detect("cases\\TestCase11-N50000-k20-mu45", 50000);
//        detect("cases\\TestCase12-N50000-k20-mu50", 50000);
        detect("cases\\TestCase13-N100000-k20-mu45", 100000);
//          detect("cases\\TestCase14-N100000-k20-mu50", 100000);

    }

    public static void detect(String addres, int siz) {


        Vector<Vector<int[]>> graph = MyUtils.readGraph(addres + "\\network.txt", siz);
        //     Vector<Vector<int[]>> graph = readGraph("mine.txt", 8);

        MyUtils.chapGraph(graph);
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
        long begin = System.currentTimeMillis();
        while (changed && num_passes < allPases) {
            orders = MyUtils.random_shuffle(orders);
            num_passes++;
            changed = false;

            for (int counter = 0; counter < n; counter++) {
                int i = orders[counter];
                int[] labelScore = new int[n];
                Vector<int[]> v = graph.get(i);
                for (int i1 = 0; i1 < v.size(); i1++) {
                    int des = v.get(i1)[0];
                    if (des != i) {
                        labelScore[labels[des]]++;
                    }
                }
//                for (int d = 0; d < n; d++) {
//                    if (d != i) {
//                    }
//                    //  System.out.println("the value : " + pw[i][d]);
//                }


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
        long end = System.currentTimeMillis();
        NormalWay.printComms(labels);

        Vector<Integer> v = new Vector<>();
        //new Vector(Arrays.asList(labels));
        v.add(0, 0);
        for (int i = 0; i < labels.length; i++) {
            v.add(labels[i]);
        }
        try {

            float nmi = MyUtils.NMI(v, addres + "\\community.txt");
            System.out.println(nmi);
            MyUtils.report(addres, "Usual_LPA_", nmi, end - begin);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
