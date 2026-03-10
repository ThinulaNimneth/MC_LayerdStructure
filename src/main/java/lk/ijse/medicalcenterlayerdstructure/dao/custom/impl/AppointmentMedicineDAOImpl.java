package lk.ijse.medicalcenterlayerdstructure.dao.custom.impl;

import lk.ijse.medicalcenterlayerdstructure.dao.custom.AppointmentMedicineDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.AppointmentMedicineDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.AppointmentMedicine;
import lk.ijse.medicalcenterlayerdstructure.util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AppointmentMedicineDAOImpl implements AppointmentMedicineDAO {

    @Override
    public boolean save(AppointmentMedicine am) throws SQLException {
        return CrudUtil.execute("INSERT INTO Appointment_Medicine (appointment_id, medicine_id, quantity) VALUES (?, ?, ?)", am.getAppointmentId(), am.getMedicineId(), am.getQuantity());
    }

    @Override
    public boolean deleteByAppointmentId(int appointmentId) throws SQLException {
        return CrudUtil.execute("DELETE FROM Appointment_Medicine WHERE appointment_id = ?", appointmentId);
    }

    @Override
    public List<AppointmentMedicineDTO> getByAppointmentId(int appointmentId) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT am.*, m.name as medicine_name, m.price FROM Appointment_Medicine am JOIN Medicine m ON am.medicine_id = m.medicine_id WHERE am.appointment_id = ?", appointmentId);
        List<AppointmentMedicineDTO> list = new ArrayList<>();
        while (rs.next()) {
            AppointmentMedicineDTO dto = new AppointmentMedicineDTO();
            dto.setAppointmentId(rs.getInt("appointment_id")); dto.setMedicineId(rs.getInt("medicine_id"));
            dto.setMedicineName(rs.getString("medicine_name")); dto.setPrice(rs.getBigDecimal("price")); dto.setQuantity(rs.getInt("quantity"));
            list.add(dto);
        }
        return list;
    }
}
