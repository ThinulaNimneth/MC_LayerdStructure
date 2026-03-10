package lk.ijse.medicalcenterlayerdstructure.entity;

public class Supplier {
    private int supplierId;
    private String name;
    private String contacts;
    private String address;

    public Supplier() {}
    public Supplier(int supplierId, String name, String contacts, String address) {
        this.supplierId = supplierId; this.name = name; this.contacts = contacts; this.address = address;
    }
    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContacts() { return contacts; }
    public void setContacts(String contacts) { this.contacts = contacts; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}