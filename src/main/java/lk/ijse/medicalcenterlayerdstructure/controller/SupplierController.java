package lk.ijse.medicalcenterlayerdstructure.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.medicalcenterlayerdstructure.bo.custom.BOFactory;
import lk.ijse.medicalcenterlayerdstructure.bo.custom.MedicineBO;
import lk.ijse.medicalcenterlayerdstructure.bo.custom.SupplierBO;
import lk.ijse.medicalcenterlayerdstructure.dto.MedicineDTO;
import lk.ijse.medicalcenterlayerdstructure.dto.SupplierDTO;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;



public class SupplierController implements Initializable {

    @FXML
    private TextField txtSupplierId;

    @FXML
    private TextField txtSupplierName;

    @FXML
    private TextField txtContacts;

    @FXML
    private TextArea txtAddress;

    @FXML
    private TextField txtSearchById;

    @FXML
    private TextField txtSearchByName;

    @FXML
    private ComboBox<MedicineDTO> cmbMedicine;

    @FXML
    private ListView<MedicineDTO> lstAssignedMedicines;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnAssignMedicine;

    @FXML
    private Button btnRemoveMedicine;

    @FXML
    private TableView<SupplierDTO> tblSuppliers;

    @FXML
    private TableColumn<SupplierDTO, Integer> colSupplierId;

    @FXML
    private TableColumn<SupplierDTO, String> colSupplierName;

    @FXML
    private TableColumn<SupplierDTO, String> colContacts;

    @FXML
    private TableColumn<SupplierDTO, String> colAddress;

    @FXML
    private TableColumn<SupplierDTO, String> colMedicines;

