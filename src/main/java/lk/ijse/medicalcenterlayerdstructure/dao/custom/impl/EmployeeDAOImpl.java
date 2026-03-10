package lk.ijse.medicalcenterlayerdstructure.dao.custom.impl;

import lk.ijse.medicalcenterlayerdstructure.dao.custom.EmployeeDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.EmployeeDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.Employee;
import lk.ijse.medicalcenterlayerdstructure.util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class EmployeeDAOImpl implements EmployeeDAO {

    @Override
    public boolean save(Employee employee) throws SQLException {
        return CrudUtil.execute("INSERT INTO Employee (user_id, employee_name, job_title) VALUES (?, ?, ?)", employee.getUserId(), employee.getEmployeeName(), employee.getJobTitle());
    }

    @Override
    public boolean update(Employee employee) throws SQLException {
        return CrudUtil.execute("UPDATE Employee SET employee_name = ?, job_title = ? WHERE employee_id = ?", employee.getEmployeeName(), employee.getJobTitle(), employee.getEmployeeId());
    }

    @Override
    public boolean delete(int employeeId) throws SQLException {
        return CrudUtil.execute("DELETE FROM Employee WHERE employee_id = ?", employeeId);
    }

    @Override
    public List<EmployeeDTO> getAll() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT employee_id, user_id, employee_name, job_title FROM Employee ORDER BY employee_id DESC");
        List<EmployeeDTO> list = new ArrayList<>();
        while (rs.next()) {
            EmployeeDTO dto = new EmployeeDTO();
            dto.setEmployeeId(rs.getInt("employee_id")); dto.setUserId(rs.getInt("user_id"));
            dto.setEmployeeName(rs.getString("employee_name")); dto.setJobTitle(rs.getString("job_title"));
            list.add(dto);
        }
        return list;
    }

    @Override
    public boolean userExists(int userId) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT COUNT(*) FROM User WHERE user_id = ?", userId);
        return rs.next() && rs.getInt(1) > 0;
    }

    @Override
    public boolean isUserAssignedToEmployee(int userId) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT COUNT(*) FROM Employee WHERE user_id = ?", userId);
        return rs.next() && rs.getInt(1) > 0;
    }

    @Override
    public boolean isUserAssignedToDifferentEmployee(int userId, int empId) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT COUNT(*) FROM Employee WHERE user_id = ? AND employee_id != ?", userId, empId);
        return rs.next() && rs.getInt(1) > 0;
    }
}
