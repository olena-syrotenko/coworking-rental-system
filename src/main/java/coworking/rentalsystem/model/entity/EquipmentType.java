package coworking.rentalsystem.model.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentType {
    int id;
    String name;
    double price;
    String imageUrl;
}
