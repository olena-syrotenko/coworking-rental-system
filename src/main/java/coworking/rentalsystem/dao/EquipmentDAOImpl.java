package coworking.rentalsystem.dao;

import coworking.rentalsystem.model.entity.EquipmentType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EquipmentDAOImpl implements IEquipmentDAO{
    private Connection connection;
    public EquipmentDAOImpl(Connection con){
        this.connection = con;
    }

    private static final String GET_EQUIPMENT_TYPE = "SELECT id, name, price, image FROM coworking.equipment_type";

    @Override
    public ArrayList<EquipmentType> getEquipmentType() throws SQLException {
        ArrayList<EquipmentType> equipmentTypes = new ArrayList<>();

        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(GET_EQUIPMENT_TYPE);
        while (rs.next()) {
            equipmentTypes.add(new EquipmentType(rs.getInt(1), rs.getString(2),
                    rs.getDouble(3), rs.getString(4)));
        }
        rs.close();
        st.close();
        return equipmentTypes;
    }
}
