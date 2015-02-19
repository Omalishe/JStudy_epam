/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PhoneStation.command;

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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        userMenu.addEntry(new MenuEntry(pages.ADMIN_URL+"?section=services&action=showAll","mneShowAllServices"));
        userMenu.addEntry(new MenuEntry(pages.ADMIN_URL+"?section=bills&action=showPending","mneShowPendingBills"));
        userMenu.addEntry(new MenuEntry(pages.ADMIN_URL+"?section=bills&action=showAll","mneShowAllBills"));
        userMenu.addEntry(new MenuEntry(pages.ADMIN_URL+"?section=calls&action=showAll","mneShowAllCalls"));
        userMenu.addEntry(new MenuEntry(pages.ADMIN_URL+"?section=abonents&action=showAll","mneShowAllAbonents"));
        return userMenu;
    }
    
    private void displayMenu(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
        httpRequest.setAttribute("userMenu", getUserMenu());
        httpRequest.setAttribute("pageCaption", "pgcAdminPage");
        try {
            httpRequest.getRequestDispatcher(pages.ADMIN_PAGE).forward(httpRequest, httpResponse);
        } catch (ServletException ex) {
            httpLogger.error("http dispatch error:", ex);
        } catch (IOException ex) {
            coreLogger.error("http dispatch error:", ex);
        }
        
    }
    
    private Date getStartDateOrStartOfMonth(HttpServletRequest request, DateFormat df){
        //DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        Date startDate;
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
        return startDate;
    }
    
    private Date getEndDateOrEndOfMonth(HttpServletRequest request, DateFormat df){
        Date endDate;
        Calendar cal = Calendar.getInstance();
        
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
        return endDate;
    }
    
    private int getSelectedUserId(HttpServletRequest request){
        int selectedUserID;
        try {
            selectedUserID = Integer.parseInt((String) request.getParameter("selectedUserId"));
        } catch (NumberFormatException numberFormatException) {
            coreLogger.error("Error getting selected user id;", numberFormatException);
            selectedUserID=-1;
        } catch (NullPointerException e){
            selectedUserID=-1;
        }
        return selectedUserID;
    }
    
    public void dispatchRequest(HttpServletRequest request, HttpServletResponse response, String data, String pageCaption){
        request.setAttribute("data", data);
        request.setAttribute("pageCaption", pageCaption);
        request.setAttribute("userMenu", getUserMenu());
        try {
            request.getRequestDispatcher(pages.ADMIN_PAGE).forward(request, response);
        } catch (ServletException ex) {
            httpLogger.error("http dispatch error:", ex);
        } catch (IOException ex) {
            coreLogger.error("http dispatch error:", ex);
        }
    }

    private void processServices(HttpServletRequest request, HttpServletResponse response){
        String action = (String)request.getParameter("action");
        if (action==null) action = "";
        
        int selectedUserID = getSelectedUserId(request);
        request.setAttribute("selectedUserId", selectedUserID);
        
        DaoServices ds = DaoFactory.getDaoServices();
        DaoUsers du = DaoFactory.getDaoUsers();
        
        switch(action){
            case "showAll": 
                request.setAttribute("usersList", du.getUsers());
                request.setAttribute("servicesList", ds.getServices(selectedUserID));
                dispatchRequest(request, response, "services", "pgcServices");
                break;
            case "serviceAddForm": 
                dispatchRequest(request, response, "serviceAddForm", "pgcAddService");
                break;
            case "serviceAddCompletion":
            {
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
                    if (ds.addService(svc)) request.setAttribute("pageText", "lblServiceRegistrationSucessful");
                    else request.setAttribute("pageText", "lblServiceRegistrationFailure");
                }

                dispatchRequest(request, response ,"actionResult","pgcAddService");
            }
                break;
            case "serviceEditForm": 
            {
                int serviceId=0;
                try {
                    serviceId = Integer.parseInt(request.getParameter("serviceId"));
                } catch (NumberFormatException|NullPointerException numberFormatException) {
                    serviceId = -1;
                }
                Service svc = ds.getServiceById(serviceId);
                request.setAttribute("editedService", svc);
                dispatchRequest(request, response, "serviceEditForm", "pgcServiceEdit");
            }   
                break;
            case "serviceEditCompletion": 
            {
                
                int serviceId=0;
                boolean validated=true;
                try {
                    serviceId = Integer.parseInt(request.getParameter("serviceId"));
                } catch (NumberFormatException|NullPointerException numberFormatException) {
                    serviceId = -1;
                    validated = false;
                }
                Service svc = ds.getServiceById(serviceId);
                
                String serviceName = "";
                double cost = 0.0;
                
                try {
                    serviceName = request.getParameter("serviceName");
                    cost = Double.parseDouble(request.getParameter("cost"));
                } catch (NullPointerException|NumberFormatException e) {
                    validated=false;
                }
                
                if (!validated){
                    request.setAttribute("pageText", "lblServiceModificationFailure");
                }else{
                    svc.setName(serviceName);
                    svc.setPrice(cost);
                    if (ds.updateService(svc)) request.setAttribute("pageText", "lblServiceModificationSuccessful");
                    else request.setAttribute("pageText", "lblServiceModificationFailure");
                }

                dispatchRequest(request, response ,"actionResult","pgcServiceEdit");
            }
                break;
            case "serviceDelete": 
                int serviceId=0;
                try {
                    serviceId = Integer.parseInt(request.getParameter("serviceId"));
                } catch (NumberFormatException|NullPointerException numberFormatException) {
                    serviceId = -1;
                }
                if (ds.deleteService(serviceId)) request.setAttribute("pageText", "lblServiceDeletionSuccessful");
                else request.setAttribute("pageText", "lblServiceDeletionFailure");
                dispatchRequest(request, response, "actionResult", "pgcServices");
                break;
            default: displayMenu(request, response);
        }
    }

    private void processBills(HttpServletRequest request, HttpServletResponse response){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        
        Date startDate = getStartDateOrStartOfMonth(request, df);
        Date endDate = getEndDateOrEndOfMonth(request, df);
        int selectedUserID = getSelectedUserId(request);
        
        request.setAttribute("startDate", df.format(startDate)); //to set the selected date in refreshed form
        request.setAttribute("endDate", df.format(endDate)); //or beginning/end of month on newly called form
        request.setAttribute("selectedUserId", selectedUserID);
        
        
        String action = (String)request.getParameter("action");
        if (action==null) action = "";
        request.setAttribute("showAction", action); //for update forms
        
        boolean pending = false;
        
        DaoBills db = DaoFactory.getDaoBills();
        DaoUsers du = DaoFactory.getDaoUsers();
        
        switch(action){
            case "showPending": 
                pending = true;
            case "showAll": 
                request.setAttribute("billsList", db.getBills(selectedUserID, startDate, endDate, pending));
                request.setAttribute("usersList", du.getUsers());
                dispatchRequest(request, response, "bills", "pgcBills");
                break;
            case "createBill":
                if (db.createBill(selectedUserID, startDate, endDate)) request.setAttribute("pageText", "lblBillCreationSuccessful");
                else request.setAttribute("pageText", "lblBillCreationFailure");
                dispatchRequest(request, response, "actionResult", "pgcBills");
                break;
                
            default: displayMenu(request, response);
        }
    }
    
    private void processCalls(HttpServletRequest request, HttpServletResponse response){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate=getStartDateOrStartOfMonth(request, df);
        Date endDate=getEndDateOrEndOfMonth(request, df);
        int selectedUserID = getSelectedUserId(request);
        
        request.setAttribute("startDate", df.format(startDate));
        request.setAttribute("endDate", df.format(endDate));
        request.setAttribute("selectedUserId", selectedUserID);

        String action = (String)request.getParameter("action");
        if (action==null) action = "";
        
        DaoCalls daoCalls = DaoFactory.getDaoCalls();
        DaoUsers du = DaoFactory.getDaoUsers();
        
        switch(action){
            case "showAll":
                request.setAttribute("usersList", du.getUsers());
                request.setAttribute("callsList", daoCalls.getCalls(selectedUserID, startDate, endDate));
                dispatchRequest(request, response, "callsListing", "pgcCalls");
                break;
            default: displayMenu(request, response); break; //anything else will be implemented later
        }
    }

    private void processAbonents(HttpServletRequest request, HttpServletResponse response) {
        String action = (String)request.getParameter("action");
        if (action==null) action = "";
        
        int selectedUserID = getSelectedUserId(request);
        request.setAttribute("selectedUserId", selectedUserID);
        
        DaoUsers daoUsers = DaoFactory.getDaoUsers();
        User user = null;
        switch(action){
            case "showAll":
                request.setAttribute("usersList", daoUsers.getUsers());
                dispatchRequest(request, response, "usersListing", "pgcUsers");
                break; 
            case "addAbonentForm": 
                dispatchRequest(request, response, "addAbonentForm", "pgcAddAbonent");
                break;
            case "addAbonentCompletion":{
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
                    user = new User();
                    user.setIsAdmin(isAdmin);
                    user.setUserName(username);
                    user.setPassword(password);
                    user.setIsDisabled(isBlocked);
                    user.setPhoneNumber(phoneNumber);
                    
                    if (daoUsers.addUser(user)) request.setAttribute("pageText", "lblAbonentRegistrationSucessful");
                    else request.setAttribute("pageText", "lblAbonentRegistrationFailure");
                }

                request.setAttribute("data", "actionResult");
                dispatchRequest(request, response, "actionResult", "pgcAddAbonent");}
                break;
            case "editAbonent": 
                user = daoUsers.getUserById(selectedUserID);
                request.setAttribute("editedUser", user);
                dispatchRequest(request, response, "editAbonentForm", "pgcEditAbonent");
                break;
            case "editAbonentCompletion":{
                user = daoUsers.getUserById(selectedUserID);
                
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
                    request.setAttribute("pageText", "lblAbonentModificationFailure");
                }else{
                    user.setIsAdmin(isAdmin);
                    user.setUserName(username);
                    user.setPassword(password);
                    user.setIsDisabled(isBlocked);
                    user.setPhoneNumber(phoneNumber);
                    
                    if(daoUsers.updateUser(user, !"".equals(password)))request.setAttribute("pageText", "lblAbonentModificationSucessful");
                    else request.setAttribute("pageText", "lblAbonentModificationFailure");
                }

                request.setAttribute("data", "actionResult");
                dispatchRequest(request, response, "actionResult", "pgcEditAbonent");}
                
                break;
            case "deleteAbonent": 
                if (daoUsers.deleteUser(selectedUserID)) request.setAttribute("pageText", "lblUserDeletionSuccessful");
                else request.setAttribute("pageText", "lblUserDeletionFailure");
                dispatchRequest(request, response, "actionResult", "pgcUsers");
                break;
            default: displayMenu(request, response);
        }
    }

}