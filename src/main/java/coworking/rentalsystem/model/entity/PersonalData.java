package coworking.rentalsystem.model.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalData {
    int id;
    String firstName;
    String lastName;
    String middleName;
    String passportId;
    String ITN;
    String authority;
    String phoneNumber;

}
