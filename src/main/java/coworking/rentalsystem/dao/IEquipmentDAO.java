package coworking.rentalsystem.dao;

import coworking.rentalsystem.model.entity.EquipmentType;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IEquipmentDAO {
    ArrayList<EquipmentType> getEquipmentType() throws SQLException;
}
