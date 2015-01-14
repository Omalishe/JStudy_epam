/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework11_cyclicbarrier;


/**
 *
 * @author oleksandr
 */

class CyclicBarrier implements Runnable{
    int barrierCount;
    volatile int clientsCount=0;
    Runnable target;
    volatile boolean barrierBroken;

    public CyclicBarrier(int barrierCount, Runnable target) {
        this.barrierCount = barrierCount;
        this.target = target;
    }
    
    public synchronized void await(){
        System.out.println(Thread.currentThread().getName()+" came to party...");
        clientsCount++;
        barrierBroken = (clientsCount>=barrierCount);
        while (!barrierBroken){
            try {
                wait();
            } catch (InterruptedException ex) {
            }
        }
        notifyAll();
    }

    @Override
    public synchronized void run() {
        while(true){
            while (!barrierBroken) try {
                wait();
                } catch (InterruptedException ex) {return;}
            System.out.println("The whole bunch is here, let the party start!!!");
            new Thread(target).start();
            clientsCount=0;
            barrierBroken=false;
            if(Thread.interrupted()) return;
        }
    }
}

class BarrierClient implements Runnable{
    
    CyclicBarrier bar;

    public BarrierClient(CyclicBarrier bar) {
        this.bar = bar;
    }
    
    @Override
    public void run() {
        bar.await();
        System.out.println(Thread.currentThread().getName()+" partying hard!");
    }
    
}



public class HomeWork11_CyclicBarrier {

    private final static int BARRIER_COUNT=5;
    public static void main(String[] args) throws InterruptedException {
        CyclicBarrier bar = new CyclicBarrier(BARRIER_COUNT, new Runnable() {

            @Override
            public void run() {
                System.out.println("Party is rolling!!!");
            }
        });
        new Thread(bar).start();
        int repeatsCount = BARRIER_COUNT*3;
        for (int i=0;i<repeatsCount;i++){
            new Thread(new BarrierClient(bar),"Partier_"+i).start();
            Thread.sleep(1000);
        }
    }
    
}
