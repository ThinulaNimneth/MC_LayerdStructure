package lk.ijse.medicalcenterlayerdstructure.dao.custom.impl;

import lk.ijse.medicalcenterlayerdstructure.dao.custom.MedicalHistoryDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.MedicalHistoryDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.MedicalHistory;
import lk.ijse.medicalcenterlayerdstructure.util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MedicalHistoryDAOImpl implements MedicalHistoryDAO {

    @Override
    public boolean save(MedicalHistory mh) throws SQLException {
        return CrudUtil.execute("INSERT INTO MedicalHistory (patient_id, doctor_id, diagnosis, prescription, date) VALUES (?, ?, ?, ?, ?)", mh.getPatientId(), mh.getDoctorId(), mh.getDiagnosis(), mh.getPrescription(), mh.getDate());
    }

    @Override
    public boolean update(MedicalHistory mh) throws SQLException {
        return CrudUtil.execute("UPDATE MedicalHistory SET diagnosis = ?, prescription = ?, date = ? WHERE history_id = ?", mh.getDiagnosis(), mh.getPrescription(), mh.getDate(), mh.getHistoryId());
    }

    @Override
    public boolean delete(int historyId) throws SQLException {
        return CrudUtil.execute("DELETE FROM MedicalHistory WHERE history_id = ?", historyId);
    }

    @Override
    public MedicalHistoryDTO findById(int historyId) throws SQLException {
        String sql = "SELECT mh.*, p.name as patient_name, u.user_name as doctor_name FROM MedicalHistory mh JOIN Patient p ON mh.patient_id = p.patient_id JOIN Doctor d ON mh.doctor_id = d.doctor_id JOIN User u ON d.user_id = u.user_id WHERE mh.history_id = ?";
        ResultSet rs = CrudUtil.execute(sql, historyId);
        if (rs.next()) return mapRow(rs);
        return null;
    }

    @Override
    public List<MedicalHistoryDTO> getAll() throws SQLException {
        String sql = "SELECT mh.*, p.name as patient_name, u.user_name as doctor_name FROM MedicalHistory mh JOIN Patient p ON mh.patient_id = p.patient_id JOIN Doctor d ON mh.doctor_id = d.doctor_id JOIN User u ON d.user_id = u.user_id ORDER BY mh.history_id DESC";
        return mapResultSet(CrudUtil.execute(sql));
    }

    @Override
    public List<MedicalHistoryDTO> getByPatient(int patientId) throws SQLException {
        String sql = "SELECT mh.*, p.name as patient_name, u.user_name as doctor_name FROM MedicalHistory mh JOIN Patient p ON mh.patient_id = p.patient_id JOIN Doctor d ON mh.doctor_id = d.doctor_id JOIN User u ON d.user_id = u.user_id WHERE mh.patient_id = ? ORDER BY mh.date DESC";
        return mapResultSet(CrudUtil.execute(sql, patientId));
    }

    private MedicalHistoryDTO mapRow(ResultSet rs) throws SQLException {
        MedicalHistoryDTO dto = new MedicalHistoryDTO();
        dto.setHistoryId(rs.getInt("history_id")); dto.setPatientId(rs.getInt("patient_id")); dto.setDoctorId(rs.getInt("doctor_id"));
        dto.setPatientName(rs.getString("patient_name")); dto.setDoctorName(rs.getString("doctor_name"));
        dto.setDiagnosis(rs.getString("diagnosis")); dto.setPrescription(rs.getString("prescription")); dto.setDate(rs.getDate("date").toLocalDate());
        return dto;
    }

    private List<MedicalHistoryDTO> mapResultSet(ResultSet rs) throws SQLException {
        List<MedicalHistoryDTO> list = new ArrayList<>();
        while (rs.next()) list.add(mapRow(rs));
        return list;
    }
}
