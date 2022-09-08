package coworking.rentalsystem.service;

import coworking.rentalsystem.dao.DAOFactory;
import coworking.rentalsystem.dao.IUserDAO;
import coworking.rentalsystem.dao.typeDAO;
import coworking.rentalsystem.model.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements IUserService, UserDetailsService {
    private final static DAOFactory daoFactory;
    private static IUserDAO userDAO;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    static{
        daoFactory = DAOFactory.getDAOFactory(typeDAO.MySQL);
    }

    @Override
    public boolean validateUserData(User user) {
        return !(user.getUsername().isEmpty()
                || user.getPassword().isEmpty()
                || user.getRole() == null
                || user.getPersonalData() == null
                || user.getPersonalData().getFirstName().isEmpty()
                || user.getPersonalData().getLastName().isEmpty()
                || user.getPersonalData().getPassportId().length() != 9
                || user.getPersonalData().getITN().length() != 10
                || user.getPersonalData().getAuthority().isEmpty());
    }

    @Override
    public User getUserByEmail(String email) {
        daoFactory.open();
        try {
            userDAO = daoFactory.getUserDAO();
            User user = userDAO.getUserByEmail(email);
            daoFactory.close();
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        daoFactory.close();
        return null;
    }

    @Override
    public User getPersonalDataByEmail(String email) {
        daoFactory.open();
        User user = null;
        try {
            userDAO = daoFactory.getUserDAO();
            user = userDAO.getPersonalDataByEmail(email);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        daoFactory.close();
        return user;
    }


    @Override
    public void addUser(User user) {
        daoFactory.beginTransaction();
        try {
            userDAO = daoFactory.getUserDAO();
            user.setRole(userDAO.getRoleByName("ROLE_USER"));
            encodePassword(user);
            if(!validateUserData(user))  daoFactory.rollbackTransaction();
            else{
                userDAO.insertUser(user);
                user.getPersonalData().setId(userDAO.getUserByEmail(user.getUsername()).getId());
                userDAO.insertUserPersonalData(user.getPersonalData());
                daoFactory.commitTransaction();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            daoFactory.rollbackTransaction();
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByEmail(username);
        List<SimpleGrantedAuthority> grantedAuthority = new ArrayList<>();
        grantedAuthority.add(new SimpleGrantedAuthority( user.getRole().getName()));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthority);
    }

    private void encodePassword(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }
}
