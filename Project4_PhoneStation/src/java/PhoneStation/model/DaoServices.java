/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PhoneStation.model;

import PhoneStation.beans.User;
import PhoneStation.beans.Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DaoServices {
    Logger sqlLogger = LogManager.getLogger("app.sql");
    
    private Connection connection;
    
    
    void setConnection(Connection connection) {
        this.connection=connection;
    }
    
    
    public List<Service> getServices(User user){
        if (user==null) return getServicesByUserID(-1);
        else return getServicesByUserID(user.getId());
    }
    
    public List<Service> getServices(int UserID){
        return getServicesByUserID(UserID);
    }

    public List<Service> getServicesByUserID(int userID) {
        List<Service> services = new ArrayList<>();
        try {
            PreparedStatement preparedStatement;
            if (userID!=-1){
                String sql = "select * from user_services where user_id = ?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, userID);
            }else{
                String sql = "select id as service_id, name as service_name, price as service_price from services";
                preparedStatement = connection.prepareStatement(sql);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Service sv = new Service();
                sv.setId(resultSet.getInt("service_id"));
                sv.setName(resultSet.getString("service_name"));
                sv.setPrice(resultSet.getDouble("service_price"));
                services.add(sv);
            }
            return services;
        } catch (SQLException e) {
            sqlLogger.error("Error performing a queue: ", e);
        }
        return null;
    }
    
    public List<Service> getServices(){
        return getServicesByUserID(-1);
    }
    
    public void addService(Service svc){
        
    }
    
    public void updateService(Service svc){
        
    }
    
    public void removeService(Service svc){
        
    }
    
    public void changeUserServices(Integer svcId, Integer userId, boolean enable){
        String sqlStatement;
        if (enable) sqlStatement= "Insert ignore into connected_services values(?,?)";
        else sqlStatement = "delete from connected_services where users_id = ? and services_id = ?";
        try {
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, svcId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            sqlLogger.error("Error performing sql query: ", e);
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        try {
            connection.close();
        } finally {
            super.finalize();
        }
    }
    
    
    
}
