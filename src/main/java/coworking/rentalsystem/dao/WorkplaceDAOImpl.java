package coworking.rentalsystem.dao;

import coworking.rentalsystem.model.entity.*;
import java.sql.*;
import java.util.ArrayList;

public class WorkplaceDAOImpl implements IWorkplaceDAO {

    private Connection connection;

    WorkplaceDAOImpl(Connection con){
        this.connection = con;
    }

    private static final String GET_ALL_TARIFFS = "SELECT tariff.id, tariff.name, id_time_unit, duration, id_room_type, room_type.name, description, image, price FROM tariff JOIN time_unit ON tariff.id_time_unit = time_unit.id JOIN room_type ON tariff.id_room_type = room_type.id ORDER BY id_room_type, id_time_unit";
    private static final String GET_ALL_TARIFFS_CHEAP = "SELECT tariff.id, tariff.name, id_time_unit, duration, id_room_type, room_type.name, description, image, price FROM tariff JOIN time_unit ON tariff.id_time_unit = time_unit.id JOIN room_type ON tariff.id_room_type = room_type.id ORDER BY price";
    private static final String GET_ALL_TARIFFS_EXPENSIVE = "SELECT tariff.id, tariff.name, id_time_unit, duration, id_room_type, room_type.name, description, image, price FROM tariff JOIN time_unit ON tariff.id_time_unit = time_unit.id JOIN room_type ON tariff.id_room_type = room_type.id ORDER BY price DESC";
    private static final String GET_SERVICE_BY_TARIFF_ID = "SELECT id_service, service.name FROM tariff JOIN tariff_has_service ON tariff.id = id_tariff JOIN service ON id_service = service.id WHERE id_tariff = ?";
    private static final String GET_ROOM_BY_ROOM_TYPE_ID = "SELECT room.id, area, max_places, id_room_type, name, description, image FROM room JOIN room_type ON id_room_type = room_type.id WHERE id_room_type = ?";
    private static final String GET_TIME_UNIT_BY_TARIFF_ID = "SELECT time_unit.id, duration FROM tariff JOIN time_unit ON tariff.id_time_unit = time_unit.id WHERE tariff.id = ?";
    private static final String GET_ROOM_TYPE_BY_TARIFF_ID = "SELECT room_type.id, room_type.name, description, image FROM room_type JOIN tariff ON room_type.id = tariff.id_room_type WHERE tariff.id = ?";
    private static final String GET_AVAILABLE_ROOM_BY_TYPE = "{call AvailableRoom(?,?,?)}";
    private static final String GET_AVAILABLE_PLACE_BY_ROOM = "{call FirstAvailablePlace(?,?,?)}";

    @Override
    public ArrayList<Tariff> getAllTariffs() throws SQLException {
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(GET_ALL_TARIFFS);
        ArrayList<Tariff> tariffs = new ArrayList<>();
        while(rs.next()){
            tariffs.add(createTariff(rs));
        }
        rs.close();
        st.close();
        return tariffs;
    }

    @Override
    public ArrayList<Tariff> getAllTariffsOrderCheap() throws SQLException {
        ArrayList<Tariff> tariffs = new ArrayList<>();
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(GET_ALL_TARIFFS_CHEAP);
        while (rs.next()) tariffs.add(createTariff(rs));
        return tariffs;
    }

    private Tariff createTariff(ResultSet rs) throws SQLException {
        return new Tariff(rs.getInt(1), rs.getString(2),
                new TimeUnit(rs.getInt(3), rs.getString(4)),
                new RoomType(rs.getInt(5), rs.getString(6), rs.getString(7), rs.getString(8)),
                rs.getDouble(9),new ArrayList<>());
    }

    private Room createRoom(ResultSet rs) throws SQLException{
        return new Room(rs.getInt(1), rs.getDouble(2), rs.getInt(3),
                new RoomType(rs.getInt(4), rs.getString(5), rs.getString(6),
                        rs.getString(7)));
    }

    @Override
    public ArrayList<Tariff> getAllTariffsOrderExpensive() throws SQLException {
        ArrayList<Tariff> tariffs = new ArrayList<>();
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(GET_ALL_TARIFFS_EXPENSIVE);
        while (rs.next()) tariffs.add(createTariff(rs));
        return tariffs;
    }

    @Override
    public ArrayList<Service> getServiceByTariff(int id_tariff) throws SQLException {
        ArrayList<Service> services = new ArrayList<>();
        PreparedStatement ps = connection.prepareStatement(GET_SERVICE_BY_TARIFF_ID);
        ps.setInt(1,id_tariff);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            services.add(new Service(rs.getInt(1), rs.getString(2)));
        }
        rs.close();
        ps.close();
        return services;
    }

    @Override
    public ArrayList<Room> getRoomByType(int id_room_type) throws SQLException {
        ArrayList<Room> rooms = new ArrayList<>();
        PreparedStatement ps = connection.prepareStatement(GET_ROOM_BY_ROOM_TYPE_ID);
        ps.setInt(1, id_room_type);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            rooms.add(createRoom(rs));
        }
        rs.close();
        ps.close();
        return rooms;
    }

    @Override
    public TimeUnit getTimeUnitByTariff(int id_tariff) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(GET_TIME_UNIT_BY_TARIFF_ID);
        ps.setInt(1, id_tariff);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) return new TimeUnit(rs.getInt(1), rs.getString(2));
        return null;
    }

    @Override
    public RoomType getRoomTypeByTariff(int id_tariff) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(GET_ROOM_TYPE_BY_TARIFF_ID);
        ps.setInt(1, id_tariff);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) return new RoomType(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
        return null;
    }

    @Override
    public ArrayList<Room> getAvailableRoom(Timestamp start, Timestamp end, int id_room_type) throws SQLException {
        ArrayList<Room> rooms = new ArrayList<>();
        CallableStatement cs = connection.prepareCall(GET_AVAILABLE_ROOM_BY_TYPE);
        cs.setString(1, start.toString());
        cs.setString(2, end.toString());
        cs.setInt(3, id_room_type);
        ResultSet rs = cs.executeQuery();
        while (rs.next()){
            rooms.add(createRoom(rs));
        }
        rs.close();
        cs.close();
        return rooms;
    }

    @Override
    public Place getAvailablePlace(Timestamp start, Timestamp end, int id_room) throws SQLException {
        CallableStatement cs = connection.prepareCall(GET_AVAILABLE_PLACE_BY_ROOM);
        cs.setString(1, start.toString());
        cs.setString(2,end.toString());
        cs.setInt(3, id_room);
        ResultSet rs = cs.executeQuery();

        Place place;
        if(rs.next()) {
            Room room = new Room();
            room.setId(rs.getInt(3));
            place = new Place(rs.getInt(1), rs.getDouble(2), room);
        } else throw new SQLException();
        rs.close();
        cs.close();
        return place;
    }

}
