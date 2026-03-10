package lk.ijse.medicalcenterlayerdstructure.dao.custom.impl;

import lk.ijse.medicalcenterlayerdstructure.dao.custom.DoctorDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.DoctorDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.Doctor;
import lk.ijse.medicalcenterlayerdstructure.util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DoctorDAOImpl implements DoctorDAO {

    @Override
    public boolean save(Doctor doctor) throws SQLException {
        return CrudUtil.execute("INSERT INTO Doctor (user_id, specialization, is_available) VALUES (?, ?, ?)",
                doctor.getUserId(), doctor.getSpecialization(), doctor.isAvailable());
    }

    @Override
    public boolean update(Doctor doctor) throws SQLException {
        return CrudUtil.execute("UPDATE Doctor SET specialization = ?, is_available = ? WHERE doctor_id = ?",
                doctor.getSpecialization(), doctor.isAvailable(), doctor.getDoctorId());
    }

    @Override
    public boolean delete(int doctorId) throws SQLException {
        return CrudUtil.execute("DELETE FROM Doctor WHERE doctor_id = ?", doctorId);
    }

    @Override
    public DoctorDTO findById(int doctorId) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT d.*, u.user_name, e.employee_name FROM Doctor d JOIN User u ON d.user_id = u.user_id JOIN Employee e ON d.user_id = e.user_id WHERE d.doctor_id = ?", doctorId);
        if (rs.next()) {
            return new DoctorDTO(rs.getInt("doctor_id"), rs.getInt("user_id"), rs.getString("employee_name"), rs.getString("specialization"), rs.getBoolean("is_available"));
        }
        return null;
    }

    @Override
    public List<DoctorDTO> findByName(String name) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT d.*, u.user_name, e.employee_name FROM Doctor d JOIN User u ON d.user_id = u.user_id JOIN Employee e ON d.user_id = e.user_id WHERE e.employee_name LIKE ? ORDER BY d.doctor_id DESC", "%" + name + "%");
        return mapResultSetToDoctorList(rs);
    }

    @Override
    public List<DoctorDTO> getAll() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT d.*, u.user_name, e.employee_name FROM Doctor d JOIN User u ON d.user_id = u.user_id JOIN Employee e ON d.user_id = e.user_id ORDER BY d.doctor_id DESC");
        return mapResultSetToDoctorList(rs);
    }

    @Override
    public List<DoctorDTO> getAvailableDoctors() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT d.*, u.user_name, e.employee_name FROM Doctor d JOIN User u ON d.user_id = u.user_id JOIN Employee e ON d.user_id = e.user_id WHERE d.is_available = true ORDER BY e.employee_name");
        return mapResultSetToDoctorList(rs);
    }

    @Override
    public int getNextDoctorId() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT MAX(doctor_id) as max_id FROM Doctor");
        if (rs.next()) return rs.getInt("max_id") + 1;
        return 1;
    }

    @Override
    public boolean isDoctorExists(int doctorId) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT COUNT(*) as count FROM Doctor WHERE doctor_id = ?", doctorId);
        if (rs.next()) return rs.getInt("count") > 0;
        return false;
    }

    private List<DoctorDTO> mapResultSetToDoctorList(ResultSet rs) throws SQLException {
        List<DoctorDTO> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new DoctorDTO(rs.getInt("doctor_id"), rs.getInt("user_id"), rs.getString("employee_name"), rs.getString("specialization"), rs.getBoolean("is_available")));
        }
        return list;
    }
}