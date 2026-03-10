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
import lk.ijse.medicalcenterlayerdstructure.bo.custom.MedicalHistoryBO;
import lk.ijse.medicalcenterlayerdstructure.bo.custom.PatientBO;
import lk.ijse.medicalcenterlayerdstructure.dto.DoctorDTO;
import lk.ijse.medicalcenterlayerdstructure.dto.MedicalHistoryDTO;
import lk.ijse.medicalcenterlayerdstructure.dto.PatientDTO;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;




public class MedicalHistoryController implements Initializable {

    @FXML
    private TextField txtHistoryId;

    @FXML
    private ComboBox<PatientDTO> cmbPatient;

    @FXML
    private ComboBox<DoctorDTO> cmbDoctor;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextArea txtDiagnosis;

    @FXML
    private TextArea txtPrescription;

    @FXML
    private TextField txtSearchPatient;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnClear;

    @FXML
    private TableView<MedicalHistoryDTO> tblMedicalHistory;

    @FXML
    private TableColumn<MedicalHistoryDTO, Integer> colHistoryId;

    @FXML
    private TableColumn<MedicalHistoryDTO, String> colPatientName;

    @FXML
    private TableColumn<MedicalHistoryDTO, String> colDoctorName;

    @FXML
    private TableColumn<MedicalHistoryDTO, String> colDiagnosis;

    @FXML
    private TableColumn<MedicalHistoryDTO, String> colPrescription;

    @FXML
    private TableColumn<MedicalHistoryDTO, LocalDate> colDate;

