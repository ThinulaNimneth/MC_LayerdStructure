package lk.ijse.medicalcenterlayerdstructure.dao.custom.impl;

import lk.ijse.medicalcenterlayerdstructure.dao.custom.UserDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.UserDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.User;
import lk.ijse.medicalcenterlayerdstructure.util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UserDAOImpl implements UserDAO {

    @Override
    public boolean save(User u) throws SQLException {
        return CrudUtil.execute("INSERT INTO User (user_name, password, role) VALUES (?, ?, ?)", u.getUserName(), u.getPassword(), u.getRole());
    }

    @Override
    public boolean update(User u) throws SQLException {
        return CrudUtil.execute("UPDATE User SET user_name = ?, password = ?, role = ? WHERE user_id = ?", u.getUserName(), u.getPassword(), u.getRole(), u.getUserId());
    }

    @Override
    public boolean delete(int userId) throws SQLException {
        return CrudUtil.execute("DELETE FROM User WHERE user_id = ?", userId);
    }

    @Override
    public UserDTO findById(int userId) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM User WHERE user_id = ?", userId);
        if (rs.next()) return mapRow(rs);
        return null;
    }

    @Override
    public UserDTO findByCredentials(String username, String password) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM User WHERE user_name = ? AND password = ?", username, password);
        if (rs.next()) return mapRow(rs);
        return null;
    }

    @Override
    public List<UserDTO> getAll() throws SQLException {
        return mapResultSet(CrudUtil.execute("SELECT * FROM User ORDER BY user_id DESC"));
    }

    @Override
    public List<UserDTO> getByRole(String role) throws SQLException {
        return mapResultSet(CrudUtil.execute("SELECT * FROM User WHERE role = ? ORDER BY user_name", role));
    }

    @Override
    public List<String> getAllUserNamesForDoctors() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT user_name FROM User WHERE role = 'Doctor' ORDER BY user_name");
        List<String> list = new ArrayList<>();
        while (rs.next()) list.add(rs.getString("user_name"));
        return list;
    }

    @Override
    public int getUserIdByName(String userName) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT user_id FROM User WHERE user_name = ?", userName);
        if (rs.next()) return rs.getInt("user_id");
        return -1;
    }

    @Override
    public String getUserNameById(int userId) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT user_name FROM User WHERE user_id = ?", userId);
        if (rs.next()) return rs.getString("user_name");
        return null;
    }

    @Override
    public boolean userExists(String userName) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT COUNT(*) FROM User WHERE user_name = ?", userName);
        if (rs.next()) return rs.getInt(1) > 0;
        return false;
    }

    @Override
    public boolean usernameExists(String userName, int excludeUserId) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT COUNT(*) FROM User WHERE user_name = ? AND user_id != ?", userName, excludeUserId);
        if (rs.next()) return rs.getInt(1) > 0;
        return false;
    }

    @Override
    public List<UserDTO> getUsersNotRegisteredAsDoctors() throws SQLException {
        return mapResultSet(CrudUtil.execute("SELECT u.* FROM User u LEFT JOIN Doctor d ON u.user_id = d.user_id WHERE u.role = 'Doctor' AND d.doctor_id IS NULL ORDER BY u.user_name"));
    }

    @Override
    public List<String> getAvailableUsersForDoctorRegistration() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT u.user_name FROM User u LEFT JOIN Doctor d ON u.user_id = d.user_id WHERE u.role = 'Doctor' AND d.doctor_id IS NULL ORDER BY u.user_name");
        List<String> list = new ArrayList<>();
        while (rs.next()) list.add(rs.getString("user_name"));
        return list;
    }

    @Override
    public boolean updateRole(int userId, String role) throws SQLException {
        return CrudUtil.execute("UPDATE User SET role = ? WHERE user_id = ?", role, userId);
    }

    @Override
    public boolean isUserRegisteredAsDoctor(int userId) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT COUNT(*) FROM Doctor WHERE user_id = ?", userId);
        if (rs.next()) return rs.getInt(1) > 0;
        return false;
    }

    private UserDTO mapRow(ResultSet rs) throws SQLException {
        return new UserDTO(rs.getInt("user_id"), rs.getString("user_name"), rs.getString("password"), rs.getString("role"));
    }

    private List<UserDTO> mapResultSet(ResultSet rs) throws SQLException {
        List<UserDTO> list = new ArrayList<>();
        while (rs.next()) list.add(mapRow(rs));
        return list;
    }
}
