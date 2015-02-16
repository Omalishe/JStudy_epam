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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author oleksandr
 */
public class DaoUsers {
    Logger sqlLogger = LogManager.getLogger("app.sql");
    
    private Connection connection;
    
    
    void setConnection(Connection connection) {
        this.connection=connection;
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        String sql="select * from users";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                User user = new User();
                user.setUserName(resultSet.getString("username"));
                user.setIsAdmin(resultSet.getBoolean("is_admin"));
                user.setIsDisabled(resultSet.getBoolean("is_blocked"));
                user.setPhoneNumber(resultSet.getString("phone_number"));
                user.setId(resultSet.getInt("id"));
                users.add(user);
            }
            return users;
        } catch (SQLException ex) {
            sqlLogger.error("Error performing a query: ", ex);
            return null;
        }
    }
    
    public User getUserById(int id){
        User user = new User();
        String sql="select * from users where id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                user.setUserName(resultSet.getString("username"));
                user.setIsAdmin(resultSet.getBoolean("is_admin"));
                user.setIsDisabled(resultSet.getBoolean("is_blocked"));
                user.setPhoneNumber(resultSet.getString("phone_number"));
                user.setId(resultSet.getInt("id"));
            }
            return user;
        } catch (SQLException ex) {
            sqlLogger.error("Error performing a query: ", ex);
            return null;
        }
    }
    
    public boolean addUser(User user){
        java.sql.Date addDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        String sql="insert into users values(null,?,?,?,?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getPassword());
            statement.setDate(3, addDate);
            statement.setString(4, user.getPhoneNumber());
            statement.setBoolean(5, user.isIsAdmin());
            statement.setBoolean(6, user.isIsDisabled());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            sqlLogger.error("Error performing query: ",ex);
            return false;
        }
        
    }
    
    private boolean enableUserByID(int userID, boolean enable){
        String sql = "update users set is_blocked = ? where id = ?";
        boolean blocked = !enable;
        
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setBoolean(1, blocked);
            statement.setInt(2, userID);
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            sqlLogger.error("Error peroforming query: ", ex);
            return false;
        }
    }
    
    public boolean enableUser (int userID, boolean enable){
        return enableUserByID(userID, enable);
    }
    
    public boolean enableUser (User user, boolean enable){
        if (user==null) return enableUserByID(-1,enable);
        else return enableUserByID(user.getId(),enable);
    }
    
    public boolean deleteUser(int selectedUserID) {
        return deleteUserById(selectedUserID);
    }
    
    public boolean deleteUser(User user) {
        if (user==null) return deleteUserById(-1);
        else return deleteUserById(user.getId());
    }
    
    private boolean deleteUserById(int selectedUserID) {
        String sql = "delete from users where id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, selectedUserID);
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            sqlLogger.error("Error performing query: ", ex);
            return false;
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        connection.close();
    }

    public boolean updateUser(User user, boolean passwordChanged) {
        if (user == null) return false;
        String sql = "update users set username = ?"
                + "phone_number = ?"
                + "is_admin = ?"
                + "is_blocked = ?"
                + (passwordChanged?"password = ?":"")
                + "where id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getPhoneNumber());
            statement.setBoolean(3, user.isIsAdmin());
            statement.setBoolean(4, user.isIsDisabled());
            if (passwordChanged) {
                statement.setString(5, user.getPassword());
                statement.setInt(6, user.getId());
            }else{
                statement.setInt(5, user.getId());
            }
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            sqlLogger.error("Error performing query:",ex);
            return false;
        }
        
    }


}