    private final MedicalHistoryBO medicalHistoryBO = (MedicalHistoryBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.MEDICAL_HISTORY);
    private final PatientBO patientBO = (PatientBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.PATIENT);
    private final DoctorBO doctorBO = (DoctorBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.DOCTOR);
    private ObservableList<MedicalHistoryDTO> historyList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colHistoryId.setCellValueFactory(new PropertyValueFactory<>("historyId"));
        colPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colDoctorName.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        colDiagnosis.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        colPrescription.setCellValueFactory(new PropertyValueFactory<>("prescription"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        loadPatients();
        loadDoctors();

        //default day
        datePicker.setValue(LocalDate.now());

        loadAllMedicalHistories();

        //row click
        tblMedicalHistory.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                MedicalHistoryDTO selectedHistory = tblMedicalHistory.getSelectionModel().getSelectedItem();
                if (selectedHistory != null) {
                    populateFields(selectedHistory);
                }
            }
        });

        generateNextHistoryId();
    }

    @FXML
    private void handleSave() {
        if (!validateInputs()) {
            return;
        }

        try {
            PatientDTO selectedPatient = cmbPatient.getValue();
            DoctorDTO selectedDoctor = cmbDoctor.getValue();

            MedicalHistoryDTO historyDTO = new MedicalHistoryDTO(
                    selectedPatient.getPatientId(),
                    selectedDoctor.getDoctorId(),
                    txtDiagnosis.getText().trim(),
                    txtPrescription.getText().trim()
            );
            historyDTO.setDate(datePicker.getValue());

            boolean isSaved = medicalHistoryBO.saveMedicalHistory(historyDTO);

            if (isSaved) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Medical history saved successfully!\n" +
                                "Patient: " + selectedPatient.getName() + "\n" +
                                "Doctor: " + selectedDoctor.getDoctorName());
                handleClear();
                loadAllMedicalHistories();
                generateNextHistoryId();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save medical history!");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate() {
        if (txtHistoryId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a medical history to update!");
            return;
        }

        if (!validateInputs()) {
            return;
        }

        try {
            MedicalHistoryDTO historyDTO = new MedicalHistoryDTO();
            historyDTO.setHistoryId(Integer.parseInt(txtHistoryId.getText()));
            historyDTO.setDiagnosis(txtDiagnosis.getText().trim());
            historyDTO.setPrescription(txtPrescription.getText().trim());
            historyDTO.setDate(datePicker.getValue());

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Update");
            confirmAlert.setHeaderText("Update Medical History");
            confirmAlert.setContentText("Are you sure you want to update this medical history record?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean isUpdated = medicalHistoryBO.updateMedicalHistory(historyDTO);

                if (isUpdated) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Medical history updated successfully!");
                    handleClear();
                    loadAllMedicalHistories();
                    generateNextHistoryId();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update medical history!");
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete() {
        if (txtHistoryId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a medical history to delete!");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Medical History");
        confirmAlert.setContentText("Are you sure you want to delete this medical history record?\n" +
                "This action cannot be undone.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                int historyId = Integer.parseInt(txtHistoryId.getText());
                boolean isDeleted = medicalHistoryBO.deleteMedicalHistory(historyId);

                if (isDeleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Medical history deleted successfully!");
                    handleClear();
                    loadAllMedicalHistories();
                    generateNextHistoryId();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete medical history!");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleSearchByPatient() {
        String searchPatientId = txtSearchPatient.getText().trim();

        if (searchPatientId.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please enter a patient ID to search!");
            return;
        }

        try {
            int patientId = Integer.parseInt(searchPatientId);
            List<MedicalHistoryDTO> histories = medicalHistoryBO.getMedicalHistoriesByPatient(patientId);

            if (!histories.isEmpty()) {
                historyList.clear();
                historyList.addAll(histories);
                tblMedicalHistory.setItems(historyList);
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Found " + histories.size() + " medical history record(s) for patient ID: " + patientId);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Not Found",
                        "No medical history found for patient ID: " + patientId);
                loadAllMedicalHistories();
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid numeric patient ID!");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClear() {
        txtHistoryId.clear();
        cmbPatient.getSelectionModel().clearSelection();
        cmbDoctor.getSelectionModel().clearSelection();
        txtDiagnosis.clear();
        txtPrescription.clear();
        txtSearchPatient.clear();
        datePicker.setValue(LocalDate.now());
        tblMedicalHistory.getSelectionModel().clearSelection();
        generateNextHistoryId();
        loadAllMedicalHistories();
    }

    private void loadAllMedicalHistories() {
        try {
            List<MedicalHistoryDTO> histories = medicalHistoryBO.getAllMedicalHistories();
            historyList.clear();
            historyList.addAll(histories);
            tblMedicalHistory.setItems(historyList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Error loading medical histories: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadPatients() {
        try {
            List<PatientDTO> patients = patientBO.getAllPatients();
            ObservableList<PatientDTO> patientList = FXCollections.observableArrayList(patients);
            cmbPatient.setItems(patientList);


            cmbPatient.setCellFactory(param -> new ListCell<PatientDTO>() {
                @Override
                protected void updateItem(PatientDTO item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getName() + " (ID: " + item.getPatientId() + ")");
                    }
                }
            });

            cmbPatient.setButtonCell(new ListCell<PatientDTO>() {
                @Override
                protected void updateItem(PatientDTO item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getName() + " (ID: " + item.getPatientId() + ")");
                    }
                }
            });
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Error loading patients: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadDoctors() {
        try {
            List<DoctorDTO> doctors = doctorBO.getAvailableDoctors();
            ObservableList<DoctorDTO> doctorList = FXCollections.observableArrayList(doctors);
            cmbDoctor.setItems(doctorList);

            //doctor name and specialization display
            cmbDoctor.setCellFactory(param -> new ListCell<DoctorDTO>() {
                @Override
                protected void updateItem(DoctorDTO item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getDoctorName() + " - " + item.getSpecialization());
                    }
                }
            });

            cmbDoctor.setButtonCell(new ListCell<DoctorDTO>() {
                @Override
                protected void updateItem(DoctorDTO item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getDoctorName() + " - " + item.getSpecialization());
                    }
                }
            });
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Error loading doctors: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void populateFields(MedicalHistoryDTO history) {
        txtHistoryId.setText(String.valueOf(history.getHistoryId()));

        //patient combo box
        for (PatientDTO patient : cmbPatient.getItems()) {
            if (patient.getPatientId() == history.getPatientId()) {
                cmbPatient.setValue(patient);
                break;
            }
        }

        //doctor combo box
        for (DoctorDTO doctor : cmbDoctor.getItems()) {
            if (doctor.getDoctorId() == history.getDoctorId()) {
                cmbDoctor.setValue(doctor);
                break;
            }
        }

        txtDiagnosis.setText(history.getDiagnosis());
        txtPrescription.setText(history.getPrescription());
        datePicker.setValue(history.getDate());
    }

    private void generateNextHistoryId() {
        try {
            java.util.List<lk.ijse.medicalcenterlayerdstructure.dto.MedicalHistoryDTO> all = medicalHistoryBO.getAllMedicalHistories();
            int nextId = all.isEmpty() ? 1 : all.stream().mapToInt(lk.ijse.medicalcenterlayerdstructure.dto.MedicalHistoryDTO::getHistoryId).max().getAsInt() + 1;
            txtHistoryId.setText(String.valueOf(nextId));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Failed to generate history ID: " + e.getMessage());
            e.printStackTrace();
        }
    }
    //valid
    private boolean validateInputs() {

        if (cmbPatient.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please select a patient!");
            cmbPatient.requestFocus();
            return false;
        }

        if (cmbDoctor.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please select a doctor!");
            cmbDoctor.requestFocus();
            return false;
        }

        if (txtDiagnosis.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Diagnosis is required!");
            txtDiagnosis.requestFocus();
            return false;
        }

        if (txtPrescription.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Prescription is required!");
            txtPrescription.requestFocus();
            return false;
        }

        if (datePicker.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Date is required!");
            datePicker.requestFocus();
            return false;
        }

        if (datePicker.getValue().isAfter(LocalDate.now())) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Date cannot be in the future!");
            datePicker.requestFocus();
            return false;
        }

        return true;
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
