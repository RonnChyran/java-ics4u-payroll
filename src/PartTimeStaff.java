
/**
 * Created by chyyran on 11/25/16.
 */
public final class PartTimeStaff extends Employee {

    private double sickDaysTaken;
    private double hoursAssigned;
    private double hourlyRate;

    protected PartTimeStaff(String firstName, String lastName, int employeeNumber,
                            double hoursAssigned, double hourlyRate, int sickDaysTaken) {
        super(firstName, lastName, employeeNumber);
        this.sickDaysTaken = sickDaysTaken;
        this.hourlyRate = hourlyRate;
        this.hoursAssigned = hoursAssigned;
    }

    @Override
    public double pay() {
        return (hoursAssigned - sickDaysTaken) * hourlyRate;
    }

    @Override
    public void deductSickDay(double sickDays) throws SickDaysException {
        if(sickDays < 0.5) throw new SickDaysException(this, "Can not deduct less than half a day.");
        this.sickDaysTaken += sickDays;
    }

    @Override
    public void resetSickDay() {
        this.sickDaysTaken = 0;
    }

    public double getHourlyRate() {
        return this.hourlyRate;
    }

    @Override
    public void printPayStub() {
        ToStringBuilder sb = new ToStringBuilder();
        sb.prop("Employee Number", this.getEmployeeNumber())
                .prop("Amount Earned", this.pay())
                .prop("Sick Days Taken", this.sickDaysTaken);
        System.out.print(sb);
    }

    public int compareToSickDay(PartTimeStaff employee) {
        return compareSickDay(this, employee);
    }

    public static int compareSickDay(PartTimeStaff employeeOne, PartTimeStaff employeeTwo) {
        return Double.compare(employeeTwo.sickDaysTaken, employeeOne.sickDaysTaken);
    }

    @Override
    public String toString() {
        return super.toString() +", Title: Part Time";
    }
}
