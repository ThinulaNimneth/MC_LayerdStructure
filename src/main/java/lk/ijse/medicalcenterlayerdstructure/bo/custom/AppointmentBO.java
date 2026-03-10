package lk.ijse.medicalcenterlayerdstructure.bo.custom;

import lk.ijse.medicalcenterlayerdstructure.bo.SuperBO;
import lk.ijse.medicalcenterlayerdstructure.dto.AppointmentDTO;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentBO extends SuperBO {
    boolean saveAppointment(AppointmentDTO dto) throws SQLException;
    boolean updateAppointment(AppointmentDTO dto) throws SQLException;
    boolean deleteAppointment(int appointmentId) throws SQLException;
    AppointmentDTO searchAppointment(int appointmentId) throws SQLException;
    List<AppointmentDTO> getAllAppointments() throws SQLException;
    List<AppointmentDTO> getAppointmentsByPatient(int patientId) throws SQLException;
    List<AppointmentDTO> getAppointmentsByDoctor(int doctorId) throws SQLException;
    List<AppointmentDTO> getAppointmentsByDate(LocalDate date) throws SQLException;
    List<AppointmentDTO> getTodayAppointments() throws SQLException;
    List<AppointmentDTO> getUpcomingAppointments() throws SQLException;
    boolean isTimeSlotAvailable(int doctorId, LocalDate date, LocalTime time, int excludeId) throws SQLException;
    List<LocalTime> getAvailableTimeSlots(int doctorId, LocalDate date) throws SQLException;
    int getAppointmentCount() throws SQLException;
    int getTodayAppointmentCount() throws SQLException;
    boolean isAppointmentExists(int appointmentId) throws SQLException;
}
