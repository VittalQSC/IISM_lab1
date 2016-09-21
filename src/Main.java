import java.math.BigInteger;
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
    static ArrayList<Integer> gamma;
    static int k = 1000;
    static int L = 10;

    public static void main(String[] args) {
        // A

        // input: (alpha, beta, M)
        // beta = 2*20 + 3
        beta = (int) (Math.pow(2, 20) + 3);
        M = (int) Math.pow(2, 31);

        int alpha_z0 = beta;

        D1 = new ArrayList<Double>(k);
        alpha_z = new ArrayList<Integer>(k);

        for (int i = 0; i < k; i++) {
            if (i == 0) {
                alpha_z.add(alpha_z0);
                D1.add( (double)alpha_z0 / (double)M);
            } else {
                BigInteger biBeta = BigInteger.valueOf(beta);
                BigInteger biAlpha_z = BigInteger.valueOf(alpha_z.get(i - 1));
                BigInteger mul = biBeta.multiply(biAlpha_z);
                BigInteger mod = mul.mod(BigInteger.valueOf(M));

//                BigInteger bi
                alpha_z.add(mod.intValue());
                D1.add((double)alpha_z.get(i) / (double)M);
            }

        }
        System.out.println(D1);

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
        D3 = new ArrayList<Double>(k);
        for (int i = 0; i < k; i++) {
            D3.add(D1.get(
                    (int) (D2.get(i) * k)
            ));

        }
        System.out.println(D3.size());

        // D

        double T = getT(D3, L);
        System.out.println(T);

        // 1 - eps = 0.95
        // nu = L - 1
        // table -> delta
        // T = 16.669999999999998
        // delta = 	16,9190
        // T < delta good else bad
    }

    public static double getT(ArrayList<Double> x, int L) {
        double T = 0;
        int n = k;
        double pi = (double)1/L;
        for (int i = 1; i < L; i++) {
            int mi = 0;
            for (Double xi : x) {
                if ((i - 1) * ((double)1/L) < xi && xi < i * ((double)1/L)) {
                    mi++;
                }
            }
            T += Math.pow((mi - n * pi), 2) / (n * pi);
        }
        return T;
    }

    public static Double Kolmogorov (ArrayList<Double> D) {
        double d = 0;
        int n = D.size();
        for (int i = 0; i < n; i++) {
            if ((double)(i+1)/n - D.get(i) > d) {
                d = (double)(i+1)/n - D.get(i);
            }
        }
        // "sqrt(n)*Dn = " + Math.sqrt(n)*d + "; delta = 1.36"
        return Math.sqrt(n)*d;
    }
}
