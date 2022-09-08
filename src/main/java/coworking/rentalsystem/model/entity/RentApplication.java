package coworking.rentalsystem.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RentApplication {
    int id;
    Timestamp createDate;
    Timestamp lastChange;
    Timestamp rentStart;
    Timestamp rentEnd;
    Place place;
    Status status;
    String leaseAgreement;
    User user;
    User administrator;
    double rentAmount;
    Tariff tariff;
}
