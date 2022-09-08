package coworking.rentalsystem.service;

import coworking.rentalsystem.model.entity.EquipmentType;
import java.util.ArrayList;

public interface IEquipmentService {
    ArrayList<EquipmentType> getEquipmentType();
}
