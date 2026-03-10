package lk.ijse.medicalcenterlayerdstructure.dao.custom;


import lk.ijse.medicalcenterlayerdstructure.dao.SuperDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.MedicalHistoryDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.MedicalHistory;
import java.sql.SQLException;
import java.util.List;


public interface MedicalHistoryDAO extends SuperDAO {
    boolean save(MedicalHistory medicalHistory) throws SQLException;
    boolean update(MedicalHistory medicalHistory) throws SQLException;
    boolean delete(int historyId) throws SQLException;
    MedicalHistoryDTO findById(int historyId) throws SQLException;
    List<MedicalHistoryDTO> getAll() throws SQLException;
    List<MedicalHistoryDTO> getByPatient(int patientId) throws SQLException;
}
