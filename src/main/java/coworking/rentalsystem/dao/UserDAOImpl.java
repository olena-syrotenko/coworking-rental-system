package coworking.rentalsystem.dao;

import coworking.rentalsystem.model.entity.PersonalData;
import coworking.rentalsystem.model.entity.Role;
import coworking.rentalsystem.model.entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements IUserDAO{
    private Connection connection;
    public UserDAOImpl(Connection con){
        this.connection = con;
    }

    private static final String GET_USER_BY_EMAIL = "SELECT user.id, email, password, id_role, name FROM user JOIN role ON user.id_role = role.id WHERE email = ?";
    private static final String GET_ROLE_BY_NAME = "SELECT id, name FROM role WHERE name = ?";
    private static final String GET_PERSONAL_DATA_BY_EMAIL = "SELECT user.id, email, first_name, last_name, middle_name, passport_id, ITN, authority, phone_number FROM personal_data JOIN user ON user.id = personal_data.id WHERE user.email = ?";
    private static final String INSERT_USER = "INSERT INTO user (`email`, `password`, `id_role`) VALUES (?,?,?)";
    private static final String INSERT_PERSONAL_DATA = "INSERT INTO personal_data (`first_name`, `last_name`, `middle_name`, `passport_id`, `ITN`, `authority`, `id`,`phone_number`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    @Override
    public User getUserByEmail(String email) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(GET_USER_BY_EMAIL);
        ps.setString(1,email);
        ResultSet rs = ps.executeQuery();
        User user;
        if (rs.next()) user = new User(rs.getInt(1), rs.getString(2),
                rs.getString(3), new Role(rs.getInt(4), rs.getString(5)), null);
        else throw new SQLException();
        rs.close();
        ps.close();
        return user;
    }

    @Override
    public Role getRoleByName(String name) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(GET_ROLE_BY_NAME);
        ps.setString(1,name);
        ResultSet rs = ps.executeQuery();
        Role role;
        if(rs.next()) role = new Role(rs.getInt(1), rs.getString(2));
        else throw new SQLException();
        rs.close();
        ps.close();
        return role;
    }

    @Override
    public User getPersonalDataByEmail(String email) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(GET_PERSONAL_DATA_BY_EMAIL);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        User user = new User();
        if(rs.next()){
            user.setUsername(rs.getString(2));
            user.setPersonalData(new PersonalData(rs.getInt(1), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7),
                    rs.getString(8), rs.getString(9)));
        }
        return user;
    }

    @Override
    public boolean insertUser(User user) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(INSERT_USER);
        ps.setString(1,user.getUsername());
        ps.setString(2,user.getPassword());
        ps.setInt(3, user.getRole().getId());
        return ps.executeUpdate() > 0;
    }

    @Override
    public boolean insertUserPersonalData(PersonalData personalData) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(INSERT_PERSONAL_DATA);
        ps.setString(1,personalData.getFirstName());
        ps.setString(2, personalData.getLastName());
        ps.setString(3, personalData.getMiddleName());
        ps.setString(4, personalData.getPassportId());
        ps.setString(5, personalData.getITN());
        ps.setString(6, personalData.getAuthority());
        ps.setInt(7, personalData.getId());
        ps.setString(8, personalData.getPhoneNumber());
        return ps.executeUpdate() > 0;
    }
}
