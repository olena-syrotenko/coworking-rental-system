package coworking.rentalsystem.model.entity;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomType {
    int id;
    String name;
    String description;
    String imageUrl = "/assets/rooms/default_room.jpg";
}
