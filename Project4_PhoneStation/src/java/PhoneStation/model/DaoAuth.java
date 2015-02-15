/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PhoneStation.model;

import PhoneStation.beans.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author oleksandr
 */
public class DaoAuth {
    Connection connection;
    Logger sqlLogger = LogManager.getLogger("app.sql");
    
    public User authenticateUser(String name, String password) {
        String sql = "Select * from users where username = ? and password = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next() ){
                User user = new User();
                user.setUserName(name);
                user.setIsAdmin(resultSet.getBoolean("is_admin"));
                user.setIsDisabled(resultSet.getBoolean("is_blocked"));
                user.setId(resultSet.getInt("id"));
                return user;
            }
        } catch (SQLException e) {
            sqlLogger.error("Error authentificating user", e);
        }
        return null;
    }

    void setConnection(Connection connection) {
        this.connection=connection;
    }

    @Override
    protected void finalize() throws Throwable {
        connection.close();
    }
    
    
    
}
