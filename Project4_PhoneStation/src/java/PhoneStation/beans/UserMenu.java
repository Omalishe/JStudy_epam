package PhoneStation.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * A JavaBean object representing User Menu, used by front-end user pages. 
 * JSTL's forEach iterates through its items and prints them onto pages
 * @author Oleksandr Malishevskyi
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
