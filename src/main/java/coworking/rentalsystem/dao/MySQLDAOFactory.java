package coworking.rentalsystem.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySQLDAOFactory extends DAOFactory{
    private static Properties connectionInfo;
    private Connection connection;

    MySQLDAOFactory() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectionInfo = new Properties();
        connectionInfo.put("user", "root");
        connectionInfo.put("password", "helen");
        connectionInfo.put("useUnicode", "true");
        connectionInfo.put("characterEncoding", "utf-8");
        connectionInfo.put("serverTimezone", "UTC");
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost/coworking", connectionInfo);
    }

    @Override
    public IWorkplaceDAO getWorkplaceDAO() throws SQLException {
        return new WorkplaceDAOImpl(getConnection());
    }

    @Override
    public IEquipmentDAO getEquipmentDAO() throws SQLException {
        return new EquipmentDAOImpl(getConnection());
    }

    @Override
    public IUserDAO getUserDAO() throws SQLException {
        return new UserDAOImpl(getConnection());
    }

    @Override
    public IRentDAO getRentDAO() throws SQLException {
        return new RentDAOImpl(getConnection());
    }

    @Override
    public void open() {
        try {
            connection = getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beginTransaction() {
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void commitTransaction() {
        try {
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rollbackTransaction() {
        try {
            connection.rollback();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
