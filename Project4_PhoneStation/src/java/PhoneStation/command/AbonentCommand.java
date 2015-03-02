package PhoneStation.command;

import PhoneStation.beans.User;
import PhoneStation.beans.Call;
import PhoneStation.beans.*;
import PhoneStation.model.*;
import PhoneStation.pages.Pages;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.logging.log4j.*;

/**
 * This class holds actions for any query from abonent page 
 * @author Oleksandr Malishevskyi
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
        userMenu.addEntry(new MenuEntry(Pages.ABONENT_URL+"?section=services&action=showMy","mneShowMyServices"));
        userMenu.addEntry(new MenuEntry(Pages.ABONENT_URL+"?section=services&action=showAll","mneShowAllServices"));
        userMenu.addEntry(new MenuEntry(Pages.ABONENT_URL+"?section=calls&action=showMy","mneShowMyCalls"));
        userMenu.addEntry(new MenuEntry(Pages.ABONENT_URL+"?section=bills&action=showAll","mneShowAllBills"));
        userMenu.addEntry(new MenuEntry(Pages.ABONENT_URL+"?section=bills&action=showPending","mneShowPendingBills"));
        userMenu.addEntry(new MenuEntry(Pages.ABONENT_URL+"?section=calls&action=registerForm","mnePlaceACall"));
        return userMenu;
    }
    
    private void displayMenu(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
        httpRequest.setAttribute("userMenu", getUserMenu());
        httpRequest.setAttribute("pageCaption", "pgcAbonentPage");
        try {
            httpRequest.getRequestDispatcher(Pages.ABONENT_PAGE).forward(httpRequest, httpResponse);
        } catch (ServletException ex) {
            httpLogger.error("http dispatch error:", ex);
        } catch (IOException ex) {
            coreLogger.error("http dispatch error:", ex);
        }
        
    }
    
    private Date getStartDateOrStartOfMonth(HttpServletRequest request, DateFormat df){
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
    
    public void dispatchRequest(HttpServletRequest request, HttpServletResponse response, String data, String pageCaption){
        request.setAttribute("data", data);
        request.setAttribute("pageCaption", pageCaption);
        request.setAttribute("userMenu", getUserMenu());
        try {
            request.getRequestDispatcher(Pages.ABONENT_PAGE).forward(request, response);
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
        DaoServices ds = DaoFactory.getDaoServices();
        
        switch(action){
            case "showMy": 
                HttpSession ses = request.getSession();
                user = (User) ses.getAttribute("currentUser");
            case "showAll": 
                    
                request.setAttribute("servicesList", ds.getServices(user));
                
                String data;
                if ("showMy".equals(action))data="myServices";
                else data="services";
                
                dispatchRequest(request, response, data, "pgcServices");
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
            
            dispatchRequest(request, response, "actionResult", "pgcServices");
        }else{
            displayMenu(request, response);
        }
    }

    private void processBills(HttpServletRequest request, HttpServletResponse response){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = getStartDateOrStartOfMonth(request, df);
        Date endDate = getEndDateOrEndOfMonth(request, df);
        request.setAttribute("startDate", df.format(startDate));
        request.setAttribute("endDate", df.format(endDate));

        String action = (String)request.getParameter("action");
        if (action==null) action = "";
        request.setAttribute("showAction", action); //for update forms
        
        HttpSession ses = request.getSession();
        User user = (User) ses.getAttribute("currentUser");
        boolean pending = false;
        
        DaoBills db = DaoFactory.getDaoBills();
        
        switch(action){
            case "showPending": 
                pending = true;
            case "showAll": 
                request.setAttribute("billsList", db.getBills(user, startDate, endDate, pending));
                dispatchRequest(request, response, "bills", "pgcBills");
                break;
            case "payBill": 
                db.payBill(Integer.parseInt((String) request.getParameter("billId"))); 
                request.setAttribute("pageText", "lblIdPaymentSuccessful");
                dispatchRequest(request, response, "actionResult", "pgcBills");
                break;
            default: displayMenu(request, response);
        }
    }
    
    private void processCalls(HttpServletRequest request, HttpServletResponse response){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = getStartDateOrStartOfMonth(request, df);
        Date endDate = getEndDateOrEndOfMonth(request, df);
        request.setAttribute("endDate", df.format(endDate));
        request.setAttribute("startDate", df.format(startDate));
        
        String action = (String)request.getParameter("action");
        if (action==null) action = "";
        
        HttpSession ses = request.getSession();
        User user = (User) ses.getAttribute("currentUser");

        DaoCalls daoCalls = DaoFactory.getDaoCalls();
        
        switch(action){
            case "showMy":
                request.setAttribute("callsList", daoCalls.getCalls(user, startDate, endDate));
                dispatchRequest(request, response, "callsListing", "pgcCalls");
                break;
            case "registerForm":
                request.setAttribute("registerDate", df.format(Calendar.getInstance().getTime()));
                dispatchRequest(request, response, "callRegisterForm", "pgcRegisterCall");
                break; 
            case "registerComplete":
                Date registerDate;
                
                try {
                    registerDate = df.parse((String)request.getParameter("registerDate"));
                } catch (ParseException|NullPointerException ex) {
                    registerDate = Calendar.getInstance().getTime();
                }
                
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
                
                request.setAttribute("registerDate", registerDate);
                request.setAttribute("pageText", "lblCallRegistrationSuccessful");
                dispatchRequest(request, response, "actionResult", "pgcRegisterCall");
                break;
            default: displayMenu(request, response); break;
        }
    }
}
