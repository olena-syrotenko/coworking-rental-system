package coworking.rentalsystem.dao;

import coworking.rentalsystem.model.entity.*;
import java.sql.*;
import java.util.ArrayList;

public class RentDAOImpl implements IRentDAO{
    private Connection connection;

    RentDAOImpl(Connection con){
        this.connection = con;
    }

    private static final String INSERT_RENT_APPLICATION = "INSERT INTO rent_application (`rent_start`,`rent_end`,`id_place`,`id_status`,`id_user`,`id_tariff`) VALUES(?,?,?,?,?,?)";
    private static final String GET_USER_APPLICATION_BY_EMAIL = "SELECT rent_application.id, create_date, last_change, rent_start, rent_end, lease_agreement, rent_amount, place.id, place.area, room.id, room.area, room_type.id, room_type.name, status.id, status.name,id_user, email, first_name, last_name, middle_name, passport_id, ITN, authority, phone_number,tariff.id, tariff.name FROM rent_application JOIN place ON id_place = place.id JOIN room ON id_room = room.id JOIN room_type ON id_room_type = room_type.id JOIN status ON id_status = status.id JOIN user ON id_user = user.id JOIN personal_data ON id_user = personal_data.id JOIN tariff ON id_tariff = tariff.id WHERE user.email = ? ORDER BY create_date DESC";
    private static final String GET_ALL_STATUS = "SELECT id, name FROM status";
    private static final String GET_USER_APPLICATION_BY_ID_STATUS = "SELECT rent_application.id, create_date, last_change, rent_start, rent_end, lease_agreement, rent_amount, place.id, place.area, room.id, room.area, room_type.id, room_type.name, status.id, status.name,id_user, email, first_name, last_name, middle_name, passport_id, ITN, authority, phone_number,tariff.id, tariff.name FROM rent_application JOIN place ON id_place = place.id JOIN room ON id_room = room.id JOIN room_type ON id_room_type = room_type.id JOIN status ON id_status = status.id JOIN user ON id_user = user.id JOIN personal_data ON id_user = personal_data.id JOIN tariff ON id_tariff = tariff.id WHERE user.email = ? and id_status = ? ORDER BY create_date DESC";
    private static final String UPDATE_STATUS_IN_APPLICATION = "UPDATE rent_application SET id_status = ? WHERE id = ?";
    private static final String GET_ALL_APPLICATION_BY_STATUS = "SELECT rent_application.id, create_date, last_change, rent_start, rent_end, lease_agreement, rent_amount, place.id, place.area, room.id, room.area, room_type.id, room_type.name, status.id, status.name,id_user, email, first_name, last_name, middle_name, passport_id, ITN, authority, phone_number,tariff.id, tariff.name FROM rent_application JOIN place ON id_place = place.id JOIN room ON id_room = room.id JOIN room_type ON id_room_type = room_type.id JOIN status ON id_status = status.id JOIN user ON id_user = user.id JOIN personal_data ON id_user = personal_data.id JOIN tariff ON id_tariff = tariff.id WHERE id_status = ? ORDER BY create_date ";
    private static final String GET_ADMIN_APPLICATION_BY_STATUS = "SELECT rent_application.id, create_date, last_change, rent_start, rent_end, lease_agreement, rent_amount, place.id, place.area, room.id, room.area, room_type.id, room_type.name, status.id, status.name,id_user, email, first_name, last_name, middle_name, passport_id, ITN, authority, phone_number,tariff.id, tariff.name FROM rent_application JOIN place ON id_place = place.id JOIN room ON id_room = room.id JOIN room_type ON id_room_type = room_type.id JOIN status ON id_status = status.id JOIN user ON id_user = user.id JOIN personal_data ON id_user = personal_data.id JOIN tariff ON id_tariff = tariff.id WHERE id_admin = ? AND id_status = ? ORDER BY create_date";
    private static final String UPDATE_ADMIN_IN_APPLICATION = "UPDATE rent_application SET id_admin = ? WHERE id = ?";
    private static final String GET_APPLICATION_BY_ID = "SELECT rent_application.id, create_date, last_change, rent_start, rent_end, lease_agreement, rent_amount, place.id, place.area, room.id, room.area, room_type.id, room_type.name, status.id, status.name,id_user, email, first_name, last_name, middle_name, passport_id, ITN, authority, phone_number,tariff.id, tariff.name FROM rent_application JOIN place ON id_place = place.id JOIN room ON id_room = room.id JOIN room_type ON id_room_type = room_type.id JOIN status ON id_status = status.id JOIN user ON id_user = user.id JOIN personal_data ON id_user = personal_data.id JOIN tariff ON id_tariff = tariff.id WHERE rent_application.id = ?";

