package coworking.rentalsystem.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    int id;
    double area;
    int maxPlace;
    RoomType roomType;
}
