package lk.ijse.medicalcenterlayerdstructure.dto;

import java.time.LocalDate;

public class MedicalHistoryDTO {
    private int historyId;
    private int patientId;
    private int doctorId;
    private String diagnosis;
    private String prescription;
    private LocalDate date;
    private String patientName;
    private String doctorName;

    public MedicalHistoryDTO() {
    }

    public MedicalHistoryDTO(int historyId, String patientName, String contacts, String doctorName, String diagnosis, String prescription, LocalDate date) {
        this.historyId = historyId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.date = date;
    }

    public MedicalHistoryDTO(int patientId, int doctorId, String diagnosis, String prescription) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.date = LocalDate.now();
    }


    public int getHistoryId() { return historyId; }
    public void setHistoryId(int historyId) { this.historyId = historyId; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getPrescription() { return prescription; }
    public void setPrescription(String prescription) { this.prescription = prescription; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
}