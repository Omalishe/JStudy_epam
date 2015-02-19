
package PhoneStation.controller;

import PhoneStation.command.Command;
import PhoneStation.command.CommandFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Main "dispatcher" class of the site. 
 * it gets appropriate command from the factory (command must implement local "Command" interface)
 * and calls it "execute" method. 
 * @author Oleksandr Malishevskyi
 */
public class Controller extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Command command = CommandFactory.newCommand(request);
        command.execute(request,response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
