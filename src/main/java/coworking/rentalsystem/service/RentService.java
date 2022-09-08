package coworking.rentalsystem.service;

import coworking.rentalsystem.dao.DAOFactory;
import coworking.rentalsystem.dao.IRentDAO;
import coworking.rentalsystem.dao.typeDAO;
import coworking.rentalsystem.model.StatusType;
import coworking.rentalsystem.model.entity.RentApplication;
import coworking.rentalsystem.model.entity.Status;
import java.sql.SQLException;
import java.util.ArrayList;

public class RentService implements IRentService{
    private final static DAOFactory daoFactory;
    private static IRentDAO rentDAO;

    static{
        daoFactory = DAOFactory.getDAOFactory(typeDAO.MySQL);
    }

    @Override
    public int insertRentApplication(RentApplication rentApplication) {
        daoFactory.beginTransaction();
        int idNewApplication = 0;
        try {
            rentDAO = daoFactory.getRentDAO();
            rentApplication.setStatus(new Status(StatusType.NEW.getId(), "Новий"));
            idNewApplication = rentDAO.insertRentApplication(rentApplication);
        } catch (SQLException e) {
            e.printStackTrace();
            daoFactory.rollbackTransaction();
        }
        daoFactory.commitTransaction();
        return idNewApplication;
    }

    @Override
    public ArrayList<RentApplication> getUserApplication(String email) {
        ArrayList<RentApplication> rentApplications = new ArrayList<>();
        daoFactory.open();
        try {
            rentDAO = daoFactory.getRentDAO();
            rentApplications = rentDAO.getApplicationByUserEmail(email);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        daoFactory.close();
        return rentApplications;
    }

    @Override
    public ArrayList<RentApplication> getUserApplicationByStatus(String username, int id_status) {
        ArrayList<RentApplication> rentApplications = new ArrayList<>();
        daoFactory.open();
        try {
            rentDAO = daoFactory.getRentDAO();
            rentApplications = rentDAO.getUserApplicationByStatus(username, id_status);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        daoFactory.close();
        return rentApplications;
    }

    @Override
    public ArrayList<Status> getAllStatus() {
        ArrayList<Status> statuses = new ArrayList<>();
        daoFactory.open();
        try {
            rentDAO = daoFactory.getRentDAO();
            statuses = rentDAO.getStatus();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        daoFactory.close();
        return statuses;
    }

    @Override
    public boolean updateStatusApplication(int id_application, int id_status) {
        daoFactory.open();
        boolean isUpdate = false;
        try {
            rentDAO = daoFactory.getRentDAO();
            isUpdate = rentDAO.updateApplicationStatus(id_application, id_status);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        daoFactory.close();
        return isUpdate;
    }

    @Override
    public boolean updateStatusApplication(int id_admin, int id_application, int id_status) {
        daoFactory.open();
        boolean isUpdate = false;
        try {
            rentDAO = daoFactory.getRentDAO();
            rentDAO.updateAdminInApplication(id_admin, id_application);
            isUpdate = rentDAO.updateApplicationStatus(id_application, id_status);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        daoFactory.close();
        return isUpdate;
    }

    @Override
    public ArrayList<RentApplication> getAllApplicationByStatus(int id_status) {
        ArrayList<RentApplication> rentApplications = new ArrayList<>();
        daoFactory.open();
        try {
            rentDAO = daoFactory.getRentDAO();
            rentApplications = rentDAO.getAllApplicationByStatus(id_status);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        daoFactory.close();
        return rentApplications;
    }

    @Override
    public RentApplication getRentApplicationById(int id_application) {
        RentApplication rentApplication = null;
        daoFactory.open();
        try {
            rentDAO = daoFactory.getRentDAO();
            rentApplication = rentDAO.getRentApplicationById(id_application);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rentApplication;
    }

}
