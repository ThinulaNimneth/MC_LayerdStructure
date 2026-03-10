package lk.ijse.medicalcenterlayerdstructure.bo.custom.impl;

import lk.ijse.medicalcenterlayerdstructure.bo.custom.PatientBO;
import lk.ijse.medicalcenterlayerdstructure.dao.DAOFactory;
import lk.ijse.medicalcenterlayerdstructure.dao.custom.PatientDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.PatientDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.Patient;
import java.sql.SQLException;
import java.util.List;



public class PatientBOImpl implements PatientBO {
    PatientDAO patientDAO = (PatientDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.PATIENT);

    @Override
    public boolean savePatient(PatientDTO dto) throws SQLException {
        return patientDAO.save(new Patient(dto.getPatientId(), dto.getName(), dto.getGender(), dto.getContacts()));
    }

    @Override
    public boolean updatePatient(PatientDTO dto) throws SQLException {
        return patientDAO.update(new Patient(dto.getPatientId(), dto.getName(), dto.getGender(), dto.getContacts()));
    }

    @Override
    public boolean deletePatient(int patientId) throws SQLException {
        return patientDAO.delete(patientId);
    }

    @Override
    public PatientDTO searchPatient(int patientId) throws SQLException {
        return patientDAO.findById(patientId);
    }

    @Override
    public List<PatientDTO> searchPatientByContact(String contact) throws SQLException {
        return patientDAO.findByContact(contact);
    }

    @Override
    public List<PatientDTO> getAllPatients() throws SQLException {
        return patientDAO.getAll();
    }
}
