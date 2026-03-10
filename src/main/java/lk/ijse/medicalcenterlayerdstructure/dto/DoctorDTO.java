package lk.ijse.medicalcenterlayerdstructure.dto;

public class DoctorDTO {
    private int doctorId;
    private int userId;
    private String doctorName;
    private String specialization;
    private boolean isAvailable;

    public DoctorDTO() {
    }

    public DoctorDTO(int doctorId, int userId, String doctorName, String specialization, boolean isAvailable) {
        this.doctorId = doctorId;
        this.userId = userId;
        this.doctorName = doctorName;
        this.specialization = specialization;
        this.isAvailable = isAvailable;
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

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getAvailability() {
        return isAvailable ? "Available" : "Not Available";
    }

    @Override
    public String toString() {
        return doctorName + " - " + specialization;
    }
}