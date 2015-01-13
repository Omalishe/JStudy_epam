/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework10_resturant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

enum TypeOfDish{
     soup
    ,cake
    ,tea;
}

class Restaurant{
    private final int numOfTables;
    private volatile int freeTables;

    public Restaurant(int numOfTables) {
        this.numOfTables = numOfTables;
        this.freeTables = numOfTables;
    }
    
    public synchronized void occupyTable(){
        while(freeTables==0){
            try {
                wait();
            } catch (InterruptedException ex) {
            }
        }
        freeTables--;
        System.out.println("Client "+Thread.currentThread().getName()+" just occupied a table");
        notifyAll();
    }
    
    public synchronized void freeTable(){
        while (freeTables>=numOfTables){ 
            try { //empty restaurant, waiting for clients
            wait();
            } catch (InterruptedException ex) {}
        }
        freeTables++;
        System.out.println("Client "+Thread.currentThread().getName()+" just freed a table");
        notifyAll();
    }
    
}

class Cook implements Runnable{
    private TypeOfDish tod;
    private final int maxQueue;
    private final int cookPause;
    private volatile int currentDishes;
    public Cook(TypeOfDish tod) {
        this.tod = tod;
        switch(tod){
            case cake:maxQueue=10; cookPause=20000; break;
            case soup:maxQueue=3; cookPause=10000 ;break;
            case tea:maxQueue=5; cookPause=2000; break;
            default:maxQueue=6; cookPause=1000;
        }
    }
    
    @Override
    public void run() {
        while(true){
            cookDish();
            try {
                Thread.sleep(cookPause);
            } catch (InterruptedException ex) {}
        }
    }

    private synchronized void cookDish() {
        while(currentDishes>=maxQueue){
            try {
                wait();
            } catch (InterruptedException ex) {}
        }
        currentDishes++;
        System.out.println("Cook "+Thread.currentThread().getName()+" just cooked a "+tod.toString());
        notifyAll();
    }
    
    public synchronized void getDish(){
        while (currentDishes==0){
            notifyAll();
            try {
                wait();
            } catch (InterruptedException ex) {}
        }
        currentDishes--;
        notifyAll();
    }
    
    
    
}

class Client implements Runnable{
    
    Restaurant rest;
    Cook soupCook;
    Cook cakeCook;
    Cook teaCook;
    String name;

    public Client(Restaurant rest, Cook soupCook, Cook cakeCook, Cook teaCook, String name) {
        this.rest = rest;
        this.soupCook = soupCook;
        this.cakeCook = cakeCook;
        this.teaCook = teaCook;
        this.name = name;
    }
    
    
    @Override
    public void run() {
        rest.occupyTable();
        
        soupCook.getDish();
        try {
            Thread.sleep(15000); //eating soup
        } catch (InterruptedException ex) {}
        System.out.println("Client "+Thread.currentThread().getName()+" just ate a soup");
        
        cakeCook.getDish();
        try {
            Thread.sleep(20000); //eating cake
        } catch (InterruptedException ex) {}
        System.out.println("Client "+Thread.currentThread().getName()+" just ate a cake");
        
        teaCook.getDish();
        try {
            Thread.sleep(6000); //drinking tea
        } catch (InterruptedException ex) {}
        System.out.println("Client "+Thread.currentThread().getName()+" just drunk a tea");
        
        rest.freeTable();
        System.out.println("Client "+Thread.currentThread().getName()+" just left a restaurant");
    }
    
}
/**
 *
 * @author oleksandr
 */
public class HomeWork10_Resturant {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int numOfTables;
        System.out.print("Enter the number of tables in a restaurant: ");
        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
        try {
            numOfTables = Integer.parseInt(buf.readLine());
        } catch (IOException ex) {
            numOfTables=0;
        }
        
        Restaurant res = new Restaurant(numOfTables);
        Cook soupCook = new Cook(TypeOfDish.soup); new Thread(soupCook,"Soup Cook").start();
        Cook cakeCook = new Cook(TypeOfDish.cake); new Thread(cakeCook,"Cake Cook").start();
        Cook teaCook = new Cook(TypeOfDish.tea); new Thread(teaCook,"Tea Cook").start();
        
        int clientOverLoad = (int)(Math.random()*100);
        for (int i=0;i<(numOfTables+clientOverLoad);i++){
            new Thread(new Client(res, soupCook, cakeCook, teaCook, "Client_"+i),"Client_"+i).start();
        }
    }
    
}
