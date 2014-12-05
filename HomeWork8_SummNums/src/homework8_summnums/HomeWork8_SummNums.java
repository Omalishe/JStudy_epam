package homework8_summnums;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.time.LocalTime;

class Calculator implements Runnable{
    private int step;
    private int start;
    private int accum;
    private int top;

    public Calculator(int step, int start, int top) {
        this.step = step;
        this.start = start;
        this.top = top;
    }
    
    
    @Override
    public void run() {
        int currTop = top>>1;
        for (int i=start;i<=currTop;i+=step){
            accum+=i;
            if (i!=currTop) accum+=(top-i);
        }
    }
    
    public int getAccum(){
        return accum;
    }
    
}


public class HomeWork8_SummNums {
    private static int n=0;
    private static int k=0;
    private static Runnable [] calcPool;
    private static Thread [] threadPool;
    
    public static void main(String[] args) throws InterruptedException {
        System.out.print("Enter top of the heap: ");
        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
        try {
            n = Integer.parseInt(buf.readLine());
        } catch (IOException ex) {
            n = 0;
        }
        
        System.out.print("Enter the number of threads: ");
        buf = new BufferedReader(new InputStreamReader(System.in));
        try {
            k = Integer.parseInt(buf.readLine());
        } catch (IOException ex) {
            k = 0;
        }
        
        calcPool = new Runnable[k];
        threadPool = new Thread[k];
        
        for (int i=0;i<k;i++){
            calcPool[i] = new Calculator(k,i,n);
            threadPool[i] = new Thread(calcPool[i]);
            threadPool[i].start();
        }
        for (int i=0;i<k;i++){
            threadPool[i].join();
        }
        
        int result= 0;
        for (int i=0;i<k;i++){
            result+=((Calculator)calcPool[i]).getAccum();
        }
        //System.out.println(LocalTime.now());
        System.out.println(result);
        
    }
    
}
