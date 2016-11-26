/**
 * Created by chyyran on 11/24/16.
 */
public final class FullTimeStaff extends Employee {

    private static final int MONTHS_IN_YEAR = 12;

    private static final int yearlySickDay = 20;

    private double yearlySalary;
    private double sickDaysLeft;

    public FullTimeStaff(String firstName, String lastName, int employeeNumber,
                         double yearlySalary, double sickDaysLeft) {
        super(firstName, lastName, employeeNumber);
        this.yearlySalary = yearlySalary;
        this.sickDaysLeft = sickDaysLeft;
    }

    public FullTimeStaff(String firstName, String lastName, int employeeNumber,
                         double yearlySalary) {
        this(firstName, lastName, employeeNumber, yearlySalary, FullTimeStaff.yearlySickDay);
    }

    @Override
    public double pay() {
        return this.yearlySalary / FullTimeStaff.MONTHS_IN_YEAR;
    }

    public double getSalary() {
        return this.yearlySalary;
    }

    @Override
    public void deductSickDay(double sickDays) throws SickDaysException {
        if(sickDays < 0.5) throw new SickDaysException(this, "Can not deduct less than half a day.");
        if(this.sickDaysLeft - sickDays < 0) throw new SickDaysException(this, String
                .format("Can not %1$f sick days, staff member does not have enough sick days.", sickDays));
        this.sickDaysLeft -= sickDays;
    }

    @Override
    public void resetSickDay() {
        this.sickDaysLeft = FullTimeStaff.yearlySickDay;
    }

    @Override
    public void printPayStub() {
        ToStringBuilder sb = new ToStringBuilder();
        sb.prop("Employee Number", this.getEmployeeNumber())
                .prop("Amount Earned", this.pay())
                .prop("Remaining Sick Days", this.sickDaysLeft);
        System.out.print(sb);
    }

    public int compareToSickDay(FullTimeStaff employee) {
        return FullTimeStaff.compareSickDay(this, employee);
    }

    public static int compareSickDay(FullTimeStaff employeeOne, FullTimeStaff employeeTwo) {
        return Double.compare(employeeOne.sickDaysLeft, employeeTwo.sickDaysLeft);
    }

    @Override
    public String toString() {
        return super.toString() +", Title: Full Time";
    }
}
