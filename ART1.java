/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package art1;

/**
 *
 * @author Singgih
 */
public class ART1 {

    private static final int n = 7;    // angka dari inputan
    private static final int m = 3;    // maximal cluster
    private static final double VIGILANCE = 0.7;
    private static final int PATTERNS = 5;
    private static final int TRAINING_PATTERNS = 5;  // jumlah data tranning
    private static int pattern[][] = null;           // tranning pattern

    private static double bw[][] = null;        //berat bawah
    private static double tw[][] = null;        //berat atas

    private static int f1a[] = null;            //input layer.
    private static int f1b[] = null;            //interface layer.
    private static double f2[] = null;
    
    private static void initialize()
    {
        pattern = new int[][] {{1, 1, 0, 0, 0, 0, 1}, 
                               {0, 0, 1, 1, 1, 1, 0}, 
                               {1, 0, 1, 1, 1, 1, 0}, 
                               {0, 0, 0, 1, 1, 1, 0}, 
                               {1, 1, 0, 1, 1, 1, 0}};
        
        // inisial berat bawwah
        System.out.println("Inisial Berat:");
        bw = new double[m][n];
        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                bw[i][j] = 1.0 / (1.0 + n);
                System.out.print(bw[i][j] + ", ");
            } // j
            System.out.print("\n");
        } // i
        
        System.out.println();
        
        // inisail berat atas
        tw = new double[m][n];
        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                tw[i][j] = 1.0;
                System.out.print(tw[i][j] + ", ");
            } // j
            System.out.print("\n");
        } // i
        System.out.println();
        
        f1a = new int[n];
        f1b = new int[n];
        f2 = new double[m];

        return;
    }
    
    private static void ART1()
    {
        int inputSum = 0;
        int activationSum = 0;
        int f2Max = 0;
        boolean reset = true;

        System.out.println("ART1:\n");
        for(int vecNum = 0; vecNum < PATTERNS; vecNum++)
        {
            System.out.println("Vektor: " + vecNum + "\n");

            // Inisial f2 
            for(int i = 0; i < m; i++)
            {
                f2[i] = 0.0;
            }

            // input pattern 
            for(int i = 0; i < n; i++)
            {
                f1a[i] = pattern[vecNum][i];
            }
   
            inputSum = vectorSum(f1a);
            System.out.println("Jumlah Input (si) = " + inputSum + "\n");

            for(int i = 0; i < n; i++)
            {
                f1b[i] = f1a[i];
            }
            
            for(int i = 0; i < m; i++)
            {
                for(int j = 0; j < n; j++)
                {
                    f2[i] += bw[i][j] * (double)f1a[j];
                    System.out.print(String.format("%f", f2[i]) + ", ");
                } // j
                System.out.println();
            } // i
            System.out.println();

            reset = true;
            while(reset == true)
            {
                
                f2Max = maximum(f2);

                for(int i = 0; i < n; i++)
                {
                    System.out.println(f1b[i] + " * " + String.format("%.1f", tw[f2Max][i]) + " = " + String.format("%.1f", f1b[i] * tw[f2Max][i]));
                    f1b[i] = f1a[i] * (int)Math.floor(tw[f2Max][i]);
                }

                activationSum = vectorSum(f1b);
                System.out.println("Jumlah Aktivasi (x(i)) = " + activationSum + "\n");

                reset = testForReset(activationSum, inputSum, f2Max);

            }

            // Tranning data
            if(vecNum < TRAINING_PATTERNS){
                updateWeights(activationSum, f2Max);
            }

            System.out.println("Vektor #" + vecNum + " cluster #" + f2Max + "\n");

        } 
        return;
    }
    
    private static int vectorSum(int[] nodeArray)
    {
        int sum = 0;

        // input pattern.
        for(int i = 0; i < n; i++)
        {
            sum += nodeArray[i];
        }

        return sum;
    }
    
    private static void updateWeights(int activationSum, int f2Max)
    {
        //Update berat bawah
        for(int i = 0; i < n; i++)
        {
            bw[f2Max][i] = ((double)f1b[i]) / (0.5 + (double)activationSum);
        }
        
        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                System.out.print(String.format("%f", bw[i][j]) + ", ");
            } //j
            System.out.println();
        } // i
        System.out.println();
        
        //Update berat atas
        for(int i = 0; i < n; i++)
        {
            tw[f2Max][i] = f1b[i];
        }
        
        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                System.out.print(String.format("%.1f", tw[i][j]) + ", ");
            } // j
            System.out.println();
        } // i
        System.out.println();

        return;
    }
    
    private static boolean testForReset(int activationSum, int inputSum, int f2Max)
    {
        if((double)activationSum / (double)inputSum >= VIGILANCE){
            return false; 
        }else{
            f2[f2Max] = -1.0;
            return true; 
        }
    }
    
    private static int maximum(double[] nodeArray)
    {
        int winner = 0;
        boolean foundNewWinner = false;
        boolean done = false;

        while(!done)
        {
            foundNewWinner = false;
            for(int i = 0; i < m; i++)
            {
                if(i != winner){    
                    if(nodeArray[i] > nodeArray[winner]){
                        winner = i;
                        foundNewWinner = true;
                    }
                }
            }

            if(foundNewWinner == false){
                done = true;
            }
        }
        return winner;
    }
    
    private static void printResults()
    {
        System.out.println("Hasil Akhir Berat:");
        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                System.out.print(String.format("%f", bw[i][j]) + ", ");
            } // j
            System.out.print("\n");
        } // i
        System.out.println();
        
        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                System.out.print(String.format("%.1f", tw[i][j]) + ", ");
            } // j
            System.out.print("\n");
        } // i

        return;
    }
    
    public static void main(String[] args)
    {
        initialize();
        for(int x =0; x<2; x++){
            ART1();
            printResults();   
        }
        return;
    }
    
}
