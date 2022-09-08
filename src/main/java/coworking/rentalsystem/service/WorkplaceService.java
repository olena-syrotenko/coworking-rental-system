package coworking.rentalsystem.service;

import coworking.rentalsystem.dao.DAOFactory;
import coworking.rentalsystem.dao.IWorkplaceDAO;
import coworking.rentalsystem.dao.typeDAO;
import coworking.rentalsystem.model.entity.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class WorkplaceService implements IWorkplaceService{
    private final static DAOFactory daoFactory;
    private static IWorkplaceDAO workplaceDAO;

    static{
        daoFactory = DAOFactory.getDAOFactory(typeDAO.MySQL);
    }

    public ArrayList<Tariff> getTariffs(){
        ArrayList<Tariff> tariffs = new ArrayList<>();
        daoFactory.open();
        try {
            workplaceDAO = daoFactory.getWorkplaceDAO();
            tariffs = workplaceDAO.getAllTariffs();
            for (Tariff tariff : tariffs) {
                ArrayList<Service> serviceByTariff = workplaceDAO.getServiceByTariff(tariff.getId());
                tariff.setServices(serviceByTariff);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        daoFactory.close();
        return tariffs;
    }

    @Override
    public ArrayList<Tariff> getCheapTariffs() {
        ArrayList<Tariff> tariffs = new ArrayList<>();
        daoFactory.open();
        try {
            workplaceDAO = daoFactory.getWorkplaceDAO();
            tariffs = workplaceDAO.getAllTariffsOrderCheap();
            for (Tariff tariff : tariffs) {
                ArrayList<Service> serviceByTariff = workplaceDAO.getServiceByTariff(tariff.getId());
                tariff.setServices(serviceByTariff);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        daoFactory.close();
        return tariffs;
    }

    @Override
    public ArrayList<Tariff> getExpensiveTariffs() {
        ArrayList<Tariff> tariffs = new ArrayList<>();
        daoFactory.open();
        try {
            workplaceDAO = daoFactory.getWorkplaceDAO();
            tariffs = workplaceDAO.getAllTariffsOrderExpensive();
            for (Tariff tariff : tariffs) {
                ArrayList<Service> serviceByTariff = workplaceDAO.getServiceByTariff(tariff.getId());
                tariff.setServices(serviceByTariff);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        daoFactory.close();
        return tariffs;
    }

    @Override
    public ArrayList<Room> getRoomByTariff(int id_tariff) {
        ArrayList<Room> rooms = new ArrayList<>();
        daoFactory.open();
        try {
            workplaceDAO = daoFactory.getWorkplaceDAO();
            rooms = workplaceDAO.getRoomByType(workplaceDAO.getRoomTypeByTariff(id_tariff).getId());
        } catch (SQLException e) {
            e.printStackTrace();
            daoFactory.close();
        }
        daoFactory.close();
        return rooms;
    }

    @Override
    public String getDurationByTariff(int id_tariff) {
        TimeUnit timeUnit = null;
        daoFactory.open();
        try {
            workplaceDAO = daoFactory.getWorkplaceDAO();
            timeUnit = workplaceDAO.getTimeUnitByTariff(id_tariff);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        daoFactory.close();
        return timeUnit == null ? " " : timeUnit.getDuration();
    }

    @Override
    public ArrayList<Room> getAvailableRoomByTariff(Timestamp start, Timestamp end, int id_tariff) {
        ArrayList<Room> rooms = new ArrayList<>();
        daoFactory.open();
        try {
            workplaceDAO = daoFactory.getWorkplaceDAO();
            rooms = workplaceDAO.getAvailableRoom(start, end, workplaceDAO.getRoomTypeByTariff(id_tariff).getId());
        } catch (SQLException e) {
            e.printStackTrace();
            daoFactory.close();
        }
        daoFactory.close();
        return rooms;
    }

    @Override
    public Place getAvailablePlaceByRoom(Timestamp start, Timestamp end, int id_room) {
        daoFactory.open();
        Place place = null;
        try {
            workplaceDAO = daoFactory.getWorkplaceDAO();
            place = workplaceDAO.getAvailablePlace(start,end,id_room);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        daoFactory.close();
        return place;
    }
}
