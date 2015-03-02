package PhoneStation.jstl_helpers;

import PhoneStation.beans.MultiLang;
import java.util.Map;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Custom tag to display names of services and user in selected language
 * @author Oleksandr Malishevskyi
 */
public class displayNameSelector extends SimpleTagSupport {
    private String lang;
    private Object obj;

    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();
        
        try {
            Map<String,String> langMap = ((MultiLang)obj).getNameLangMap();
            out.print(langMap.get(lang));

            JspFragment f = getJspBody();
            if (f != null) {
                f.invoke(out);
            }
            
        } catch (java.io.IOException ex) {
            throw new JspException("Error in displayNameSelector tag", ex);
        }
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

}
