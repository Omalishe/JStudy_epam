package PhoneStation.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Just an interface with one and only method "Execute". All "command" 
 * instances must implement it and override the "execute" method
 * @author Oleksandr Malishevskyi
 */
public interface Command {
    void execute(HttpServletRequest httpRequest, HttpServletResponse httpResponse);
}
