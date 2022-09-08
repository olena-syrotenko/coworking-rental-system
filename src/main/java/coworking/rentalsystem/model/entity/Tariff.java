package coworking.rentalsystem.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tariff {
    int id;
    String name;
    TimeUnit timeUnit;
    RoomType roomType;
    double price;
    List<Service> services;
}
