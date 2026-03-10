package lk.ijse.medicalcenterlayerdstructure.entity;

public class Employee {
    private int employeeId;
    private int userId;
    private String employeeName;
    private String jobTitle;

    public Employee() {}
    public Employee(int employeeId, int userId, String employeeName, String jobTitle) {
        this.employeeId = employeeId; this.userId = userId; this.employeeName = employeeName; this.jobTitle = jobTitle;
    }
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
}
