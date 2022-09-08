package coworking.rentalsystem.dao;

import coworking.rentalsystem.model.entity.RentApplication;
import coworking.rentalsystem.model.entity.Status;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IRentDAO {
    ArrayList<RentApplication> getApplicationByUserEmail(String email) throws SQLException;
    ArrayList<RentApplication> getUserApplicationByStatus(String email, int id_status) throws SQLException;
    ArrayList<RentApplication> getAllApplicationByStatus(int id_status) throws SQLException;
    RentApplication getRentApplicationById(int id_application) throws SQLException;
    ArrayList<Status> getStatus() throws SQLException;
    int insertRentApplication(RentApplication rentApplication) throws SQLException;
    boolean updateApplicationStatus(int id_application, int id_status) throws SQLException;
    boolean updateAdminInApplication(int id_admin, int id_application) throws SQLException;
}
