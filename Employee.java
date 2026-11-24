import java.io.Serializable;

public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int employeeId;
    private final String name;
    private final String department;
    private final double salary;

    public Employee(int employeeId, String name, String department, double salary) {
        if (employeeId <= 0 || name.isBlank() || department.isBlank()
                || !Double.isFinite(salary) || salary < 0) {
            throw new IllegalArgumentException("Enter a positive ID, name, department and valid salary.");
        }
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return String.format("%d | %s | %s | %.2f", employeeId, name, department, salary);
    }
}
