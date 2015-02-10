/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common_classes;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author oleksandr
 */
public class Message implements Serializable{
    private String nickname;
    private String text;
    private Date date;
    private static final long serialVersionUID=1L;

    public Message(String nickname, String text, Date date) {
        this.nickname = nickname;
        this.text = text;
        this.date = date;
    }

    public String getNickname() {
        return nickname;
    }

    public String getText() {
        return text;
    }

    public Date getDate() {
        return date;
    }
    
}
