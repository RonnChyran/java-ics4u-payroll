import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.*;

/**
 * Created by chyyran on 11/25/16.
 */
public final class Payroll implements Iterable<Employee> {

    private Employee[] payroll;

    public Payroll(Employee[] payroll) {
        this.payroll = payroll;
    }

    public void listAllEmployee() {
        for(Employee e : this) {
            System.out.println(e);
        }
    }

    public void enterSickDay(int employeeNumber, double sickDaysToDeduct) throws EmployeeNotFoundException,
            SickDaysException {
        for(Employee e : this) {
            if(e.getEmployeeNumber() == employeeNumber) {
                e.deductSickDay(sickDaysToDeduct);
                return;
            }
        }
        throw new EmployeeNotFoundException(employeeNumber);
    }

    public void printAllPayStubs() {
        for(Employee e : this) {
            e.printPayStub();
        }
    }

    public void saveStaffList(String filename) throws IOException {
       // new PersistenceUtility<Employee[]>(filename).save(this.payroll);
        new SerializationUtility<>(filename).save(this.payroll);
    }

    public void loadStaffList(String filename) throws IOException {
        this.payroll = new SerializationUtility<>(filename).load(new Employee[] {});
          //  this.payroll = new PersistenceUtility<Employee[]>(filename).load();

    }

    public double averageSalary() {
        return Arrays.stream(this.payroll).filter(e -> e instanceof FullTimeStaff)
        .mapToDouble(e -> ((FullTimeStaff)e).getSalary())
        .average().getAsDouble();
    }

    public double averageHourlyRate() {
        return Arrays.stream(this.payroll).filter(e -> e instanceof PartTimeStaff)
                .mapToDouble(e -> ((PartTimeStaff)e).getHourlyRate())
                .average().getAsDouble();
    }

    public Employee mostAbsentFullTime() {
        return Arrays.stream(this.payroll).filter(e -> e instanceof FullTimeStaff)
                .map(e -> (FullTimeStaff)e)
                .min(FullTimeStaff::compareSickDay).get();
    }

    public Employee mostAbsentPartTime() {
        return Arrays.stream(this.payroll).filter(e -> e instanceof PartTimeStaff)
                .map(e -> (PartTimeStaff)e)
                .min(PartTimeStaff::compareToSickDay).get();
    }

    public void yearlySickDayReset() {
        for(int i = 0; i < this.payroll.length; i++){
            if(this.payroll[i] == null) continue;
            if(!(this.payroll[i] instanceof FullTimeStaff)) continue;
            this.payroll[i].resetSickDay();
        }
    }


    public void monthlySickDayReset() {
        for(int i = 0; i < this.payroll.length; i++){
            if(this.payroll[i] == null) continue;
            if(!(this.payroll[i] instanceof PartTimeStaff)) continue;
            this.payroll[i].resetSickDay();
        }
    }

    @Override
    public Iterator<Employee> iterator() {
        return Arrays.asList(this.payroll).iterator();
    }

}
