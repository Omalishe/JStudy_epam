/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2_vowelstoconsonants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author oleksandr
 */
public class Symbol {
    private char representation;
    private boolean isVowel;
    private static Pattern pt = Pattern.compile("[eiyouaEIYOUAаеєыоіюэуияАЕЄЫОІЮЭУИЯ]");
    
    
    public char getRepresentation(){
        return representation;
    }
    
    public boolean isVowel(){
        return isVowel;
    }
    
    public Symbol(char representation) {
        this.representation = representation;
        //Pattern pt = Pattern.compile("[eiyouaEIYOUAаеєыоіюэуияАЕЄЫОІЮЭУИЯ]");
        Matcher m = pt.matcher(""+representation);
        this.isVowel=m.matches();
        /*switch(representation){
            //latins
            case 'e':case 'E':case 'y':case 'Y':case 'u':case 'U':
            case 'i':case 'I':case 'o':case 'O':case 'a':case 'A':
            //cyrillics
            case 'а':case 'А':case 'о':case 'О':case 'у':case 'У':
            case 'е':case 'Е':case 'і':case 'І':case 'и':case 'И':
            case 'є':case 'Є':case 'ю':case 'Ю':case 'я':case 'Я':
            case 'ы':case 'Ы':case 'э':case 'Э': 
                this.isVowel=true; break;
            default: this.isVowel = false;
        }*/
    }
    
    
}
