package lk.ijse.medicalcenterlayerdstructure.bo.custom;


import lk.ijse.medicalcenterlayerdstructure.bo.SuperBO;
import lk.ijse.medicalcenterlayerdstructure.dto.AppointmentMedicineDTO;
import java.sql.SQLException;
import java.util.List;

public interface AppointmentMedicineBO extends SuperBO {

    boolean saveAppointmentMedicine(AppointmentMedicineDTO dto) throws SQLException;
    boolean deleteAppointmentMedicines(int appointmentId) throws SQLException;
    List<AppointmentMedicineDTO> getAppointmentMedicines(int appointmentId) throws SQLException;
}
