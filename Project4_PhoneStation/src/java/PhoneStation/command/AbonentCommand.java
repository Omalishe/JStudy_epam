/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PhoneStation.command;

import PhoneStation.beans.User;
import PhoneStation.beans.Service;
import PhoneStation.beans.Call;
import PhoneStation.beans.Bill;
import PhoneStation.beans.*;
import PhoneStation.model.*;
import PhoneStation.pages.pages;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.logging.log4j.*;

/**
 *
 * @author oleksandr
 */
class AbonentCommand implements Command {
    private static Logger httpLogger = LogManager.getLogger("app.http");
    private static Logger coreLogger = LogManager.getLogger("app.core");
    
    public AbonentCommand() {
    }

    @Override
    public void execute(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        String section = httpRequest.getParameter("section");
        if (section==null) section="";
        
        switch(section){
            case "services": processServices(httpRequest, httpResponse); break;
            case "calls": processCalls(httpRequest, httpResponse); break;
            case "bills": processBills(httpRequest, httpResponse); break;
            default: displayMenu(httpRequest, httpResponse);
        }
    }
    
    private UserMenu getUserMenu(){
        UserMenu userMenu = new UserMenu();
        userMenu.addEntry(new MenuEntry(pages.ABONENT_URL.getValue()+"?section=services&action=showMy","mneShowMyServices"));
        userMenu.addEntry(new MenuEntry(pages.ABONENT_URL.getValue()+"?section=services&action=showAll","mneShowAllServices"));
        userMenu.addEntry(new MenuEntry(pages.ABONENT_URL.getValue()+"?section=calls&action=showMy","mneShowMyCalls"));
        userMenu.addEntry(new MenuEntry(pages.ABONENT_URL.getValue()+"?section=bills&action=showAll","mneShowAllBills"));
        userMenu.addEntry(new MenuEntry(pages.ABONENT_URL.getValue()+"?section=bills&action=showPending","mneShowPendingBills"));
        userMenu.addEntry(new MenuEntry(pages.ABONENT_URL.getValue()+"?section=calls&action=registerForm","mnePlaceACall"));
        return userMenu;
    }
    
    private void displayMenu(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
        httpRequest.setAttribute("userMenu", getUserMenu());
        httpRequest.setAttribute("pageCaption", "pgcAbonentPage");
        try {
            httpRequest.getRequestDispatcher(pages.ABONENT_PAGE.getValue()).forward(httpRequest, httpResponse);
        } catch (ServletException ex) {
            httpLogger.error("http dispatch error:", ex);
        } catch (IOException ex) {
            coreLogger.error("http dispatch error:", ex);
        }
        
    }
    
    private void processServices(HttpServletRequest request, HttpServletResponse response){
        String action = (String)request.getParameter("action");
        if (action==null) action = "";
        User user = null;
        switch(action){
            case "showMy": 
                HttpSession ses = request.getSession();
                user = (User) ses.getAttribute("currentUser");
            case "showAll": 
                try {
                    DaoServices ds = DaoFactory.getDaoServices();
                    List<Service> servicesList = ds.getServices(user);
                    
                    request.setAttribute("servicesList", servicesList);
                    request.setAttribute("pageCaption", "pgcServices");
                    request.setAttribute("userMenu", getUserMenu());
                    
                    if ("showMy".equals(action))request.setAttribute("data", "myServices");
                    else request.setAttribute("data", "services");
                    
                    request.getRequestDispatcher(pages.ABONENT_PAGE.getValue()).forward(request, response);
                } catch (ServletException ex) {
                    httpLogger.error("http dispatch error:", ex);
                } catch (IOException ex) {
                    coreLogger.error("http dispatch error:", ex);
                }
                break;
            case "enableServices": changeUserServices(request, response, true); break;
            case "disableServices": changeUserServices(request, response, false); break;
            default: displayMenu(request, response);
        }
    }
    
    private void changeUserServices(HttpServletRequest request, HttpServletResponse response, boolean enable){
        String[] checked_services = request.getParameterValues("checked_services");
        if (checked_services!=null) {
            DaoServices ds = DaoFactory.getDaoServices();
            User user = (User) request.getSession().getAttribute("currentUser");
            for (String id : checked_services) {
                ds.changeUserServices(Integer.parseInt(id), user.getId(),enable);
            }
            
            if (enable) request.setAttribute("pageText","lblServiceLinkSuccess");
            else request.setAttribute("pageText","lblServiceUnlinkSuccess");
            
            request.setAttribute("data", "actionResult");
            request.setAttribute("userMenu", getUserMenu());
            request.setAttribute("pageCaption", "pgcServices");
            
            try {
                request.getRequestDispatcher(pages.ABONENT_PAGE.getValue()).forward(request, response);
            } catch (ServletException ex) {
                httpLogger.error("http dispatch error:", ex);
            } catch (IOException ex) {
                coreLogger.error("http dispatch error:", ex);
            }
        }else{
            displayMenu(request, response);
        }
    }

