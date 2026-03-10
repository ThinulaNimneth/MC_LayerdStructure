package lk.ijse.medicalcenterlayerdstructure.bo.custom.impl;

import lk.ijse.medicalcenterlayerdstructure.bo.custom.DoctorBO;
import lk.ijse.medicalcenterlayerdstructure.dao.DAOFactory;
import lk.ijse.medicalcenterlayerdstructure.dao.custom.DoctorDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.DoctorDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.Doctor;
import java.sql.SQLException;
import java.util.List;


public class DoctorBOImpl implements DoctorBO {
    DoctorDAO doctorDAO = (DoctorDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.DOCTOR);

    @Override
    public boolean saveDoctor(DoctorDTO dto) throws SQLException {
        return doctorDAO.save(new Doctor(0, dto.getUserId(), dto.getDoctorName(), dto.getSpecialization(), dto.isAvailable()));
    }

    @Override
    public boolean updateDoctor(DoctorDTO dto) throws SQLException {
        return doctorDAO.update(new Doctor(dto.getDoctorId(), dto.getUserId(), dto.getDoctorName(), dto.getSpecialization(), dto.isAvailable()));
    }

    @Override
    public boolean deleteDoctor(int doctorId) throws SQLException {
        return doctorDAO.delete(doctorId);
    }

    @Override
    public DoctorDTO searchDoctor(int doctorId) throws SQLException {
        return doctorDAO.findById(doctorId);
    }

    @Override
    public List<DoctorDTO> searchDoctorByName(String name) throws SQLException {
        return doctorDAO.findByName(name);
    }

    @Override
    public List<DoctorDTO> getAllDoctors() throws SQLException {
        return doctorDAO.getAll();
    }

    @Override
    public List<DoctorDTO> getAvailableDoctors() throws SQLException {
        return doctorDAO.getAvailableDoctors();
    }

    @Override
    public int getNextDoctorId() throws SQLException {
        return doctorDAO.getNextDoctorId();
    }

    @Override
    public boolean isDoctorExists(int doctorId) throws SQLException {
        return doctorDAO.isDoctorExists(doctorId);
    }
}
