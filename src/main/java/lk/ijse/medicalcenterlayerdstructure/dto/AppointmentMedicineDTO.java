package lk.ijse.medicalcenterlayerdstructure.dto;

import java.math.BigDecimal;

public class AppointmentMedicineDTO {
    private int appointmentId;
    private int medicineId;
    private String medicineName;
    private BigDecimal price;
    private int quantity;

    public AppointmentMedicineDTO() {
    }

    public AppointmentMedicineDTO(int appointmentId, int medicineId, int quantity) {
        this.appointmentId = appointmentId;
        this.medicineId = medicineId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }

    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return medicineName + " x" + quantity + " = $" + price.multiply(BigDecimal.valueOf(quantity));
    }
}