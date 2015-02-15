
package PhoneStation.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author oleksandr
 */
public interface Command {
    void execute(HttpServletRequest httpRequest, HttpServletResponse httpResponse);
}
