package coworking.rentalsystem.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Equipment {
    int id;
    EquipmentType equipmentType;
    Brand brand;
    String model;
    String description;
    int amount;
}