    private final SupplierBO supplierBO = (SupplierBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.SUPPLIER);
    private final MedicineBO medicineBO = (MedicineBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.MEDICINE);
    private ObservableList<SupplierDTO> supplierList = FXCollections.observableArrayList();
    private ObservableList<MedicineDTO> assignedMedicinesList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContacts.setCellValueFactory(new PropertyValueFactory<>("contacts"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colMedicines.setCellValueFactory(new PropertyValueFactory<>("medicineNames"));

        loadAllSuppliers();
        loadMedicines();


        txtSearchByName.textProperty().addListener((observable, oldValue, newValue) -> {
            searchSuppliersByName(newValue);
        });


        tblSuppliers.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                SupplierDTO selectedSupplier = tblSuppliers.getSelectionModel().getSelectedItem();
                if (selectedSupplier != null) {
                    populateFields(selectedSupplier);
                }
            }
        });


        addPhoneValidation(txtContacts);


        generateNextSupplierId();
    }

    @FXML
    private void handleSave() {
        if (!validateInputs()) {
            return;
        }

        try {
            SupplierDTO supplierDTO = new SupplierDTO(
                    txtSupplierName.getText().trim(),
                    txtContacts.getText().trim(),
                    txtAddress.getText().trim()
            );

            boolean isSaved = supplierBO.saveSupplier(supplierDTO);

            if (isSaved) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Supplier saved successfully!");
                handleClear();
                loadAllSuppliers();
                generateNextSupplierId();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save supplier!");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate() {
        if (txtSupplierId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a supplier to update!");
            return;
        }

        if (!validateInputs()) {
            return;
        }

        try {
            SupplierDTO supplierDTO = new SupplierDTO(
                    Integer.parseInt(txtSupplierId.getText()),
                    txtSupplierName.getText().trim(),
                    txtContacts.getText().trim(),
                    txtAddress.getText().trim()
            );

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Update");
            confirmAlert.setHeaderText("Update Supplier");
            confirmAlert.setContentText("Are you sure you want to update this supplier?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean isUpdated = supplierBO.updateSupplier(supplierDTO);

                if (isUpdated) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Supplier updated successfully!");
                    handleClear();
                    loadAllSuppliers();
                    generateNextSupplierId();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update supplier!");
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid supplier ID format!");
        }
    }

    @FXML
    private void handleDelete() {
        if (txtSupplierId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a supplier to delete!");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Supplier");
        confirmAlert.setContentText("Are you sure you want to delete this supplier?\n" +
                "This will also remove all medicine associations.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                int supplierId = Integer.parseInt(txtSupplierId.getText());
                boolean isDeleted = supplierBO.deleteSupplier(supplierId);

                if (isDeleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Supplier deleted successfully!");
                    handleClear();
                    loadAllSuppliers();
                    generateNextSupplierId();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete supplier!");
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
            showAlert(Alert.AlertType.WARNING, "Warning", "Please enter a supplier ID to search!");
            return;
        }

        try {
            int supplierId = Integer.parseInt(searchId);
            SupplierDTO supplier = supplierBO.searchSupplier(supplierId);

            if (supplier != null) {
                populateFields(supplier);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Supplier found!");
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Not Found", "No supplier found with ID: " + supplierId);
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
    private void handleAssignMedicine() {
        if (txtSupplierId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please save the supplier first before assigning medicines!");
            return;
        }

        MedicineDTO selectedMedicine = cmbMedicine.getSelectionModel().getSelectedItem();
        if (selectedMedicine == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a medicine to assign!");
            return;
        }

        try {
            int supplierId = Integer.parseInt(txtSupplierId.getText());
            int medicineId = selectedMedicine.getMedicineId();

            // Check if already assigned
            if (supplierBO.isMedicineAssigned(supplierId, medicineId)) {
                showAlert(Alert.AlertType.WARNING, "Already Assigned", "This medicine is already assigned to this supplier!");
                return;
            }

            boolean isAssigned = supplierBO.assignMedicineToSupplier(supplierId, medicineId);

            if (isAssigned) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Medicine assigned successfully!");
                loadAssignedMedicines(supplierId);
                loadAllSuppliers();
                cmbMedicine.getSelectionModel().clearSelection();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to assign medicine!");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRemoveMedicine() {
        if (txtSupplierId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a supplier first!");
            return;
        }

        MedicineDTO selectedMedicine = lstAssignedMedicines.getSelectionModel().getSelectedItem();
        if (selectedMedicine == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a medicine to remove!");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Remove");
        confirmAlert.setHeaderText("Remove Medicine Assignment");
        confirmAlert.setContentText("Are you sure you want to remove this medicine from the supplier?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                int supplierId = Integer.parseInt(txtSupplierId.getText());
                int medicineId = selectedMedicine.getMedicineId();

                boolean isRemoved = supplierBO.removeMedicineFromSupplier(supplierId, medicineId);

                if (isRemoved) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Medicine removed successfully!");
                    loadAssignedMedicines(supplierId);
                    loadAllSuppliers();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to remove medicine!");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleClear() {
        txtSupplierId.clear();
        txtSupplierName.clear();
        txtContacts.clear();
        txtAddress.clear();
        txtSearchById.clear();
        txtSearchByName.clear();
        cmbMedicine.getSelectionModel().clearSelection();
        assignedMedicinesList.clear();
        tblSuppliers.getSelectionModel().clearSelection();
        generateNextSupplierId();
    }

    private void loadAllSuppliers() {
        try {
            List<SupplierDTO> suppliers = supplierBO.getAllSuppliers();
            supplierList.clear();
            supplierList.addAll(suppliers);
            tblSuppliers.setItems(supplierList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error loading suppliers: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadMedicines() {
        try {
            List<MedicineDTO> medicines = medicineBO.getAllMedicines();
            cmbMedicine.setItems(FXCollections.observableArrayList(medicines));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error loading medicines: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadAssignedMedicines(int supplierId) {
        try {
            List<MedicineDTO> medicines = supplierBO.getMedicinesBySupplier(supplierId);
            assignedMedicinesList.clear();
            assignedMedicinesList.addAll(medicines);
            lstAssignedMedicines.setItems(assignedMedicinesList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error loading assigned medicines: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void searchSuppliersByName(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            loadAllSuppliers();
            return;
        }

        try {
            List<SupplierDTO> suppliers = supplierBO.searchSupplierByName(searchTerm.trim());
            supplierList.clear();
            supplierList.addAll(suppliers);
            tblSuppliers.setItems(supplierList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error searching suppliers: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void populateFields(SupplierDTO supplier) {
        txtSupplierId.setText(String.valueOf(supplier.getSupplierId()));
        txtSupplierName.setText(supplier.getName());
        txtContacts.setText(supplier.getContacts());
        txtAddress.setText(supplier.getAddress());
        loadAssignedMedicines(supplier.getSupplierId());
    }

    private void generateNextSupplierId() {
        try {
            int count = supplierBO.getSupplierCount();
            txtSupplierId.setText(String.valueOf(count + 1));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to generate supplier ID: " + e.getMessage());
        }
    }

    private void addPhoneValidation(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9+\\-\\s]*")) {
                textField.setText(oldValue);
            }
        });
    }

    private boolean validateInputs() {

        if (txtSupplierName.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Supplier name is required!");
            txtSupplierName.requestFocus();
            return false;
        }


        if (txtContacts.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Contact number is required!");
            txtContacts.requestFocus();
            return false;
        }

        if (txtContacts.getText().trim().length() < 10) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Contact number must be at least 10 digits!");
            txtContacts.requestFocus();
            return false;
        }


        if (txtAddress.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Address is required!");
            txtAddress.requestFocus();
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
