package PhoneStation.beans;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
* A POJO describing the "User" DB Entity
* @author Oleksandr Malishevskyi
*/
public class User implements MultiLang{
    private String userName;
    private boolean isAdmin;
    private boolean isDisabled;
    private String password;
    private Integer id;
    private String phoneNumber;
    private Map<String,String> nameLangMap = new HashMap<>();

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isIsAdmin() {
        return isAdmin;
    }
    
    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean isIsDisabled() {
        return isDisabled;
    }
    
    public boolean getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void addLang(String lang, String name){
        nameLangMap.put(lang, name);
    }
    
    @Override
    public Map getNameLangMap(){
        Map<String,String> returnValue = new HashMap<>();
        for (Entry en:nameLangMap.entrySet()){
            returnValue.put((String)en.getKey(), (String)en.getValue());
        }
        return returnValue;
    }
    
    @Override
    public void clearLangs() {
        nameLangMap.clear();
    }

}
