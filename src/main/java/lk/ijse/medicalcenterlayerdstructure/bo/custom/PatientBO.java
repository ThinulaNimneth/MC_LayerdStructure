package lk.ijse.medicalcenterlayerdstructure.bo.custom;

import lk.ijse.medicalcenterlayerdstructure.bo.SuperBO;
import lk.ijse.medicalcenterlayerdstructure.dto.PatientDTO;
import java.sql.SQLException;
import java.util.List;



public interface PatientBO extends SuperBO {
    boolean savePatient(PatientDTO dto) throws SQLException;
    boolean updatePatient(PatientDTO dto) throws SQLException;
    boolean deletePatient(int patientId) throws SQLException;
    PatientDTO searchPatient(int patientId) throws SQLException;
    List<PatientDTO> searchPatientByContact(String contact) throws SQLException;
    List<PatientDTO> getAllPatients() throws SQLException;
}
