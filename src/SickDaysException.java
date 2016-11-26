/**
 * Created by chyyran on 11/24/16.
 */
public class SickDaysException extends Exception {
    private Employee employee;

    public SickDaysException(Employee employee, String message) {
        super(message);
        this.employee = employee;
    }

    public Employee getEmployee() {
        return this.employee;
    }
}
