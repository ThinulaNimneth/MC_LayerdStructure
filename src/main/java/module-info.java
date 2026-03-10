module lk.ijse.medicalcenterlayerdstructure {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;              // ← MISSING - causes ALL SQLException red lines
    requires mysql.connector.j;     // ← MISSING - causes DBConnection red lines

    opens lk.ijse.medicalcenterlayerdstructure to javafx.fxml;
    opens lk.ijse.medicalcenterlayerdstructure.controller to javafx.fxml; // ← MISSING
    exports lk.ijse.medicalcenterlayerdstructure;
}