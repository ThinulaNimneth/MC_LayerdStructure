package lk.ijse.medicalcenterlayerdstructure.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.medicalcenterlayerdstructure.bo.custom.BOFactory;
import lk.ijse.medicalcenterlayerdstructure.bo.custom.DoctorBO;
import lk.ijse.medicalcenterlayerdstructure.bo.custom.EmployeeBO;
import lk.ijse.medicalcenterlayerdstructure.bo.custom.UserBO;
import lk.ijse.medicalcenterlayerdstructure.dto.DoctorDTO;
import lk.ijse.medicalcenterlayerdstructure.dto.EmployeeDTO;
import lk.ijse.medicalcenterlayerdstructure.dto.UserDTO;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;



public class DoctorController implements Initializable {

    @FXML private TextField txtDoctorId;
    @FXML private TextField txtUserId;
    @FXML private TextField txtDoctorName;
    @FXML private TextField txtSpecialization;
    @FXML private ComboBox<String> cmbAvailability;
    @FXML private TextField txtSearchById;
    @FXML private TextField txtSearchByName;
    @FXML private Button btnSave;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;
    @FXML private TableView<DoctorDTO> tblDoctors;
    @FXML private TableColumn<DoctorDTO, Integer> colDoctorId;
    @FXML private TableColumn<DoctorDTO, String> colDoctorName;
    @FXML private TableColumn<DoctorDTO, String> colSpecialization;
    @FXML private TableColumn<DoctorDTO, String> colAvailability;

