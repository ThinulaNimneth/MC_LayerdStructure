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
import lk.ijse.medicalcenterlayerdstructure.dto.MedicineDTO;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;



public class MedicineController implements Initializable {

    @FXML
    private TextField txtMedicineId;

    @FXML
    private TextField txtMedicineName;

    @FXML
    private TextField txtQuantity;

    @FXML
    private TextField txtCurrentStock;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtSearchById;

    @FXML
    private TextField txtSearchByName;

    @FXML
    private Label lblStockStatus;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnClear;

    @FXML
    private TableView<MedicineDTO> tblMedicines;

    @FXML
    private TableColumn<MedicineDTO, Integer> colMedicineId;

    @FXML
    private TableColumn<MedicineDTO, String> colMedicineName;

    @FXML
    private TableColumn<MedicineDTO, Integer> colQuantity;

    @FXML
    private TableColumn<MedicineDTO, Integer> colCurrentStock;

    @FXML
    private TableColumn<MedicineDTO, BigDecimal> colPrice;

    private final MedicineBO medicineBO = (MedicineBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.MEDICINE);
    private ObservableList<MedicineDTO> medicineList = FXCollections.observableArrayList();

    // Stock alert
    private static final int LOW_STOCK_THRESHOLD = 20;
    private static final int CRITICAL_STOCK_THRESHOLD = 10;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colMedicineId.setCellValueFactory(new PropertyValueFactory<>("medicineId"));
        colMedicineName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colCurrentStock.setCellValueFactory(new PropertyValueFactory<>("currentStock"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));


        loadAllMedicines();

        txtSearchByName.textProperty().addListener((observable, oldValue, newValue) -> {
            searchMedicinesByName(newValue);
        });


