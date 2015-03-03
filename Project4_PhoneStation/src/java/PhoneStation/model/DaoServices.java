package PhoneStation.model;

import PhoneStation.beans.User;
import PhoneStation.beans.Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        String sql;
        if (userID!=-1){
            sql = "select * from user_services where user_id = ?";
            
        }else{
            sql = "select id as service_id, price as service_price,  "
                    + "services_name_translations.lang as lang, "
                    + "services_name_translations.name as service_name "
                    + "from services "
                    + "left join services_name_translations as services_name_translations "
                    + "on services_name_translations.services_id = services.id ";
            
        }
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            
            if (userID!=-1) preparedStatement.setInt(1, userID);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            int curId=0;
            Service sv = new Service();
            while (resultSet.next()){
                if (curId!=resultSet.getInt("service_id")){
                    curId =resultSet.getInt("service_id");
                    sv = new Service();
                    sv.setId(resultSet.getInt("service_id"));
                    sv.setPrice(resultSet.getDouble("service_price"));
                    services.add(sv);
                }
                sv.addLang(resultSet.getString("lang"), resultSet.getString("service_name"));
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
        if (svc==null) return false;
        
        try (Statement st = connection.createStatement()){
            connection.setAutoCommit(false);
            
            st.executeUpdate("Insert into services values(null,'"+svc.getPrice()+"')",Statement.RETURN_GENERATED_KEYS);
            
            int genId=-1;
            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) genId = rs.getInt(1);
            else throw new SQLException("no autoincrement");
            
            Map<String,String> givenNames = svc.getNameLangMap();
            for (Map.Entry en:givenNames.entrySet()){
                st.addBatch(" insert into services_name_translations values ("+genId+",'"+en.getKey()+"','"+en.getValue()+"') ");
            }
            st.executeBatch();
            connection.commit();
            
            return true;
        } catch (SQLException e) {
            sqlLogger.error("Error performing sql query: ",e);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                sqlLogger.error("Error rolling transaction back: ", ex);
            }
            return false;
        }finally{
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                sqlLogger.error("Error setting autocommit to true: ", ex);
            }
        }
    }
    
    public boolean changeUserServices(Integer svcId, Integer userId, boolean enable){
        String sqlStatement;
        if (enable) sqlStatement= "Insert ignore into connected_services values(?,?)";
        else sqlStatement = "delete from connected_services where users_id = ? and services_id = ?";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)){
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
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, serviceId);
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            sqlLogger.error("Error performing query: ", ex);
            return false;
        }
    }
    
    public Service getServiceById(int serviceId) {
        String sql = "select * from services "
                + "left join services_name_translations "
                + "on services_name_translations.services_id = services.id "
                + "where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, serviceId);
            ResultSet resultSet = statement.executeQuery();
            Service svc = new Service();
            while(resultSet.next()){
                svc.setId(resultSet.getInt("id"));
                svc.setPrice(resultSet.getDouble("price"));
                svc.addLang(resultSet.getString("lang"), resultSet.getString("name"));
            }
            return svc;
        } catch (SQLException ex) {
            sqlLogger.error("Error performing query: ", ex);
        }
        return null;
    }
    
    public boolean updateService(Service svc) {
        if (svc==null) return false;
        try (Statement st = connection.createStatement()){
            connection.setAutoCommit(false);
            st.addBatch("update services set price = '"+svc.getPrice()+"' where id = "+svc.getId()+" ");
            Map<String,String> givenNames = svc.getNameLangMap();
            for (Map.Entry en:givenNames.entrySet()){
                st.addBatch("replace into services_name_translations values("+svc.getId()+",'"+en.getKey()+"','"+en.getValue()+"') ");
            }
            st.executeBatch();
            connection.commit();
            return true;
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                sqlLogger.error("Error rolling transaction back: ", ex);
            }
            sqlLogger.error("Error performing sql query: ", ex);
            return false;
        }finally{
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                sqlLogger.error("Error stting autocommit to true: ", ex);
            }
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
