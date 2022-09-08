package coworking.rentalsystem.service;

import coworking.rentalsystem.dao.DAOFactory;
import coworking.rentalsystem.dao.IEquipmentDAO;
import coworking.rentalsystem.dao.typeDAO;
import coworking.rentalsystem.model.entity.EquipmentType;
import java.sql.SQLException;
import java.util.ArrayList;

public class EquipmentService implements IEquipmentService{
    private final static DAOFactory daoFactory;
    private static IEquipmentDAO equipmentDAO;

    static{
        daoFactory = DAOFactory.getDAOFactory(typeDAO.MySQL);
    }

    @Override
    public ArrayList<EquipmentType> getEquipmentType() {
        ArrayList<EquipmentType> equipmentTypes = new ArrayList<>();
        daoFactory.open();
        try {
            equipmentDAO = daoFactory.getEquipmentDAO();
            equipmentTypes = equipmentDAO.getEquipmentType();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        daoFactory.close();
        return equipmentTypes;
    }
}
