/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PhoneStation.beans;

/**
 *
 * @author oleksandr
 */
public class MenuEntry {
    private String location;
    private String title;

    public MenuEntry() {
    }

    public MenuEntry(String location, String title) {
        this.location = location;
        this.title = title;
    }

    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
}
