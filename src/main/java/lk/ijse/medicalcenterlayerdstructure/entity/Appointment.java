package lk.ijse.medicalcenterlayerdstructure.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private int appointmentId;
    private int patientId;
    private int doctorId;
    private int userId;
    private BigDecimal amount;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

    public Appointment() {}
    public Appointment(int appointmentId, int patientId, int doctorId, int userId, BigDecimal amount, LocalDate appointmentDate, LocalTime appointmentTime) {
        this.appointmentId = appointmentId; this.patientId = patientId; this.doctorId = doctorId;
        this.userId = userId; this.amount = amount; this.appointmentDate = appointmentDate; this.appointmentTime = appointmentTime;
    }
    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }
    public LocalTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }
}
