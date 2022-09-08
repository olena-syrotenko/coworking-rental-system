package coworking.rentalsystem.service;

import coworking.rentalsystem.model.entity.Place;
import coworking.rentalsystem.model.entity.Room;
import coworking.rentalsystem.model.entity.Tariff;
import java.sql.Timestamp;
import java.util.ArrayList;

public interface IWorkplaceService {
    ArrayList<Tariff> getTariffs();
    ArrayList<Tariff> getCheapTariffs();
    ArrayList<Tariff> getExpensiveTariffs();
    ArrayList<Room> getRoomByTariff(int id_tariff);
    String getDurationByTariff(int id_tariff);
    ArrayList<Room> getAvailableRoomByTariff(Timestamp start, Timestamp end, int id_tariff);
    Place getAvailablePlaceByRoom(Timestamp start, Timestamp end, int id_room);
}
