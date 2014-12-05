/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework8_matrix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;



class MatrixMultiplier implements Runnable{

    int startRow =0;
    int endRow =0;
    int[][] matrixA;
    int[][] matrixB;
    int[][] matrixOut;

    public MatrixMultiplier(int[][] matrixA, int[][] matrixB, int [][]destMatrix, int startRow, int endRow) {
        this.startRow=startRow;
        this.endRow = endRow;
        this.matrixA=matrixA;
        this.matrixB=matrixB;
        this.matrixOut=destMatrix;
    }
    
    
    
    @Override
    public void run() {
        int n = matrixA.length; //common size for all matrixes
        if (n>0){ //then all the matrixes have non-zero size
            for (int i=startRow;i<=endRow;i++){
                for (int j=0;j<n;j++){
                    for (int k=0;k<n;k++){
                        matrixOut[i][k]+=matrixA[i][j]*matrixB[j][k];
                    }
                }
            }
        }
    }
    
}


public class HomeWork8_Matrix {

    public static void main(String[] args) throws InterruptedException {
        int n = 0;
        int k = 0;
        System.out.print("Enter the matrix constrain: ");
        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
        try {
            n = Integer.parseInt(buf.readLine());
        } catch (IOException ex) {
            n=0;
        }
        
        
        System.out.print("Enter number of threads: ");
        try {
            k = Integer.parseInt(buf.readLine());
        } catch (IOException ex) {
            k=0;
        }
        
        if (n<1||k<1) return;
        
        
        Thread[] threadPool = new Thread[k];
        
        int [][] matrixA = new int[n][n];
        int [][] matrixB = new int[n][n];
        int [][] matrixOut = new int [n][n];
        System.out.println(LocalTime.now());
        //filling matrixes with random numbers
        for (int i=0;i<n;i++){
            for (int j=0;j<n;j++){
                matrixA[i][j]=(int)(Math.random()*100);
                matrixB[i][j]=(int)(Math.random()*100);
            }
        }
        System.out.println(LocalTime.now());
        
        //calculating row range for each thread
        int rowPerThread = n/k;
        int rowsLeft =n;
        int start =0;
        int end = rowPerThread-1;
        for (int i=0;i<k;i++){
            if(rowsLeft>0){
                if (i==(k-1)) end=n-1; //last thread gets all the leftover rows
                threadPool[i]=new Thread(new MatrixMultiplier(matrixA, matrixB, matrixOut, start, end));
                threadPool[i].start();
                
                
                
                rowsLeft-=rowPerThread;
                start+=rowPerThread;
                end = start+Math.min(rowsLeft, rowPerThread)-1;
            }
        }
        System.out.println(LocalTime.now());
        
        for (int i=0;i<k;i++){
            threadPool[i].join(); //main thread waits all other to finish
        }
        System.out.println(LocalTime.now());
  
        /*System.out.println("Source matrix A:");
        for (int i=0;i<n;i++){
            for (int j=0;j<n;j++){
                System.out.print(matrixA[i][j]+"\t");
            }
            System.out.println("");
        }
        
        System.out.println("Source matrix B:");
        for (int i=0;i<n;i++){
            for (int j=0;j<n;j++){
                System.out.print(matrixB[i][j]+"\t");
            }
            System.out.println("");
        }
        
        System.out.println("Result matrix C:");
        for (int i=0;i<n;i++){
            for (int j=0;j<n;j++){
                System.out.print(matrixOut[i][j]+"\t");
            }
            System.out.println("");
        }*/
                
    }
    
}
