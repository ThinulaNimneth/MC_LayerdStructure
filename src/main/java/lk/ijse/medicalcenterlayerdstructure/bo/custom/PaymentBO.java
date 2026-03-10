package lk.ijse.medicalcenterlayerdstructure.bo.custom;


import lk.ijse.medicalcenterlayerdstructure.bo.SuperBO;
import lk.ijse.medicalcenterlayerdstructure.dto.PaymentDTO;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;



public interface PaymentBO extends SuperBO {
    boolean savePayment(PaymentDTO dto) throws SQLException;
    boolean updatePayment(PaymentDTO dto) throws SQLException;
    boolean updatePaymentStatus(int paymentId, String status) throws SQLException;
    boolean deletePayment(int paymentId) throws SQLException;
    PaymentDTO searchPayment(int paymentId) throws SQLException;
    List<PaymentDTO> getAllPayments() throws SQLException;
    List<PaymentDTO> getPaymentsByPatient(int patientId) throws SQLException;
    List<PaymentDTO> getPaymentsByDate(LocalDate date) throws SQLException;
    List<PaymentDTO> getPaymentsByStatus(String status) throws SQLException;
    boolean isPaymentExistsForAppointment(int appointmentId) throws SQLException;
    BigDecimal getTotalRevenue(LocalDate startDate, LocalDate endDate) throws SQLException;
    int getPaymentCount() throws SQLException;
    BigDecimal getTodayRevenue() throws SQLException;
}
