package lk.ijse.medicalcenterlayerdstructure.util;

import lk.ijse.medicalcenterlayerdstructure.dto.UserDTO;

public class SessionManager {
    private static SessionManager instance;
    private UserDTO currentUser;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setCurrentUser(UserDTO user) {
        this.currentUser = user;
        System.out.println("Session started: " + user.getUserName() + " (" + user.getRole() + ")");
    }

    public UserDTO getCurrentUser() {
        return currentUser;
    }

    public String getCurrentUserRole() {
        return currentUser != null ? currentUser.getRole() : null;
    }

    public String getCurrentUserName() {
        return currentUser != null ? currentUser.getUserName() : null;
    }

    public int getCurrentUserId() {
        return currentUser != null ? currentUser.getUserId() : -1;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        return "Admin".equals(getCurrentUserRole());
    }

    public boolean isDoctor() {
        return "Doctor".equals(getCurrentUserRole());
    }

    public boolean isReceptionist() {
        return "Receptionist".equals(getCurrentUserRole());
    }

    public void clearSession() {
        if (currentUser != null) {
            System.out.println(" Session ended: " + currentUser.getUserName());
        }
        currentUser = null;
    }
}
