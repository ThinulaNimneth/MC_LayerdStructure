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
import lk.ijse.medicalcenterlayerdstructure.bo.custom.PaymentBO;
import lk.ijse.medicalcenterlayerdstructure.dto.PaymentDTO;
import lk.ijse.medicalcenterlayerdstructure.dto.PatientDTO;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;



public class PaymentController implements Initializable {

    @FXML
    private TextField txtSearchContact;

    @FXML
    private TextField txtPatientId;

    @FXML
    private TextField txtPatientName;

    @FXML
    private TextField txtAppointmentId;

    @FXML
    private TextField txtConsultationFee;

    @FXML
    private TextField txtMedicineCharges;

    @FXML
    private TextField txtTotalAmount;

    @FXML
    private TableView<PaymentDTO> tblPayment;

    @FXML
    private TableColumn<PaymentDTO, Integer> colPaymentId;

    @FXML
    private TableColumn<PaymentDTO, Integer> colPatientId;

    @FXML
    private TableColumn<PaymentDTO, String> colPatientName;

    @FXML
    private TableColumn<PaymentDTO, Integer> colAppointmentId;

    @FXML
    private TableColumn<PaymentDTO, BigDecimal> colConsultationFee;

    @FXML
    private TableColumn<PaymentDTO, BigDecimal> colMedicineCharges;

    @FXML
    private TableColumn<PaymentDTO, BigDecimal> colTotalAmount;

    @FXML
    private TableColumn<PaymentDTO, LocalDate> colPaymentDate;

    private final PaymentBO paymentBO = (PaymentBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.PAYMENT);
    private final PatientBO patientBO = (PatientBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.PATIENT);
    private ObservableList<PaymentDTO> paymentList = FXCollections.observableArrayList();

