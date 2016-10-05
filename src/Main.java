import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
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
//        System.out.println(D1);

        // B
        D2 = new ArrayList<Double>(k);
        Random r = new Random();
        double randomValue = 0;
        for (int i = 0; i < k; i++) {
            randomValue = r.nextDouble();
            D2.add(randomValue);
        }
//        System.out.println(D2.size());

        // C
        D3 = new ArrayList<Double>(k);
        for (int i = 0; i < k; i++) {
            D3.add(D1.get(
                    (int) (D2.get(i) * k)
            ));

        }
//        System.out.println(D3.size());

        // D

        double T = getT(D3, L);
        System.out.println(T);

        // 1 - eps = 0.95
        // nu = L - 1
        // table -> delta
        // T = 16.669999999999998
        // delta = 	16,9190
        // T < delta good else bad
        System.out.println(Kolmogorov(D3));

        lab2();
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
        D.sort(new Comparator<Double>() {
            @Override
            public int compare(Double aDouble, Double t1) {

                return (int)(aDouble - t1);
            }
        });
        for (int i = 0; i < n; i++) {
            if ((double)(i+1)/n - D.get(i) > d) {
                d = (double)(i+1)/n - D.get(i);
            }
        }
        // "sqrt(n)*Dn = " + Math.sqrt(n)*d + "; delta = 1.36"
//        return Math.sqrt(n)*d;
        return d;
    }

    public static void lab2() {
        int n = 1000;
        double p;

        ArrayList<Double> BSV = getBSV(n);
        p = 0.7;
        ArrayList<Integer> DSV_Bernoulli = getDSV_Bernoulli(BSV, p);
        System.out.println("Bernoulli " + DSV_Bernoulli);

        double expectedValue = p;
        double ourExpectedValue = getExpectedValue_Bernoulli(DSV_Bernoulli);
        System.out.println("expectedValue " + expectedValue + " outExpectedValue " + ourExpectedValue);

        double dispersion = p * (1 - p);
        double ourDispersion = getDispersion_Bernoulli(DSV_Bernoulli, expectedValue);
        System.out.println("dispersion " + dispersion + " ourDispersion " + ourDispersion);

        ArrayList<Double> DSV_Bernoulli_Double = new ArrayList<Double>();
        for (Integer integer : DSV_Bernoulli) {
            DSV_Bernoulli_Double.add((double)integer);
        }
//        double T = getT(DSV_Bernoulli_Double, 10);
//        System.out.println("Bernoulli Pirson " + T);
        test_bernulli_distribution(DSV_Bernoulli);

        System.out.println();

        n = 5;
        p = 0.25;
        BSV = getBSV(n);
        Integer DSV_Binomial = getDSV_Binomial(BSV, p);
        System.out.println("Binomial " + DSV_Binomial);

        expectedValue = n * p;
        ourExpectedValue = getExpectedValue_Binomial(DSV_Bernoulli);
        System.out.println("expectedValue " + expectedValue + " outExpectedValue " + ourExpectedValue);

        dispersion = n * p * (1 - p);
        ourDispersion = getDispersion_Binomial(DSV_Bernoulli, expectedValue);
        System.out.println("dispersion " + dispersion + " ourDispersion " + ourDispersion);

    }

    public static void test_bernulli_distribution(ArrayList<Integer> array) {
        double[] m = {0,0};
        for (Integer integer : array) {
            if (integer == 1) {
                m[1]++;
            } else  {
                m[0]++;
            }
        }

        double[] p = { 0.3, 0.7 };
        double sum = 0;
        for (int i = 0; i < 2; i++)
        {
            double a = Math.pow(m[i] - array.size() * p[i], 2);
            sum = sum + a / (array.size() * p[i]);
        }

        System.out.println("Pirson Bernulli: " + sum);
    }


    private static Double getDispersion_Bernoulli(ArrayList<Integer> dsv_bernoulli, Double expectedValue) {
        double dispersion = 0;
        int n = dsv_bernoulli.size();
        for (Integer success : dsv_bernoulli) {
            dispersion += (double)Math.pow(success - expectedValue, 2) / n;
        }
        return dispersion;
    }


    private static Double getExpectedValue_Bernoulli(ArrayList<Integer> DSV_Bernoulli) {
        double expectedValue = 0;
        int n = DSV_Bernoulli.size();
        for (Integer success : DSV_Bernoulli) {
            expectedValue += (double)success / n;
        }
        return expectedValue;
    }

    private static Double getDispersion_Binomial(ArrayList<Integer> dsv_bernoulli, Double expectedValue) {
        double dispersion = 0;
        int n = dsv_bernoulli.size();
        for (Integer success : dsv_bernoulli) {
            dispersion += (double)Math.pow(success - expectedValue, 2) / n;
        }
        return dispersion;
    }


    private static Double getExpectedValue_Binomial(ArrayList<Integer> DSV_Bernoulli) {
        double expectedValue = 0;
        int n = DSV_Bernoulli.size();
        for (Integer success : DSV_Bernoulli) {
            expectedValue += (double)success / n;
        }
        return expectedValue;
    }

    private static ArrayList<Integer> getDSV_Bernoulli(ArrayList<Double> bsv, double p) {
        ArrayList<Integer> dsv = new ArrayList<Integer>();
        for (Double sv : bsv) {
                dsv.add(sv <= p ? 1 : 0);
        }
        return  dsv;
    }

    private static Integer getDSV_Binomial(ArrayList<Double> bsv, double p) {
        ArrayList<Integer> DSV_Bernoulli = getDSV_Bernoulli(bsv, p);
        int x = 0;
        for (Integer success : DSV_Bernoulli) {
            if (success == 1) {
                x++;
            }
        }
        return  x;
    }



    public static ArrayList<Double> getBSV(int k) {
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
//        System.out.println(D1);

        // B
        D2 = new ArrayList<Double>(k);
        Random r = new Random();
        double randomValue = 0;
        for (int i = 0; i < k; i++) {
            randomValue = r.nextDouble();
            D2.add(randomValue);
        }
//        System.out.println(D2.size());

        // C
        D3 = new ArrayList<Double>(k);
        for (int i = 0; i < k; i++) {
            D3.add(D1.get(
                    (int) (D2.get(i) * k)
            ));

        }
//        System.out.println(D3.size());
        return D3;
    }
}
