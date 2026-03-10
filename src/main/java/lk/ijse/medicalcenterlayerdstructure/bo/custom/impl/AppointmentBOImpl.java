package lk.ijse.medicalcenterlayerdstructure.bo.custom.impl;

import lk.ijse.medicalcenterlayerdstructure.bo.custom.AppointmentBO;
import lk.ijse.medicalcenterlayerdstructure.dao.DAOFactory;
import lk.ijse.medicalcenterlayerdstructure.dao.custom.AppointmentDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.AppointmentDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.Appointment;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentBOImpl implements AppointmentBO {
    AppointmentDAO appointmentDAO = (AppointmentDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.APPOINTMENT);

    @Override
    public boolean saveAppointment(AppointmentDTO dto) throws SQLException {
        return appointmentDAO.save(new Appointment(0, dto.getPatientId(), dto.getDoctorId(), dto.getUserId(), dto.getAmount(), dto.getAppointmentDate(), dto.getAppointmentTime()));
    }

    @Override
    public boolean updateAppointment(AppointmentDTO dto) throws SQLException {
        return appointmentDAO.update(new Appointment(dto.getAppointmentId(), dto.getPatientId(), dto.getDoctorId(), dto.getUserId(), dto.getAmount(), dto.getAppointmentDate(), dto.getAppointmentTime()));
    }

    @Override
    public boolean deleteAppointment(int appointmentId) throws SQLException {
        return appointmentDAO.delete(appointmentId);
    }

    @Override
    public AppointmentDTO searchAppointment(int appointmentId) throws SQLException {
        return appointmentDAO.findById(appointmentId);
    }

    @Override
    public List<AppointmentDTO> getAllAppointments() throws SQLException {
        return appointmentDAO.getAll();
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByPatient(int patientId) throws SQLException {
        return appointmentDAO.getByPatient(patientId);
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByDoctor(int doctorId) throws SQLException {
        return appointmentDAO.getByDoctor(doctorId);
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByDate(LocalDate date) throws SQLException {
        return appointmentDAO.getByDate(date);
    }

    @Override
    public List<AppointmentDTO> getTodayAppointments() throws SQLException {
        return appointmentDAO.getTodayAppointments();
    }

    @Override
    public List<AppointmentDTO> getUpcomingAppointments() throws SQLException {
        return appointmentDAO.getUpcomingAppointments();
    }

    @Override
    public boolean isTimeSlotAvailable(int doctorId, LocalDate date, LocalTime time, int excludeId) throws SQLException {
        return appointmentDAO.isTimeSlotAvailable(doctorId, date, time, excludeId);
    }

    @Override
    public List<LocalTime> getAvailableTimeSlots(int doctorId, LocalDate date) throws SQLException {
        List<LocalTime> bookedSlots = appointmentDAO.getBookedTimeSlots(doctorId, date);
        List<LocalTime> available = new ArrayList<>();
        LocalTime current = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(17, 30);
        while (!current.isAfter(end)) {
            if (!bookedSlots.contains(current)) available.add(current);
            current = current.plusMinutes(30);
        }
        return available;
    }

    @Override
    public int getAppointmentCount() throws SQLException {
        return appointmentDAO.getAppointmentCount();
    }

    @Override
    public int getTodayAppointmentCount() throws SQLException {
        return appointmentDAO.getTodayAppointmentCount();
    }

    @Override
    public boolean isAppointmentExists(int appointmentId) throws SQLException {
        return appointmentDAO.isAppointmentExists(appointmentId);
    }
}

