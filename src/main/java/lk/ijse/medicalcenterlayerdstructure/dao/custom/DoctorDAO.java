package lk.ijse.medicalcenterlayerdstructure.dao.custom;


import lk.ijse.medicalcenterlayerdstructure.dao.SuperDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.DoctorDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.Doctor;
import java.sql.SQLException;
import java.util.List;



public interface DoctorDAO extends SuperDAO {
    boolean save(Doctor doctor) throws SQLException;
    boolean update(Doctor doctor) throws SQLException;
    boolean delete(int doctorId) throws SQLException;
    DoctorDTO findById(int doctorId) throws SQLException;
    List<DoctorDTO> findByName(String name) throws SQLException;
    List<DoctorDTO> getAll() throws SQLException;
    List<DoctorDTO> getAvailableDoctors() throws SQLException;
    int getNextDoctorId() throws SQLException;
    boolean isDoctorExists(int doctorId) throws SQLException;
}