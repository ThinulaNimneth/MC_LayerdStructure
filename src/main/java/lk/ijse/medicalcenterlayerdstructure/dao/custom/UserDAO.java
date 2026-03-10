package lk.ijse.medicalcenterlayerdstructure.dao.custom;

import lk.ijse.medicalcenterlayerdstructure.dao.SuperDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.UserDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.User;
import java.sql.SQLException;
import java.util.List;

public interface UserDAO extends SuperDAO {
    boolean save(User user) throws SQLException;
    boolean update(User user) throws SQLException;
    boolean delete(int userId) throws SQLException;
    UserDTO findById(int userId) throws SQLException;
    UserDTO findByCredentials(String username, String password) throws SQLException;
    List<UserDTO> getAll() throws SQLException;
    List<UserDTO> getByRole(String role) throws SQLException;
    List<String> getAllUserNamesForDoctors() throws SQLException;
    int getUserIdByName(String userName) throws SQLException;
    String getUserNameById(int userId) throws SQLException;
    boolean userExists(String userName) throws SQLException;
    boolean usernameExists(String userName, int excludeUserId) throws SQLException;
    List<UserDTO> getUsersNotRegisteredAsDoctors() throws SQLException;
    List<String> getAvailableUsersForDoctorRegistration() throws SQLException;
    boolean updateRole(int userId, String role) throws SQLException;
    boolean isUserRegisteredAsDoctor(int userId) throws SQLException;
}
