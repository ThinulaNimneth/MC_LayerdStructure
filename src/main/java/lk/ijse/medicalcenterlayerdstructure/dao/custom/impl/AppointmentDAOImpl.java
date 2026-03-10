package lk.ijse.medicalcenterlayerdstructure.dao.custom.impl;

import lk.ijse.medicalcenterlayerdstructure.dao.custom.AppointmentDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.AppointmentDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.Appointment;
import lk.ijse.medicalcenterlayerdstructure.util.CrudUtil;   // ✅ FIXED: was DAOFactory
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAOImpl implements AppointmentDAO {

    @Override
    public boolean save(Appointment a) throws SQLException {
        return CrudUtil.execute("INSERT INTO Appointment (patient_id, doctor_id, user_id, amount, appointment_date, appointment_time) VALUES (?, ?, ?, ?, ?, ?)",
                a.getPatientId(), a.getDoctorId(), a.getUserId(), a.getAmount(), a.getAppointmentDate(), a.getAppointmentTime());
    }

    @Override
    public boolean update(Appointment a) throws SQLException {
        return CrudUtil.execute("UPDATE Appointment SET patient_id = ?, doctor_id = ?, user_id = ?, amount = ?, appointment_date = ?, appointment_time = ? WHERE appointment_id = ?",
                a.getPatientId(), a.getDoctorId(), a.getUserId(), a.getAmount(), a.getAppointmentDate(), a.getAppointmentTime(), a.getAppointmentId());
    }

    @Override
    public boolean delete(int appointmentId) throws SQLException {
        return CrudUtil.execute("DELETE FROM Appointment WHERE appointment_id = ?", appointmentId);
    }

    @Override
    public AppointmentDTO findById(int appointmentId) throws SQLException {
        String sql = "SELECT a.*, p.name as patient_name, CONCAT(e.employee_name, ' - ', d.specialization) as doctor_name FROM Appointment a JOIN Patient p ON a.patient_id = p.patient_id JOIN Doctor d ON a.doctor_id = d.doctor_id JOIN Employee e ON d.user_id = e.user_id WHERE a.appointment_id = ?";
        ResultSet rs = CrudUtil.execute(sql, appointmentId);
        if (rs.next()) return mapRow(rs);
        return null;
    }

    @Override
    public List<AppointmentDTO> getAll() throws SQLException {
        String sql = "SELECT a.*, p.name as patient_name, CONCAT(e.employee_name, ' - ', d.specialization) as doctor_name FROM Appointment a JOIN Patient p ON a.patient_id = p.patient_id JOIN Doctor d ON a.doctor_id = d.doctor_id JOIN Employee e ON d.user_id = e.user_id ORDER BY a.appointment_date DESC, a.appointment_time DESC, a.appointment_id DESC";
        return mapResultSet(CrudUtil.execute(sql));
    }

    @Override
    public List<AppointmentDTO> getByPatient(int patientId) throws SQLException {
        String sql = "SELECT a.*, p.name as patient_name, CONCAT(e.employee_name, ' - ', d.specialization) as doctor_name FROM Appointment a JOIN Patient p ON a.patient_id = p.patient_id JOIN Doctor d ON a.doctor_id = d.doctor_id JOIN Employee e ON d.user_id = e.user_id WHERE a.patient_id = ? ORDER BY a.appointment_date DESC";
        return mapResultSet(CrudUtil.execute(sql, patientId));
    }

    @Override
    public List<AppointmentDTO> getByDoctor(int doctorId) throws SQLException {
        String sql = "SELECT a.*, p.name as patient_name, CONCAT(e.employee_name, ' - ', d.specialization) as doctor_name FROM Appointment a JOIN Patient p ON a.patient_id = p.patient_id JOIN Doctor d ON a.doctor_id = d.doctor_id JOIN Employee e ON d.user_id = e.user_id WHERE a.doctor_id = ? ORDER BY a.appointment_date DESC";
        return mapResultSet(CrudUtil.execute(sql, doctorId));
    }

    @Override
    public List<AppointmentDTO> getByDate(LocalDate date) throws SQLException {
        String sql = "SELECT a.*, p.name as patient_name, CONCAT(e.employee_name, ' - ', d.specialization) as doctor_name FROM Appointment a JOIN Patient p ON a.patient_id = p.patient_id JOIN Doctor d ON a.doctor_id = d.doctor_id JOIN Employee e ON d.user_id = e.user_id WHERE a.appointment_date = ? ORDER BY a.appointment_time ASC";
        return mapResultSet(CrudUtil.execute(sql, date));
    }

    @Override
    public List<AppointmentDTO> getTodayAppointments() throws SQLException {
        String sql = "SELECT a.*, p.name as patient_name, CONCAT(e.employee_name, ' - ', d.specialization) as doctor_name FROM Appointment a JOIN Patient p ON a.patient_id = p.patient_id JOIN Doctor d ON a.doctor_id = d.doctor_id JOIN Employee e ON d.user_id = e.user_id WHERE a.appointment_date = CURRENT_DATE ORDER BY a.appointment_time ASC";
        return mapResultSet(CrudUtil.execute(sql));
    }

    @Override
    public List<AppointmentDTO> getUpcomingAppointments() throws SQLException {
        String sql = "SELECT a.*, p.name as patient_name, CONCAT(e.employee_name, ' - ', d.specialization) as doctor_name FROM Appointment a JOIN Patient p ON a.patient_id = p.patient_id JOIN Doctor d ON a.doctor_id = d.doctor_id JOIN Employee e ON d.user_id = e.user_id WHERE a.appointment_date >= CURRENT_DATE ORDER BY a.appointment_date ASC, a.appointment_time ASC";
        return mapResultSet(CrudUtil.execute(sql));
    }

    @Override
    public boolean isTimeSlotAvailable(int doctorId, LocalDate date, LocalTime time, int excludeId) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT COUNT(*) as count FROM Appointment WHERE doctor_id = ? AND appointment_date = ? AND appointment_time = ? AND appointment_id != ?", doctorId, date, time, excludeId);
        if (rs.next()) return rs.getInt("count") == 0;
        return true;
    }

    @Override
    public List<LocalTime> getBookedTimeSlots(int doctorId, LocalDate date) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT appointment_time FROM Appointment WHERE doctor_id = ? AND appointment_date = ? ORDER BY appointment_time", doctorId, date);
        List<LocalTime> slots = new ArrayList<>();
        while (rs.next()) {
            Time t = rs.getTime("appointment_time");
            if (t != null) slots.add(t.toLocalTime());
        }
        return slots;
    }

    @Override
    public int getAppointmentCount() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT COUNT(*) as count FROM Appointment");
        if (rs.next()) return rs.getInt("count");
        return 0;
    }

    @Override
    public int getTodayAppointmentCount() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT COUNT(*) as count FROM Appointment WHERE appointment_date = CURRENT_DATE");
        if (rs.next()) return rs.getInt("count");
        return 0;
    }

    @Override
    public boolean isAppointmentExists(int appointmentId) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT COUNT(*) as count FROM Appointment WHERE appointment_id = ?", appointmentId);
        if (rs.next()) return rs.getInt("count") > 0;
        return false;
    }

    private AppointmentDTO mapRow(ResultSet rs) throws SQLException {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setAppointmentId(rs.getInt("appointment_id"));
        dto.setPatientId(rs.getInt("patient_id"));
        dto.setDoctorId(rs.getInt("doctor_id"));
        dto.setUserId(rs.getInt("user_id"));
        dto.setAmount(rs.getBigDecimal("amount"));
        dto.setAppointmentDate(rs.getDate("appointment_date").toLocalDate());
        Time t = rs.getTime("appointment_time");
        if (t != null) dto.setAppointmentTime(t.toLocalTime());
        dto.setPatientName(rs.getString("patient_name"));
        dto.setDoctorName(rs.getString("doctor_name"));
        return dto;
    }

    private List<AppointmentDTO> mapResultSet(ResultSet rs) throws SQLException {
        List<AppointmentDTO> list = new ArrayList<>();
        while (rs.next()) list.add(mapRow(rs));
        return list;
    }
}
