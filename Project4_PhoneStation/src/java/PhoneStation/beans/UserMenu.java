/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PhoneStation.beans;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author oleksandr
 */
public class UserMenu {
    private List<MenuEntry> items = new ArrayList<>();

    public UserMenu() {
    }
    
    public void addEntry(MenuEntry entry){
        this.items.add(entry);
    }
    
    public List getItems(){
        return this.items;
    }
}
