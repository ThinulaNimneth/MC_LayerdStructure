package lk.ijse.medicalcenterlayerdstructure.bo.custom;

import lk.ijse.medicalcenterlayerdstructure.bo.SuperBO;
import lk.ijse.medicalcenterlayerdstructure.dto.UserDTO;
import java.sql.SQLException;
import java.util.List;

public interface UserBO extends SuperBO {

    boolean saveUser(UserDTO dto) throws SQLException;
    boolean updateUser(UserDTO dto) throws SQLException;
    boolean deleteUser(int userId) throws SQLException;
    UserDTO searchUser(int userId) throws SQLException;
    UserDTO searchUserByCredentials(String username, String password) throws SQLException;
    List<UserDTO> getAllUsers() throws SQLException;
    List<UserDTO> getUsersByRole(String role) throws SQLException;
    List<String> getAllUserNamesForDoctors() throws SQLException;
    int getUserIdByName(String userName) throws SQLException;
    String getUserNameById(int userId) throws SQLException;
    boolean userExists(String userName) throws SQLException;
    boolean usernameExists(String userName, int excludeUserId) throws SQLException;
    List<UserDTO> getUsersNotRegisteredAsDoctors() throws SQLException;
    List<String> getAvailableUsersForDoctorRegistration() throws SQLException;
    boolean updateUserRole(int userId, String role) throws SQLException;
    boolean isUserRegisteredAsDoctor(int userId) throws SQLException;
}