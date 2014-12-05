package homework8_trapeziums;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;

class TrapeziumCalculator implements Runnable {
    
    private double start;
    private double end;
    private double accum;
    private double xStep;
    private double f(double x){
        return Math.sin(x);
    }

    public TrapeziumCalculator(double start, double end, double xStep) {
        this.start = start;
        this.end = end;
        this.xStep=xStep;
    }
    
    
    private double area(double start, double end){
        return (f(end)+f(start)/2)*(end-start);
    }
    
    public double getAccum(){
        return accum;
    }
    
    @Override
    public void run() {
        double localStart = start;
        double localEnd = localStart+xStep;
        while (localStart<end){
            accum+=Math.copySign(area(localStart, localEnd),1);
            localStart+=xStep;
            localEnd+=xStep;
            localEnd = Math.min(localEnd, end);
        }
    }

}

public class HomeWork8_Trapeziums {

    private final static double xStep = 0.001d;
    private static double start=0;
    private static double end=0;
    private final static int numOfThreads = 3; //optimal
    private static Thread [] threadPool;
    private static Runnable [] trapeziumPool;
    public static void main(String[] args) throws InterruptedException {
        System.out.print("Enter the beginning curve: ");
        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
        try {
            start = Double.parseDouble(buf.readLine());
        } catch (IOException ex) {
            start = 0;
        }
        System.out.print("Enter the end of curve: ");
        buf = new BufferedReader(new InputStreamReader(System.in));
        try {
            end = Double.parseDouble(buf.readLine());
        } catch (IOException ex) {
            end = 0;
        }
        
        System.out.println(LocalTime.now());
        double distancePerThread=(end-start)/numOfThreads;
        threadPool = new Thread[numOfThreads];
        trapeziumPool = new Runnable[numOfThreads];
        double localStart = start;
        double localEnd = start+distancePerThread-xStep;
        
        for (int i=0;i<numOfThreads;i++){
            trapeziumPool[i] = new TrapeziumCalculator(localStart, localEnd, xStep);
            threadPool[i] = new Thread(trapeziumPool[i]);
            threadPool[i].start();
            localStart+=distancePerThread;
            localEnd+=distancePerThread-xStep;
            localEnd = Math.min(localEnd, end);
        }
        
        for (int i=0;i<numOfThreads;i++){
            threadPool[i].join();
        }
        
        double result= 0;
        for (int i=0;i<numOfThreads;i++){
            result+=((TrapeziumCalculator)trapeziumPool[i]).getAccum();
        }
        System.out.println(LocalTime.now());
        System.out.println(result);
    }

}
