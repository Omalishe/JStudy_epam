/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework15_chatserver;

import common_classes.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashSet;

/**
 *
 * @author oleksandr
 */

class ClientsPool{
    private HashSet<ClientThread> pool = new HashSet();
    
    public void addUser(ClientThread cl){
        pool.add(cl);
    }
    
    public void resend(Message m){
        for (ClientThread client : pool) {
            client.sendMsg(m);
        }
    }
    
    public void closeSession(ClientThread cl){
        pool.remove(cl);
    }
}

class ClientThread implements Runnable{
    private Socket client;
    private ClientsPool pool;
    ObjectOutputStream out;
    public ClientThread(Socket client,ClientsPool pool) {
        this.client = client;
        this.pool = pool;
        //pool.addUser(this);
    }
    
    
    @Override
    public void run() {
        try {
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());
            System.out.println("SIn");
            
            if (out==null) {
                out = new ObjectOutputStream(client.getOutputStream());
            }
            out.writeObject(new Message("Server", "Hello!", new Date()));
            out.flush();
            System.out.println("Sout");
            while (true){
                Message m = (Message)in.readObject();
                System.out.println("Got message");
                pool.resend(m);
            }
        } catch (IOException | ClassNotFoundException ex) {}
        finally{
            try {
                client.close();
            } catch (IOException ex) {}
            pool.closeSession(this);
            
        }
    }
    
    public void sendMsg(Message m){
        try {
            if (out==null) {
                out = new ObjectOutputStream(client.getOutputStream());
            }
            out.writeObject(m);
            out.flush();
        } catch (IOException ex) {
        }
        
    }
}

public class HomeWork15_ChatServer {

    private static ServerSocket listener;
    private static ClientsPool pool = new ClientsPool();
    public static void main(String[] args) {
        try {
            listener = new ServerSocket(25252);
            System.out.println("Server started");
            while(true){
                Socket client = listener.accept();
                System.out.println("Clietn "+client+" connected");
                ClientThread cl = new ClientThread(client, pool);
                pool.addUser(cl);
                new Thread(cl).start();
            }
            
        } catch (IOException ex) {
            System.out.println("Error binding to port 25252");
        }
        finally{
            try {
                listener.close();
            } catch (IOException ex) {
            }
        }
    }
    
}
