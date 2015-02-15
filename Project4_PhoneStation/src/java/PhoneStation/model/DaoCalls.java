/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PhoneStation.model;

import PhoneStation.beans.User;
import PhoneStation.beans.Call;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author oleksandr
 */
public class DaoCalls {
    Logger sqlLogger = LogManager.getLogger("app.sql");
    
    private Connection connection;
    
    
    void setConnection(Connection connection) {
        this.connection=connection;
    }

    public List<Call> getCalls(User user, Date startDate, Date endDate){
        if (user==null) return getCallsByUserId(-1, startDate, endDate);
        else return getCallsByUserId(user.getId(), startDate, endDate);
    }
    
    public List<Call> getCalls(int userID, Date startDate, Date endDate){
        return getCallsByUserId(userID, startDate, endDate);
    }
    
    public List<Call> getCallsByUserId(int userID, Date startDate, Date endDate) {
        List<Call> calls = new ArrayList<>();
        String sql = "select * from placed_calls where time_start between ? and ?"+(userID==-1?"":" and users_id = ? ");
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDate(1, new java.sql.Date(startDate.getTime()));
            preparedStatement.setDate(2, new java.sql.Date(endDate.getTime()));
            if (userID!=-1) preparedStatement.setInt(3, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Call call = new Call();
                call.setId(resultSet.getInt("id"));
                call.setTimeStart(resultSet.getDate("time_start"));
                call.setDuration(resultSet.getInt("duration"));
                call.setCost(resultSet.getDouble("cost"));
                call.setUsersId(resultSet.getInt("users_id"));
                calls.add(call);
            }
            return calls;
        } catch (SQLException e) {
            sqlLogger.error("Error queuing sql: ",e);
        }
        return null;
    }

    public void registerCall(Call call) {
        String sql = "insert into placed_calls values (null,?,?,?,?)";
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(sql);
            statement.setDate(1, new java.sql.Date(call.getTimeStart().getTime()));
            statement.setInt(2, call.getDuration());
            statement.setDouble(3, call.getCost());
            statement.setInt(4, call.getUsersId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            sqlLogger.error("Error on sql: ",ex);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        connection.close();
    }
    
    
}