package lk.ijse.medicalcenterlayerdstructure.dao.custom;

import lk.ijse.medicalcenterlayerdstructure.dao.SuperDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.PatientDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.Patient;
import java.sql.SQLException;
import java.util.List;



public interface PatientDAO extends SuperDAO {
    boolean save(Patient patient) throws SQLException;
    boolean update(Patient patient) throws SQLException;
    boolean delete(int patientId) throws SQLException;
    PatientDTO findById(int patientId) throws SQLException;
    List<PatientDTO> findByContact(String contact) throws SQLException;
    List<PatientDTO> getAll() throws SQLException;
}
