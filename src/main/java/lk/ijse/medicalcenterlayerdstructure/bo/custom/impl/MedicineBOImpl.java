package lk.ijse.medicalcenterlayerdstructure.bo.custom.impl;

import lk.ijse.medicalcenterlayerdstructure.bo.custom.MedicineBO;
import lk.ijse.medicalcenterlayerdstructure.dao.DAOFactory;
import lk.ijse.medicalcenterlayerdstructure.dao.custom.MedicineDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.MedicineDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.Medicine;
import java.sql.SQLException;
import java.util.List;


public class MedicineBOImpl implements MedicineBO {
    MedicineDAO medicineDAO = (MedicineDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.MEDICINE);

    @Override
    public boolean saveMedicine(MedicineDTO dto) throws SQLException {
        return medicineDAO.save(new Medicine(0, dto.getName(), dto.getQuantity(), dto.getCurrentStock(), dto.getPrice()));
    }

    @Override
    public boolean updateMedicine(MedicineDTO dto) throws SQLException {
        return medicineDAO.update(new Medicine(dto.getMedicineId(), dto.getName(), dto.getQuantity(), dto.getCurrentStock(), dto.getPrice()));
    }

    @Override
    public boolean deleteMedicine(int medicineId) throws SQLException {
        return medicineDAO.delete(medicineId);
    }

    @Override
    public MedicineDTO searchMedicine(int medicineId) throws SQLException {
        return medicineDAO.findById(medicineId);
    }

    @Override
    public List<MedicineDTO> getAllMedicines() throws SQLException {
        return medicineDAO.getAll();
    }

    @Override
    public List<MedicineDTO> searchMedicineByName(String name) throws SQLException {
        return medicineDAO.findByName(name);
    }

    @Override
    public boolean updateStock(int medicineId, int quantity) throws SQLException {
        return medicineDAO.updateStock(medicineId, quantity);
    }

    @Override
    public boolean checkStockAvailability(int medicineId, int requiredQuantity) throws SQLException {
        return medicineDAO.checkStockAvailability(medicineId, requiredQuantity);
    }
}
