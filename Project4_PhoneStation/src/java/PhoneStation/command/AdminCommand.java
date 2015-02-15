/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PhoneStation.command;

import PhoneStation.beans.Bill;
import PhoneStation.beans.Call;
import PhoneStation.beans.MenuEntry;
import PhoneStation.beans.UserMenu;
import PhoneStation.model.DaoFactory;
import PhoneStation.model.DaoServices;
import PhoneStation.beans.Service;
import PhoneStation.beans.User;
import PhoneStation.model.DaoBills;
import PhoneStation.model.DaoCalls;
import PhoneStation.model.DaoUsers;
import PhoneStation.pages.pages;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author oleksandr
 */
public class AdminCommand implements Command{

    private static org.apache.logging.log4j.Logger httpLogger = LogManager.getLogger("app.http");
    private static org.apache.logging.log4j.Logger coreLogger = LogManager.getLogger("app.core");
    
    public AdminCommand() {
    }
    
    @Override
    public void execute(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        String section = httpRequest.getParameter("section");
        if (section==null) section = "";
        
        switch(section){
            case "services": processServices(httpRequest, httpResponse); break;
            case "calls": processCalls(httpRequest, httpResponse); break;
            case "bills": processBills(httpRequest, httpResponse); break;
            case "abonents": processAbonents(httpRequest, httpResponse); break;
            default: displayMenu(httpRequest, httpResponse);
        }
    }
    
    private UserMenu getUserMenu(){
        UserMenu userMenu = new UserMenu();
        userMenu.addEntry(new MenuEntry(pages.ADMIN_URL.getValue()+"?section=services&action=showAll","mneShowAllServices"));
        userMenu.addEntry(new MenuEntry(pages.ADMIN_URL.getValue()+"?section=bills&action=showPending","mneShowPendingBills"));
        userMenu.addEntry(new MenuEntry(pages.ADMIN_URL.getValue()+"?section=bills&action=showAll","mneShowAllBills"));
        userMenu.addEntry(new MenuEntry(pages.ADMIN_URL.getValue()+"?section=calls&action=showAll","mneShowAllCalls"));
        userMenu.addEntry(new MenuEntry(pages.ADMIN_URL.getValue()+"?section=abonents&action=showAll","mneShowAllAbonents"));
        return userMenu;
    }
    
    private void displayMenu(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
        httpRequest.setAttribute("userMenu", getUserMenu());
        httpRequest.setAttribute("pageCaption", "pgcAdminPage");
        try {
            httpRequest.getRequestDispatcher(pages.ADMIN_PAGE.getValue()).forward(httpRequest, httpResponse);
        } catch (ServletException ex) {
            httpLogger.error("http dispatch error:", ex);
        } catch (IOException ex) {
            coreLogger.error("http dispatch error:", ex);
        }
        
    }

    private void processServices(HttpServletRequest request, HttpServletResponse response){
        String action = (String)request.getParameter("action");
        if (action==null) action = "";
        
        int selectedUserID;
        try {
            selectedUserID = Integer.parseInt((String) request.getParameter("selectedUserId"));
        } catch (NumberFormatException numberFormatException) {
            coreLogger.error("Error getting selected user id;", numberFormatException);
            selectedUserID=-1;
        } catch (NullPointerException e){
            selectedUserID=-1;
        }
        request.setAttribute("selectedUserId", selectedUserID);
        DaoServices ds = DaoFactory.getDaoServices();
        
        switch(action){
            case "showAll": 
                try {
                    List<Service> servicesList = ds.getServices(selectedUserID);
                    
                    DaoUsers du = DaoFactory.getDaoUsers();
                    List<User> usersList = du.getUsers();
                    request.setAttribute("usersList", usersList);
                    
                    request.setAttribute("servicesList", servicesList);
                    request.setAttribute("pageCaption", "pgcServices");
                    request.setAttribute("userMenu", getUserMenu());
                    request.setAttribute("data", "services");
                    
                    request.getRequestDispatcher(pages.ADMIN_PAGE.getValue()).forward(request, response);
                } catch (ServletException ex) {
                    httpLogger.error("http dispatch error:", ex);
                } catch (IOException ex) {
                    coreLogger.error("http dispatch error:", ex);
                }
                break;
            case "serviceAddForm":
                try{
                    request.setAttribute("data", "serviceAddForm");
                    request.setAttribute("pageCaption", "pgcAddService");
                    request.setAttribute("userMenu", getUserMenu());
                    request.getRequestDispatcher(pages.ADMIN_PAGE.getValue()).forward(request, response);
                } catch (ServletException ex) {
                    httpLogger.error("http dispatch error:", ex);
                } catch (IOException ex) {
                    coreLogger.error("http dispatch error:", ex);
                }
                break;
            case "serviceAddCompletion":
                request.setAttribute("pageCaption", "pgcAddService");
                String serviceName = "";
                double cost = 0.0;
                
                boolean validated=true;
                try {
                    serviceName = request.getParameter("serviceName");
                    cost = Double.parseDouble(request.getParameter("cost"));
                } catch (NullPointerException|NumberFormatException e) {
                    validated=false;
                }
                if (!validated){
                    request.setAttribute("pageText", "lblServiceRegistrationFailure");
                }else{
                    Service svc = new Service();
                    svc.setName(serviceName);
                    svc.setPrice(cost);
                    ds.addService(svc);
                    request.setAttribute("pageText", "lblServiceRegistrationSucessful");
                }

                request.setAttribute("data", "actionResult");
                request.setAttribute("userMenu", getUserMenu());
                try {
                    request.getRequestDispatcher(pages.ADMIN_PAGE.getValue()).forward(request, response);
                } catch (ServletException ex) {
                    httpLogger.error("http dispatch error:", ex);
                } catch (IOException ex) {
                    coreLogger.error("http dispatch error:", ex);
                }
                
                
                break;
            case "enableServices": //changeUserServices(request, response, selectedUserID, true); break;
            case "disableServices": //changeUserServices(request, response, selectedUserID, false); break;
            default: displayMenu(request, response);
        }
    }
    
