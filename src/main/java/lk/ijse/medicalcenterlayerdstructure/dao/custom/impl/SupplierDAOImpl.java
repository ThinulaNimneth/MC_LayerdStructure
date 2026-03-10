package lk.ijse.medicalcenterlayerdstructure.dao.custom.impl;

import lk.ijse.medicalcenterlayerdstructure.dao.custom.SupplierDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.MedicineDTO;
import lk.ijse.medicalcenterlayerdstructure.dto.SupplierDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.Supplier;
import lk.ijse.medicalcenterlayerdstructure.util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class SupplierDAOImpl implements SupplierDAO {

    @Override
    public boolean save(Supplier s) throws SQLException {
        return CrudUtil.execute("INSERT INTO Supplier (name, contacts, address) VALUES (?, ?, ?)", s.getName(), s.getContacts(), s.getAddress());
    }

    @Override
    public boolean update(Supplier s) throws SQLException {
        return CrudUtil.execute("UPDATE Supplier SET name = ?, contacts = ?, address = ? WHERE supplier_id = ?", s.getName(), s.getContacts(), s.getAddress(), s.getSupplierId());
    }

    @Override
    public boolean delete(int supplierId) throws SQLException {
        CrudUtil.execute("DELETE FROM Supplier_Medicine WHERE supplier_id = ?", supplierId);
        return CrudUtil.execute("DELETE FROM Supplier WHERE supplier_id = ?", supplierId);
    }

    @Override
    public SupplierDTO findById(int supplierId) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM Supplier WHERE supplier_id = ?", supplierId);
        if (rs.next()) {
            SupplierDTO dto = new SupplierDTO(rs.getInt("supplier_id"), rs.getString("name"), rs.getString("contacts"), rs.getString("address"));
            dto.setMedicineNames(getMedicineNamesForSupplier(supplierId));
            return dto;
        }
        return null;
    }

    @Override
    public List<SupplierDTO> getAll() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM Supplier ORDER BY supplier_id DESC");
        return mapResultSet(rs);
    }

    @Override
    public List<SupplierDTO> findByName(String name) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM Supplier WHERE name LIKE ? ORDER BY name", "%" + name + "%");
        return mapResultSet(rs);
    }

    @Override
    public boolean assignMedicine(int supplierId, int medicineId) throws SQLException {
        return CrudUtil.execute("INSERT INTO Supplier_Medicine (supplier_id, medicine_id) VALUES (?, ?)", supplierId, medicineId);
    }

    @Override
    public boolean removeMedicine(int supplierId, int medicineId) throws SQLException {
        return CrudUtil.execute("DELETE FROM Supplier_Medicine WHERE supplier_id = ? AND medicine_id = ?", supplierId, medicineId);
    }

    @Override
    public boolean isMedicineAssigned(int supplierId, int medicineId) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT COUNT(*) as count FROM Supplier_Medicine WHERE supplier_id = ? AND medicine_id = ?", supplierId, medicineId);
        if (rs.next()) return rs.getInt("count") > 0;
        return false;
    }

    @Override
    public List<MedicineDTO> getMedicinesBySupplier(int supplierId) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT m.* FROM Medicine m INNER JOIN Supplier_Medicine sm ON m.medicine_id = sm.medicine_id WHERE sm.supplier_id = ? ORDER BY m.name", supplierId);
        List<MedicineDTO> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new MedicineDTO(rs.getInt("medicine_id"), rs.getString("name"), rs.getInt("quantity"), rs.getInt("current_stock"), rs.getBigDecimal("price")));
        }
        return list;
    }

    @Override
    public List<SupplierDTO> getSuppliersByMedicine(int medicineId) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT s.* FROM Supplier s INNER JOIN Supplier_Medicine sm ON s.supplier_id = sm.supplier_id WHERE sm.medicine_id = ? ORDER BY s.name", medicineId);
        List<SupplierDTO> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new SupplierDTO(rs.getInt("supplier_id"), rs.getString("name"), rs.getString("contacts"), rs.getString("address")));
        }
        return list;
    }

    @Override
    public int getSupplierCount() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT COUNT(*) as count FROM Supplier");
        if (rs.next()) return rs.getInt("count");
        return 0;
    }

    @Override
    public List<SupplierDTO> getSuppliersWithLowStock(int threshold) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT DISTINCT s.* FROM Supplier s INNER JOIN Supplier_Medicine sm ON s.supplier_id = sm.supplier_id INNER JOIN Medicine m ON sm.medicine_id = m.medicine_id WHERE m.current_stock <= ? ORDER BY s.name", threshold);
        return mapResultSet(rs);
    }

    private String getMedicineNamesForSupplier(int supplierId) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT m.name FROM Medicine m INNER JOIN Supplier_Medicine sm ON m.medicine_id = sm.medicine_id WHERE sm.supplier_id = ? ORDER BY m.name", supplierId);
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        while (rs.next()) {
            if (!first) sb.append(", ");
            sb.append(rs.getString("name"));
            first = false;
        }
        return sb.length() > 0 ? sb.toString() : "No medicines assigned";
    }

    private List<SupplierDTO> mapResultSet(ResultSet rs) throws SQLException {
        List<SupplierDTO> list = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("supplier_id");
            SupplierDTO dto = new SupplierDTO(id, rs.getString("name"), rs.getString("contacts"), rs.getString("address"));
            dto.setMedicineNames(getMedicineNamesForSupplier(id));
            list.add(dto);
        }
        return list;
    }
}
