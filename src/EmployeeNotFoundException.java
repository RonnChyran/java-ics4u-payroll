/**
 * Created by chyyran on 11/25/16.
 */
public class EmployeeNotFoundException extends Exception {
    private int employeeNumber;

    public EmployeeNotFoundException(int nonExistentEmployee) {
        super("Employee #" + nonExistentEmployee + " was not found.");
        this.employeeNumber = nonExistentEmployee;
    }

    public int getEmployeeNumber() {
        return this.employeeNumber;
    }
}
