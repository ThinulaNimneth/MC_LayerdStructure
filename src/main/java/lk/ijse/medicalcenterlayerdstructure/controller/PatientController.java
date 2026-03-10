package lk.ijse.medicalcenterlayerdstructure.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.medicalcenterlayerdstructure.bo.custom.BOFactory;
import lk.ijse.medicalcenterlayerdstructure.bo.custom.PatientBO;
import lk.ijse.medicalcenterlayerdstructure.dto.PatientDTO;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class PatientController implements Initializable {

    @FXML private TextField txtPatientId;
    @FXML private TextField txtPatientName;
    @FXML private ComboBox<String> cmbGender;
    @FXML private TextField txtContacts;
    @FXML private TextField txtSearchById;
    @FXML private TextField txtSearchByContact;
    @FXML private Button btnSave;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;
    @FXML private Button btnSearchById;
    @FXML private TableView<PatientDTO> tblPatients;
    @FXML private TableColumn<PatientDTO, Integer> colPatientId;
    @FXML private TableColumn<PatientDTO, String> colName;
    @FXML private TableColumn<PatientDTO, String> colGender;
    @FXML private TableColumn<PatientDTO, String> colContacts;

    private final PatientBO patientBO = (PatientBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.PATIENT);
    private ObservableList<PatientDTO> patientList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbGender.setItems(FXCollections.observableArrayList("Male", "Female", ""));
        colPatientId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colContacts.setCellValueFactory(new PropertyValueFactory<>("contacts"));
        loadAllPatients();
        txtSearchByContact.textProperty().addListener((observable, oldValue, newValue) -> searchPatientsByContact(newValue));
        tblPatients.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                PatientDTO selected = tblPatients.getSelectionModel().getSelectedItem();
                if (selected != null) populateFields(selected);
            }
        });
    }

    @FXML
    private void handleSave() {
        if (!validateInputs()) return;
        try {
            PatientDTO dto = new PatientDTO(0, txtPatientName.getText().trim(), cmbGender.getValue(), txtContacts.getText().trim());
            if (patientBO.savePatient(dto)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Patient saved successfully.!");
                handleClear(); loadAllPatients();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save patient!");
            }
        } catch (SQLException e) { showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage()); }
    }

    @FXML
    private void handleUpdate() {
        if (txtPatientId.getText().isEmpty()) { showAlert(Alert.AlertType.WARNING, "Warning", "Please select a patient to update!"); return; }
        if (!validateInputs()) return;
        try {
            PatientDTO dto = new PatientDTO(Integer.parseInt(txtPatientId.getText()), txtPatientName.getText().trim(), cmbGender.getValue(), txtContacts.getText().trim());
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Update"); confirm.setHeaderText("Update Patient"); confirm.setContentText("Are you sure you want to update this patient?");
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (patientBO.updatePatient(dto)) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Patient updated successfully!");
                    handleClear(); loadAllPatients();
                } else { showAlert(Alert.AlertType.ERROR, "Error", "Failed to update patient!"); }
            }
        } catch (SQLException e) { showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage()); }
    }

    @FXML
    private void handleDelete() {
        if (txtPatientId.getText().isEmpty()) { showAlert(Alert.AlertType.WARNING, "Warning", "Please select a patient to delete!"); return; }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete"); confirm.setHeaderText("Delete Patient"); confirm.setContentText("Are you sure you want to delete this patient? This action cannot be undone.");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (patientBO.deletePatient(Integer.parseInt(txtPatientId.getText()))) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Patient deleted successfully!");
                    handleClear(); loadAllPatients();
                } else { showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete patient!"); }
            } catch (SQLException e) { showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage()); }
        }
    }

    @FXML
    private void handleSearchById() {
        String searchId = txtSearchById.getText().trim();
        if (searchId.isEmpty()) { showAlert(Alert.AlertType.WARNING, "Warning", "Please enter a patient ID to search!"); return; }
        try {
            int id = Integer.parseInt(searchId);
            PatientDTO patient = patientBO.searchPatient(id);
            if (patient != null) { populateFields(patient); showAlert(Alert.AlertType.INFORMATION, "Success", "Patient found!"); }
            else { showAlert(Alert.AlertType.INFORMATION, "Not Found", "No patient found with ID: " + id); handleClear(); }
        } catch (NumberFormatException e) { showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid numeric ID!"); }
        catch (SQLException e) { showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage()); }
    }

    @FXML
    private void handleClear() {
        txtPatientId.clear(); txtPatientName.clear(); cmbGender.getSelectionModel().clearSelection();
        txtContacts.clear(); txtSearchById.clear(); txtSearchByContact.clear();
        tblPatients.getSelectionModel().clearSelection();
    }

    private void loadAllPatients() {
        try {
            List<PatientDTO> patients = patientBO.getAllPatients();
            patientList.clear(); patientList.addAll(patients); tblPatients.setItems(patientList);
        } catch (SQLException e) { showAlert(Alert.AlertType.ERROR, "Database Error", "Error loading patients: " + e.getMessage()); }
    }

    private void searchPatientsByContact(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) { loadAllPatients(); return; }
        try {
            List<PatientDTO> patients = patientBO.searchPatientByContact(searchTerm.trim());
            patientList.clear(); patientList.addAll(patients); tblPatients.setItems(patientList);
        } catch (SQLException e) { showAlert(Alert.AlertType.ERROR, "Database Error", "Error searching patients: " + e.getMessage()); }
    }

    private void populateFields(PatientDTO patient) {
        txtPatientId.setText(String.valueOf(patient.getPatientId()));
        txtPatientName.setText(patient.getName()); cmbGender.setValue(patient.getGender()); txtContacts.setText(patient.getContacts());
    }

    private boolean validateInputs() {
        if (txtPatientName.getText().trim().isEmpty()) { showAlert(Alert.AlertType.WARNING, "Validation Error", "Patient name is required!"); txtPatientName.requestFocus(); return false; }
        if (cmbGender.getValue() == null) { showAlert(Alert.AlertType.WARNING, "Validation Error", "Gender is required!"); cmbGender.requestFocus(); return false; }
        if (txtContacts.getText().trim().isEmpty()) { showAlert(Alert.AlertType.WARNING, "Validation Error", "Contact number is required!"); txtContacts.requestFocus(); return false; }
        return true;
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType); alert.setTitle(title); alert.setHeaderText(null); alert.setContentText(content); alert.showAndWait();
    }
}

