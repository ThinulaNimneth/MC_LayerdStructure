package lk.ijse.medicalcenterlayerdstructure.entity;

public class Doctor {
    private int doctorId;
    private int userId;
    private String doctorName;
    private String specialization;
    private boolean available;

    public Doctor() {}
    public Doctor(int doctorId, int userId, String doctorName, String specialization, boolean available) {
        this.doctorId = doctorId; this.userId = userId; this.doctorName = doctorName;
        this.specialization = specialization; this.available = available;
    }
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
