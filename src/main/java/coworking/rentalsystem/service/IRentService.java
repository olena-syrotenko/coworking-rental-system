package coworking.rentalsystem.service;

import coworking.rentalsystem.model.entity.RentApplication;
import coworking.rentalsystem.model.entity.Status;
import java.util.ArrayList;

public interface IRentService {
    int insertRentApplication(RentApplication rentApplication);
    ArrayList<RentApplication> getUserApplication(String username);
    ArrayList<RentApplication> getUserApplicationByStatus(String username, int id_status);
    ArrayList<Status> getAllStatus();
    boolean updateStatusApplication(int id_application, int id_status);
    boolean updateStatusApplication(int id_admin, int id_application, int id_status);
    ArrayList<RentApplication> getAllApplicationByStatus(int id_status);
    RentApplication getRentApplicationById(int id_application);
}
