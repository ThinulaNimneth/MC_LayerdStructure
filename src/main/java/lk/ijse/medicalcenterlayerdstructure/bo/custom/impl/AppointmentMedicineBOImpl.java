package lk.ijse.medicalcenterlayerdstructure.bo.custom.impl;

import lk.ijse.medicalcenterlayerdstructure.bo.custom.AppointmentMedicineBO;
import lk.ijse.medicalcenterlayerdstructure.dao.DAOFactory;
import lk.ijse.medicalcenterlayerdstructure.dao.custom.AppointmentMedicineDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.AppointmentMedicineDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.AppointmentMedicine;
import java.sql.SQLException;
import java.util.List;



public class AppointmentMedicineBOImpl implements AppointmentMedicineBO {
    AppointmentMedicineDAO dao = (AppointmentMedicineDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.APPOINTMENT_MEDICINE);

    @Override
    public boolean saveAppointmentMedicine(AppointmentMedicineDTO dto) throws SQLException {
        return dao.save(new AppointmentMedicine(dto.getAppointmentId(), dto.getMedicineId(), dto.getQuantity()));
    }

    @Override
    public boolean deleteAppointmentMedicines(int appointmentId) throws SQLException {
        return dao.deleteByAppointmentId(appointmentId);
    }

    @Override
    public List<AppointmentMedicineDTO> getAppointmentMedicines(int appointmentId) throws SQLException {
        return dao.getByAppointmentId(appointmentId);
    }
}
