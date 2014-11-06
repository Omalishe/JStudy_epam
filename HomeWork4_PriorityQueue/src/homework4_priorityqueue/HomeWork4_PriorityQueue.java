package homework4_priorityqueue;

import java.util.Comparator;



class MyPriorityQueue<T>{
    private static byte INIT_SIZE=11;
    public Object[] queue;
    private int size;
    private MyComparator comp;

    class MyComparator implements Comparator{

    @Override
    public int compare(Object o1, Object o2) {
        if (o1.hashCode()>o2.hashCode())
            return 1;
        else if (o1.hashCode()<o2.hashCode()) return -1;
        else return 0;
    }
    
}
    
    
    public MyPriorityQueue(){
        this(INIT_SIZE);
    }
    
    public MyPriorityQueue(int initSize){
        this.comp = new MyComparator();
        this.size = 0;
        this.queue = new Object[initSize];
    }
    
    public void add(T obj){
        if (obj == null) throw new NullPointerException();
        if (size>=queue.length) throw new OutOfMemoryError(); 
        int i = size;
        size = i + 1;
        if (i == 0)
            queue[0] = obj;
        else
            siftUp(i, obj);
    }
    
    public T poll(){
        if (size<=0) return null;
        int topIndex = size-1;
        size--;
        T returnValue = (T) queue[0];
        T lastValue = (T) queue[topIndex];
        queue[topIndex]=null;
        if(topIndex!=0) siftDown(0,lastValue);
        return returnValue;
    }
    
    public T peek(){
        return (size==0)?null:(T)queue[0];
    }
    
    public int getSize(){
        return size;
    }

    private void siftUp(int i, T obj) {
        while (i>0){
            int parent = (i-1)>>>1;
            Object pObj = queue[parent];
            if (comp.compare(obj, pObj)>=0) break;
            queue[i]=pObj;
            i=parent;
        }
        queue[i]=obj;
    }
    
    private void siftDown(int i, T lastValue) {
        int limiter = size>>>1;
        while (i<limiter){
            int left = 2*i+1;
            int right = 2*(i+1);
            Object childValue = queue[left];
            int replaceIndex = left;
            if (queue[right]!=null)
                if (comp.compare(queue[left], queue[right])>=0) {
                    childValue = queue[right];
                    replaceIndex = right;
                } 
            
            if (comp.compare(childValue, lastValue)>=0) break;
            queue[i] = childValue;
            i=replaceIndex;
            
        }
        queue[i]=lastValue;
    }
    
}


public class HomeWork4_PriorityQueue {

    public static void main(String[] args) {
        MyPriorityQueue<Integer> mpq = new MyPriorityQueue<>(100);
        mpq.add(5);
        mpq.add(10);
        mpq.add(25);
        mpq.add(13);
        mpq.add(7);
        mpq.add(45);
        mpq.add(12);
        mpq.add(18);
        mpq.add(4);
        mpq.add(3);
        mpq.add(2);
        mpq.add(1);
        mpq.add(200);
        mpq.add(6);
        mpq.add(100);
        
        for (int i=0;i<mpq.getSize();i++){
            System.out.println(mpq.queue[i]);
        }
        
        System.out.println("----------------------");
        
        int size = mpq.getSize();
        
        for (int i=0;i<size;i++){
            System.out.println(mpq.poll());
        }
        
        
        for (int i=0;i<size;i++){
            System.out.println(mpq.peek());
        }
        
        
    }
}
