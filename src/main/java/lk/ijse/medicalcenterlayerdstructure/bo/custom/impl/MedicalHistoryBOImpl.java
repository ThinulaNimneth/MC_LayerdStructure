package lk.ijse.medicalcenterlayerdstructure.bo.custom.impl;

import lk.ijse.medicalcenterlayerdstructure.bo.custom.MedicalHistoryBO;
import lk.ijse.medicalcenterlayerdstructure.dao.DAOFactory;
import lk.ijse.medicalcenterlayerdstructure.dao.custom.MedicalHistoryDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.MedicalHistoryDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.MedicalHistory;
import java.sql.SQLException;
import java.util.List;


public class MedicalHistoryBOImpl implements MedicalHistoryBO {
    MedicalHistoryDAO dao = (MedicalHistoryDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.MEDICAL_HISTORY);

    @Override
    public boolean saveMedicalHistory(MedicalHistoryDTO dto) throws SQLException {
        return dao.save(new MedicalHistory(0, dto.getPatientId(), dto.getDoctorId(), dto.getDiagnosis(), dto.getPrescription(), dto.getDate()));
    }

    @Override
    public boolean updateMedicalHistory(MedicalHistoryDTO dto) throws SQLException {
        return dao.update(new MedicalHistory(dto.getHistoryId(), dto.getPatientId(), dto.getDoctorId(), dto.getDiagnosis(), dto.getPrescription(), dto.getDate()));
    }

    @Override
    public boolean deleteMedicalHistory(int historyId) throws SQLException {
        return dao.delete(historyId);
    }

    @Override
    public MedicalHistoryDTO searchMedicalHistory(int historyId) throws SQLException {
        return dao.findById(historyId);
    }

    @Override
    public List<MedicalHistoryDTO> getAllMedicalHistories() throws SQLException {
        return dao.getAll();
    }

    @Override
    public List<MedicalHistoryDTO> getMedicalHistoriesByPatient(int patientId) throws SQLException {
        return dao.getByPatient(patientId);
    }
}
