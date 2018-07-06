import java.util.ArrayList;

/**
 * Created by ASUS on 01/07/2018.
 */
public class SparceMatrix {
    static class node {
        int s;
        int d;
        float value;
        int temp;

        @Override
        public String toString() {
            return "node{" +
                    "s=" + s +
                    ", d=" + d +
                    ", value=" + value +
                    '}';
        }

        public node(int u, int v, float value) {
            this.s = u;
            this.d = v;
            this.value = value;
        }
    }

    int n = 0;
    public ArrayList<node> matrix;
    int[] start;
    int[] end;

    public SparceMatrix(int n) {
        this.n = n;
        matrix = new ArrayList<>();
        start = new int[n];
        end = new int[n];
        for (int i = 0; i < start.length; i++) {
            start[i] = -1;
        }
    }

    public void addEntry(int i, int j, float f) {
        matrix.add(new node(i, j, f));
        if (start[i] == -1) {
            start[i] = matrix.size() - 1;
            end[i] = matrix.size() - 1;
        } else {
            end[i]++;
        }
    }

    public float getEntry(int i, int j,boolean temp) {

        int s = start[i];
        int e = end[i];

        if (s == -1) {
            return 0;
        }
        while (s <= e) {
//            System.out.println("S :" + s + " E :" + e);
//            System.out.println("Sd :" + matrix.get(s).d + " Ed :" + matrix.get(e).d);
            int mid = (e + s) / 2;
            int v = matrix.get(mid).d;
            if (v == j) {
                if(temp) {
                    return matrix.get(mid).temp;
                }else {
                    return matrix.get(mid).value;

                }
            } else if (j < v) {
                e = mid - 1;
            } else {
                s = mid + 1;
            }
        }
//        for (int k = s; k <= e; k++) {
//            if (matrix.get(k).d == j) {
//                return matrix.get(k).value;
//            }
//        }
        return 0;
    }


    public void divideby(int d) {
        for (int i = 0; i < matrix.size() && d != 0; i++) {
            matrix.get(i).temp = (int) matrix.get(i).value;
            matrix.get(i).value = matrix.get(i).value / d;
        }
    }

    public float sumAll() {
        float s = 0;
        for (int i = 0; i < matrix.size(); i++) {
            s += matrix.get(i).value;
        }
        return s;
    }

    public float valueOf(int i) {
        return matrix.get(i).value;
    }
}
