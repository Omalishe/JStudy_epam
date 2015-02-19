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


/**
 * Data Access Object to perform different actions on "Service" DB Entities
 * @author Oleksandr Malishevskyi
 */
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

    private List<Service> getServicesByUserID(int userID) {
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
    
    public boolean addService(Service svc){
        String sqlStatement;
        sqlStatement= "Insert into services values(null,?,?)";
        try {
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, svc.getName());
            preparedStatement.setDouble(2, svc.getPrice());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            sqlLogger.error("Error performing sql query: ", e);
            return false;
        }
    }
    
    public boolean changeUserServices(Integer svcId, Integer userId, boolean enable){
        String sqlStatement;
        if (enable) sqlStatement= "Insert ignore into connected_services values(?,?)";
        else sqlStatement = "delete from connected_services where users_id = ? and services_id = ?";
        try {
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, svcId);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            sqlLogger.error("Error performing sql query: ", e);
            return false;
        }
    }
    
    public boolean deleteService(int serviceId) {
        return deleteServiceById(serviceId);
    }
    
    public boolean deleteService(Service svc){
        if (svc==null) return deleteServiceById(-1);
        else return deleteServiceById(svc.getId());
    }
    
    private boolean deleteServiceById(int serviceId){
        String sql = "delete from services where id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, serviceId);
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            sqlLogger.error("Error performing query: ", ex);
            return false;
        }
    }
    
    public Service getServiceById(int serviceId) {
        String sql = "select * from services where id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, serviceId);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                Service svc = new Service();
                svc.setId(resultSet.getInt("id"));
                svc.setName(resultSet.getString("name"));
                svc.setPrice(resultSet.getDouble("price"));
                return svc;
            }
        } catch (SQLException ex) {
            sqlLogger.error("Error performing query: ", ex);
        }
        return null;
    }
    
    public boolean updateService(Service svc) {
        String sql = "update services set name = ?, price = ? where id = ?";
        
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, svc.getName());
            statement.setDouble(2, svc.getPrice());
            statement.setInt(3, svc.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            sqlLogger.error("Error performing sql query: ", ex);
            return false;
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
