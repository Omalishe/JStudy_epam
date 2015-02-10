/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2_vowelstoconsonants;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class Word implements Comparable{
    private List<Symbol> chainOfSymbols = null;
    private int vowelCount;
    private int totalCount;
    private double vowelRelation;
    private String completeWord; //TODO:Think of replacing with mutable buffer
    private boolean isCompiled;
    
    public synchronized void add(char c){
        if (isCompiled) init(); //with new char we've got to recompile the word later
        chainOfSymbols.add(new Symbol(c));
    }
    
    public double getRelation(){
        return vowelRelation;
    }
    
    public synchronized void compile(){
        init(); //in case of calling from toString() and compareTo()
        StringBuilder sb = new StringBuilder();
        for (Symbol a:chainOfSymbols){
            totalCount++;
            if (a.isVowel()) vowelCount++;
            sb.append(a.getRepresentation());
        }
        completeWord=sb.toString();
        if (totalCount==0){
            vowelRelation=0;
        }else{
            vowelRelation=new BigDecimal(((double)vowelCount)/totalCount).setScale(3, RoundingMode.HALF_UP).doubleValue();
        }
        isCompiled=true;
    }
    
    @Override
    public String toString(){
        if (!isCompiled) compile();
        return completeWord;
    }
    

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Word other = (Word) obj;
        if (!Objects.equals(this.completeWord.intern(), other.completeWord.intern())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.completeWord.intern());
        return hash;
    }

    @Override
    public int compareTo(Object o) { //will be used in PriorityQueue
        if (!isCompiled) compile();
        if (this.getRelation()>((Word)o).getRelation()) return 1;
        if (this.getRelation()<((Word)o).getRelation()) return -1;
        return 0;
    }
    private synchronized void init(){
        vowelCount=0;
        totalCount=0;
        vowelRelation=0;
        isCompiled=false;
    }
    
    public Word() {
        chainOfSymbols=new LinkedList<>();
        init();
    }
    
    
}
