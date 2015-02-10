/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework14_unblockingqueue;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

class UnBlockingQueue<E>{
    
    private static class Node<E>{
        final E item;
        final AtomicReference<Node<E>> next;
        AtomicBoolean deleting;

        public Node(E item, Node<E> next) {
            this.item = item;
            this.next = new AtomicReference<>(next);
            this.deleting = new AtomicBoolean(false);
        }
    }
    
    private AtomicReference<Node<E>> head = new AtomicReference<>(new Node(null, null));
    private AtomicReference<Node<E>> tail = new AtomicReference<>(head.get());
    
    public boolean add(E item){
        Node newNode = new Node(item, null);
        while (true){
            Node<E> curTail = tail.get();
            Node<E> nextTail = curTail.next.get();
            
            if (curTail == tail.get()){
                if (nextTail==null){
                    if (curTail.next.compareAndSet(null, newNode)){
                        tail.compareAndSet(curTail, newNode);
                        return true;
                    }
                }else{
                    tail.compareAndSet(curTail, nextTail);
                }
            }
        }
    }
    
    public E poll(){
        Node<E> curHead = head.get();
        while (true){
            Node<E> candidate = curHead.next.get();
            if (candidate!=null) {
                //System.out.println("through candidate");
                Node<E> heir = candidate.next.get();
                if (candidate == curHead.next.get()) {
                    //System.out.println("through curhead");
                    if (!candidate.deleting.get()) {
                        //System.out.println("through deleting");
                        if (candidate.deleting.compareAndSet(false, true)) {
                            //System.out.println("through finish");
                            curHead.next.compareAndSet(candidate, heir);
                            return candidate.item;
                        }
                    } else {
                        curHead.next.compareAndSet(candidate, heir);
                    }
                }
            }else{
                tail.compareAndSet(tail.get(), head.get());
                return null;
            }
        }
    }
}

class QueueRunner implements Runnable{
    UnBlockingQueue q;

    public QueueRunner(UnBlockingQueue qu) {
        this.q = qu;
    }
    
    @Override
    public void run() {
        Random r = new Random();
        for(int i=0;i<Integer.MAX_VALUE;i++){
            boolean d = r.nextBoolean();
            if (d){
                String a="Item"+i+r.nextInt();
                q.add(a);
                System.out.println("Thread "+Thread.currentThread().getName()+" just put "+a+" in queue");
            }else{
                String item = (String)q.poll();
                if (item!=null){
                    System.err.println("Thread "+Thread.currentThread().getName()+ " just extracted "+item);
                }else{
                    System.out.println("Thread "+Thread.currentThread().getName()+ " polling failed");
                }
            }
            try {
                Thread.sleep(r.nextInt(5)*1000);
            } catch (InterruptedException ex) {
            }
        }
    }
    
}


public class HomeWork14_UnBlockingQueue {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Random r= new Random();
        int threadCount = r.nextInt(10);
        UnBlockingQueue<String> q = new UnBlockingQueue<>();
        
        for (int i=0;i<threadCount;i++){
            new Thread(new QueueRunner(q),"Thread_"+i).start();
        }
    }
    
}
