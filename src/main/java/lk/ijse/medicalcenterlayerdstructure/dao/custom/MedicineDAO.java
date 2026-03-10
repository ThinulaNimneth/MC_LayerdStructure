package lk.ijse.medicalcenterlayerdstructure.dao.custom;


import lk.ijse.medicalcenterlayerdstructure.dao.SuperDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.MedicineDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.Medicine;
import java.sql.SQLException;
import java.util.List;



public interface MedicineDAO extends SuperDAO {
    boolean save(Medicine medicine) throws SQLException;
    boolean update(Medicine medicine) throws SQLException;
    boolean delete(int medicineId) throws SQLException;
    MedicineDTO findById(int medicineId) throws SQLException;
    List<MedicineDTO> getAll() throws SQLException;
    List<MedicineDTO> findByName(String name) throws SQLException;
    boolean updateStock(int medicineId, int quantity) throws SQLException;
    boolean checkStockAvailability(int medicineId, int requiredQuantity) throws SQLException;
}
