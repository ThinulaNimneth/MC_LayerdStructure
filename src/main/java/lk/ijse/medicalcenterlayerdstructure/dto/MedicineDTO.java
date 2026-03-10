package lk.ijse.medicalcenterlayerdstructure.dto;

import java.math.BigDecimal;

public class MedicineDTO {
    private int medicineId;
    private String name;
    private int quantity;
    private int currentStock;
    private BigDecimal price;
    private String supplierName;

    public MedicineDTO() {
    }

    public MedicineDTO(String name, int quantity, int currentStock, BigDecimal price) {
        this.name = name;
        this.quantity = quantity;
        this.currentStock = currentStock;
        this.price = price;
    }

    public MedicineDTO(int medicineId, String name, int quantity, int currentStock, BigDecimal price) {
        this.medicineId = medicineId;
        this.name = name;
        this.quantity = quantity;
        this.currentStock = currentStock;
        this.price = price;
    }


    public int getMedicineId() {
        return medicineId;
    }
    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCurrentStock() {
        return currentStock;
    }
    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSupplierName() {
        return supplierName;
    }
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @Override
    public String toString() {
        return name + " - Stock: " + currentStock + " - Rs" + price;
    }
}