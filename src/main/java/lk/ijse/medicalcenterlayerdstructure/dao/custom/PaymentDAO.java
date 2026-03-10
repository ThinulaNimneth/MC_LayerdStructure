package lk.ijse.medicalcenterlayerdstructure.dao.custom;



import lk.ijse.medicalcenterlayerdstructure.dao.SuperDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.PaymentDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.Payment;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface PaymentDAO extends SuperDAO {
    boolean save(Payment payment) throws SQLException;
    boolean update(Payment payment) throws SQLException;
    boolean updateStatus(int paymentId, String status) throws SQLException;
    boolean delete(int paymentId) throws SQLException;
    PaymentDTO findById(int paymentId) throws SQLException;
    List<PaymentDTO> getAll() throws SQLException;
    List<PaymentDTO> getByPatient(int patientId) throws SQLException;
    List<PaymentDTO> getByDate(LocalDate date) throws SQLException;
    List<PaymentDTO> getByStatus(String status) throws SQLException;
    boolean isPaymentExistsForAppointment(int appointmentId) throws SQLException;
    BigDecimal getTotalRevenue(LocalDate startDate, LocalDate endDate) throws SQLException;
    int getPaymentCount() throws SQLException;
    BigDecimal getTodayRevenue() throws SQLException;
}
