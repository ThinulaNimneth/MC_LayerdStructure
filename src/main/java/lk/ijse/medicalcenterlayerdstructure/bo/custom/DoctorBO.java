package lk.ijse.medicalcenterlayerdstructure.bo.custom;

import lk.ijse.medicalcenterlayerdstructure.bo.SuperBO;
import lk.ijse.medicalcenterlayerdstructure.dto.DoctorDTO;
import java.sql.SQLException;
import java.util.List;

public interface DoctorBO extends SuperBO {
    boolean saveDoctor(DoctorDTO dto) throws SQLException;
    boolean updateDoctor(DoctorDTO dto) throws SQLException;
    boolean deleteDoctor(int doctorId) throws SQLException;
    DoctorDTO searchDoctor(int doctorId) throws SQLException;
    List<DoctorDTO> searchDoctorByName(String name) throws SQLException;
    List<DoctorDTO> getAllDoctors() throws SQLException;
    List<DoctorDTO> getAvailableDoctors() throws SQLException;
    int getNextDoctorId() throws SQLException;
    boolean isDoctorExists(int doctorId) throws SQLException;
}