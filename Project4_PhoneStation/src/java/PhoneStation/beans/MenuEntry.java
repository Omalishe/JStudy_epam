package PhoneStation.beans;

/**
 * A JavaBean representing the menu entry in user menu, displayed on pages. 
 * Objects of this class are stored in UserMenu's "items" property and are 
 * printed onto JSP pages on each iteration of JSTL's forEach
 * @author Oleksandr Malishevskyi
 */
public class MenuEntry {
    private String location; //where the "href" attribute of <a> tag has to point
    private String title; //contents of the <a></a> and what users sees in menu

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

    public void setTitle(String title) {
        this.title = title;
    }
    
}
