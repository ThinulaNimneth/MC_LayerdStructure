package lk.ijse.medicalcenterlayerdstructure.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CrudUtil {
    @SuppressWarnings("unchecked")
    public static <T> T execute(String sql, Object... obj) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ptsm = conn.prepareStatement(sql);

        for (int i = 0; i < obj.length; i++) {
            ptsm.setObject(i + 1, obj[i]);
        }

        if (sql.startsWith("SELECT") || sql.startsWith("select")) {

            ResultSet resultSet = ptsm.executeQuery();
            return (T) resultSet;
        } else {

            int affectedRows = ptsm.executeUpdate();
            boolean isSuccess = affectedRows > 0;
            return (T) (Boolean) isSuccess;
        }
    }
}
