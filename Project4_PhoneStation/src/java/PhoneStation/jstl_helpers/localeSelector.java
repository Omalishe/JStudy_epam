package PhoneStation.jstl_helpers;

import PhoneStation.pages.Langs;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;


/**
 * Custom tag to display available languages menu
 * @author Malishevskyi Olekdsndr
 */
public class localeSelector extends SimpleTagSupport {

    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();
        try {
            out.println("<ul class=\"nav navbar-nav navbar-right\">");
            for (String lang:Langs.langs){
                out.println("<li><a class=\"navbar-link\" href = \"?locale="+lang+"\">"+lang+"</a></li>");
            }
            out.println("</ul>");

            JspFragment f = getJspBody();
            if (f != null) {
                f.invoke(out);
            }

        } catch (java.io.IOException ex) {
            throw new JspException("Error in localeSelector tag", ex);
        }
    }
    
}
