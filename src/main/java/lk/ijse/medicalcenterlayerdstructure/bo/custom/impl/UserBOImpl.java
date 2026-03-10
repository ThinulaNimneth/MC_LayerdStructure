package lk.ijse.medicalcenterlayerdstructure.bo.custom.impl;

import lk.ijse.medicalcenterlayerdstructure.bo.custom.UserBO;
import lk.ijse.medicalcenterlayerdstructure.dao.DAOFactory;
import lk.ijse.medicalcenterlayerdstructure.dao.custom.UserDAO;
import lk.ijse.medicalcenterlayerdstructure.dto.UserDTO;
import lk.ijse.medicalcenterlayerdstructure.entity.User;
import java.sql.SQLException;
import java.util.List;

public class UserBOImpl implements UserBO {
    UserDAO userDAO = (UserDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.USER);

    @Override
    public boolean saveUser(UserDTO dto) throws SQLException {
        return userDAO.save(new User(0, dto.getUserName(), dto.getPassword(), dto.getRole()));
    }

    @Override
    public boolean updateUser(UserDTO dto) throws SQLException {
        return userDAO.update(new User(dto.getUserId(), dto.getUserName(), dto.getPassword(), dto.getRole()));
    }

    @Override
    public boolean deleteUser(int userId) throws SQLException {
        return userDAO.delete(userId);
    }

    @Override
    public UserDTO searchUser(int userId) throws SQLException {
        return userDAO.findById(userId);
    }

    @Override
    public UserDTO searchUserByCredentials(String username, String password) throws SQLException {
        return userDAO.findByCredentials(username, password);
    }

    @Override
    public List<UserDTO> getAllUsers() throws SQLException {
        return userDAO.getAll();
    }

    @Override
    public List<UserDTO> getUsersByRole(String role) throws SQLException {
        return userDAO.getByRole(role);
    }

    @Override
    public List<String> getAllUserNamesForDoctors() throws SQLException {
        return userDAO.getAllUserNamesForDoctors();
    }

    @Override
    public int getUserIdByName(String userName) throws SQLException {
        return userDAO.getUserIdByName(userName);
    }

    @Override
    public String getUserNameById(int userId) throws SQLException {
        return userDAO.getUserNameById(userId);
    }

    @Override
    public boolean userExists(String userName) throws SQLException {
        return userDAO.userExists(userName);
    }

    @Override
    public boolean usernameExists(String userName, int excludeUserId) throws SQLException {
        return userDAO.usernameExists(userName, excludeUserId);
    }

    @Override
    public List<UserDTO> getUsersNotRegisteredAsDoctors() throws SQLException {
        return userDAO.getUsersNotRegisteredAsDoctors();
    }

    @Override
    public List<String> getAvailableUsersForDoctorRegistration() throws SQLException {
        return userDAO.getAvailableUsersForDoctorRegistration();
    }

    @Override
    public boolean updateUserRole(int userId, String role) throws SQLException {
        return userDAO.updateRole(userId, role);
    }

    @Override
    public boolean isUserRegisteredAsDoctor(int userId) throws SQLException {
        return userDAO.isUserRegisteredAsDoctor(userId);
    }
}
