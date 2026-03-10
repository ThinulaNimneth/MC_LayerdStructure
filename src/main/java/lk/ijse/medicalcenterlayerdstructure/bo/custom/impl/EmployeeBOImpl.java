package lk.ijse.medicalcenterlayerdstructure.bo.custom.impl;

import lk.ijse.medicalcenterlayerdstructure.bo.custom.EmployeeBO;
import lk.ijse.medicalcenterlayerdstructure.dao.DAOFactory;
import lk.ijse.medicalcenterlayerdstructure.dao.custom.EmployeeDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.EmployeeDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.Employee;
import java.sql.SQLException;
import java.util.List;

public class EmployeeBOImpl implements EmployeeBO {
    EmployeeDAO employeeDAO = (EmployeeDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.EMPLOYEE);

    @Override
    public boolean saveEmployee(EmployeeDTO dto) throws SQLException {
        return employeeDAO.save(new Employee(0, dto.getUserId(), dto.getEmployeeName(), dto.getJobTitle()));
    }

    @Override
    public boolean updateEmployee(EmployeeDTO dto) throws SQLException {
        return employeeDAO.update(new Employee(dto.getEmployeeId(), dto.getUserId(), dto.getEmployeeName(), dto.getJobTitle()));
    }

    @Override
    public boolean deleteEmployee(int employeeId) throws SQLException {
        return employeeDAO.delete(employeeId);
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() throws SQLException {
        return employeeDAO.getAll();
    }

    @Override
    public boolean userExists(int userId) throws SQLException {
        return employeeDAO.userExists(userId);
    }

    @Override
    public boolean isUserAssignedToEmployee(int userId) throws SQLException {
        return employeeDAO.isUserAssignedToEmployee(userId);
    }

    @Override
    public boolean isUserAssignedToDifferentEmployee(int userId, int empId) throws SQLException {
        return employeeDAO.isUserAssignedToDifferentEmployee(userId, empId);
    }
}
