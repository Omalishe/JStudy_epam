/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PhoneStation.model;

import PhoneStation.beans.User;
import PhoneStation.beans.Bill;
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
public class DaoBills {
    Logger sqlLogger = LogManager.getLogger("app.sql");
    
    private Connection connection;
    
    
    void setConnection(Connection connection) {
        this.connection=connection;
    }
    
    public List<Bill> getBills(User user, Date startDate, Date endDate, boolean pending) {
        if (user==null) return getBillsByUserID(-1, startDate, endDate, pending);
        else return getBillsByUserID(user.getId(), startDate, endDate, pending);
    }
    
    public List<Bill> getBills(int userID, Date startDate, Date endDate, boolean pending) {
        return getBillsByUserID(userID, startDate, endDate, pending);
    }

    private List<Bill> getBillsByUserID(int userID, Date startDate, Date endDate, boolean pending) {
        List<Bill> bills = new ArrayList<>();
        String sql = "select * from pay_bills where date_issued between ? and ?"+(pending?" and is_payed=false ":"")+(userID==-1?"":" users_id = ? ");
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDate(1, new java.sql.Date(startDate.getTime()));
            preparedStatement.setDate(2, new java.sql.Date(endDate.getTime()));
            if (userID!=-1) preparedStatement.setInt(3, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Bill bill = new Bill();
                bill.setId(resultSet.getInt("id"));
                bill.setUsersId(resultSet.getInt("users_id"));
                bill.setDateIssued(resultSet.getDate("date_issued"));
                bill.setDateIssued(resultSet.getDate("pay_month"));
                bill.setAmount(resultSet.getDouble("amount"));
                bill.setIsPayed(resultSet.getBoolean("is_payed"));
                bills.add(bill);
            }
            return bills;
        } catch (SQLException e) {
            sqlLogger.error("Error queuing sql: ",e);
        }
        return null;
    }

    public boolean payBill(int billId) {
        String sql = "update pay_bills set is_payed=true where id = ?";
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, billId);
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            sqlLogger.error("Error on sql: ",ex);
            return false;
        }
    }

    public boolean createBill(int selectedUserID, Date startDate, Date endDate) {
        return false; //TODO still got to implement this
    }
    
    @Override
    protected void finalize() throws Throwable {
        connection.close();
    }

    
    
}
