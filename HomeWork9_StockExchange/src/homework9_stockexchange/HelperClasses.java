/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework9_stockexchange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author oleksandr
 */

class TradeMarket implements Runnable{

    private ArrayList<Obligation> tradePool=new ArrayList<>();
    private volatile double currentIndex;
    private volatile double prevIndex;
    private double startIndex=500;
    private double startCapitalization;
    private final static double deltaIndex = 50;
    private boolean marketIsAlive = true;
    
    
    public boolean marketIsAlive(){
        return marketIsAlive;
    }
    
    public Obligation getRandomObligation(){
        int index = (int)(Math.random()*tradePool.size());
        return tradePool.get(index);
    }
    
    public Iterator<Obligation> getPoolIterator(){
        return tradePool.iterator();
    }
    
    public synchronized void addObligation(Obligation o){
        tradePool.add(o);
    }
    
    public void addObligation(String companyName){
        this.addObligation(new Obligation(companyName));
    }
    
    private void calculateStartIndex(){
        synchronized(tradePool){
            for (Obligation o:tradePool){
                startCapitalization+=o.getPrice()*o.getOnStockCount();
            }
        }
    }
    private boolean recalculateIndex(double prevIndex){
        double currentCapitalization=0;
        synchronized(tradePool){
            for (Obligation o:tradePool){
                currentCapitalization+=o.getPrice()*o.getOnStockCount();
            }
        }
        currentIndex = startIndex*(currentCapitalization/startCapitalization);
        System.err.println("Current market index is: "+currentIndex);
        return (prevIndex-currentIndex) <= deltaIndex;
    }
    
    @Override
    public void run() {
        if (tradePool.size()<=0){
            System.out.println("No obligations to trade on!");
            marketIsAlive=false;
            return;
        }
        calculateStartIndex();
        //main task of this single thread is to recalculate index once in 15 secs
        //and stop trades if index change speed is to high
        while (marketIsAlive){
            synchronized(tradePool){
                boolean continueTrades = recalculateIndex(prevIndex);
                if (!continueTrades) {
                    System.out.println("Stock index is falling to fast, stopping trades");
                    marketIsAlive=false;
                    return;
                }
                tradePool.notifyAll();
                try {
                    tradePool.wait(15000);
                } catch (InterruptedException ex) {
                    System.out.println("Thread "+Thread.currentThread().getName()+" was pochemu-to interrupted");
                }
            }
            if (Thread.interrupted()) return;
        }
    }
    
}


class Obligation {

    private double price=500;
    private String companyName;
    private int salesCount=0;
    private int buysCount=0;
    private int onStockCount=500;

    public Obligation(String companyName) {
        this.companyName = companyName;
    }

    public void recalculatePrice(){
        double deltaPricePercent = Math.random();
        if (salesCount>buysCount) price-=(deltaPricePercent*price); else price+=(deltaPricePercent*price);
        System.out.println("New obligation price! Obligation: "+companyName+", price: "+price);
    }
    
    public synchronized double getPrice() {
        return price;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public synchronized int getSalesCount() {
        return salesCount;
    }

    public synchronized void setSalesCount(int salesCount) {
        this.salesCount = salesCount;
    }

    public synchronized int getBuysCount() {
        return buysCount;
    }

    public synchronized void setBuysCount(int buysCount) {
        this.buysCount = buysCount;
    }

    public synchronized int getOnStockCount() {
        return onStockCount;
    }

    public synchronized void setOnStockCount(int onStockCount) {
        this.onStockCount = onStockCount;
    }

    void incrementSalesCount() {
        salesCount++;
    }

    void incrementBuysCount() {
        buysCount++;
    }

}

class Broker implements Runnable{
    private TradeMarket workingMarket;
    HashMap<Obligation,Integer> obligationsCount= new HashMap<>();
    
    
    public Broker(TradeMarket workingMarket) {
        this.workingMarket = workingMarket;
        Iterator iter = workingMarket.getPoolIterator();
        while (iter.hasNext()){
            
            obligationsCount.put((Obligation)iter.next(), 0); //current leftovers of obligations at each broker
        }
    }
    
    
    @Override
    public void run() {
        while (workingMarket.marketIsAlive()){
            Obligation ro = workingMarket.getRandomObligation();
            int byOrSell = (int)(Math.random()*2); //if result is greater than 1, then we try to sell, otherwise - to buy
            System.out.println("BuyOrSell: "+byOrSell);
            synchronized(ro){
                int obligationsToBuySell = (int)(Math.random()*10); //trying to buy or sell up to 10 obligations
                int obligationsOnHands = obligationsCount.get(ro);
                int obligationsOnMarket = ro.getOnStockCount();
                if (byOrSell>=1){ //trying to sell
                    if (obligationsToBuySell<=obligationsOnHands){
                        obligationsCount.put(ro, obligationsOnHands-obligationsToBuySell);
                        ro.incrementSalesCount();
                        ro.setOnStockCount(obligationsToBuySell+obligationsOnMarket);
                        System.out.println("Broker "+Thread.currentThread().getName()+" just selled "+obligationsToBuySell+
                                " obligations of "+ro.getCompanyName()+" company");
                    }else{
                        System.out.println("Broker "+Thread.currentThread().getName()+" just tryed to sell "+obligationsToBuySell+
                                " obligations of "+ro.getCompanyName()+" company, but doesn't have enough");
                    }
                }else{
                    if (obligationsToBuySell<=obligationsOnMarket){
                        obligationsCount.put(ro, obligationsOnHands+obligationsToBuySell);
                        ro.incrementBuysCount();
                        ro.setOnStockCount(obligationsOnMarket-obligationsToBuySell);
                        System.out.println("Broker "+Thread.currentThread().getName()+" just bought "+obligationsToBuySell+
                                " obligations of "+ro.getCompanyName()+" company");
                    }else{
                        System.out.println("Broker "+Thread.currentThread().getName()+" just tryed to buy "+obligationsToBuySell+
                                " obligations of "+ro.getCompanyName()+" company, but there's insufficient amount on market");
                    }
                }
            }
            int waitingTime = (int)(Math.random()*15000);
            try {
                Thread.sleep(waitingTime);
            } catch (InterruptedException ex) {
                return; //just exit
            }
            if (Thread.interrupted()) return;
        }
    }
    
}

class Pricer implements Runnable{
    private TradeMarket workingMarket;

    public Pricer(TradeMarket workingMarket) {
        this.workingMarket = workingMarket;
    }
    
    @Override
    public void run() {
        while (workingMarket.marketIsAlive()){
            Iterator iter = workingMarket.getPoolIterator();
            while (iter.hasNext()){
                Obligation o = (Obligation)iter.next();
                synchronized(o){
                    o.recalculatePrice();
                }
            }
            int sleepTime = (int)(Math.random()*10000);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {return;}
            if (Thread.interrupted()) return;
        }
    }
    
}