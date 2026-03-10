package lk.ijse.medicalcenterlayerdstructure.bo.custom.impl;


import lk.ijse.medicalcenterlayerdstructure.bo.custom.PaymentBO;
import lk.ijse.medicalcenterlayerdstructure.dao.DAOFactory;
import lk.ijse.medicalcenterlayerdstructure.dao.custom.PaymentDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.PaymentDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.Payment;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;



public class PaymentBOImpl implements PaymentBO {
    PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.PAYMENT);

    @Override
    public boolean savePayment(PaymentDTO dto) throws SQLException {
        return paymentDAO.save(new Payment(0, dto.getPatientId(), dto.getAppointmentId(), dto.getConsultationFee(), dto.getMedicineCharges(), dto.getTotalAmount(), dto.getPaymentDate(), dto.getPaymentTime(), dto.getPaymentStatus()));
    }

    @Override
    public boolean updatePayment(PaymentDTO dto) throws SQLException {
        return paymentDAO.update(new Payment(dto.getPaymentId(), dto.getPatientId(), dto.getAppointmentId(), dto.getConsultationFee(), dto.getMedicineCharges(), dto.getTotalAmount(), dto.getPaymentDate(), dto.getPaymentTime(), dto.getPaymentStatus()));
    }

    @Override
    public boolean updatePaymentStatus(int paymentId, String status) throws SQLException {
        return paymentDAO.updateStatus(paymentId, status);
    }

    @Override
    public boolean deletePayment(int paymentId) throws SQLException {
        return paymentDAO.delete(paymentId);
    }

    @Override
    public PaymentDTO searchPayment(int paymentId) throws SQLException {
        return paymentDAO.findById(paymentId);
    }

    @Override
    public List<PaymentDTO> getAllPayments() throws SQLException {
        return paymentDAO.getAll();
    }

    @Override
    public List<PaymentDTO> getPaymentsByPatient(int patientId) throws SQLException {
        return paymentDAO.getByPatient(patientId);
    }

    @Override
    public List<PaymentDTO> getPaymentsByDate(LocalDate date) throws SQLException {
        return paymentDAO.getByDate(date);
    }

    @Override
    public List<PaymentDTO> getPaymentsByStatus(String status) throws SQLException {
        return paymentDAO.getByStatus(status);
    }

    @Override
    public boolean isPaymentExistsForAppointment(int appointmentId) throws SQLException {
        return paymentDAO.isPaymentExistsForAppointment(appointmentId);
    }

    @Override
    public BigDecimal getTotalRevenue(LocalDate startDate, LocalDate endDate) throws SQLException {
        return paymentDAO.getTotalRevenue(startDate, endDate);
    }

    @Override
    public int getPaymentCount() throws SQLException {
        return paymentDAO.getPaymentCount();
    }

    @Override
    public BigDecimal getTodayRevenue() throws SQLException {
        return paymentDAO.getTodayRevenue();
    }
}
