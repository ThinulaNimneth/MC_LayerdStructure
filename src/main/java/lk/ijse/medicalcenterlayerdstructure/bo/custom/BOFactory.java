package lk.ijse.medicalcenterlayerdstructure.bo.custom;

import lk.ijse.medicalcenterlayerdstructure.bo.SuperBO;
import lk.ijse.medicalcenterlayerdstructure.bo.custom.impl.*;

public class BOFactory {
    private static BOFactory instance;
    private BOFactory() {}
    public static BOFactory getInstance() {
        return instance == null ? instance = new BOFactory() : instance;
    }
    public enum BOTypes {
        PATIENT, DOCTOR, EMPLOYEE, MEDICINE, APPOINTMENT, APPOINTMENT_MEDICINE,
        MEDICAL_HISTORY, PAYMENT, SUPPLIER, USER
    }
    public SuperBO getBO(BOTypes boType) {
        switch (boType) {
            case PATIENT: return new PatientBOImpl();
            case DOCTOR: return new DoctorBOImpl();
            case EMPLOYEE: return new EmployeeBOImpl();
            case MEDICINE: return new MedicineBOImpl();
            case APPOINTMENT: return new AppointmentBOImpl();
            case APPOINTMENT_MEDICINE: return new AppointmentMedicineBOImpl();
            case MEDICAL_HISTORY: return new MedicalHistoryBOImpl();
            case PAYMENT: return new PaymentBOImpl();
            case SUPPLIER: return new SupplierBOImpl();
            case USER: return new UserBOImpl();
            default: return null;
        }
    }
}
