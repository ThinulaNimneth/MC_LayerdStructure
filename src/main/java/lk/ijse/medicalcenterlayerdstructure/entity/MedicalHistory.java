package lk.ijse.medicalcenterlayerdstructure.entity;

import java.time.LocalDate;

public class MedicalHistory {
    private int historyId;
    private int patientId;
    private int doctorId;
    private String diagnosis;
    private String prescription;
    private LocalDate date;

    public MedicalHistory() {}
    public MedicalHistory(int historyId, int patientId, int doctorId, String diagnosis, String prescription, LocalDate date) {
        this.historyId = historyId; this.patientId = patientId; this.doctorId = doctorId;
        this.diagnosis = diagnosis; this.prescription = prescription; this.date = date;
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
}
