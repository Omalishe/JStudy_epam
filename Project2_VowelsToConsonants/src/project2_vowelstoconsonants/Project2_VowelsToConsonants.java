/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2_vowelstoconsonants;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author oleksandr
 */
public class Project2_VowelsToConsonants {

    /**
     * @param args the command line arguments 
     * @throws java.io.IOException 
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br;
        PriorityQueue<Word> pq = new PriorityQueue<>();
        try {
            //reading file to array of words
            br = new BufferedReader(new InputStreamReader(new FileInputStream("/home/oleksandr/tale")));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Project2_VowelsToConsonants.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        Set<Word> wordSet = new HashSet();
        char c;
        int ic;
        Word ob = new Word();
        while ((ic=br.read())!=-1){
            c=(char)ic;
            if ((c==' ')||(c=='!')||(c=='.')||(c==',')||(c=='?')||(c=='\t')||(c=='\n')){
                ob.compile();
                wordSet.add(ob);
                ob = new Word();
            }else{
                ob.add(c);
            }
        }
        
        pq.addAll(wordSet);
        Word a;
        while((a=pq.poll())!=null){
            System.out.println(""+a+" " + a.getRelation());// + " "+a.hashCode());
        }
        
    }
    
}
