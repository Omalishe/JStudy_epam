package PhoneStation.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * A POJO describing the "Service" DB Entity
 * @author Oleksandr Malishevskyi
 */
public class Service implements MultiLang{
    private Integer id;
    private Double price;
    private Map<String,String> nameLangMap = new HashMap<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    
    @Override
    public void addLang(String lang, String name){
        nameLangMap.put(lang, name);
    }
    
    @Override
    public Map getNameLangMap(){
        Map<String,String> returnValue = new HashMap<>();
        for (Map.Entry en:nameLangMap.entrySet()){
            returnValue.put((String)en.getKey(), (String)en.getValue());
        }
        return returnValue;
    }

    @Override
    public void clearLangs() {
        nameLangMap.clear();
    }
    
}
