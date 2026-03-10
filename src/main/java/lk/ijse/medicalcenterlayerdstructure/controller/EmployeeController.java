package lk.ijse.medicalcenterlayerdstructure.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.medicalcenterlayerdstructure.bo.custom.BOFactory;
import lk.ijse.medicalcenterlayerdstructure.bo.custom.EmployeeBO;
import lk.ijse.medicalcenterlayerdstructure.dto.EmployeeDTO;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;



public class EmployeeController implements Initializable {

    @FXML private AnchorPane employeeContent;
    @FXML private TextField txtEmployeeId;
    @FXML private TextField txtUserId;
    @FXML private TextField txtEmployeeName;
    @FXML private TextField txtJobTitle;
    @FXML private TableView<EmployeeDTO> tblEmployees;
    @FXML private TableColumn<EmployeeDTO, Integer> colId;
    @FXML private TableColumn<EmployeeDTO, String> colName;
    @FXML private TableColumn<EmployeeDTO, String> colJob;

    private final EmployeeBO employeeBO = (EmployeeBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.EMPLOYEE);
    private ObservableList<EmployeeDTO> employeeList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        employeeList = FXCollections.observableArrayList();
        colId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        colJob.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
        loadAllEmployees();
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (!validateFields()) return;
        try {
            int userId = Integer.parseInt(txtUserId.getText());
            if (!employeeBO.userExists(userId)) { showAlert(Alert.AlertType.ERROR, "Error", "User ID does not exist!"); return; }
            if (employeeBO.isUserAssignedToEmployee(userId)) { showAlert(Alert.AlertType.ERROR, "Error", "This User is already assigned to another employee!"); return; }
            if (employeeBO.saveEmployee(getEmployeeFromFields())) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Employee saved successfully!"); clearFields(); loadAllEmployees();
            }
        } catch (SQLException e) { showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage()); }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (txtEmployeeId.getText().isEmpty()) { showAlert(Alert.AlertType.WARNING, "Warning", "Please select an employee!"); return; }
        if (!validateFields()) return;
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Update this employee?", ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                int userId = Integer.parseInt(txtUserId.getText());
                int empId = Integer.parseInt(txtEmployeeId.getText());
                if (!employeeBO.userExists(userId)) { showAlert(Alert.AlertType.ERROR, "Error", "User ID does not exist!"); return; }
                if (employeeBO.isUserAssignedToDifferentEmployee(userId, empId)) { showAlert(Alert.AlertType.ERROR, "Error", "User already assigned to another employee!"); return; }
                if (employeeBO.updateEmployee(getEmployeeFromFields())) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Employee updated successfully!"); clearFields(); loadAllEmployees();
                }
            } catch (SQLException e) { showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage()); }
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        if (txtEmployeeId.getText().isEmpty()) { showAlert(Alert.AlertType.WARNING, "Warning", "Select an employee to delete!"); return; }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete this employee?", ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (employeeBO.deleteEmployee(Integer.parseInt(txtEmployeeId.getText()))) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Employee deleted!"); clearFields(); loadAllEmployees();
                }
            } catch (SQLException e) { showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage()); }
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) { clearFields(); }

    @FXML
    void tblClicked(MouseEvent event) {
        EmployeeDTO emp = tblEmployees.getSelectionModel().getSelectedItem();
        if (emp != null) setFieldsFromEmployee(emp);
    }

    private void loadAllEmployees() {
        try {
            tblEmployees.getItems().clear();
            employeeList.setAll(employeeBO.getAllEmployees());
            tblEmployees.setItems(employeeList); tblEmployees.refresh();
        } catch (SQLException e) { showAlert(Alert.AlertType.ERROR, "Load Error", e.getMessage()); }
    }

    private boolean validateFields() {
        if (txtUserId.getText().isEmpty() || txtEmployeeName.getText().isEmpty() || txtJobTitle.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "All fields required!"); return false;
        }
        try { Integer.parseInt(txtUserId.getText()); }
        catch (NumberFormatException e) { showAlert(Alert.AlertType.WARNING, "Validation Error", "User ID must be a number!"); return false; }
        return true;
    }

    private EmployeeDTO getEmployeeFromFields() {
        EmployeeDTO dto = new EmployeeDTO();
        if (!txtEmployeeId.getText().isEmpty()) dto.setEmployeeId(Integer.parseInt(txtEmployeeId.getText()));
        dto.setUserId(Integer.parseInt(txtUserId.getText())); dto.setEmployeeName(txtEmployeeName.getText().trim()); dto.setJobTitle(txtJobTitle.getText().trim());
        return dto;
    }

    private void setFieldsFromEmployee(EmployeeDTO dto) {
        txtEmployeeId.setText(String.valueOf(dto.getEmployeeId())); txtUserId.setText(String.valueOf(dto.getUserId()));
        txtEmployeeName.setText(dto.getEmployeeName()); txtJobTitle.setText(dto.getJobTitle());
    }

    private void clearFields() {
        txtEmployeeId.clear(); txtUserId.clear(); txtEmployeeName.clear(); txtJobTitle.clear(); tblEmployees.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type); alert.setTitle(title); alert.setHeaderText(null); alert.setContentText(content); alert.showAndWait();
    }
}
