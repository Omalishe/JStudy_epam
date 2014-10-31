/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework3;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author oleksandr
 */


class BlueRayDisc{
    ArrayList<FileOnDisc> files;

      
    private class FileOnDisc{
        boolean isFolder;
        FileOnDisc parent;
        String name;
    }

    public BlueRayDisc() {
        files = new ArrayList<>();
    }
    
    public BlueRayDisc(String initialFileName) {
        files = new ArrayList<>();
        FileOnDisc firstFile = new FileOnDisc();
        firstFile.name = initialFileName;
        files.add(firstFile);
    }
    
    
}

class myDoubleLinkedList implements Iterator, Iterable{
    
    Node currentNode;
    Header header;
    
    public myDoubleLinkedList(Integer data){
        this.header = new Header();
        this.header.firstNode = new Node();
        this.header.firstNode.data = data;
        this.currentNode = this.header.firstNode;
    }
    
    public myDoubleLinkedList(){
        this.header = new Header();
        this.header.firstNode = new Node();
        this.currentNode = this.header.firstNode;
    }
    
    
    
    
    public void add(Integer data){
        myDoubleLinkedList.Node tempNode = new Node();
        tempNode.data=data;
        tempNode.prevNode = this.currentNode;
        this.currentNode.nextNode = tempNode;
        //this.currentNode.prevNode = this.currentNode;
        this.currentNode = this.currentNode.nextNode;
        this.header.lastNode = this.currentNode;
    }
    
    public void reset(){
        this.currentNode = this.header.firstNode;
    }
    
    public void toLast(){
        this.currentNode = this.header.lastNode;
    }
    
    
    class Node{
        Node nextNode;
        Node prevNode;
        Integer data;
    }
    
    class Header{
        Node firstNode;
        Node lastNode;
    }
    
    @Override
    public boolean hasNext() {
        return this.currentNode!=null;
    }
    
    public boolean hasPrevious(){
        return this.currentNode.prevNode!=null;
    }

    @Override
    public Object next() {
        Node returnValue = this.currentNode;
        this.currentNode = this.currentNode.nextNode;
        return returnValue;
    }
    
    public Object previous(){
        Node returnValue = this.currentNode;
        this.currentNode = this.currentNode.prevNode;
        return returnValue;
    }

    @Override
    public Iterator iterator() {
        return this;
    }
    
}


class myLinkedList implements Iterator, Iterable{

    Node currentNode;
    Node firstNode;
    
    public myLinkedList(Integer data){
        this.firstNode = new Node();
        this.firstNode.data = data;
        this.currentNode = this.firstNode;
    }
    
    public myLinkedList(){
        this.firstNode = new Node();
        this.currentNode = this.firstNode;
    }
    
    
    class Node{
        Integer data;
        Node nextNode;
        public Integer getData(){ 
            return data;
        }
        
    }
    
    @Override
    public boolean hasNext() {
        return this.currentNode!=null;
    }

    @Override
    public Object next() {
        Node returnValue = this.currentNode;
        this.currentNode = this.currentNode.nextNode;
        return returnValue;
    }

    @Override
    public Iterator iterator() {
        return this;
    }
    
    public void add(Integer data){
        myLinkedList.Node tempNode = new Node();
        tempNode.data=data;
        this.currentNode.nextNode = tempNode;
        this.currentNode = this.currentNode.nextNode;
    }
    
    public void reset(){
        this.currentNode=this.firstNode;
    }
    
}


/**
 *
 * @author oleksandr
 */
public class HomeWork3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Linked list:---------------------------");
        
        
        myLinkedList mll = new myLinkedList(0);
        mll.add(1);
        mll.add(2);
        mll.add(3);
        
        mll.reset();
        
        while(mll.hasNext()){
            myLinkedList.Node tl = (myLinkedList.Node) mll.next();
            System.out.println(tl.data);
        }
        
        
        System.out.println("Double linked list:-------------------");
        
        myDoubleLinkedList mdll = new myDoubleLinkedList(0);
        mdll.add(1);
        mdll.add(2);
        mdll.add(3);
        mdll.add(4);
        mdll.add(5);
        
        
        System.out.println("Forward iteration");
        mdll.reset();
        while(mdll.hasNext()){
            myDoubleLinkedList.Node tl = (myDoubleLinkedList.Node) mdll.next();
            System.out.println(tl.data);
        }
        
        System.out.println("Backward iteration");
        mdll.toLast();
        while(mdll.hasPrevious()){
            myDoubleLinkedList.Node tl = (myDoubleLinkedList.Node) mdll.previous();
            System.out.println(tl.data);
        }

    }
    
}
