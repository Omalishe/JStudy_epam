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
 * Data Access Object to perform different actions on "Phone call" DB Entities
 * @author Oleksandr Malishevskyi
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
    
    private List<Call> getCallsByUserId(int userID, Date startDate, Date endDate) {
        List<Call> calls = new ArrayList<>();
        String sql ="select \n" +
                    "placed_calls.id as id,\n" +
                    "placed_calls.time_start as time_start,\n" +
                    "placed_calls.duration as duration, \n" +
                    "placed_calls.cost as cost,\n" +
                    "placed_calls.users_id as users_id,\n" +
                    "users.username as username\n" +
                    "from placed_calls \n" +
                    "left join users on users.id = placed_calls.users_id\n"
                + "where time_start between ? and ?"+(userID==-1?"":" and users_id = ? ");
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
                call.setUserName(resultSet.getString("username"));
                calls.add(call);
            }
            return calls;
        } catch (SQLException e) {
            sqlLogger.error("Error queuing sql: ",e);
        }
        return null;
    }

    public boolean registerCall(Call call) {
        String sql = "insert into placed_calls values (null,?,?,?,?)";
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(sql);
            statement.setDate(1, new java.sql.Date(call.getTimeStart().getTime()));
            statement.setInt(2, call.getDuration());
            statement.setDouble(3, call.getCost());
            statement.setInt(4, call.getUsersId());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            sqlLogger.error("Error on sql: ",ex);
            return false;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        connection.close();
    }
    
    
}
