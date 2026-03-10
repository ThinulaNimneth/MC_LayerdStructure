package lk.ijse.medicalcenterlayerdstructure.dao.custom.impl;

import lk.ijse.medicalcenterlayerdstructure.dao.custom.MedicineDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.MedicineDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.Medicine;
import lk.ijse.medicalcenterlayerdstructure.util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MedicineDAOImpl implements MedicineDAO {

    @Override
    public boolean save(Medicine medicine) throws SQLException {
        return CrudUtil.execute("INSERT INTO Medicine (name, quantity, current_stock, price) VALUES (?, ?, ?, ?)", medicine.getName(), medicine.getQuantity(), medicine.getCurrentStock(), medicine.getPrice());
    }

    @Override
    public boolean update(Medicine medicine) throws SQLException {
        return CrudUtil.execute("UPDATE Medicine SET name = ?, quantity = ?, current_stock = ?, price = ? WHERE medicine_id = ?", medicine.getName(), medicine.getQuantity(), medicine.getCurrentStock(), medicine.getPrice(), medicine.getMedicineId());
    }

    @Override
    public boolean delete(int medicineId) throws SQLException {
        CrudUtil.execute("DELETE FROM Supplier_Medicine WHERE medicine_id = ?", medicineId);
        CrudUtil.execute("DELETE FROM Appointment_Medicine WHERE medicine_id = ?", medicineId);
        return CrudUtil.execute("DELETE FROM Medicine WHERE medicine_id = ?", medicineId);
    }

    @Override
    public MedicineDTO findById(int medicineId) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM Medicine WHERE medicine_id = ?", medicineId);
        if (rs.next()) {
            return new MedicineDTO(rs.getInt("medicine_id"), rs.getString("name"), rs.getInt("quantity"), rs.getInt("current_stock"), rs.getBigDecimal("price"));
        }
        return null;
    }

    @Override
    public List<MedicineDTO> getAll() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM Medicine ORDER BY medicine_id DESC");
        return mapResultSet(rs);
    }

    @Override
    public List<MedicineDTO> findByName(String name) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM Medicine WHERE name LIKE ? ORDER BY name", "%" + name + "%");
        return mapResultSet(rs);
    }

    @Override
    public boolean updateStock(int medicineId, int quantity) throws SQLException {
        return CrudUtil.execute("UPDATE Medicine SET current_stock = current_stock - ? WHERE medicine_id = ? AND current_stock >= ?", quantity, medicineId, quantity);
    }

    @Override
    public boolean checkStockAvailability(int medicineId, int requiredQuantity) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT current_stock FROM Medicine WHERE medicine_id = ?", medicineId);
        if (rs.next()) return rs.getInt("current_stock") >= requiredQuantity;
        return false;
    }

    private List<MedicineDTO> mapResultSet(ResultSet rs) throws SQLException {
        List<MedicineDTO> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new MedicineDTO(rs.getInt("medicine_id"), rs.getString("name"), rs.getInt("quantity"), rs.getInt("current_stock"), rs.getBigDecimal("price")));
        }
        return list;
    }
}
