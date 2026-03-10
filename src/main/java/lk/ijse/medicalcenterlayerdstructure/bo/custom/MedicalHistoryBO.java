package lk.ijse.medicalcenterlayerdstructure.bo.custom;

import lk.ijse.medicalcenterlayerdstructure.bo.SuperBO;
import lk.ijse.medicalcenterlayerdstructure.dto.MedicalHistoryDTO;
import java.sql.SQLException;
import java.util.List;



public interface MedicalHistoryBO extends SuperBO {
    boolean saveMedicalHistory(MedicalHistoryDTO dto) throws SQLException;
    boolean updateMedicalHistory(MedicalHistoryDTO dto) throws SQLException;
    boolean deleteMedicalHistory(int historyId) throws SQLException;
    MedicalHistoryDTO searchMedicalHistory(int historyId) throws SQLException;
    List<MedicalHistoryDTO> getAllMedicalHistories() throws SQLException;
    List<MedicalHistoryDTO> getMedicalHistoriesByPatient(int patientId) throws SQLException;
}