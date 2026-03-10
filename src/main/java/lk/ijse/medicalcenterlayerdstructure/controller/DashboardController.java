package lk.ijse.medicalcenterlayerdstructure.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import lk.ijse.medicalcenterlayerdstructure.util.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



public class DashboardController implements Initializable {

    @FXML
    private AnchorPane mainContent;

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnPatient;

    @FXML
    private Button btnAppointment;

    @FXML
    private Button btnDoctor;

    @FXML
    private Button btnEmployee;

    @FXML
    private Button btnMedicine;

    @FXML
    private Button btnSupplier;

    @FXML
    private Button btnPayment;

    @FXML
    private Button btnHistory;

    @FXML
    private Label lblWelcome;

    private SessionManager sessionManager;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sessionManager = SessionManager.getInstance();


        if (!sessionManager.isLoggedIn()) {
            System.err.println(" No user logged in!");
            return;
        }


        if (lblWelcome != null) {
            lblWelcome.setText("Welcome, " + sessionManager.getCurrentUserName() +
                    " (" + sessionManager.getCurrentUserRole() + ")");
        }


        handleDashboard(null);

        System.out.println(" Dashboard loaded for: " + sessionManager.getCurrentUserName());
        System.out.println(" Role: " + sessionManager.getCurrentUserRole());
    }


    private boolean hasAccess(String moduleName) {
        String role = sessionManager.getCurrentUserRole();

        if (role == null) {
            return false;
        }

        role = role.toLowerCase();

        switch (moduleName.toLowerCase()) {
            case "dashboard":
                return true;

            case "patient":
                return role.equals("admin") || role.equals("receptionist");

            case "appointment":
                return role.equals("admin") || role.equals("doctor") || role.equals("receptionist");

            case "doctor":
                return role.equals("admin") || role.equals("receptionist");

            case "employee":
                return role.equals("admin");

            case "payment":
                return role.equals("admin") || role.equals("receptionist");

            case "medicine":
                return role.equals("admin");

            case "supplier":
                return role.equals("admin");

            case "history":
                return role.equals("admin") || role.equals("doctor") || role.equals("receptionist");

            default:
                return false;
        }
    }

    //alert

    private void showAccessDeniedAlert(String moduleName) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Access Denied");
        alert.setHeaderText("You don't have permission to access");
        alert.setContentText("Only certain roles can access " + moduleName + " management.\n\n" +
                "Your role: " + sessionManager.getCurrentUserRole() + "\n" +
                "Required role: " + getRequiredRoleForModule(moduleName));
        alert.showAndWait();
    }


    private String getRequiredRoleForModule(String moduleName) {
        switch (moduleName.toLowerCase()) {
            case "patient":
                return "Admin or Receptionist";
            case "appointment":
                return "Admin, Doctor, or Receptionist";
            case "doctor":
                return "Admin or Receptionist";
            case "employee":
                return "Admin only";
            case "payment":
                return "Admin or Receptionist";
            case "medicine":
                return "Admin only";
            case "supplier":
                return "Admin only";
            case "history":
                return "Admin, Doctor, or Receptionist";
            default:
                return "Authorized users only";
        }
    }



    @FXML
    void handleDashboard(ActionEvent event) {
        loadContent("/lk/ijse/medicalcenterlayerdstructure/dashboard-info.fxml", "Dashboard Home");
    }

    @FXML
    private void handlePatient(ActionEvent event) {
        if (hasAccess("patient")) {
            loadContent("/lk/ijse/medicalcenterlayerdstructure/patient-form.fxml", "Patient Management");
        } else {
            showAccessDeniedAlert("Patient");
        }
    }

    @FXML
    void handleAppointment(ActionEvent event) {
        if (hasAccess("appointment")) {
            loadContent("/lk/ijse/medicalcenterlayerdstructure/appointment-form.fxml", "Appointment Management");
        } else {
            showAccessDeniedAlert("Appointment");
        }
    }

    @FXML
    void handleDoctor(ActionEvent event) {
        if (hasAccess("doctor")) {
            loadContent("/lk/ijse/medicalcenterlayerdstructure/doctor-form.fxml", "Doctor Management");
        } else {
            showAccessDeniedAlert("Doctor");
        }
    }

    @FXML
    void handleEmployee(ActionEvent event) {
        if (hasAccess("employee")) {
            loadContent("/lk/ijse/medicalcenterlayerdstructure/employee-form.fxml", "Employee Management");
        } else {
            showAccessDeniedAlert("Employee");
        }
    }

    @FXML
    void handlePayment(ActionEvent event) {
        if (hasAccess("payment")) {
            loadContent("/lk/ijse/medicalcenterlayerdstructure/payment-form.fxml", "Payment Management");
        } else {
            showAccessDeniedAlert("Payment");
        }
    }

    @FXML
    void handleMedicine(ActionEvent event) {
        if (hasAccess("medicine")) {
            loadContent("/lk/ijse/medicalcenterlayerdstructure/medicine-inventory.fxml", "Medicine Inventory");
        } else {
            showAccessDeniedAlert("Medicine");
        }
    }

    @FXML
    void handleHistory(ActionEvent event) {
        if (hasAccess("history")) {
            loadContent("/lk/ijse/medicalcenterlayerdstructure/medical-history.fxml", "Medical History");
        } else {
            showAccessDeniedAlert("Medical History");
        }
    }

    @FXML
    void handleSupplier(ActionEvent event) {
        if (hasAccess("supplier")) {
            loadContent("/lk/ijse/medicalcenterlayerdstructure/Supplier.fxml", "Supplier Management");
        } else {
            showAccessDeniedAlert("Supplier");
        }
    }


    private void loadContent(String fxmlPath, String contentName) {
        try {
            Parent content = FXMLLoader.load(getClass().getResource(fxmlPath));
            mainContent.getChildren().setAll(content);
            System.out.println(" Loaded: " + contentName + " by " + sessionManager.getCurrentUserName());
        } catch (IOException e) {
            System.err.println(" Error loading " + contentName + ": " + e.getMessage());
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load content");
            alert.setContentText("Unable to load " + contentName);
            alert.showAndWait();
        }
    }
}
