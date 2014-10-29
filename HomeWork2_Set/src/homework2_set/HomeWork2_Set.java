/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework2_set;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

interface OuterSet extends Iterable<Integer>{

    public int size();
    public boolean isEmpty();
    public boolean add(Integer e);
    public boolean remove(Object o);
    public void clear();
    public OuterSet intersection(OuterSet intersectionSet);
    public OuterSet union(OuterSet unionSet);
    public OuterSet clone();
    public void printToScreen();
    
}

class mySetOnArray implements OuterSet {

    public ArrayList<Integer> a;

    mySetOnArray() {
        this.a = new ArrayList();
    }

    @Override
    public int size() {
        return a.size();
    }

    @Override
    public boolean isEmpty() {
        return a.isEmpty();
    }

    @Override
    public boolean add(Integer e) {
        return a.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return a.remove(o);
    }

    @Override
    public void clear() {
        a.clear();
    }

    @Override
    public void printToScreen() {
        for (Object a : this.a) {
            System.out.println(a);
        }
    }

    @Override
    public OuterSet intersection(OuterSet intersectionSet) {
        OuterSet returnValue = new mySetOnArray();
        for (Integer i : this.a) {
            Iterator iter = intersectionSet.iterator();
            while (iter.hasNext()) {
                Integer j = (Integer) iter.next();
                if (i.equals(j)) {
                    returnValue.add(j);
                }
            }
        }
        return returnValue;
    }

    @Override
    public OuterSet union(OuterSet unionSet) {
        OuterSet returnValue = (OuterSet) this.a.clone();
        Iterator iter = unionSet.iterator();
        boolean valuefound = false;
        while (iter.hasNext()) {
            Integer i = (Integer) iter.next();
            valuefound = false;
            for (Integer j : this.a) {
                if (i.equals(j)) {
                    valuefound = true;
                }
            }
            if (!valuefound) {
                returnValue.add(i);
            }
        }
        return returnValue;
    }

    @Override
    public OuterSet clone() {
        Iterator iter = this.iterator();
        OuterSet returnValue = new mySetOnList();
        while (iter.hasNext()){
            returnValue.add((Integer) iter.next());
        }
        return returnValue;
    }

    @Override
    public Iterator iterator() {
        return this.a.iterator();
    }

}

class mySetOnList implements OuterSet {

    public LinkedList<Integer> a;

    mySetOnList() {
        this.a = new LinkedList();
    }

    @Override
    public int size() {
        return a.size();
    }

    @Override
    public boolean isEmpty() {
        return a.isEmpty();
    }

    @Override
    public boolean add(Integer e) {
        return a.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return a.remove(o);
    }

    @Override
    public void clear() {
        a.clear();
    }

    @Override
    public void printToScreen() {
        for (Object a : this.a) {
            System.out.println(a);
        }
    }

    @Override
    public OuterSet intersection(OuterSet intersectionSet) {
        OuterSet returnValue = new mySetOnList();
        for (Integer i : this.a) {
            Iterator iter = intersectionSet.iterator();
            while (iter.hasNext()) {
                Integer j = (Integer) iter.next();
                if (i.equals(j)) {
                    returnValue.add(j);
                }
            }
        }
        return returnValue;
    }

    @Override
    public OuterSet union(OuterSet unionSet) {
        OuterSet returnValue = this.clone();
        Iterator iter = unionSet.iterator();
        boolean valuefound = false;
        while (iter.hasNext()) {
            Integer i = (Integer) iter.next();
            valuefound = false;
            for (Integer j : this.a) {
                if (i.equals(j)) {
                    valuefound = true;
                }
            }
            if (!valuefound) {
                returnValue.add(i);
            }
        }
        return returnValue;
    }

    @Override
    public OuterSet clone() {
        Iterator iter = this.iterator();
        OuterSet returnValue = new mySetOnList();
        while (iter.hasNext()){
            returnValue.add((Integer) iter.next());
        }
        return returnValue;
    }

     
    @Override
    public Iterator iterator() {
        return this.a.iterator();
    }

}

public class HomeWork2_Set {

    public static void main(String[] args) {

        OuterSet a = new mySetOnList();
        a.add(1);
        a.add(2);
        a.add(3);
        OuterSet b = new mySetOnArray();
        b.add(2);
        b.add(3);
        b.add(4);
        
        
        System.out.println("First collection-------------------");
        a.printToScreen();
        
        System.out.println("Second collection------------------");
        b.printToScreen();
        System.out.println("-----------------------------------");
        OuterSet intersect = a.intersection(b);
        OuterSet union = a.union(b);
        
        
        System.out.println("Intersection-----------------------");
        intersect.printToScreen();
        
        System.out.println("Union------------------------------");
        union.printToScreen();

    }

}
