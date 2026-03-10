package lk.ijse.medicalcenterlayerdstructure.bo.custom;

import lk.ijse.medicalcenterlayerdstructure.bo.SuperBO;
import lk.ijse.medicalcenterlayerdstructure.dto.MedicineDTO;
import java.sql.SQLException;
import java.util.List;



public interface MedicineBO extends SuperBO {
    boolean saveMedicine(MedicineDTO dto) throws SQLException;
    boolean updateMedicine(MedicineDTO dto) throws SQLException;
    boolean deleteMedicine(int medicineId) throws SQLException;
    MedicineDTO searchMedicine(int medicineId) throws SQLException;
    List<MedicineDTO> getAllMedicines() throws SQLException;
    List<MedicineDTO> searchMedicineByName(String name) throws SQLException;
    boolean updateStock(int medicineId, int quantity) throws SQLException;
    boolean checkStockAvailability(int medicineId, int requiredQuantity) throws SQLException;
}