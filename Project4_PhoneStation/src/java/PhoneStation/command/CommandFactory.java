/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PhoneStation.command;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class CommandFactory {
    private static Map<String, Command> commands = new HashMap<>();
    static{
        //commands.put("/auth", new AuthCommand() );
        //commands.put("default", new DefaultCommand() );
        commands.put("/admin", new AdminCommand());
        commands.put("/auth", new AuthCommand());
        commands.put("/abonent", new AbonentCommand());

    }

    public static Command newCommand(HttpServletRequest request){
        String userPath = request.getServletPath();
        if (commands.containsKey(userPath)){
            return commands.get(userPath);
        } else {
            return null;//commands.get("default");
        }
    }
}
