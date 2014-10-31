/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework2;

/**
 *
 * @author oleksandr
 */
public class HomeWork2 {

    //1st task
    public static byte getByteSize(){
        byte value=1;
        byte newValue=value;
        byte n=-1;
        do {
            newValue=value;
            n++;
            value=(byte)(newValue<<1);
        }while(newValue!=value);
        return (byte)(n/8);
    }

    public static byte getShortSize(){
        short value=1;
        short newValue=value;
        byte n=-1;
        do {
            newValue=value;
            n++;
            value=(short)(newValue<<1);
        }while(newValue!=value);
        return (byte)(n/8);
    }

    public static byte getIntSize(){
        int value=1;
        int newValue=value;
        byte n=-1;
        do {
            newValue=value;
            n++;
            value=(int)(newValue<<1);
        }while(newValue!=value);
        return (byte)(n/8);
    }

    public static byte getLongSize(){
        long value=1;
        long newValue=value;
        byte n=-1;
        do {
            newValue=value;
            n++;
            value=(long)(newValue<<1);
        }while(newValue!=value);
        return (byte)(n/8);
    }

    public static byte getFloatSize(){
        float value=1;
        float newValue=value;
        byte n=-1;
        do {
            newValue=value;
            n++;
            value=(float)((int)newValue<<1);
        }while(newValue!=value);
        return (byte)(n/8);
    }

    public static byte getDoubleSize(){
        double value=1;
        double newValue=value;
        byte n=-1;
        do {
            newValue=value;
            n++;
            value=(double)((long)newValue<<1);
        }while(newValue!=value);
        return (byte)(n/8);
    }

    public static byte getCharSize(){
        char value=1;
        char newValue=value;
        byte n=-1;
        do {
            newValue=value;
            n++;
            value=(char)((int)newValue<<1);
        }while(newValue!=value);
        return (byte)(n/8);
    }
    
    //2nd task
    public static int setCustomBit(int sourceValue, byte sourceByte, boolean value){
        int helperValue=(value?1:0);
        helperValue=helperValue<<sourceByte;
        return sourceValue|helperValue;
    }
    
    public static void main(String[] args) {
        
        //Task 1
        System.out.println("Size of byte: "+getByteSize());
        System.out.println("Size of short: "+getShortSize());
        System.out.println("Size of int: "+getIntSize());
        System.out.println("Size of long: "+getLongSize());
        System.out.println("Size of float: "+getFloatSize());
        System.out.println("Size of double: "+getDoubleSize());
        System.out.println("Size of char: "+getCharSize());
        
        
        //Task 2
        int a=1;
        a=setCustomBit(a, (byte)1, true);
        System.out.println(a);
        
        //Task 3
        double accumulator = 1;
        String denominator="";
        for (byte i=1;i<=7;i++){
            denominator+="9";
            accumulator+=1./Integer.parseInt(denominator);
        }
        System.out.println("Left to right: "+accumulator);
        
        accumulator=0;
        denominator="9999999";
        while (!"".equals(denominator)){
            accumulator+=1./Integer.parseInt(denominator);
            denominator = denominator.substring(1);
        }
        accumulator++;
        System.out.println("Right to left: "+accumulator);
        
        //Task 3.1 - one-string represention
        System.out.print("Left to right: ");
        System.out.println(1+1/9+1/99+1/999+1/9999+1/99999+1/999999+1/9999999);
        
        System.out.print("Right to left: ");
        System.out.println(1/9999999+1/999999+1/99999+1/9999+1/999+1/99+1/9+1);
        
        
        //Task 3.2 - one-string represention with forcing to double
        System.out.print("Left to right: ");
        System.out.println(1+1./9+1./99+1./999+1./9999+1./99999+1./999999+1./9999999);
        
        System.out.print("Right to left: ");
        System.out.println(1./9999999+1./999999+1./99999+1./9999+1./999+1./99+1./9+1);


    }
    
}
