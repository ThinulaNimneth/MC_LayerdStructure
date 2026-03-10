package lk.ijse.medicalcenterlayerdstructure.dao.custom;

import lk.ijse.medicalcenterlayerdstructure.dao.SuperDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.EmployeeDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.Employee;
import java.sql.SQLException;
import java.util.List;



public interface EmployeeDAO extends SuperDAO {
    boolean save(Employee employee) throws SQLException;
    boolean update(Employee employee) throws SQLException;
    boolean delete(int employeeId) throws SQLException;
    List<EmployeeDTO> getAll() throws SQLException;
    boolean userExists(int userId) throws SQLException;
    boolean isUserAssignedToEmployee(int userId) throws SQLException;
    boolean isUserAssignedToDifferentEmployee(int userId, int empId) throws SQLException;
}