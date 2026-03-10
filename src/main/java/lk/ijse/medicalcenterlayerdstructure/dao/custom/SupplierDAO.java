package lk.ijse.medicalcenterlayerdstructure.dao.custom;


import lk.ijse.medicalcenterlayerdstructure.dao.SuperDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.MedicineDTO;
import lk.ijse.medicalcenterlayerdstructure.dto.SupplierDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.Supplier;
import java.sql.SQLException;
import java.util.List;

public interface SupplierDAO extends SuperDAO {
    boolean save(Supplier supplier) throws SQLException;
    boolean update(Supplier supplier) throws SQLException;
    boolean delete(int supplierId) throws SQLException;
    SupplierDTO findById(int supplierId) throws SQLException;
    List<SupplierDTO> getAll() throws SQLException;
    List<SupplierDTO> findByName(String name) throws SQLException;
    boolean assignMedicine(int supplierId, int medicineId) throws SQLException;
    boolean removeMedicine(int supplierId, int medicineId) throws SQLException;
    boolean isMedicineAssigned(int supplierId, int medicineId) throws SQLException;
    List<MedicineDTO> getMedicinesBySupplier(int supplierId) throws SQLException;
    List<SupplierDTO> getSuppliersByMedicine(int medicineId) throws SQLException;
    int getSupplierCount() throws SQLException;
    List<SupplierDTO> getSuppliersWithLowStock(int threshold) throws SQLException;
}
