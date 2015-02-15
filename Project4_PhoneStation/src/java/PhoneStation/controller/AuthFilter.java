/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PhoneStation.controller;

import PhoneStation.beans.User;
import PhoneStation.pages.pages;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author oleksandr
 */
public class AuthFilter implements Filter {
    
    private FilterConfig filterConfig = null;
    
    public AuthFilter() {
    }    
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser==null){
            req.setAttribute("textLogin", "Loginische");
            req.setAttribute("textPassword", "Parolishche");
            req.setAttribute("textSubmit", "submitishe");
            req.getRequestDispatcher(pages.LOGIN_PAGE.getValue()).forward(request, response);
        }else
        {
            //res.s
            //if(currentUser.isIsAdmin()) req.getRequestDispatcher(pages.ADMIN_URL.getValue()).forward(request, response);
            //else req.getRequestDispatcher(pages.ABONENT_URL.getValue()).forward(request, response);
            if (currentUser.isIsAdmin()){
                if (req.getServletPath().equals("/admin")) chain.doFilter(request, response);
                else res.sendRedirect("admin");
            }else{
                if (req.getServletPath().equals("/abonent")) chain.doFilter(request, response);
                else res.sendRedirect("abonent");
            }
            
        }
        //chain.doFilter(request, response);
         
    }

    /**
     * Return the filter configuration object for this filter.
     * @return 
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {        
    }

    /**
     * Init method for this filter
     * @param filterConfig
     */
    @Override
    public void init(FilterConfig filterConfig) {        
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("AuthFilter()");
        }
        StringBuffer sb = new StringBuffer("AuthFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }
    
        
}
