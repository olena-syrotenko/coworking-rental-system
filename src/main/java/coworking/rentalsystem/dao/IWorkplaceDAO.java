package coworking.rentalsystem.dao;

import coworking.rentalsystem.model.entity.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public interface IWorkplaceDAO {
    ArrayList<Tariff> getAllTariffs() throws SQLException;
    ArrayList<Tariff> getAllTariffsOrderCheap() throws SQLException;
    ArrayList<Tariff> getAllTariffsOrderExpensive() throws SQLException;
    ArrayList<Service> getServiceByTariff(int id_tariff) throws SQLException;
    ArrayList<Room> getRoomByType(int id_room_type) throws  SQLException;
    TimeUnit getTimeUnitByTariff(int id_tariff) throws SQLException;
    RoomType getRoomTypeByTariff(int id_tariff) throws SQLException;
    ArrayList<Room> getAvailableRoom(Timestamp start, Timestamp end, int id_room_type) throws SQLException;
    Place getAvailablePlace(Timestamp start, Timestamp end, int id_room) throws SQLException;
}
