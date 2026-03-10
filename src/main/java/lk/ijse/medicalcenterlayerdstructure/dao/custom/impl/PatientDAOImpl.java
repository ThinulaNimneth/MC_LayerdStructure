package lk.ijse.medicalcenterlayerdstructure.dao.custom.impl;

import lk.ijse.medicalcenterlayerdstructure.dao.custom.PatientDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.PatientDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.Patient;
import lk.ijse.medicalcenterlayerdstructure.util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class PatientDAOImpl implements PatientDAO {

    @Override
    public boolean save(Patient patient) throws SQLException {
        return CrudUtil.execute("INSERT INTO Patient (patient_id, name, gender, contacts) VALUES (?, ?, ?, ?)",
                patient.getPatientId(), patient.getName(), patient.getGender(), patient.getContacts());
    }

    @Override
    public boolean update(Patient patient) throws SQLException {
        return CrudUtil.execute("UPDATE Patient SET name = ?, gender = ?, contacts = ? WHERE patient_id = ?",
                patient.getName(), patient.getGender(), patient.getContacts(), patient.getPatientId());
    }

    @Override
    public boolean delete(int patientId) throws SQLException {
        return CrudUtil.execute("DELETE FROM Patient WHERE patient_id = ?", patientId);
    }

    @Override
    public PatientDTO findById(int patientId) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM Patient WHERE patient_id = ?", patientId);
        if (rs.next()) {
            return new PatientDTO(rs.getInt("patient_id"), rs.getString("name"), rs.getString("gender"), rs.getString("contacts"));
        }
        return null;
    }

    @Override
    public List<PatientDTO> findByContact(String contact) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM Patient WHERE contacts LIKE ? ORDER BY patient_id DESC", "%" + contact + "%");
        List<PatientDTO> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new PatientDTO(rs.getInt("patient_id"), rs.getString("name"), rs.getString("gender"), rs.getString("contacts")));
        }
        return list;
    }

    @Override
    public List<PatientDTO> getAll() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM Patient ORDER BY patient_id DESC");
        List<PatientDTO> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new PatientDTO(rs.getInt("patient_id"), rs.getString("name"), rs.getString("gender"), rs.getString("contacts")));
        }
        return list;
    }
}
