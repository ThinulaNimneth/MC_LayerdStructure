package lk.ijse.medicalcenterlayerdstructure.entity;

public class AppointmentMedicine {
    private int appointmentId;
    private int medicineId;
    private int quantity;

    public AppointmentMedicine() {}
    public AppointmentMedicine(int appointmentId, int medicineId, int quantity) {
        this.appointmentId = appointmentId; this.medicineId = medicineId; this.quantity = quantity;
    }
    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }
    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
