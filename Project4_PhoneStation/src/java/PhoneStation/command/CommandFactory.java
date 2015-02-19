package PhoneStation.command;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * Main command factory of the site. 
 * @author Oleksandr Malishevskyi
 */
public class CommandFactory {
    private static Map<String, Command> commands = new HashMap<>();
    static{
        //array holding mapping of commands according to user queries
        commands.put("/admin", new AdminCommand());
        commands.put("/auth", new AuthCommand());
        commands.put("/abonent", new AbonentCommand());

    }

    public static Command newCommand(HttpServletRequest request){
        String userPath = request.getServletPath();
        if (commands.containsKey(userPath)){
            return commands.get(userPath);
        } else {
            return commands.get("/auth"); //turn user to login page if query is rubbish
        }
    }
}
