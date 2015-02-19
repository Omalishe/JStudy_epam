package PhoneStation.command;

import PhoneStation.beans.User;
import PhoneStation.model.*;
import PhoneStation.pages.pages;
import java.io.IOException;
import javax.servlet.http.*;

/**
 * This command is called from  auth filter to identify user and let him to log im or log out 
 * @author Oleksandr Malishevskyi
 */
class AuthCommand implements Command {

    public AuthCommand() {
    }

    @Override
    public void execute(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        String action = httpRequest.getParameter("action");
        if (action!=null&&action.equals("logout")){
            httpRequest.getSession().invalidate();
            try {
                httpResponse.sendRedirect(pages.INDEX_PAGE);
            } catch (IOException ex) {
            }
        }else{
            HttpSession ses = httpRequest.getSession();
            User currentUser = (User) ses.getAttribute("currentUser");
            if (currentUser==null){ //trying to find user
                String name = httpRequest.getParameter("user_name");
                String password = httpRequest.getParameter("user_password");
                if (name!=null&&password!=null){
                    DaoAuth daoAuth = DaoFactory.getDaoAuth();
                    currentUser = daoAuth.authenticateUser(name,password);
                }
            }
            
            if (currentUser!=null) { 
                ses.setAttribute("currentUser", currentUser);
                try {
                    if (currentUser.isIsAdmin()) {
                        httpResponse.sendRedirect(pages.ADMIN_URL);
                    } else {
                        httpResponse.sendRedirect(pages.ABONENT_URL);
                    }
                } catch (IOException iOException) {
                }
            } else {//still is not identified
                try {
                httpResponse.sendRedirect(pages.INDEX_PAGE);
                } catch (IOException ex) {
                }
            }
        }
    }
    
}