    private void processBills(HttpServletRequest request, HttpServletResponse response){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate;
        Date endDate;
        Calendar cal = Calendar.getInstance();
        
        String action = (String)request.getParameter("action");
        if (action==null) action = "";
        request.setAttribute("showAction", action); //for update forms
        
        HttpSession ses = request.getSession();
        User user = (User) ses.getAttribute("currentUser");
        boolean pending = false;
        
        try {
            startDate = df.parse((String)request.getParameter("startDate"));
        } catch (ParseException |NullPointerException ex) {
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE,0);
            cal.set(Calendar.SECOND,0);
            cal.set(Calendar.MILLISECOND,0);
            startDate = cal.getTime();
        }
        
        
        try {
            endDate = df.parse((String)request.getParameter("endDate"));
        } catch (ParseException|NullPointerException ex) {
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE,59);
            cal.set(Calendar.SECOND,59);
            cal.set(Calendar.MILLISECOND,999);
            endDate = cal.getTime();
        }
        request.setAttribute("startDate", df.format(startDate));
        request.setAttribute("endDate", df.format(endDate));
        
        switch(action){
            case "showPending": 
                pending = true;
            case "showAll": 
                try {
                    DaoBills db = DaoFactory.getDaoBills();
                    List<Bill> billsList = db.getBills(user, startDate, endDate, pending);
                    request.setAttribute("data", "bills");
                    request.setAttribute("billsList", billsList);
                    request.setAttribute("pageCaption", "pgcBills");
                    request.setAttribute("userMenu", getUserMenu());
                    request.getRequestDispatcher(pages.ABONENT_PAGE.getValue()).forward(request, response);
                } catch (ServletException ex) {
                    httpLogger.error("http dispatch error:", ex);
                } catch (IOException ex) {
                    coreLogger.error("http dispatch error:", ex);
                }
                break;
            case "payBill": 
                DaoBills db = DaoFactory.getDaoBills();
                db.payBill(Integer.parseInt((String) request.getParameter("billId"))); 
                request.setAttribute("data", "actionResult");
                request.setAttribute("pageText", "lblIdPaymentSuccessful");
                request.setAttribute("userMenu", getUserMenu());
                try {
                    request.getRequestDispatcher(pages.ABONENT_PAGE.getValue()).forward(request, response);
                } catch (ServletException ex) {
                    httpLogger.error("http dispatch error:", ex);
                } catch (IOException ex) {
                    coreLogger.error("http dispatch error:", ex);
                }
                break;
            default: displayMenu(request, response);
        }
    }
    
    private void processCalls(HttpServletRequest request, HttpServletResponse response){
        String action = (String)request.getParameter("action");
        if (action==null) action = "";
        
        HttpSession ses = request.getSession();
        User user = (User) ses.getAttribute("currentUser");
        
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate;
        Date endDate;
        Calendar cal = Calendar.getInstance();
        
        try {
            startDate = df.parse((String)request.getParameter("startDate"));
        } catch (ParseException |NullPointerException ex) {
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE,0);
            cal.set(Calendar.SECOND,0);
            cal.set(Calendar.MILLISECOND,0);
            startDate = cal.getTime();
        }
        
        
        try {
            endDate = df.parse((String)request.getParameter("endDate"));
        } catch (ParseException|NullPointerException ex) {
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE,59);
            cal.set(Calendar.SECOND,59);
            cal.set(Calendar.MILLISECOND,999);
            endDate = cal.getTime();
        }
        request.setAttribute("startDate", df.format(startDate));
        request.setAttribute("endDate", df.format(endDate));
        
        DaoCalls daoCalls = DaoFactory.getDaoCalls();
        
        
        switch(action){
            case "showMy":
                try {
                    List<Call> callsList = daoCalls.getCalls(user, startDate, endDate);
                    request.setAttribute("data", "callsListing");
                    request.setAttribute("callsList", callsList);
                    request.setAttribute("pageCaption", "pgcCalls");
                    request.setAttribute("userMenu", getUserMenu());
                    request.getRequestDispatcher(pages.ABONENT_PAGE.getValue()).forward(request, response);
                } catch (ServletException ex) {
                    httpLogger.error("http dispatch error:", ex);
                } catch (IOException ex) {
                    coreLogger.error("http dispatch error:", ex);
                }
                break;
            case "registerForm":
                try {
                    request.setAttribute("registerDate", df.format(Calendar.getInstance().getTime()));
                    request.setAttribute("data", "callRegisterForm");
                    request.setAttribute("pageCaption", "pgcRegisterCall");
                    request.setAttribute("userMenu", getUserMenu());
                    request.getRequestDispatcher(pages.ABONENT_PAGE.getValue()).forward(request, response);
                } catch (ServletException ex) {
                    httpLogger.error("http dispatch error:", ex);
                } catch (IOException ex) {
                    coreLogger.error("http dispatch error:", ex);
                }
                break; 
            case "registerComplete":
                Date registerDate;
                
                try {
                    registerDate = df.parse((String)request.getParameter("registerDate"));
                } catch (ParseException|NullPointerException ex) {
                    registerDate = Calendar.getInstance().getTime();
                }
                
                request.setAttribute("registerDate", registerDate);
                request.setAttribute("pageCaption", "pgcRegisterCall");
                
                int duration=0;
                try {
                    duration = Integer.parseInt((String) request.getParameter("duration"));
                } catch (NumberFormatException|NullPointerException numberFormatException) {
                    coreLogger.error("Error getting duration while registering a call", numberFormatException);
                    
                }
                double cost=0;
                try {
                    cost = Double.parseDouble((String) request.getParameter("cost"));
                } catch (NumberFormatException|NullPointerException numberFormatException) {
                    coreLogger.error("Error getting cost while registering a call", numberFormatException);
                }
                
                Call call = new Call();
                call.setTimeStart(registerDate);
                call.setDuration(duration);
                call.setCost(cost);
                call.setUsersId(user.getId());
                
                daoCalls.registerCall(call);
                
                request.setAttribute("data", "actionResult");
                request.setAttribute("pageText", "lblCallRegistrationSuccessful");
                request.setAttribute("userMenu", getUserMenu());
                try {
                    request.getRequestDispatcher(pages.ABONENT_PAGE.getValue()).forward(request, response);
                } catch (ServletException ex) {
                    httpLogger.error("http dispatch error:", ex);
                } catch (IOException ex) {
                    coreLogger.error("http dispatch error:", ex);
                }
                break;
            default: displayMenu(request, response); break;
        }
    }
}
