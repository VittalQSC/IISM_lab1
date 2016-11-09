using System;
using System.IO;

namespace ISM_LAB3
{
    public class Program
    {
        private static readonly string Directory = AppDomain.CurrentDomain.BaseDirectory;
        private static readonly Random Random = new Random();

        private static readonly double[] VapeArray = new double[1000];
        private const double VapeA = 1;
        private const double VapeB = 0.5;

        private static readonly double[] LaplasArray = new double[1000];
        private const double LaplasA = 0.5;

        //Вейбулл WG(a,b), a = 1, b = 0.5; 
        public static void VapeDistribution()
        {
            for (int i = 0; i < VapeArray.Length; i++)
            {
                double y = Random.NextDouble();
                VapeArray[i] = Math.Pow(-Math.Log(y) / VapeA, 1 / VapeB);
            }

            Array.Sort(VapeArray);
        }

        //Лаплас L(a), a = 0.5;
        public static void LaplasDistribution()
        {
            for (int i = 0; i < LaplasArray.Length; i++)
            {
                double y = Random.NextDouble();
                if (y > 0 && y <= 0.5)
                {
                    LaplasArray[i] = 1 / LaplasA * Math.Log(2 * y);
                }
                if (y > 0.5 && y <= 1)
                {
                    LaplasArray[i] = -1 * (1 / LaplasA) * Math.Log(2 * (1 - y));
                }
            }

            Array.Sort(LaplasArray);
        }

        private static readonly Func<double, double> VapeF = x =>
        {
            return 1 - Math.Exp(-VapeA * Math.Pow(x, VapeB));
        };

        private static readonly Func<double, double> LaplasF = x =>
        {
            if (x < 0)
            {
                return 0.5 * Math.Exp(2 * x);
            }

            return 1 - (0.5) * Math.Exp(-1 * 2 * x);
        };

        private static void KolmagorovTest(double[] array, Func<double, double> f)
        {
            Array.Sort(array);
            int num = array.Length;
            double dn = 0.0;
            for (int i = 0; i < array.Length; i++)
            {
                var dn0 = Math.Abs((i + 1) / (double)num - f.Invoke(array[i]));
                dn = Math.Max(dn, dn0);
            }

            Console.WriteLine("KolmagorovTest : " + Math.Sqrt(1000.0) * dn);
        }

        private static void WriteArrayToFile(double[] array, string fileName)
        {
            var textFile = new StreamWriter(Directory + fileName);
            for (int i = 0; i < array.Length; i++)
            {
                textFile.WriteLine("a*[" + i + "] = " + array[i]);
            }

            textFile.Close();
        }

        static void Main(string[] args)
        {
            VapeDistribution();
            KolmagorovTest(VapeArray, VapeF);
            LaplasDistribution();
            KolmagorovTest(LaplasArray, LaplasF);

            WriteArrayToFile(VapeArray, "VapeArray.txt");
            WriteArrayToFile(LaplasArray, "LaplasArray.txt");

            Console.ReadLine();
        }
    }
}
