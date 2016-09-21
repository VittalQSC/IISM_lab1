import java.util.ArrayList;
import java.util.Random;

/**
 * Created by vittal on 21.9.16.
 */


public class Main {
    static int beta;
    static int M;
    static ArrayList<Double> D1;
    static ArrayList<Double> D2;
    static ArrayList<Double> D3;
    static ArrayList<Integer> alpha_z;

//    public static int alpha_z(int alpha) {
////        if ()
//       return (beta * alpha) % M;
//    }

    public static void main(String[] args) {
        // A

        // input: (alpha, beta, M)
        // beta = 2*20 + 3
        beta = (int) (Math.pow(2, 20) + 3);
        M = (int) Math.pow(2, 31);

        int alpha_z0 = beta;

        int k = 1000;
        D1 = new ArrayList<Double>(k);
        alpha_z = new ArrayList<Integer>(k);

        for (int i = 0; i < k; i++) {
            if (i == 0) {
                alpha_z.add(alpha_z0);
                D1.add( (double)alpha_z0 / (double)M);
            } else {
                alpha_z.add((beta * alpha_z.get(i - 1)) % M);
                D1.add((double)alpha_z.get(i) / (double)M);
            }

        }
        System.out.println(D1.size());

        // B
        D2 = new ArrayList<Double>(k);
        Random r = new Random();
        double randomValue = 0;
        for (int i = 0; i < k; i++) {
            randomValue = r.nextDouble();
            D2.add(randomValue);
        }
        System.out.println(D2.size());

        // C
    }
}
