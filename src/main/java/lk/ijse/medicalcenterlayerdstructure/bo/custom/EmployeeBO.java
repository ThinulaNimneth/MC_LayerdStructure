package lk.ijse.medicalcenterlayerdstructure.bo.custom;

import lk.ijse.medicalcenterlayerdstructure.bo.SuperBO;
import lk.ijse.medicalcenterlayerdstructure.dto.EmployeeDTO;
import java.sql.SQLException;
import java.util.List;

public interface EmployeeBO extends SuperBO {
    boolean saveEmployee(EmployeeDTO dto) throws SQLException;
    boolean updateEmployee(EmployeeDTO dto) throws SQLException;
    boolean deleteEmployee(int employeeId) throws SQLException;
    List<EmployeeDTO> getAllEmployees() throws SQLException;
    boolean userExists(int userId) throws SQLException;
    boolean isUserAssignedToEmployee(int userId) throws SQLException;
    boolean isUserAssignedToDifferentEmployee(int userId, int empId) throws SQLException;
}
