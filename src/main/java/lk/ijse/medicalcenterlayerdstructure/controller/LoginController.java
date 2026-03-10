package lk.ijse.medicalcenterlayerdstructure.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.medicalcenterlayerdstructure.bo.custom.BOFactory;
import lk.ijse.medicalcenterlayerdstructure.bo.custom.UserBO;
import lk.ijse.medicalcenterlayerdstructure.dto.UserDTO;
import lk.ijse.medicalcenterlayerdstructure.util.SessionManager;

import java.io.IOException;
import java.sql.SQLException;



public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    private final UserBO userBO = (UserBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.USER);

    @FXML
    void handleLogin(ActionEvent event) {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please enter both username and password!");
            return;
        }

        try {

            UserDTO user = userBO.searchUserByCredentials(username, password);

            if (user != null) {

                SessionManager.getInstance().setCurrentUser(user);

                System.out.println(" Login successful: " + user.getUserName() + " (" + user.getRole() + ")");

                loadDashboard(event);

            } else {
                System.out.println("Login failed: Invalid credentials");
                showAlert(Alert.AlertType.ERROR, "Invalid username or password!");
                txtPassword.clear();
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Failed to load dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadDashboard(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/lk/ijse/medicalcenterlayerdstructure/dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1000, 600));
        stage.setTitle("MediCare System - Dashboard");
        stage.show();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(type == Alert.AlertType.ERROR ? "Error" : "Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
