package lk.ijse.medicalcenterlayerdstructure.dao.custom.impl;

import lk.ijse.medicalcenterlayerdstructure.dao.custom.PaymentDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.PaymentDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.Payment;
import lk.ijse.medicalcenterlayerdstructure.util.CrudUtil;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public boolean save(Payment p) throws SQLException {
        return CrudUtil.execute("INSERT INTO Payment (patient_id, appointment_id, consultation_fee, medicine_charges, total_amount, payment_date, payment_time, payment_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                p.getPatientId(), p.getAppointmentId(), p.getConsultationFee(), p.getMedicineCharges(), p.getTotalAmount(), p.getPaymentDate(), p.getPaymentTime(), p.getPaymentStatus());
    }

    @Override
    public boolean update(Payment p) throws SQLException {
        return CrudUtil.execute("UPDATE Payment SET consultation_fee = ?, medicine_charges = ?, total_amount = ?, payment_status = ? WHERE payment_id = ?",
                p.getConsultationFee(), p.getMedicineCharges(), p.getTotalAmount(), p.getPaymentStatus(), p.getPaymentId());
    }

    @Override
    public boolean updateStatus(int paymentId, String status) throws SQLException {
        return CrudUtil.execute("UPDATE Payment SET payment_status = ? WHERE payment_id = ?", status, paymentId);
    }

    @Override
    public boolean delete(int paymentId) throws SQLException {
        return CrudUtil.execute("DELETE FROM Payment WHERE payment_id = ?", paymentId);
    }

    @Override
    public PaymentDTO findById(int paymentId) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT p.*, pt.name as patient_name FROM Payment p JOIN Patient pt ON p.patient_id = pt.patient_id WHERE p.payment_id = ?", paymentId);
        if (rs.next()) return mapRow(rs);
        return null;
    }

    @Override
    public List<PaymentDTO> getAll() throws SQLException {
        return mapResultSet(CrudUtil.execute("SELECT p.*, pt.name as patient_name FROM Payment p JOIN Patient pt ON p.patient_id = pt.patient_id ORDER BY p.payment_id DESC"));
    }

    @Override
    public List<PaymentDTO> getByPatient(int patientId) throws SQLException {
        return mapResultSet(CrudUtil.execute("SELECT p.*, pt.name as patient_name FROM Payment p JOIN Patient pt ON p.patient_id = pt.patient_id WHERE p.patient_id = ? ORDER BY p.payment_date DESC", patientId));
    }

    @Override
    public List<PaymentDTO> getByDate(LocalDate date) throws SQLException {
        return mapResultSet(CrudUtil.execute("SELECT p.*, pt.name as patient_name FROM Payment p JOIN Patient pt ON p.patient_id = pt.patient_id WHERE p.payment_date = ? ORDER BY p.payment_time DESC", date));
    }

    @Override
    public List<PaymentDTO> getByStatus(String status) throws SQLException {
        return mapResultSet(CrudUtil.execute("SELECT p.*, pt.name as patient_name FROM Payment p JOIN Patient pt ON p.patient_id = pt.patient_id WHERE p.payment_status = ? ORDER BY p.payment_date DESC", status));
    }

    @Override
    public boolean isPaymentExistsForAppointment(int appointmentId) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT COUNT(*) as count FROM Payment WHERE appointment_id = ?", appointmentId);
        if (rs.next()) return rs.getInt("count") > 0;
        return false;
    }

    @Override
    public BigDecimal getTotalRevenue(LocalDate startDate, LocalDate endDate) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT SUM(total_amount) as total_revenue FROM Payment WHERE payment_date BETWEEN ? AND ? AND payment_status = 'Paid'", startDate, endDate);
        if (rs.next()) return rs.getBigDecimal("total_revenue");
        return BigDecimal.ZERO;
    }

    @Override
    public int getPaymentCount() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT COUNT(*) as count FROM Payment");
        if (rs.next()) return rs.getInt("count");
        return 0;
    }

    @Override
    public BigDecimal getTodayRevenue() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT SUM(total_amount) as total_revenue FROM Payment WHERE payment_date = CURRENT_DATE AND payment_status = 'Paid'");
        if (rs.next()) return rs.getBigDecimal("total_revenue");
        return BigDecimal.ZERO;
    }

    private PaymentDTO mapRow(ResultSet rs) throws SQLException {
        PaymentDTO dto = new PaymentDTO(rs.getInt("payment_id"), rs.getInt("patient_id"), rs.getInt("appointment_id"),
                rs.getBigDecimal("consultation_fee"), rs.getBigDecimal("medicine_charges"), rs.getBigDecimal("total_amount"),
                rs.getDate("payment_date").toLocalDate(), rs.getTime("payment_time").toLocalTime(), rs.getString("payment_status"));
        dto.setPatientName(rs.getString("patient_name"));
        return dto;
    }

    private List<PaymentDTO> mapResultSet(ResultSet rs) throws SQLException {
        List<PaymentDTO> list = new ArrayList<>();
        while (rs.next()) list.add(mapRow(rs));
        return list;
    }
}