    private int currentPatientId = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colPatientId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colAppointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        colConsultationFee.setCellValueFactory(new PropertyValueFactory<>("consultationFee"));
        colMedicineCharges.setCellValueFactory(new PropertyValueFactory<>("medicineCharges"));
        colTotalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colPaymentDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));

        loadAllPayments();

        // auto-calculation
        txtConsultationFee.textProperty().addListener((observable, oldValue, newValue) -> {
            calculateTotal();
        });

        txtMedicineCharges.textProperty().addListener((observable, oldValue, newValue) -> {
            calculateTotal();
        });

        //row click
        tblPayment.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                PaymentDTO selectedPayment = tblPayment.getSelectionModel().getSelectedItem();
                if (selectedPayment != null) {
                    populateFields(selectedPayment);
                }
            }
        });

        //decimal validation
        addDecimalValidation(txtConsultationFee);
        addDecimalValidation(txtMedicineCharges);

        //phone number validation
        addPhoneValidation(txtSearchContact);
    }

    @FXML
    private void btnSearchPatientOnAction() {
        String contactNumber = txtSearchContact.getText().trim();

        if (contactNumber.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please enter a contact number!");
            txtSearchContact.requestFocus();
            return;
        }

        try {
            // Sear contacts
            PatientDTO patient = searchPatientByContact(contactNumber);

            if (patient != null) {
                // Auto fil
                currentPatientId = patient.getPatientId();
                txtPatientId.setText(String.valueOf(patient.getPatientId()));
                txtPatientName.setText(patient.getName());


                int latestAppointmentId = getLatestAppointmentForPatient(patient.getPatientId());

                if (latestAppointmentId > 0) {
                    txtAppointmentId.setText(String.valueOf(latestAppointmentId));

                    if (paymentBO.isPaymentExistsForAppointment(latestAppointmentId)) {
                        showAlert(Alert.AlertType.WARNING, "Payment Exists",
                                "Payment already created for this appointment!\n" +
                                        "Please enter fees manually for a new bill.");
                        txtAppointmentId.clear();
                    }
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "No Appointment",
                            "No appointment found for this patient.\n" +
                                    "You can still create a bill by entering fees manually.");
                    txtAppointmentId.setText("N/A");
                }

                // Focus on consultation fee field
                txtConsultationFee.requestFocus();

                showAlert(Alert.AlertType.INFORMATION, "Patient Found",
                        "Patient found!\nName: " + patient.getName() +
                                "\nID: " + patient.getPatientId());

            } else {
                showAlert(Alert.AlertType.ERROR, "Not Found",
                        "No patient found with contact number: " + contactNumber +
                                "\n\nPlease register the patient first in Patient Management.");
                btnClearOnAction();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Error searching patient: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void btnCreateBillOnAction() {
        if (!validateInputs()) {
            return;
        }

        try {
            int appointmentId = 0;

            if (!txtAppointmentId.getText().equals("N/A") &&
                    !txtAppointmentId.getText().isEmpty()) {
                try {
                    appointmentId = Integer.parseInt(txtAppointmentId.getText().trim());

                    // Verify appointment
                    if (!verifyAppointmentExists(appointmentId)) {
                        showAlert(Alert.AlertType.ERROR, "Invalid Appointment",
                                "Appointment ID does not exist!");
                        return;
                    }

                    // Check payment already have
                    if (paymentBO.isPaymentExistsForAppointment(appointmentId)) {
                        showAlert(Alert.AlertType.WARNING, "Duplicate Payment",
                                "Payment already exists for this appointment!");
                        return;
                    }
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input",
                            "Invalid appointment ID format!");
                    return;
                }
            } else {

                appointmentId = createWalkInAppointment(currentPatientId);
                if (appointmentId <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Error",
                            "Failed to create payment record!");
                    return;
                }
            }

            PaymentDTO paymentDTO = new PaymentDTO(
                    0,
                    currentPatientId,
                    appointmentId,
                    new BigDecimal(txtConsultationFee.getText().trim()),
                    new BigDecimal(txtMedicineCharges.getText().trim()),
                    new BigDecimal(txtTotalAmount.getText().trim()),
                    LocalDate.now(),
                    LocalTime.now(),
                    "Paid"
            );

            boolean isSaved = paymentBO.savePayment(paymentDTO);

            if (isSaved) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Bill created successfully!\n\n" +
                                "Patient: " + txtPatientName.getText() + "\n" +
                                "Consultation Fee: Rs. " + txtConsultationFee.getText() + "\n" +
                                "Medicine Charges: Rs. " + txtMedicineCharges.getText() + "\n" +
                                "Total Amount: Rs. " + txtTotalAmount.getText() + "\n\n" +
                                "Payment Status: Paid");
                btnClearOnAction();
                loadAllPayments();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to create bill!");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Error: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error",
                    "Invalid number format!");
        }
    }

    @FXML
    private void btnClearOnAction() {
        txtSearchContact.clear();
        txtPatientId.clear();
        txtPatientName.clear();
        txtAppointmentId.clear();
        txtConsultationFee.clear();
        txtMedicineCharges.clear();
        txtTotalAmount.clear();
        currentPatientId = 0;
        tblPayment.getSelectionModel().clearSelection();
        txtSearchContact.requestFocus();
    }

    private PatientDTO searchPatientByContact(String contact) throws SQLException {
        java.util.List<lk.ijse.medicalcenterlayerdstructure.dto.PatientDTO> results = patientBO.searchPatientByContact(contact);
        return results.isEmpty() ? null : results.get(0);
    }

    private int getLatestAppointmentForPatient(int patientId) throws SQLException {
        lk.ijse.medicalcenterlayerdstructure.bo.custom.AppointmentBO apptBO = (lk.ijse.medicalcenterlayerdstructure.bo.custom.AppointmentBO) lk.ijse.medicalcenterlayerdstructure.bo.custom.BOFactory.getInstance().getBO(lk.ijse.medicalcenterlayerdstructure.bo.custom.BOFactory.BOTypes.APPOINTMENT);
        java.util.List<lk.ijse.medicalcenterlayerdstructure.dto.AppointmentDTO> apts = apptBO.getAppointmentsByPatient(patientId);
        return apts.isEmpty() ? 0 : apts.get(0).getAppointmentId();
    }

    private boolean verifyAppointmentExists(int appointmentId) throws SQLException {
        lk.ijse.medicalcenterlayerdstructure.bo.custom.AppointmentBO apptBO = (lk.ijse.medicalcenterlayerdstructure.bo.custom.AppointmentBO) lk.ijse.medicalcenterlayerdstructure.bo.custom.BOFactory.getInstance().getBO(lk.ijse.medicalcenterlayerdstructure.bo.custom.BOFactory.BOTypes.APPOINTMENT);
        return apptBO.isAppointmentExists(appointmentId);
    }

    private int createWalkInAppointment(int patientId) throws SQLException {
        lk.ijse.medicalcenterlayerdstructure.bo.custom.AppointmentBO apptBO = (lk.ijse.medicalcenterlayerdstructure.bo.custom.AppointmentBO) lk.ijse.medicalcenterlayerdstructure.bo.custom.BOFactory.getInstance().getBO(lk.ijse.medicalcenterlayerdstructure.bo.custom.BOFactory.BOTypes.APPOINTMENT);
        lk.ijse.medicalcenterlayerdstructure.dto.AppointmentDTO dto = new lk.ijse.medicalcenterlayerdstructure.dto.AppointmentDTO();
        dto.setPatientId(patientId);
        dto.setDoctorId(1);
        dto.setUserId(1);
        dto.setAmount(java.math.BigDecimal.ZERO);
        dto.setAppointmentDate(java.time.LocalDate.now());
        apptBO.saveAppointment(dto);
        java.util.List<lk.ijse.medicalcenterlayerdstructure.dto.AppointmentDTO> apts = apptBO.getAppointmentsByPatient(patientId);
        return apts.isEmpty() ? 0 : apts.get(0).getAppointmentId();
    }

    private void loadAllPayments() {
        try {
            List<PaymentDTO> payments = paymentBO.getAllPayments();
            paymentList.clear();
            paymentList.addAll(payments);
            tblPayment.setItems(paymentList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Error loading payments: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void calculateTotal() {
        try {
            String consultationFeeText = txtConsultationFee.getText().trim();
            String medicineChargesText = txtMedicineCharges.getText().trim();

            BigDecimal consultationFee = BigDecimal.ZERO;
            BigDecimal medicineCharges = BigDecimal.ZERO;

            if (!consultationFeeText.isEmpty()) {
                consultationFee = new BigDecimal(consultationFeeText);
            }

            if (!medicineChargesText.isEmpty()) {
                medicineCharges = new BigDecimal(medicineChargesText);
            }

            BigDecimal total = consultationFee.add(medicineCharges);
            txtTotalAmount.setText(total.toString());

        } catch (NumberFormatException e) {
            txtTotalAmount.clear();
        }
    }

    private void populateFields(PaymentDTO payment) {
        try {
            //patient details get
            PatientDTO patient = patientBO.searchPatient(payment.getPatientId());
            if (patient != null) {
                currentPatientId = patient.getPatientId();
                txtSearchContact.setText(patient.getContacts());
                txtPatientId.setText(String.valueOf(patient.getPatientId()));
                txtPatientName.setText(patient.getName());
            }

            txtAppointmentId.setText(String.valueOf(payment.getAppointmentId()));
            txtConsultationFee.setText(payment.getConsultationFee().toString());
            txtMedicineCharges.setText(payment.getMedicineCharges().toString());
            txtTotalAmount.setText(payment.getTotalAmount().toString());

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Error loading payment details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addDecimalValidation(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                textField.setText(oldValue);
            }
        });
    }

    private void addPhoneValidation(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9+\\-\\s]*")) {
                textField.setText(oldValue);
            }
        });
    }

    private boolean validateInputs() {

        if (currentPatientId == 0 || txtPatientName.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please search and select a patient first!");
            txtSearchContact.requestFocus();
            return false;
        }

        if (txtConsultationFee.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please enter consultation fee!");
            txtConsultationFee.requestFocus();
            return false;
        }

        try {
            BigDecimal consultationFee = new BigDecimal(txtConsultationFee.getText().trim());
            if (consultationFee.compareTo(BigDecimal.ZERO) < 0) {
                showAlert(Alert.AlertType.WARNING, "Validation Error",
                        "Consultation fee cannot be negative!");
                txtConsultationFee.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Invalid consultation fee format!");
            txtConsultationFee.requestFocus();
            return false;
        }

        if (txtMedicineCharges.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please enter medicine charges (or 0 if no medicines)!");
            txtMedicineCharges.requestFocus();
            return false;
        }

        try {
            BigDecimal medicineCharges = new BigDecimal(txtMedicineCharges.getText().trim());
            if (medicineCharges.compareTo(BigDecimal.ZERO) < 0) {
                showAlert(Alert.AlertType.WARNING, "Validation Error",
                        "Medicine charges cannot be negative!");
                txtMedicineCharges.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Invalid medicine charges format!");
            txtMedicineCharges.requestFocus();
            return false;
        }

        if (txtTotalAmount.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Total amount is required!");
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

