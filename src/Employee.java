import java.io.Serializable;

/**
 * Created by chyyran on 11/24/16.
 */
public abstract class Employee implements Serializable {
    private String firstName;
    private String lastName;
    private int employeeNumber;

    public abstract double pay();
    public abstract void deductSickDay(double sickDays) throws SickDaysException;
    public abstract void resetSickDay();
    public abstract void printPayStub();

    protected Employee(String firstName, String lastName, int employeeNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeNumber = employeeNumber;
    }

    protected String getFirstName() {
        return this.firstName;
    }

    protected String getLastName() {
        return this.lastName;
    }

    protected int getEmployeeNumber() {
        return this.employeeNumber;
    }

    @Override
    public String toString() {
        return String.format("First Name: %1$s, Last Name: %2$s, Employee Number: %3$d", this.getFirstName(),
                this.getLastName(),
                this.getEmployeeNumber());
    }
}
