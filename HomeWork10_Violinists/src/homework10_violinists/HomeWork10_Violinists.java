/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework10_violinists;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author oleksandr
 */
public class HomeWork10_Violinists {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int numOfViolonists;
        int numOfViolins;
        int numOfFiddlesticks;
        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Enter the number of violins: ");
            numOfViolins = Integer.parseInt(buf.readLine());
            System.out.print("Enter the number of fidddlesticks: ");
            numOfFiddlesticks = Integer.parseInt(buf.readLine());
        } catch (IOException ex) {
            numOfViolins = 0;
            numOfFiddlesticks=0;
        }
        
        numOfViolonists = (int)(Math.random()*10)+numOfViolins+numOfFiddlesticks;
        Philharmonia phil = new Philharmonia(numOfViolins, numOfFiddlesticks);
        for (int i=0;i<numOfViolonists;i++){
            new Thread(new Violonist(phil),"Violonist_"+i).start();
        }
        
    }
    
}

class Violonist implements Runnable{
    Philharmonia phil;

    public Violonist(Philharmonia phil) {
        this.phil = phil;
    }
    
    
    @Override
    public void run() {
        while (true){
            phil.getViolin();
            phil.getFiddlestick();
            System.out.println("Violinist "+Thread.currentThread().getName()+" plays...");
            
            try {
                Thread.sleep((int)(Math.random()*10000)); //while violinist plays
            } catch (InterruptedException ex) {
                return;
            }finally{
                
                phil.releaseFiddlestick();
                phil.releaseViolin();
            }
            
            try {
                Thread.sleep((int)(Math.random()*20000));//while violinist walks randomly
            } catch (InterruptedException ex) {
                return;
            }
            if (Thread.interrupted()) return;
        }
    }
    
}

class Philharmonia{
    
    private int numOfViolins;
    private int numOfFiddlesticks;
    private int freeViolins;
    private int freeFiddlesticks;
    private boolean notClosed;
    
    public Philharmonia(int numOfViolins, int numOfFiddlesticks) {
        this.numOfViolins = numOfViolins;
        this.numOfFiddlesticks = numOfFiddlesticks;
        this.freeViolins = numOfViolins;
        this.freeFiddlesticks = numOfFiddlesticks;
        
    }

    public synchronized void getViolin() {
        while(freeViolins==0){
            try {
                wait();
            } catch (InterruptedException ex) {
                
            }
        }
        freeViolins--;
        System.out.println("Violinist "+Thread.currentThread().getName()+" just aquired a violin");
        notifyAll();
    }

    public synchronized void getFiddlestick() {
        while(freeFiddlesticks==0){
            try {
                wait();
            } catch (InterruptedException ex) {
            }
        }
        freeFiddlesticks--;
        System.out.println("Violinist "+Thread.currentThread().getName()+" just aquired a fiddlestick");
        notifyAll();
    }
    
    public synchronized void releaseViolin(){
        while(freeViolins>=numOfViolins){
            try {
                wait();
            } catch (InterruptedException ex) {
            }
        }
        freeViolins++;
        System.out.println("Violinist "+Thread.currentThread().getName()+" just returned a violin");
        notifyAll();
    }
    
    public synchronized void releaseFiddlestick(){
        while(freeFiddlesticks>=numOfFiddlesticks){
            try {
                wait();
            } catch (InterruptedException ex) {
            }
        }
        freeFiddlesticks++;
        System.out.println("Violinist "+Thread.currentThread().getName()+" just returned a fiddlestick");
        notifyAll();
    }
}