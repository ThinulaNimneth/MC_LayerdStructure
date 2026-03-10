package lk.ijse.medicalcenterlayerdstructure.dao.custom;

import lk.ijse.medicalcenterlayerdstructure.dao.SuperDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.AppointmentMedicineDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.AppointmentMedicine;
import java.sql.SQLException;
import java.util.List;



public interface AppointmentMedicineDAO extends SuperDAO {
    boolean save(AppointmentMedicine appointmentMedicine) throws SQLException;
    boolean deleteByAppointmentId(int appointmentId) throws SQLException;
    List<AppointmentMedicineDTO> getByAppointmentId(int appointmentId) throws SQLException;
}
