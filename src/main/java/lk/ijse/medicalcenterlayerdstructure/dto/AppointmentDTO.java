package lk.ijse.medicalcenterlayerdstructure.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AppointmentDTO {

    private int appointmentId;
    private int patientId;
    private int doctorId;
    private int userId;
    private BigDecimal amount;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String patientName;
    private String doctorName;
    private String receptionistName;
    private String status;


    public AppointmentDTO() {
        this.appointmentDate = LocalDate.now();
        this.appointmentTime = LocalTime.of(9, 0); //default 9am......
        this.status = "Scheduled";
    }

    public AppointmentDTO(int appointmentId, int patientId, int doctorId, int userId,
                          BigDecimal amount, LocalDate appointmentDate, LocalTime appointmentTime) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.userId = userId;
        this.amount = amount;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = "Scheduled";
    }


    public int getAppointmentId() {
        return appointmentId;
    }
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getPatientId() {
        return patientId;
    }
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }
    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }
    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }


    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }
    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getPatientName() {
        return patientName;
    }
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getReceptionistName() {
        return receptionistName;
    }
    public void setReceptionistName(String receptionistName) {
        this.receptionistName = receptionistName;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }


    public String getFormattedTime() {
        if (appointmentTime != null) {
            return appointmentTime.format(DateTimeFormatter.ofPattern("hh:mm a"));
        }
        return "";
    }


    public String getFullAppointmentDateTime() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        return appointmentDate.format(dateFormatter) + " at " +
                appointmentTime.format(timeFormatter);
    }

    @Override
    public String toString() {
        return "Appointment #" + appointmentId + " - " + patientName +
                " with Dr. " + doctorName + " on " + getFullAppointmentDateTime();
    }
}
