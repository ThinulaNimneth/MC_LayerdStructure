package lk.ijse.medicalcenterlayerdstructure.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import lk.ijse.medicalcenterlayerdstructure.bo.custom.*;
import lk.ijse.medicalcenterlayerdstructure.dto.AppointmentDTO;
import lk.ijse.medicalcenterlayerdstructure.dto.MedicineDTO;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;



public class DashboardHomeController implements Initializable {


    @FXML private Label lblTotalPatients;
    @FXML private Label lblTodayAppointments;
    @FXML private Label lblActiveDoctors;
    @FXML private Label lblTodayRevenue;
    @FXML private Label lblLowStockMedicines;
    @FXML private Label lblAppointmentTrend;
    @FXML private Label lblPatientTrend;


    @FXML private TableView<AppointmentDTO> tblTodayAppointments;
    @FXML private TableColumn<AppointmentDTO, Integer> colAppointmentId;
    @FXML private TableColumn<AppointmentDTO, String> colPatientName;
    @FXML private TableColumn<AppointmentDTO, String> colDoctorName;
    @FXML private TableColumn<AppointmentDTO, String> colTime;
    @FXML private TableColumn<AppointmentDTO, BigDecimal> colAmount;
    @FXML private TableColumn<AppointmentDTO, String> colStatus;


    private final AppointmentBO appointmentBO = (AppointmentBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.APPOINTMENT);
    private final PatientBO patientBO = (PatientBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.PATIENT);
    private final DoctorBO doctorBO = (DoctorBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.DOCTOR);
    private final MedicineBO medicineBO = (MedicineBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.MEDICINE);
    private final PaymentBO paymentBO = (PaymentBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.PAYMENT);

    private ObservableList<AppointmentDTO> todayAppointmentsList = FXCollections.observableArrayList();
    private Timeline refreshTimeline;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setupTableColumns();

        loadDashboardData();

        startAutoRefresh();

        System.out.println(" Dashboard loaded successfully!");
    }

    private void setupTableColumns() {
        colAppointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        colPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colDoctorName.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("formattedTime"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void startAutoRefresh() {
        // Refresh data
        refreshTimeline = new Timeline(new KeyFrame(Duration.seconds(30), event -> loadDashboardData()));
        refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTimeline.play();
    }

    private void loadDashboardData() {
        try {

            loadPatientStats();
            loadAppointmentStats();
            loadDoctorStats();
            loadRevenueStats();
            loadMedicineStats();
            loadTodayAppointmentsTable();

            System.out.println(" Dashboard data refreshed at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a")));
        } catch (Exception e) {
            System.err.println(" Error loading dashboard data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadPatientStats() {
        try {
            int totalPatients = patientBO.getAllPatients().size();
            lblTotalPatients.setText(String.valueOf(totalPatients));
            lblPatientTrend.setText(" Registered");
        } catch (SQLException e) {
            lblTotalPatients.setText("Error");
            System.err.println("Error loading patient stats: " + e.getMessage());
        }
    }

    private void loadAppointmentStats() {
        try {

            int todayAppointmentCount = appointmentBO.getTodayAppointmentCount();
            lblTodayAppointments.setText(String.valueOf(todayAppointmentCount));

            LocalDate yesterday = LocalDate.now().minusDays(1);
            List<AppointmentDTO> yesterdayApts = appointmentBO.getAppointmentsByDate(yesterday);
            int yesterdayCount = yesterdayApts.size();

            if (yesterdayCount > 0 && todayAppointmentCount > 0) {
                double change = ((double)(todayAppointmentCount - yesterdayCount) / yesterdayCount) * 100;
                String trend = change >= 0 ? " +" : " ";
                lblAppointmentTrend.setText(trend + String.format("%.0f", Math.abs(change)) + "% vs yesterday");

                if (change >= 0) {
                    lblAppointmentTrend.setStyle("-fx-font-size: 9px; -fx-text-fill: #27AE60;");
                } else {
                    lblAppointmentTrend.setStyle("-fx-font-size: 9px; -fx-text-fill: #E74C3C;");
                }
            } else {
                lblAppointmentTrend.setText(" Scheduled");
            }

        } catch (SQLException e) {
            lblTodayAppointments.setText("Error");
            System.err.println("Error loading appointment stats: " + e.getMessage());
        }
    }

    private void loadDoctorStats() {
        try {
            int availableDoctors = doctorBO.getAvailableDoctors().size();
            int totalDoctors = doctorBO.getAllDoctors().size();
            lblActiveDoctors.setText(availableDoctors + "/" + totalDoctors);
        } catch (SQLException e) {
            lblActiveDoctors.setText("Error");
            System.err.println("Error loading doctor stats: " + e.getMessage());
        }
    }

    private void loadRevenueStats() {
        try {
            BigDecimal todayRevenue = paymentBO.getTodayRevenue();
            if (todayRevenue == null) {
                todayRevenue = BigDecimal.ZERO;
            }
            lblTodayRevenue.setText("Rs. " + String.format("%,.2f", todayRevenue));
        } catch (SQLException e) {
            lblTodayRevenue.setText("Rs. 0.00");
            System.err.println("Error loading revenue stats: " + e.getMessage());
        }
    }

    private void loadMedicineStats() {
        try {
            List<MedicineDTO> allMedicines = medicineBO.getAllMedicines();
            int lowStockCount = 0;
            int criticalStockCount = 0;

            for (MedicineDTO medicine : allMedicines) {
                if (medicine.getCurrentStock() <= 10) {
                    criticalStockCount++;
                } else if (medicine.getCurrentStock() <= 20) {
                    lowStockCount++;
                }
            }

            int totalLowStock = lowStockCount + criticalStockCount;
            lblLowStockMedicines.setText(String.valueOf(totalLowStock));

            //alerts
            if (criticalStockCount > 0) {
                lblLowStockMedicines.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #E74C3C;");
            } else if (lowStockCount > 0) {
                lblLowStockMedicines.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #F39C12;");
            } else {
                lblLowStockMedicines.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #27AE60;");
            }

        } catch (SQLException e) {
            lblLowStockMedicines.setText("Error");
            System.err.println("Error loading medicine stats: " + e.getMessage());
        }
    }

    private void loadTodayAppointmentsTable() {
        try {
            List<AppointmentDTO> todayAppointments = appointmentBO.getTodayAppointments();


            for (AppointmentDTO apt : todayAppointments) {
                apt.setStatus("Scheduled");
            }

            todayAppointmentsList.clear();
            todayAppointmentsList.addAll(todayAppointments);
            tblTodayAppointments.setItems(todayAppointmentsList);

        } catch (SQLException e) {
            System.err.println("Error loading today's appointments table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void cleanup() {
        if (refreshTimeline != null) {
            refreshTimeline.stop();
        }
        System.out.println(" Dashboard timeline stopped");
    }
}