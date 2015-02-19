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
            req.getRequestDispatcher(pages.LOGIN_PAGE).forward(request, response);
        }else
        {
            if (currentUser.isIsAdmin()){
                if (req.getServletPath().equals("/admin")) chain.doFilter(request, response);
                else res.sendRedirect("admin");
            }else{
                if (req.getServletPath().equals("/abonent")) chain.doFilter(request, response);
                else res.sendRedirect("abonent");
            }
        }
    }

    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
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
