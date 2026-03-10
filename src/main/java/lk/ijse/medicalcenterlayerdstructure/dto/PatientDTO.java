package lk.ijse.medicalcenterlayerdstructure.dto;

public class PatientDTO {
    private int patientId;
    private String name;
    private String gender;
    private String contacts;

    public PatientDTO() {
    }

    public PatientDTO(int patientId, String name, String gender, String contacts) {
        this.patientId = patientId;
        this.name = name;
        this.gender = gender;
        this.contacts = contacts;
    }


    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {

        this.patientId = patientId;
    }

    public String getName() {

        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }


    public String getGender() {

        return gender;
    }

    public void setGender(String gender) {

        this.gender = gender;
    }

    public String getContacts() {

        return contacts;
    }
    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return name; // For ComboBox display
    }
}