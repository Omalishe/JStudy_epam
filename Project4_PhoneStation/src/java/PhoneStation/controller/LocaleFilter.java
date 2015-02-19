package PhoneStation.controller;

import java.io.IOException;
import java.util.Locale;
import java.util.StringTokenizer;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * This filter is the first filter in chain and its purpose is do detect and set 
 * language to display user pages
 * @author Oleksandr Malishevskyi
 */
public class LocaleFilter implements Filter {
    
    private FilterConfig filterConfig = null;
    
    public LocaleFilter() {
    }    
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
            Locale browserLocale = request.getLocale();
            Locale sessionLocale = (Locale) ((HttpServletRequest)request).getSession().getAttribute("userLocale");
            String userLocale = request.getParameter("locale");
            /*
            First, we try to retrieve locale from user query, then, if it's null,
            from user session. If it's not in session yet, we get browser preferenses.
            */
            Locale chosenLocale = null;
            
            if (userLocale!=null){
                chosenLocale = new Locale(userLocale);
            }else if (sessionLocale!=null){
                chosenLocale = sessionLocale;
            }else{
                chosenLocale = browserLocale;
            }
            ((HttpServletRequest)request).getSession().setAttribute("userLocale", chosenLocale);
            request.setAttribute("language", chosenLocale.getLanguage());
            chain.doFilter(request, response);
    }

    @Override
    public void destroy() {        
    }

    @Override
    public void init(FilterConfig filterConfig) {        
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
        }
    }

    
}
