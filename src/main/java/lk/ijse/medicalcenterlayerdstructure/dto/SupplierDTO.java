package lk.ijse.medicalcenterlayerdstructure.dto;

public class SupplierDTO {
    private int supplierId;
    private String name;
    private String contacts;
    private String address;
    private String medicineNames;

    public SupplierDTO() {
    }

    public SupplierDTO(String name, String contacts, String address) {
        this.name = name;
        this.contacts = contacts;
        this.address = address;
    }

    public SupplierDTO(int supplierId, String name, String contacts, String address) {
        this.supplierId = supplierId;
        this.name = name;
        this.contacts = contacts;
        this.address = address;
    }


    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContacts() { return contacts; }
    public void setContacts(String contacts) { this.contacts = contacts; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getMedicineNames() { return medicineNames; }
    public void setMedicineNames(String medicineNames) { this.medicineNames = medicineNames; }

    @Override
    public String toString() {
        return name + " (" + contacts + ")";
    }
}