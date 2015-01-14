/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework10_semaphore;

import java.util.LinkedHashSet;

class Semaphore{
    private final int maxQueue;
    private int aquisitions=0;
    private LinkedHashSet<Thread> waitingPool=new LinkedHashSet<>();
    private LinkedHashSet<Thread> usingPool=new LinkedHashSet<>();

    public Semaphore() {
        this(5);
    }
    
    public Semaphore(int maxQueue) {
        this.maxQueue = maxQueue;
    }
    
    public synchronized void aquire() throws IllegalAquirementException{
        if (waitingPool.contains(Thread.currentThread())) throw new IllegalAquirementException(
                "Thread "+Thread.currentThread().getName()+" already aquired a resource!");
        if (usingPool.contains(Thread.currentThread())) throw new IllegalAquirementException(
                "Thread "+Thread.currentThread().getName()+" already using a resource!");
        
        waitingPool.add(Thread.currentThread());
        System.out.println("Trying to aquire a resource: "+Thread.currentThread().getName());
        while ((aquisitions>=maxQueue)||(waitingPool.iterator().next()!=Thread.currentThread())){
            try {
                wait();
            } catch (InterruptedException ex) {
            }
        }
        aquisitions++;
        waitingPool.remove(Thread.currentThread());
        usingPool.add(Thread.currentThread());
        notifyAll();
    }
    
    public synchronized void release() throws IllegalReleasmentException{
        if (!usingPool.contains(Thread.currentThread())) throw new IllegalReleasmentException(
                "Thread "+Thread.currentThread().getName()+" is not using a resource!");
        System.out.println("Trying to release a resource: "+Thread.currentThread().getName());
        while (aquisitions==0){//don't really know, how can it happen
            try {
                wait();
            } catch (InterruptedException ex) {
            }
        }
        aquisitions--;
        usingPool.remove(Thread.currentThread());
        notifyAll();
        
    }
    
    
}

class IllegalReleasmentException extends Exception{

    public IllegalReleasmentException(String message) {
        super(message);
    }
    
}

class IllegalAquirementException extends Exception{

    public IllegalAquirementException(String message) {
        super(message);
    }
    
}

class SemaphoreUser implements Runnable{
    Semaphore sem;

    public SemaphoreUser(Semaphore sem) {
        this.sem = sem;
    }

    @Override
    public void run() {
        while (true){
            
            try {
                sem.aquire();
            } catch (IllegalAquirementException ex) {
                System.out.println("Whoah! "+Thread.currentThread().getName()+" got an exception on aquirement!");
            }
            
            System.out.println(Thread.currentThread().getName()+" using resource... ");
            try {
                Thread.sleep((int)(Math.random()*10000));
            } catch (InterruptedException ex) {
            }
            
            
            try {
                sem.release();
            } catch (IllegalReleasmentException ex) {
                System.out.println("Hmmm... "+Thread.currentThread().getName()+" got an exception on releasement!");
            }
            System.out.println(Thread.currentThread().getName()+" just walking around... ");
            try {
                Thread.sleep((int)(Math.random()*10000));
            } catch (InterruptedException ex) {
            }
        }
    }
    
}

public class HomeWork10_Semaphore {
    final static int NUM_OF_RESOURCES = 5;
    
    public static void main(String[] args) {
        Semaphore sem = new Semaphore(NUM_OF_RESOURCES);
        
        int numOfThreads = (int)(Math.random()*10+NUM_OF_RESOURCES);
        for (int i=0;i<numOfThreads;i++){
            new Thread(new SemaphoreUser(sem),"User_"+i).start();
        }
    }
    
}
