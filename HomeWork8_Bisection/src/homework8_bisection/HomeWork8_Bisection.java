/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework8_bisection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

class RootFinder implements Runnable{

    private double start;
    private double end;
    private ArrayList<Double> rootsList;
    private int treshold; //number of concurrent threads;
    private int baseRecursionDeepness;
    private int currentRecursionDeepness;
    private double rootThreshold = 0.000001d;
    private Thread[] threadPool;
    public RootFinder(double start, double end, ArrayList<Double> rootsList, int treshold, int baseRecursionDeepness, int currentRecursionDeepness) {
        this.start = start;
        this.end = end;
        this.rootsList = rootsList;
        this.treshold = treshold;
        this.baseRecursionDeepness = baseRecursionDeepness;
        this.currentRecursionDeepness = currentRecursionDeepness;
    }
    
    private double theFunction(double x){
        return (x-3)*(x-3)-4;
    }
    
    @Override
    public void run() {
        double n1 = theFunction(start);
        double n2 = theFunction(end);
        double roundedStart = new BigDecimal(start).setScale(6, RoundingMode.HALF_UP).doubleValue();
        double roundedEnd = new BigDecimal(end).setScale(6, RoundingMode.HALF_UP).doubleValue();
        
        
        if ((Math.copySign(n1, 1)<=rootThreshold)&&(!rootsList.contains(roundedStart))) rootsList.add(roundedStart);
        if ((Math.copySign(n2, 1)<=rootThreshold)&&(!rootsList.contains(roundedEnd))) rootsList.add(roundedEnd);
        if (Math.copySign(n1, 1)<=rootThreshold||Math.copySign(n2, 1)<=rootThreshold) return;
        
        if ((Math.signum(n1)==Math.signum(n2))&&(currentRecursionDeepness<baseRecursionDeepness)){
            try {
                doMath();
            } catch (InterruptedException ex) {
                Logger.getLogger(RootFinder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (Math.signum(n1)!=Math.signum(n2)){
            try {
                doMath();
            } catch (InterruptedException ex) {
                Logger.getLogger(RootFinder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    private void doMath() throws InterruptedException{
        threadPool = new Thread[treshold];
        double currentThreadVector = (end-start)/treshold;
        double currentStart = start;
        double currentEnd = currentStart+currentThreadVector;
        for (int i=0;i<treshold;i++){
            if (i==(treshold-1)) currentEnd=end;
            threadPool[i]=new Thread(new RootFinder(currentStart, currentEnd, rootsList, treshold, baseRecursionDeepness, currentRecursionDeepness+1));
            threadPool[i].start();
            currentStart+=currentThreadVector;
            currentEnd+=currentThreadVector;
        }
        
        for (int i=0;i<treshold;i++){
            threadPool[i].join();
        }
    }
    
}


public class HomeWork8_Bisection {

    static final int sections = 3;
    public static void main(String[] args) throws InterruptedException {
        double start;
        double end;
        ArrayList<Double> rootsList = new ArrayList<>();
        int deepnessOfRecursion;
        System.out.print("Enter the beginning of analysed vector: ");
        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
        try {
            start = Double.parseDouble(buf.readLine());
        } catch (IOException ex) {
            start=0;
        }
        
        System.out.print("Enter the end of analysed vector: ");
        buf = new BufferedReader(new InputStreamReader(System.in));
        try {
            end = Double.parseDouble(buf.readLine());
        } catch (IOException ex) {
            end=0;
        }
        
        System.out.print("Enter the deepness of recursion (for curved functions, which has both start and end "
                + "positive or both negative): ");
        buf = new BufferedReader(new InputStreamReader(System.in));
        try {
            deepnessOfRecursion = Integer.parseInt(buf.readLine());
        } catch (IOException ex) {
            deepnessOfRecursion=0;
        }
        
        double currentThreadVector = (end-start)/sections;
        double currentStart = start;
        double currentEnd = currentStart+currentThreadVector;
        Thread[] threadPool=new Thread[sections];
        //starting calculating threads
        for (int i=0;i<sections;i++){
            if (i==(sections-1)) currentEnd=end;
            threadPool[i]=new Thread(new RootFinder(currentStart, currentEnd, rootsList, sections, deepnessOfRecursion, 0));
            threadPool[i].start();
            currentStart+=currentThreadVector;
            currentEnd+=currentThreadVector;
        }
        
        for (int i=0;i<sections;i++) threadPool[i].join();
        
        System.out.println(rootsList);
        
    }
    
}
