package lk.ijse.medicalcenterlayerdstructure.dao.custom;

import lk.ijse.medicalcenterlayerdstructure.dao.SuperDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.AppointmentDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.Appointment;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentDAO extends SuperDAO {
    boolean save(Appointment appointment) throws SQLException;
    boolean update(Appointment appointment) throws SQLException;
    boolean delete(int appointmentId) throws SQLException;
    AppointmentDTO findById(int appointmentId) throws SQLException;
    List<AppointmentDTO> getAll() throws SQLException;
    List<AppointmentDTO> getByPatient(int patientId) throws SQLException;
    List<AppointmentDTO> getByDoctor(int doctorId) throws SQLException;
    List<AppointmentDTO> getByDate(LocalDate date) throws SQLException;
    List<AppointmentDTO> getTodayAppointments() throws SQLException;
    List<AppointmentDTO> getUpcomingAppointments() throws SQLException;
    boolean isTimeSlotAvailable(int doctorId, LocalDate date, LocalTime time, int excludeId) throws SQLException;
    List<LocalTime> getBookedTimeSlots(int doctorId, LocalDate date) throws SQLException;
    int getAppointmentCount() throws SQLException;
    int getTodayAppointmentCount() throws SQLException;
    boolean isAppointmentExists(int appointmentId) throws SQLException;
}
