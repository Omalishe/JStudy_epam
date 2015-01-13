/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework9_stockexchange;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author oleksandr
 */
public class HomeWork9_StockExchange {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TradeMarket tm = new TradeMarket();
        tm.addObligation("AlterneSoft");
        tm.addObligation("Epam");
        tm.addObligation("Microsoft");
        tm.addObligation("IBM");
        tm.addObligation("Oracle");
        new Thread(tm).start(); //starting market (reindexer)
        
        new Thread(new Pricer(tm)).start(); //starting pricer
        
        int brokerCount = (int)(Math.random()*10);
        for (int i=0;i<brokerCount;i++){
            new Thread(new Broker(tm),"Broker_"+i).start();
        }
        
        try {
            Thread.sleep(100000);//long enough to see, how market works
        } catch (InterruptedException ex) {
            Logger.getLogger(HomeWork9_StockExchange.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
