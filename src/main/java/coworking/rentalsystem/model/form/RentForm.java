package coworking.rentalsystem.model.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RentForm {
    String dateFrom;
    String dateTo;
    int id_room;
    int id_tariff;
}
