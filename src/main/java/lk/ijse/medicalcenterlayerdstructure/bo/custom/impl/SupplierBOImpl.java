package lk.ijse.medicalcenterlayerdstructure.bo.custom.impl;

import lk.ijse.medicalcenterlayerdstructure.bo.custom.SupplierBO;
import lk.ijse.medicalcenterlayerdstructure.dao.DAOFactory;
import lk.ijse.medicalcenterlayerdstructure.dao.custom.SupplierDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.MedicineDTO;
import lk.ijse.medicalcenterlayerdstructure.dto.SupplierDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.Supplier;
import java.sql.SQLException;
import java.util.List;

public class SupplierBOImpl implements SupplierBO {
    SupplierDAO supplierDAO = (SupplierDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.SUPPLIER);

    @Override
    public boolean saveSupplier(SupplierDTO dto) throws SQLException {
        return supplierDAO.save(new Supplier(0, dto.getName(), dto.getContacts(), dto.getAddress()));
    }

    @Override
    public boolean updateSupplier(SupplierDTO dto) throws SQLException {
        return supplierDAO.update(new Supplier(dto.getSupplierId(), dto.getName(), dto.getContacts(), dto.getAddress()));
    }

    @Override
    public boolean deleteSupplier(int supplierId) throws SQLException {
        return supplierDAO.delete(supplierId);
    }

    @Override
    public SupplierDTO searchSupplier(int supplierId) throws SQLException {
        return supplierDAO.findById(supplierId);
    }

    @Override
    public List<SupplierDTO> getAllSuppliers() throws SQLException {
        return supplierDAO.getAll();
    }

    @Override
    public List<SupplierDTO> searchSupplierByName(String name) throws SQLException {
        return supplierDAO.findByName(name);
    }

    @Override
    public boolean assignMedicineToSupplier(int supplierId, int medicineId) throws SQLException {
        return supplierDAO.assignMedicine(supplierId, medicineId);
    }

    @Override
    public boolean removeMedicineFromSupplier(int supplierId, int medicineId) throws SQLException {
        return supplierDAO.removeMedicine(supplierId, medicineId);
    }

    @Override
    public boolean isMedicineAssigned(int supplierId, int medicineId) throws SQLException {
        return supplierDAO.isMedicineAssigned(supplierId, medicineId);
    }

    @Override
    public List<MedicineDTO> getMedicinesBySupplier(int supplierId) throws SQLException {
        return supplierDAO.getMedicinesBySupplier(supplierId);
    }

    @Override
    public List<SupplierDTO> getSuppliersByMedicine(int medicineId) throws SQLException {
        return supplierDAO.getSuppliersByMedicine(medicineId);
    }

    @Override
    public int getSupplierCount() throws SQLException {
        return supplierDAO.getSupplierCount();
    }

    @Override
    public List<SupplierDTO> getSuppliersWithLowStock(int threshold) throws SQLException {
        return supplierDAO.getSuppliersWithLowStock(threshold);
    }
}