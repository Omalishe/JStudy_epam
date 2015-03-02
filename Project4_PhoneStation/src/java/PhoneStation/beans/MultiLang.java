package PhoneStation.beans;

import java.util.Map;

/**
 * This interface allows to treat all multi-language entities as 
 * @author oleksandr
 */
public interface MultiLang {
    public void addLang(String lang, String name);
    public Map getNameLangMap();
    public void clearLangs();
}