    private void changeUserServices(HttpServletRequest request, HttpServletResponse response, int selectedUserID, boolean enable){
        String[] checked_services = request.getParameterValues("checked_services");
        if (checked_services!=null) {
            DaoServices ds = DaoFactory.getDaoServices();
            for (String id : checked_services) {
                ds.changeUserServices(Integer.parseInt(id), selectedUserID, enable);
            }
            
            if (enable) request.setAttribute("pageText","lblServiceLinkSuccess");
            else request.setAttribute("pageText","lblServiceUnlinkSuccess");
            
            request.setAttribute("data", "actionResult");
            request.setAttribute("userMenu", getUserMenu());
            request.setAttribute("pageCaption", "pgcServices");
            
            try {
                request.getRequestDispatcher(pages.ADMIN_PAGE.getValue()).forward(request, response);
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
        
        
        int selectedUserID;
        try {
            selectedUserID = Integer.parseInt((String) request.getParameter("selectedUserId"));
        } catch (NumberFormatException numberFormatException) {
            coreLogger.error("Error getting selected user id;", numberFormatException);
            selectedUserID=-1;
        } catch (NullPointerException e){
            selectedUserID=-1;
        }
        request.setAttribute("selectedUserId", selectedUserID);
        
        
        String action = (String)request.getParameter("action");
        if (action==null) action = "";
        request.setAttribute("showAction", action); //for update forms
        
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
                    List<Bill> billsList = db.getBills(selectedUserID, startDate, endDate, pending);
                    
                    DaoUsers du = DaoFactory.getDaoUsers();
                    List<User> usersList = du.getUsers();
                    request.setAttribute("usersList", usersList);
                    
                    request.setAttribute("data", "bills");
                    request.setAttribute("billsList", billsList);
                    request.setAttribute("pageCaption", "pgcBills");
                    request.setAttribute("userMenu", getUserMenu());
                    request.getRequestDispatcher(pages.ADMIN_PAGE.getValue()).forward(request, response);
                } catch (ServletException ex) {
                    httpLogger.error("http dispatch error:", ex);
                } catch (IOException ex) {
                    coreLogger.error("http dispatch error:", ex);
                }
                break;
            case "payBill":  //not used in admin console
                /* DaoBills db = DaoFactory.getDaoBills();
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
                break; */
            case "createBill":
                try {
                    DaoBills db = DaoFactory.getDaoBills();
                    db.createBill(selectedUserID, startDate, endDate);
                    request.setAttribute("data", "actionResult");
                    request.setAttribute("pageText", "lblBillCreationSuccessful");
                    request.setAttribute("userMenu", getUserMenu());
                    request.setAttribute("pageCaption", "pgcBills");
                    request.getRequestDispatcher(pages.ADMIN_PAGE.getValue()).forward(request, response);
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
        
        int selectedUserID;
        try {
            selectedUserID = Integer.parseInt((String) request.getParameter("selectedUserId"));
        } catch (NumberFormatException numberFormatException) {
            coreLogger.error("Error getting selected user id;", numberFormatException);
            selectedUserID=-1;
        } catch (NullPointerException e){
            selectedUserID=-1;
        }
        request.setAttribute("selectedUserId", selectedUserID);
        
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
            case "showAll":
                try {
                    DaoUsers du = DaoFactory.getDaoUsers();
                    List<User> usersList = du.getUsers();
                    request.setAttribute("usersList", usersList);
                    
                    List<Call> callsList = daoCalls.getCalls(selectedUserID, startDate, endDate);
                    request.setAttribute("data", "callsListing");
                    request.setAttribute("callsList", callsList);
                    request.setAttribute("pageCaption", "pgcCalls");
                    request.setAttribute("userMenu", getUserMenu());
                    request.getRequestDispatcher(pages.ADMIN_PAGE.getValue()).forward(request, response);
                } catch (ServletException ex) {
                    httpLogger.error("http dispatch error:", ex);
                } catch (IOException ex) {
                    coreLogger.error("http dispatch error:", ex);
                }
                break;
            case "registerForm": // not used here
                /*try {
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
                break; */
            case "registerComplete": //not used here either
                /*Date registerDate;
                
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
                break;*/
            default: displayMenu(request, response); break;
        }
    }

    private void processAbonents(HttpServletRequest request, HttpServletResponse response) {
        String action = (String)request.getParameter("action");
        if (action==null) action = "";
        
        int selectedUserID;
        try {
            selectedUserID = Integer.parseInt((String) request.getParameter("selectedUserId"));
        } catch (NumberFormatException numberFormatException) {
            coreLogger.error("Error getting selected user id;", numberFormatException);
            selectedUserID=-1;
        } catch (NullPointerException e){
            selectedUserID=-1;
        }
        request.setAttribute("selectedUserId", selectedUserID);
        
        DaoUsers daoUsers = DaoFactory.getDaoUsers();
        
        switch(action){
            case "showAll":
                try {
                    DaoUsers du = DaoFactory.getDaoUsers();
                    List<User> usersList = du.getUsers();
                    request.setAttribute("usersList", usersList);
                    
                    request.setAttribute("data", "usersListing");
                    request.setAttribute("pageCaption", "pgcUsers");
                    request.setAttribute("userMenu", getUserMenu());
                    request.getRequestDispatcher(pages.ADMIN_PAGE.getValue()).forward(request, response);
                } catch (ServletException ex) {
                    httpLogger.error("http dispatch error:", ex);
                } catch (IOException ex) {
                    coreLogger.error("http dispatch error:", ex);
                }
                break; 
            case "addAbonentForm":
                try{
                    request.setAttribute("data", "addAbonentForm");
                    request.setAttribute("pageCaption", "pgcAddAbonent");
                    request.setAttribute("userMenu", getUserMenu());
                    request.getRequestDispatcher(pages.ADMIN_PAGE.getValue()).forward(request, response);
                } catch (ServletException ex) {
                    httpLogger.error("http dispatch error:", ex);
                } catch (IOException ex) {
                    coreLogger.error("http dispatch error:", ex);
                }
                break; 
            case "addAbonentCompletion":
                request.setAttribute("pageCaption", "pgcAddAbonent");
                
                String username="";
                String password="";
                String phoneNumber="";
                
                boolean isAdmin = (request.getParameter("isAdmin")!=null);
                boolean isBlocked = (request.getParameter("isBlocked")!=null);
                
                boolean validated=true;
                try {
                    username = request.getParameter("username");
                    password = request.getParameter("password");
                    phoneNumber = request.getParameter("phoneNumber");
                } catch (NullPointerException e) {
                    validated=false;
                }
                
                if (!validated){
                    request.setAttribute("pageText", "lblAbonentRegistrationFailure");
                }else{
                    User user = new User();
                    user.setIsAdmin(isAdmin);
                    user.setUserName(username);
                    user.setPassword(password);
                    user.setIsDisabled(isBlocked);
                    user.setPhoneNumber(phoneNumber);
                    
                    daoUsers.addUser(user);
                    
                    request.setAttribute("pageText", "lblAbonentRegistrationSucessful");
                }

                request.setAttribute("data", "actionResult");
                request.setAttribute("userMenu", getUserMenu());
                try {
                    request.getRequestDispatcher(pages.ADMIN_PAGE.getValue()).forward(request, response);
                } catch (ServletException ex) {
                    httpLogger.error("http dispatch error:", ex);
                } catch (IOException ex) {
                    coreLogger.error("http dispatch error:", ex);
                }
                break;
            case "enableAbonent": //changeAbonentStatus(selectedUserID,true,request,response); break;
            case "disableAbonent"://changeAbonentStatus(selectedUserID,false,request,response);break;
            case "deleteAbonent": 
            default: displayMenu(request, response);
        }
    }

    private void changeAbonentStatus(int selectedUserID, boolean enabled, HttpServletRequest request, HttpServletResponse response) {
        DaoUsers ds = DaoFactory.getDaoUsers();
        ds.enableUser(selectedUserID, enabled);
        
        if (enabled) request.setAttribute("pageText","lblUserEnableSuccess");
        else request.setAttribute("pageText","lblUserDisableSuccess");

        request.setAttribute("data", "actionResult");
        request.setAttribute("userMenu", getUserMenu());
        request.setAttribute("pageCaption", "pgcUsers");

        try {
            request.getRequestDispatcher(pages.ADMIN_PAGE.getValue()).forward(request, response);
        } catch (ServletException ex) {
            httpLogger.error("http dispatch error:", ex);
        } catch (IOException ex) {
            coreLogger.error("http dispatch error:", ex);
        }
    }
}