    @Override
    public ArrayList<RentApplication> getApplicationByUserEmail(String email) throws SQLException {
        ArrayList<RentApplication> rentApplications = new ArrayList<>();
        PreparedStatement ps = connection.prepareStatement(GET_USER_APPLICATION_BY_EMAIL);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            rentApplications.add(createRentApplication(rs));
        }
        return rentApplications;
    }

    private RentApplication createRentApplication(ResultSet resultSet) throws SQLException {
        RentApplication rentApplication = new RentApplication();
        rentApplication.setId(resultSet.getInt(1));
        rentApplication.setCreateDate(Timestamp.valueOf(resultSet.getString(2)));
        rentApplication.setLastChange(Timestamp.valueOf(resultSet.getString(3)));
        rentApplication.setRentStart(Timestamp.valueOf(resultSet.getString(4)));
        rentApplication.setRentEnd(Timestamp.valueOf(resultSet.getString(5)));
        rentApplication.setLeaseAgreement(resultSet.getString(6));
        rentApplication.setRentAmount(resultSet.getDouble(7));

        Place place = new Place();
        place.setId(resultSet.getInt(8));
        place.setArea(resultSet.getDouble(9));

        Room room = new Room();
        room.setId(resultSet.getInt(10));
        room.setArea(resultSet.getDouble(11));

        RoomType roomType = new RoomType();
        roomType.setId(resultSet.getInt(12));
        roomType.setName(resultSet.getString(13));

        room.setRoomType(roomType);
        place.setRoom(room);
        rentApplication.setPlace(place);

        Status status = new Status();
        status.setId(resultSet.getInt(14));
        status.setName(resultSet.getString(15));
        rentApplication.setStatus(status);

        User user = new User();
        user.setId(resultSet.getInt(16));
        user.setUsername(resultSet.getString(17));
        user.setPersonalData(new PersonalData(resultSet.getInt(16), resultSet.getString(18),
                resultSet.getString(19), resultSet.getString(20), resultSet.getString(21),
                resultSet.getString(22), resultSet.getString(23), resultSet.getString(24)));
        rentApplication.setUser(user);

        Tariff tariff = new Tariff();
        tariff.setId(resultSet.getInt(25));
        tariff.setName(resultSet.getString(26));
        rentApplication.setTariff(tariff);

        return rentApplication;
    }

    @Override
    public int insertRentApplication(RentApplication rentApplication) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(INSERT_RENT_APPLICATION, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1,rentApplication.getRentStart().toString());
        ps.setString(2, rentApplication.getRentEnd().toString());
        ps.setInt(3, rentApplication.getPlace().getId());
        ps.setInt(4,rentApplication.getStatus().getId());
        ps.setInt(5, rentApplication.getUser().getId());
        ps.setInt(6, rentApplication.getTariff().getId());

        ps.executeUpdate();
        ResultSet generatedKey = ps.getGeneratedKeys();
        int key;
        if(generatedKey.next()) key = generatedKey.getInt(1);
        else throw new SQLException();
        generatedKey.close();
        ps.close();
        return key;
    }

    @Override
    public ArrayList<Status> getStatus() throws SQLException {
        ArrayList<Status> statuses = new ArrayList<>();
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(GET_ALL_STATUS);
        while(rs.next()){
            statuses.add(new Status(rs.getInt(1), rs.getString(2)));
        }
        return statuses;
    }

    @Override
    public ArrayList<RentApplication> getUserApplicationByStatus(String email, int id_status) throws SQLException {
        ArrayList<RentApplication> rentApplications = new ArrayList<>();
        PreparedStatement ps = connection.prepareStatement(GET_USER_APPLICATION_BY_ID_STATUS);
        ps.setString(1, email);
        ps.setInt(2, id_status);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            rentApplications.add(createRentApplication(rs));
        }
        return rentApplications;
    }

    @Override
    public boolean updateApplicationStatus(int id_application, int id_status) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(UPDATE_STATUS_IN_APPLICATION);
        ps.setInt(1, id_status);
        ps.setInt(2, id_application);
        return ps.executeUpdate() > 0;
    }

    @Override
    public boolean updateAdminInApplication(int id_admin, int id_application) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(UPDATE_ADMIN_IN_APPLICATION);
        ps.setInt(1, id_admin);
        ps.setInt(2, id_application);
        return ps.executeUpdate() > 0;
    }

    @Override
    public ArrayList<RentApplication> getAllApplicationByStatus(int id_status) throws SQLException {
        ArrayList<RentApplication> rentApplications = new ArrayList<>();
        PreparedStatement ps = connection.prepareStatement(GET_ALL_APPLICATION_BY_STATUS);
        ps.setInt(1, id_status);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            rentApplications.add(createRentApplication(rs));
        }
        return rentApplications;
    }

    @Override
    public RentApplication getRentApplicationById(int id_application) throws SQLException {
        RentApplication rentApplication = null;
        PreparedStatement ps = connection.prepareStatement(GET_APPLICATION_BY_ID);
        ps.setInt(1, id_application);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) rentApplication = createRentApplication(rs);
        return rentApplication;
    }
}
