package PhoneStation.model;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main DB entities dispatcher. Creates and holds single instance for each DB entity
 * and returns it to any command, when command asks for any
 * @author Oleksandr Malishevskyi
 */
public class DaoFactory {

    private static DataSource dataSource;
    private final static DaoAuth daoAuth;
    private final static DaoServices daoServices;
    private final static DaoUsers daoUsers;
    private final static DaoBills daoBills;
    private final static DaoCalls daoCalls;
    
    private final static Logger sqlLogger = LogManager.getLogger("app.sql");
    
    static {
        try {
            InitialContext initialContext = new InitialContext();
            dataSource = (DataSource)  initialContext.lookup("jdbc/PhoneStation");
            sqlLogger.info("DB Connection established");
        } catch (NamingException ex) {
            sqlLogger.error("Error getting data source", ex);
        }
        
        daoAuth = new DaoAuth();
        daoServices = new DaoServices();
        daoUsers = new DaoUsers();
        daoBills = new DaoBills();
        daoCalls = new DaoCalls();
        try {
            Connection conn = dataSource.getConnection();
            daoAuth.setConnection(conn);
            daoServices.setConnection(conn);
            daoUsers.setConnection(conn);
            daoBills.setConnection(conn);
            daoCalls.setConnection(conn);
        } catch (SQLException ex) {
            sqlLogger.error("Error creating DB entities objects", ex);
        }
    }

    public static DaoAuth getDaoAuth(){
        return daoAuth;
    }
    
    public static DaoServices getDaoServices(){
        return daoServices;
    }

    public static DaoUsers getDaoUsers() {
        return daoUsers;
    }

    public static DaoBills getDaoBills() {
        return daoBills;
    }

    public static DaoCalls getDaoCalls() {
        return daoCalls;
    }
}