    private final DoctorBO doctorBO = (DoctorBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.DOCTOR);
    private final UserBO userBO = (UserBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.USER);
    private final EmployeeBO employeeBO = (EmployeeBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.EMPLOYEE);
    private ObservableList<DoctorDTO> doctorList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbAvailability.setItems(FXCollections.observableArrayList("Available", "Not Available"));
        colDoctorId.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        colDoctorName.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        colSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        colAvailability.setCellValueFactory(new PropertyValueFactory<>("availability"));
        loadAllDoctors();
        txtSearchByName.textProperty().addListener((observable, oldValue, newValue) -> searchDoctorsByName(newValue));
        tblDoctors.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                DoctorDTO selected = tblDoctors.getSelectionModel().getSelectedItem();
                if (selected != null) populateFields(selected);
            }
        });
        generateNextDoctorId();
    }

    @FXML
    private void handleSave() {
        if (!validateInputs()) return;
        try {
            String doctorName = txtDoctorName.getText().trim();
            String userName = doctorName.toLowerCase().replace(" ", "_");

            int userId = userBO.getUserIdByName(userName);
            if (userId == -1) {
                UserDTO newUser = new UserDTO(0, userName, "doctor123", "Doctor");
                boolean userCreated = userBO.saveUser(newUser);
                if (!userCreated) { showAlert(Alert.AlertType.ERROR, "Error", "Failed to create user account!"); return; }
                userId = userBO.getUserIdByName(userName);
            }
            if (userId == -1) { showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve user ID!"); return; }

            if (!employeeBO.isUserAssignedToEmployee(userId)) {
                EmployeeDTO empDTO = new EmployeeDTO();
                empDTO.setUserId(userId); empDTO.setEmployeeName(doctorName); empDTO.setJobTitle("Doctor");
                if (!employeeBO.saveEmployee(empDTO)) { showAlert(Alert.AlertType.ERROR, "Error", "Failed to create employee record!"); return; }
            }

            if (userBO.isUserRegisteredAsDoctor(userId)) { showAlert(Alert.AlertType.WARNING, "Warning", "This user is already registered as a doctor!"); return; }

            boolean isAvailable = cmbAvailability.getValue().equals("Available");
            DoctorDTO dto = new DoctorDTO(0, userId, doctorName, txtSpecialization.getText().trim(), isAvailable);
            if (doctorBO.saveDoctor(dto)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Doctor saved successfully!\nUsername: " + userName + "\nPassword: doctor123");
                handleClear(); loadAllDoctors(); generateNextDoctorId();
            } else { showAlert(Alert.AlertType.ERROR, "Error", "Failed to save doctor!"); }
        } catch (SQLException e) { showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage()); }
    }

    @FXML
    private void handleUpdate() {
        if (txtDoctorId.getText().isEmpty()) { showAlert(Alert.AlertType.WARNING, "Warning", "Please select a doctor to update!"); return; }
        if (!validateInputs()) return;
        try {
            boolean isAvailable = cmbAvailability.getValue().equals("Available");
            DoctorDTO dto = new DoctorDTO(Integer.parseInt(txtDoctorId.getText()), Integer.parseInt(txtUserId.getText()), txtDoctorName.getText().trim(), txtSpecialization.getText().trim(), isAvailable);
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Update"); confirm.setHeaderText("Update Doctor"); confirm.setContentText("Are you sure you want to update this doctor?");
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                EmployeeDTO empToUpdate = new EmployeeDTO();
                empToUpdate.setUserId(Integer.parseInt(txtUserId.getText())); empToUpdate.setEmployeeName(txtDoctorName.getText().trim()); empToUpdate.setJobTitle("Doctor");
                employeeBO.updateEmployee(empToUpdate);
                if (doctorBO.updateDoctor(dto)) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Doctor updated successfully!");
                    handleClear(); loadAllDoctors(); generateNextDoctorId();
                } else { showAlert(Alert.AlertType.ERROR, "Error", "Failed to update doctor!"); }
            }
        } catch (SQLException e) { showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage()); }
    }

    @FXML
    private void handleDelete() {
        if (txtDoctorId.getText().isEmpty()) { showAlert(Alert.AlertType.WARNING, "Warning", "Please select a doctor to delete!"); return; }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete"); confirm.setHeaderText("Delete Doctor"); confirm.setContentText("Are you sure you want to delete this doctor?\nuser and employee records remain");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (doctorBO.deleteDoctor(Integer.parseInt(txtDoctorId.getText()))) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Doctor deleted successfully!");
                    handleClear(); loadAllDoctors(); generateNextDoctorId();
                } else { showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete doctor!"); }
            } catch (SQLException e) { showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage()); }
        }
    }

    @FXML
    private void handleSearchById() {
        String searchId = txtSearchById.getText().trim();
        if (searchId.isEmpty()) { showAlert(Alert.AlertType.WARNING, "Warning", "Please enter a doctor ID to search!"); return; }
        try {
            DoctorDTO doctor = doctorBO.searchDoctor(Integer.parseInt(searchId));
            if (doctor != null) { populateFields(doctor); showAlert(Alert.AlertType.INFORMATION, "Success", "Doctor found!"); }
            else { showAlert(Alert.AlertType.INFORMATION, "Not Found", "No doctor found with ID: " + searchId); handleClear(); }
        } catch (NumberFormatException e) { showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid numeric ID!"); }
        catch (SQLException e) { showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage()); }
    }

    @FXML
    private void handleClear() {
        txtDoctorId.clear(); txtUserId.clear(); txtDoctorName.clear(); txtSpecialization.clear();
        cmbAvailability.getSelectionModel().clearSelection(); txtSearchById.clear(); txtSearchByName.clear();
        tblDoctors.getSelectionModel().clearSelection(); generateNextDoctorId();
    }

    private void loadAllDoctors() {
        try {
            List<DoctorDTO> doctors = doctorBO.getAllDoctors();
            doctorList.clear(); doctorList.addAll(doctors); tblDoctors.setItems(doctorList);
        } catch (SQLException e) { showAlert(Alert.AlertType.ERROR, "Database Error", "Error loading doctors: " + e.getMessage()); }
    }

    private void searchDoctorsByName(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) { loadAllDoctors(); return; }
        try {
            List<DoctorDTO> doctors = doctorBO.searchDoctorByName(searchTerm.trim());
            doctorList.clear(); doctorList.addAll(doctors); tblDoctors.setItems(doctorList);
        } catch (SQLException e) { showAlert(Alert.AlertType.ERROR, "Database Error", "Error searching doctors: " + e.getMessage()); }
    }

    private void populateFields(DoctorDTO doctor) {
        txtDoctorId.setText(String.valueOf(doctor.getDoctorId())); txtUserId.setText(String.valueOf(doctor.getUserId()));
        txtDoctorName.setText(doctor.getDoctorName()); txtSpecialization.setText(doctor.getSpecialization()); cmbAvailability.setValue(doctor.getAvailability());
    }

    private void generateNextDoctorId() {
        try { txtDoctorId.setText(String.valueOf(doctorBO.getNextDoctorId())); }
        catch (SQLException e) { showAlert(Alert.AlertType.ERROR, "Error", "Failed to generate doctor ID: " + e.getMessage()); }
    }

    private boolean validateInputs() {
        if (txtDoctorName.getText().trim().isEmpty()) { showAlert(Alert.AlertType.WARNING, "Validation Error", "Doctor name is required!"); txtDoctorName.requestFocus(); return false; }
        if (txtSpecialization.getText().trim().isEmpty()) { showAlert(Alert.AlertType.WARNING, "Validation Error", "Specialization is required!"); txtSpecialization.requestFocus(); return false; }
        if (cmbAvailability.getValue() == null) { showAlert(Alert.AlertType.WARNING, "Validation Error", "Availability status is required!"); cmbAvailability.requestFocus(); return false; }
        return true;
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType); alert.setTitle(title); alert.setHeaderText(null); alert.setContentText(content); alert.showAndWait();
    }
}

