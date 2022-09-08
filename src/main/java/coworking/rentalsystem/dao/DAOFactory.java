package coworking.rentalsystem.dao;

import java.sql.SQLException;

public abstract class DAOFactory {
    private static DAOFactory dao = null;
    public abstract IWorkplaceDAO getWorkplaceDAO() throws SQLException;
    public abstract IEquipmentDAO getEquipmentDAO() throws SQLException;
    public abstract IUserDAO getUserDAO() throws SQLException;
    public abstract IRentDAO getRentDAO() throws SQLException;
    public abstract void open();
    public abstract void close();
    public abstract void beginTransaction();
    public abstract void commitTransaction();
    public abstract void rollbackTransaction();

    public static DAOFactory getDAOFactory(typeDAO dbType) {
        switch (dbType) {
            case MySQL -> dao = new MySQLDAOFactory();
        }
        return dao;
    }
}
