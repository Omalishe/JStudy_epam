package PhoneStation.pages;

import java.util.LinkedList;
import java.util.List;


/**
 * It's a "wrapper" class to hold all available site languages for the moment. 
 * @author Oleksandr Malishevskyi
 */
public final class Langs{
    public static final List<String> langs = new LinkedList<>();
    static{
        langs.add("en");
        langs.add("uk");
    }
}
