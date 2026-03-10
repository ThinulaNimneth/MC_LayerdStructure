package lk.ijse.medicalcenterlayerdstructure.bo.custom;

import lk.ijse.medicalcenterlayerdstructure.bo.SuperBO;
import lk.ijse.medicalcenterlayerdstructure.dto.MedicineDTO;
import lk.ijse.medicalcenterlayerdstructure.dto.SupplierDTO;
import java.sql.SQLException;
import java.util.List;

public interface SupplierBO extends SuperBO {
    boolean saveSupplier(SupplierDTO dto) throws SQLException;
    boolean updateSupplier(SupplierDTO dto) throws SQLException;
    boolean deleteSupplier(int supplierId) throws SQLException;
    SupplierDTO searchSupplier(int supplierId) throws SQLException;
    List<SupplierDTO> getAllSuppliers() throws SQLException;
    List<SupplierDTO> searchSupplierByName(String name) throws SQLException;
    boolean assignMedicineToSupplier(int supplierId, int medicineId) throws SQLException;
    boolean removeMedicineFromSupplier(int supplierId, int medicineId) throws SQLException;
    boolean isMedicineAssigned(int supplierId, int medicineId) throws SQLException;
    List<MedicineDTO> getMedicinesBySupplier(int supplierId) throws SQLException;
    List<SupplierDTO> getSuppliersByMedicine(int medicineId) throws SQLException;
    int getSupplierCount() throws SQLException;
    List<SupplierDTO> getSuppliersWithLowStock(int threshold) throws SQLException;
}
