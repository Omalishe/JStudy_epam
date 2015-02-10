/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework15_chatclient;

import common_classes.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

class MessageReader implements Runnable{
    //Socket s;
    ObjectInputStream in;// = new ObjectInputStream(s.getInputStream());
    public MessageReader(ObjectInputStream in) {
        this.in = in;
        
    }
    
    
    @Override
    public void run() {
//        try {
//            in = new ObjectInputStream(s.getInputStream());
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
        while(true){
            try {
                Message m = (Message) in.readObject();
                System.out.println("["+m.getDate()+"]"+" "+m.getNickname()+": "+m.getText());
            } catch (IOException | ClassNotFoundException ex) {
                //ex.printStackTrace();
                return;
            }
            if (Thread.interrupted()) return;
        }
    }
}


public class HomeWork15_ChatClient {
    private static InetAddress addr;
    public static void main(String[] args) {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        boolean addressEntered = false;
        String sAddr="";
        String username = "";
        
        
        while (!addressEntered) {            
            System.out.println("Enter server address: ");
            try {
                sAddr = input.readLine();
                addressEntered = true;
            } catch (IOException ex) {
                System.out.println("Wrong input");
                addressEntered = false;
            }
        }
        
        System.out.println("Enter username: ");
        try {
            username = input.readLine();
        } catch (IOException ex) {}
        
        
        try {
            addr = InetAddress.getByName(sAddr);
        } catch (UnknownHostException ex) {
            System.out.println("Error resolving host");
        }
        System.out.println("Adress: "+addr);
        
        
        
        try {
            Socket s = new Socket(addr, 25252);
            System.out.println("Connected");
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            out.writeObject(new Message(username, "<I'm joined!>", new Date()));
            out.flush();
            System.out.println("RSOut");
            ObjectInputStream in = new ObjectInputStream(s.getInputStream());
            Thread mr = new Thread(new MessageReader(in));
            mr.start();
            
            System.out.println("you can enter your messages now: ");
            
            String msg = "";
            while (!msg.equals("/exit")){
                msg = input.readLine();
                Message m = new Message(username, msg, new Date());
                out.writeObject(m);
                out.flush(); 
            }
            s.close();
            mr.interrupt();
        } catch (IOException ex) {
            System.out.println("Error connecting to server");
        }
    }
    
}