        tblMedicines.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                MedicineDTO selectedMedicine = tblMedicines.getSelectionModel().getSelectedItem();
                if (selectedMedicine != null) {
                    populateFields(selectedMedicine);
                }
            }
        });

        // stock monitor
        txtCurrentStock.textProperty().addListener((observable, oldValue, newValue) -> {
            updateStockStatus();
        });

        //qty
        addNumericValidation(txtQuantity);
        addNumericValidation(txtCurrentStock);
        addDecimalValidation(txtPrice);


        generateNextMedicineId();
    }

    @FXML
    private void handleSave() {
        if (!validateInputs()) {
            return;
        }

        try {
            MedicineDTO medicineDTO = new MedicineDTO(
                    txtMedicineName.getText().trim(),
                    Integer.parseInt(txtQuantity.getText().trim()),
                    Integer.parseInt(txtCurrentStock.getText().trim()),
                    new BigDecimal(txtPrice.getText().trim())
            );

            boolean isSaved = medicineBO.saveMedicine(medicineDTO);

            if (isSaved) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Medicine saved successfully!");
                handleClear();
                loadAllMedicines();
                generateNextMedicineId();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save medicine!");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid number format!");
        }
    }

    @FXML
    private void handleUpdate() {
        if (txtMedicineId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a medicine to update!");
            return;
        }

        if (!validateInputs()) {
            return;
        }

        try {
            MedicineDTO medicineDTO = new MedicineDTO(
                    Integer.parseInt(txtMedicineId.getText()),
                    txtMedicineName.getText().trim(),
                    Integer.parseInt(txtQuantity.getText().trim()),
                    Integer.parseInt(txtCurrentStock.getText().trim()),
                    new BigDecimal(txtPrice.getText().trim())
            );

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Update");
            confirmAlert.setHeaderText("Update Medicine");
            confirmAlert.setContentText("Are you sure you want to update this medicine?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean isUpdated = medicineBO.updateMedicine(medicineDTO);

                if (isUpdated) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Medicine updated successfully!");
                    handleClear();
                    loadAllMedicines();
                    generateNextMedicineId();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update medicine!");
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid number format!");
        }
    }

    @FXML
    private void handleDelete() {
        if (txtMedicineId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a medicine to delete!");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Medicine");
        confirmAlert.setContentText("Are you sure you want to delete this medicine?\n" +
                "This will also remove all supplier and appointment associations.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                int medicineId = Integer.parseInt(txtMedicineId.getText());
                boolean isDeleted = medicineBO.deleteMedicine(medicineId);

                if (isDeleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Medicine deleted successfully!");
                    handleClear();
                    loadAllMedicines();
                    generateNextMedicineId();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete medicine!");
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
            showAlert(Alert.AlertType.WARNING, "Warning", "Please enter a medicine ID to search!");
            return;
        }

        try {
            int medicineId = Integer.parseInt(searchId);
            MedicineDTO medicine = medicineBO.searchMedicine(medicineId);

            if (medicine != null) {
                populateFields(medicine);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Medicine found!");
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Not Found", "No medicine found with ID: " + medicineId);
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
        txtMedicineId.clear();
        txtMedicineName.clear();
        txtQuantity.clear();
        txtCurrentStock.clear();
        txtPrice.clear();
        txtSearchById.clear();
        txtSearchByName.clear();
        lblStockStatus.setText("Normal");
        lblStockStatus.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
        tblMedicines.getSelectionModel().clearSelection();
        generateNextMedicineId();
    }

    private void loadAllMedicines() {
        try {
            List<MedicineDTO> medicines = medicineBO.getAllMedicines();
            medicineList.clear();
            medicineList.addAll(medicines);
            tblMedicines.setItems(medicineList);

            // Check  low stocks
            checkLowStockAlert();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error loading medicines: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void searchMedicinesByName(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            loadAllMedicines();
            return;
        }

        try {
            List<MedicineDTO> medicines = medicineBO.searchMedicineByName(searchTerm.trim());
            medicineList.clear();
            medicineList.addAll(medicines);
            tblMedicines.setItems(medicineList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error searching medicines: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void populateFields(MedicineDTO medicine) {
        txtMedicineId.setText(String.valueOf(medicine.getMedicineId()));
        txtMedicineName.setText(medicine.getName());
        txtQuantity.setText(String.valueOf(medicine.getQuantity()));
        txtCurrentStock.setText(String.valueOf(medicine.getCurrentStock()));
        txtPrice.setText(medicine.getPrice().toString());
        updateStockStatus();
    }

    private void generateNextMedicineId() {
        try {
            List<MedicineDTO> all = medicineBO.getAllMedicines();
            int nextId = all.isEmpty() ? 1 : all.stream().mapToInt(MedicineDTO::getMedicineId).max().getAsInt() + 1;
            txtMedicineId.setText(String.valueOf(nextId));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to generate medicine ID: " + e.getMessage());
        }
    }

    private void updateStockStatus() {
        String stockText = txtCurrentStock.getText().trim();
        if (stockText.isEmpty()) {
            lblStockStatus.setText("Normal");
            lblStockStatus.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
            return;
        }

        try {
            int stock = Integer.parseInt(stockText);

            if (stock <= CRITICAL_STOCK_THRESHOLD) {
                lblStockStatus.setText(" CRITICAL - Restock Immediately!");
                lblStockStatus.setStyle("-fx-text-fill: #D32F2F; -fx-font-weight: bold; -fx-font-size: 12px;");
            } else if (stock <= LOW_STOCK_THRESHOLD) {
                lblStockStatus.setText(" Low Stock - Order Soon");
                lblStockStatus.setStyle("-fx-text-fill: #FF9800; -fx-font-weight: bold; -fx-font-size: 12px;");
            } else {
                lblStockStatus.setText(" Normal Stock");
                lblStockStatus.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold; -fx-font-size: 12px;");
            }
        } catch (NumberFormatException e) {
            lblStockStatus.setText("Invalid Stock");
            lblStockStatus.setStyle("-fx-text-fill: #9E9E9E; -fx-font-weight: bold;");
        }
    }

    private void checkLowStockAlert() {
        int criticalCount = 0;
        int lowCount = 0;

        for (MedicineDTO medicine : medicineList) {
            if (medicine.getCurrentStock() <= CRITICAL_STOCK_THRESHOLD) {
                criticalCount++;
            } else if (medicine.getCurrentStock() <= LOW_STOCK_THRESHOLD) {
                lowCount++;
            }
        }

        if (criticalCount > 0 || lowCount > 0) {
            StringBuilder message = new StringBuilder("Stock Alert:\n");
            if (criticalCount > 0) {
                message.append("• ").append(criticalCount).append(" medicine(s) at CRITICAL stock level\n");
            }
            if (lowCount > 0) {
                message.append("• ").append(lowCount).append(" medicine(s) at LOW stock level\n");
            }
            message.append("\nPlease restock immediately!");

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Stock Alert");
            alert.setHeaderText("Low Stock Warning");
            alert.setContentText(message.toString());
            alert.show();
        }
    }

    private void addNumericValidation(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void addDecimalValidation(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                textField.setText(oldValue);
            }
        });
    }

    private boolean validateInputs() {
        //valid

        if (txtMedicineName.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Medicine name is required!");
            txtMedicineName.requestFocus();
            return false;
        }


        if (txtQuantity.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Initial quantity is required!");
            txtQuantity.requestFocus();
            return false;
        }

        try {
            int quantity = Integer.parseInt(txtQuantity.getText().trim());
            if (quantity < 0) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Quantity cannot be negative!");
                txtQuantity.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Quantity must be a valid number!");
            txtQuantity.requestFocus();
            return false;
        }

        //stock
        if (txtCurrentStock.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Current stock is required!");
            txtCurrentStock.requestFocus();
            return false;
        }

        try {
            int currentStock = Integer.parseInt(txtCurrentStock.getText().trim());
            if (currentStock < 0) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Current stock cannot be negative!");
                txtCurrentStock.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Current stock must be a valid number!");
            txtCurrentStock.requestFocus();
            return false;
        }

        // price
        if (txtPrice.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Price is required!");
            txtPrice.requestFocus();
            return false;
        }

        try {
            BigDecimal price = new BigDecimal(txtPrice.getText().trim());
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Price must be greater than zero!");
                txtPrice.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Price must be a valid number!");
            txtPrice.requestFocus();
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