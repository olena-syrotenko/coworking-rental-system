package coworking.rentalsystem.service;

import coworking.rentalsystem.model.entity.User;

public interface IUserService {
    boolean validateUserData(User user);
    User getUserByEmail(String email);
    User getPersonalDataByEmail(String email);
    void addUser(User user);

}
