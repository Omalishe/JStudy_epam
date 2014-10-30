/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework3;

import java.util.Iterator;

/**
 *
 * @author oleksandr
 */

class myDoubleLinkedList implements Iterator, Iterable{
    
    Node currentNode;
    Header thisHeader;
    
    public void add(Integer data){
        
    }
    
    public void reset(){
        this.currentNode = this.thisHeader.firstNode;
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
        myLinkedList mll = new myLinkedList(0);
        mll.add(1);
        mll.add(2);
        mll.add(3);
        
        mll.reset();
        
        while(mll.hasNext()){
            myLinkedList.Node tl = (myLinkedList.Node) mll.next();
            System.out.println(tl.data);
        }
    }
    
}
