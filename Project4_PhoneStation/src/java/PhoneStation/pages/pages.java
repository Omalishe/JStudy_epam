/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PhoneStation.pages;

/**
 *
 * @author oleksandr
 */
public enum pages {
    ERROR_PAGE("/WEB-INF/view/printableError.jsp"),
    ADMIN_URL("admin"),
    ADMIN_PAGE("/WEB-INF/view/admin.jsp"),
    ABONENT_URL("abonent"),
    ABONENT_PAGE("/WEB-INF/view/abonent.jsp"),
    LOGIN_URL("logon"),
    LOGIN_PAGE("/WEB-INF/view/login.jsp"),
    
    INDEX_PAGE("index.jsp")
    
    ;
    private String value;

    private pages(String v) {
        this.value =v;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
