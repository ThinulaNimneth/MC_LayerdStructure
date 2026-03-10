package lk.ijse.medicalcenterlayerdstructure.dao;

import lk.ijse.medicalcenterlayerdstructure.dao.custom.impl.*;

public class DAOFactory {

    private static DAOFactory instance;
    private DAOFactory() {}
    public static DAOFactory getInstance() {
        return instance == null ? instance = new DAOFactory() : instance;
    }
    public enum DAOTypes {
        PATIENT, DOCTOR, EMPLOYEE, MEDICINE, APPOINTMENT, APPOINTMENT_MEDICINE,
        MEDICAL_HISTORY, PAYMENT, SUPPLIER, USER
    }
    public SuperDAO getDAO(DAOTypes daoType) {
        switch (daoType) {
            case PATIENT: return new PatientDAOImpl();
            case DOCTOR: return new DoctorDAOImpl();
            case EMPLOYEE: return new EmployeeDAOImpl();
            case MEDICINE: return new MedicineDAOImpl();
            case APPOINTMENT: return new AppointmentDAOImpl();
            case APPOINTMENT_MEDICINE: return new AppointmentMedicineDAOImpl();
            case MEDICAL_HISTORY: return new MedicalHistoryDAOImpl();
            case PAYMENT: return new PaymentDAOImpl();
            case SUPPLIER: return new SupplierDAOImpl();
            case USER: return new UserDAOImpl();
            default: return null;
        }
    }
}
