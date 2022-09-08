package coworking.rentalsystem.dao;

import coworking.rentalsystem.model.entity.PersonalData;
import coworking.rentalsystem.model.entity.Role;
import coworking.rentalsystem.model.entity.User;
import java.sql.SQLException;

public interface IUserDAO {
    User getUserByEmail(String email) throws SQLException;
    Role getRoleByName(String name) throws SQLException;
    User getPersonalDataByEmail(String email) throws SQLException;
    boolean insertUser(User user) throws SQLException;
    boolean insertUserPersonalData(PersonalData personalData) throws SQLException;
}
