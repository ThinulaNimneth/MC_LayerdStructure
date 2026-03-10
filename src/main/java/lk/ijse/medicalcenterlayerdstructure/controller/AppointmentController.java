package lk.ijse.medicalcenterlayerdstructure.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.medicalcenterlayerdstructure.bo.custom.AppointmentBO;
import lk.ijse.medicalcenterlayerdstructure.bo.custom.BOFactory;
import lk.ijse.medicalcenterlayerdstructure.bo.custom.DoctorBO;
import lk.ijse.medicalcenterlayerdstructure.bo.custom.PatientBO;
import lk.ijse.medicalcenterlayerdstructure.dto.AppointmentDTO;
import lk.ijse.medicalcenterlayerdstructure.dto.DoctorDTO;
import lk.ijse.medicalcenterlayerdstructure.dto.PatientDTO;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AppointmentController implements Initializable {

    @FXML
    private TextField txtAppointmentId;

    @FXML
    private TextField txtSearchPatient;

    @FXML
    private TextField txtPatientId;

    @FXML
    private TextField txtPatientName;

    @FXML
    private ComboBox<DoctorDTO> cmbDoctor;

    @FXML
    private DatePicker dateAppointment;

    @FXML
    private ComboBox<String> cmbTimeSlot;

    @FXML
    private TextField txtAmount;

    @FXML
    private TextField txtSearchById;

    @FXML
    private Label lblAvailableSlots;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnSearchPatient;

    @FXML
    private Button btnRefreshSlots;

    @FXML
    private TableView<AppointmentDTO> tblAppointments;

    @FXML
    private TableColumn<AppointmentDTO, Integer> colAppointmentId;

    @FXML
    private TableColumn<AppointmentDTO, String> colPatientName;

    @FXML
    private TableColumn<AppointmentDTO, String> colDoctorName;

    @FXML
    private TableColumn<AppointmentDTO, BigDecimal> colAmount;

    @FXML
    private TableColumn<AppointmentDTO, LocalDate> colDate;

    @FXML
    private TableColumn<AppointmentDTO, String> colTime;

    private final AppointmentBO appointmentBO = (
            AppointmentBO)
            BOFactory.getInstance().getBO(BOFactory.BOTypes.APPOINTMENT);


    private final lk.ijse.medicalcenterlayerdstructure.bo.custom.PatientBO patientBO = (
            PatientBO)
            BOFactory.getInstance().getBO(BOFactory.BOTypes.PATIENT);

    private final DoctorBO doctorBO = (
            DoctorBO)
            BOFactory.getInstance().getBO(BOFactory.BOTypes.DOCTOR);

    private ObservableList<AppointmentDTO> appointmentList = FXCollections.observableArrayList();

    private int currentPatientId = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colAppointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        colPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colDoctorName.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));


        colTime.setCellValueFactory(new PropertyValueFactory<>("formattedTime"));


        loadDoctors();

        //default day
        dateAppointment.setValue(LocalDate.now());


        initializeTimeSlots();


        loadAllAppointments();


        tblAppointments.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                AppointmentDTO selectedAppointment = tblAppointments.getSelectionModel().getSelectedItem();
                if (selectedAppointment != null) {
                    populateFields(selectedAppointment);
                }
            }
        });


        cmbDoctor.setOnAction(event -> refreshAvailableTimeSlots());

        dateAppointment.setOnAction(event -> refreshAvailableTimeSlots());

        addDecimalValidation(txtAmount);

        generateNextAppointmentId();
    }


    private void initializeTimeSlots() {
        ObservableList<String> timeSlots = FXCollections.observableArrayList();


        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(17, 30);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

        LocalTime currentTime = startTime;
        while (!currentTime.isAfter(endTime)) {
            timeSlots.add(currentTime.format(formatter));
            currentTime = currentTime.plusMinutes(30);
        }

        cmbTimeSlot.setItems(timeSlots);
    }


    @FXML
    private void refreshAvailableTimeSlots() {
        DoctorDTO selectedDoctor = cmbDoctor.getValue();
        LocalDate selectedDate = dateAppointment.getValue();

        if (selectedDoctor == null || selectedDate == null) {
            lblAvailableSlots.setText("Select doctor and date");
            lblAvailableSlots.setStyle("-fx-text-fill: #666;");
            return;
        }

        try {
            List<LocalTime> availableSlots = appointmentBO.getAvailableTimeSlots(
                    selectedDoctor.getDoctorId(),
                    selectedDate
            );


            lblAvailableSlots.setText(availableSlots.size() + " slots available");

            if (availableSlots.size() < 5) {
                lblAvailableSlots.setStyle("-fx-text-fill: #FF9800; -fx-font-weight: bold;");
            } else {
                lblAvailableSlots.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
            }

            updateTimeSlotAvailability(availableSlots);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database issue",
                    "Error loading available slots: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void updateTimeSlotAvailability(List<LocalTime> availableSlots) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

        cmbTimeSlot.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);

                    LocalTime time = LocalTime.parse(item, formatter);

                    if (availableSlots.contains(time)) {
                        setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #F44336; -fx-text-decoration: line-through;");
                    }
                }
            }
        });
    }

    private LocalTime parseTimeString(String timeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        return LocalTime.parse(timeString, formatter);
    }

    @FXML
    private void handleSearchPatient() {
        String searchText = txtSearchPatient.getText().trim();

        if (searchText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please enter Patient ID or Contact Number!");
            txtSearchPatient.requestFocus();
            return;
        }

        try {
            PatientDTO patient = null;

            try {
                int patientId = Integer.parseInt(searchText);
                patient = patientBO.searchPatient(patientId);
            } catch (NumberFormatException e) {
                List<PatientDTO> patients = patientBO.searchPatientByContact(searchText);
                if (!patients.isEmpty()) {
                    patient = patients.get(0);
                }
            }

            if (patient != null) {
                currentPatientId = patient.getPatientId();
                txtPatientId.setText(String.valueOf(patient.getPatientId()));
                txtPatientName.setText(patient.getName());

                showAlert(Alert.AlertType.INFORMATION, "Patient Found",
                        "Patient found!\nName: " + patient.getName() +
                                "\nID: " + patient.getPatientId());

                cmbDoctor.requestFocus();
            } else {
                showAlert(Alert.AlertType.ERROR, "Not Found",
                        "No patient found with ID/Contact: " + searchText +
                                "\n\nPlease register the patient first in Patient Management.");
                clearPatientFields();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Error searching patient: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void handleSave() {
        if (!validateInputs()) {
            return;
        }

        try {
            DoctorDTO selectedDoctor = cmbDoctor.getValue();
            LocalTime selectedTime = parseTimeString(cmbTimeSlot.getValue());


            if (!appointmentBO.isTimeSlotAvailable(
                    selectedDoctor.getDoctorId(),
                    dateAppointment.getValue(),
                    selectedTime,
                    0)) {
                showAlert(Alert.AlertType.WARNING, "Time Slot Unavailable",
                        "This time slot is already booked for Dr. " + selectedDoctor.getDoctorName() +
                                "\n\nPlease select a different time.");
                return;
            }

            AppointmentDTO appointmentDTO = new AppointmentDTO(
                    0,
                    currentPatientId,
                    selectedDoctor.getDoctorId(),
                    1,
                    new BigDecimal(txtAmount.getText().trim()),
                    dateAppointment.getValue(),
                    selectedTime
            );

            boolean isSaved = appointmentBO.saveAppointment(appointmentDTO);

            if (isSaved) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Appointment saved successfully!\n\n" +
                                "Patient: " + txtPatientName.getText() + "\n" +
                                "Doctor: " + selectedDoctor.getDoctorName() + "\n" +
                                "Date: " + dateAppointment.getValue() + "\n" +
                                "Time: " + cmbTimeSlot.getValue() + "\n" +
                                "Amount: Rs. " + txtAmount.getText());
                handleClear();
                loadAllAppointments();
                generateNextAppointmentId();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save appointment!");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate() {
        if (txtAppointmentId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select an appointment to update!");
            return;
        }

        if (!validateInputs()) {
            return;
        }

        try {
            DoctorDTO selectedDoctor = cmbDoctor.getValue();
            LocalTime selectedTime = parseTimeString(cmbTimeSlot.getValue());
            int appointmentId = Integer.parseInt(txtAppointmentId.getText());


            if (!appointmentBO.isTimeSlotAvailable(
                    selectedDoctor.getDoctorId(),
                    dateAppointment.getValue(),
                    selectedTime,
                    appointmentId)) {
                showAlert(Alert.AlertType.WARNING, "Time Slot Unavailable",
                        "This time slot is already booked for Dr. " + selectedDoctor.getDoctorName() +
                                "\n\nPlease select a different time.");
                return;
            }

            AppointmentDTO appointmentDTO = new AppointmentDTO(
                    appointmentId,
                    currentPatientId,
                    selectedDoctor.getDoctorId(),
                    1,
                    new BigDecimal(txtAmount.getText().trim()),
                    dateAppointment.getValue(),
                    selectedTime
            );

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Update");
            confirmAlert.setHeaderText("Update Appointment");
            confirmAlert.setContentText("Are you sure you want to update this appointment?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean isUpdated = appointmentBO.updateAppointment(appointmentDTO);

                if (isUpdated) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment updated successfully!");
                    handleClear();
                    loadAllAppointments();
                    generateNextAppointmentId();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update appointment!");
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete() {
        if (txtAppointmentId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select an appointment to delete!");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Appointment");
        confirmAlert.setContentText("Are you sure you want to delete this appointment?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                int appointmentId = Integer.parseInt(txtAppointmentId.getText());
                boolean isDeleted = appointmentBO.deleteAppointment(appointmentId);

                if (isDeleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment deleted successfully!");
                    handleClear();
                    loadAllAppointments();
                    generateNextAppointmentId();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete appointment!");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleSearchById() {
        String searchId = txtSearchById.getText().trim();

        if (searchId.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please enter an appointment ID to search!");
            return;
        }

        try {
            int appointmentId = Integer.parseInt(searchId);
            AppointmentDTO appointment = appointmentBO.searchAppointment(appointmentId);

            if (appointment != null) {
                populateFields(appointment);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment found!");
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Not Found",
                        "No appointment found with ID: " + appointmentId);
                handleClear();
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid numeric ID!");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClear() {
        txtAppointmentId.clear();
        clearPatientFields();
        cmbDoctor.getSelectionModel().clearSelection();
        cmbTimeSlot.getSelectionModel().clearSelection();
        txtAmount.clear();
        dateAppointment.setValue(LocalDate.now());
        txtSearchById.clear();
        currentPatientId = 0;
        tblAppointments.getSelectionModel().clearSelection();
        lblAvailableSlots.setText("Select doctor and date");
        lblAvailableSlots.setStyle("-fx-text-fill: #666;");
        generateNextAppointmentId();
        txtSearchPatient.requestFocus();
    }

    private void clearPatientFields() {
        txtSearchPatient.clear();
        txtPatientId.clear();
        txtPatientName.clear();
    }

    private void loadAllAppointments() {
        try {
            List<AppointmentDTO> appointments = appointmentBO.getAllAppointments();
            appointmentList.clear();
            appointmentList.addAll(appointments);
            tblAppointments.setItems(appointmentList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Error loading appointments: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadDoctors() {
        try {
            List<DoctorDTO> doctors = doctorBO.getAvailableDoctors();
            ObservableList<DoctorDTO> doctorList = FXCollections.observableArrayList(doctors);
            cmbDoctor.setItems(doctorList);

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

    private void populateFields(AppointmentDTO appointment) {
        try {
            txtAppointmentId.setText(String.valueOf(appointment.getAppointmentId()));

            PatientDTO patient = patientBO.searchPatient(appointment.getPatientId());
            if (patient != null) {
                currentPatientId = patient.getPatientId();
                txtPatientId.setText(String.valueOf(patient.getPatientId()));
                txtPatientName.setText(patient.getName());
                txtSearchPatient.setText(patient.getContacts());
            }

            for (DoctorDTO doctor : cmbDoctor.getItems()) {
                if (doctor.getDoctorId() == appointment.getDoctorId()) {
                    cmbDoctor.setValue(doctor);
                    break;
                }
            }

            txtAmount.setText(appointment.getAmount().toString());
            dateAppointment.setValue(appointment.getAppointmentDate());

            // time set
            if (appointment.getAppointmentTime() != null) {
                cmbTimeSlot.setValue(appointment.getFormattedTime());
            }

            refreshAvailableTimeSlots();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Error loading appointment details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void generateNextAppointmentId() {
        try {
            int count = appointmentBO.getAppointmentCount();
            txtAppointmentId.setText(String.valueOf(count + 1));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to generate appointment ID: " + e.getMessage());
        }
    }

    private void addDecimalValidation(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                textField.setText(oldValue);
            }
        });
    }

    private boolean validateInputs() {
        if (currentPatientId == 0 || txtPatientName.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please search and select a patient first!");
            txtSearchPatient.requestFocus();
            return false;
        }

        if (cmbDoctor.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, " Error", "Please select a doctor!");
            cmbDoctor.requestFocus();
            return false;
        }


        if (cmbTimeSlot.getValue() == null || cmbTimeSlot.getValue().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, " Error", "Please select an appointment time!");
            cmbTimeSlot.requestFocus();
            return false;
        }

        if (txtAmount.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, " Err", "Please enter appointment amount!");
            txtAmount.requestFocus();
            return false;
        }

        try {
            BigDecimal amount = new BigDecimal(txtAmount.getText().trim());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                showAlert(Alert.AlertType.WARNING, " Error",
                        "Amount must be greater than zero!");
                txtAmount.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Error", "Invalid amount format!");
            txtAmount.requestFocus();
            return false;
        }

        if (dateAppointment.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, " Error", "Please select an appointment date!");
            dateAppointment.requestFocus();
            return false;
        }

        if (dateAppointment.getValue().isBefore(LocalDate.now())) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Past Date Warning");
            confirmAlert.setHeaderText("Appointment Date is in the Past");
            confirmAlert.setContentText("The selected date is in the past. Do you want to continue?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            return result.isPresent() && result.get() == ButtonType.OK;
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
