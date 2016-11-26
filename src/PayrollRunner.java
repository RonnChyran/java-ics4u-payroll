import java.io.IOException;

/**
 * Created by chyyran on 11/25/16.
 */
public class PayrollRunner {
    public static void main(String[] args) {
        MenuBuilder menu = new MenuBuilder();
        Prompter prompter = new Prompter(System.in);
        Payroll payroll = new Payroll(new Employee[]{
                new FullTimeStaff("Bob", "Ross", 1, 100000),
                new FullTimeStaff("Michael", "Jackson", 2, 50000),
                new PartTimeStaff("Part", "Timer", 3, 20, 15, 0),
                new PartTimeStaff("Lazy", "Dude", 4, 20, 15, 0)
        });

        menu.option("List Employees", () -> payroll.listAllEmployee())
                .option("Enter Sick Days", () -> {
                    int employeeNumber = prompter.promptInt("Enter employee number");
                    double sickDay = prompter.promptDouble("How many sick days to deduct");

                    try {
                        payroll.enterSickDay(employeeNumber, sickDay);
                        System.out.println(String.format("Successfully deducted %2$f sick days from employee #%1$d",
                                employeeNumber, sickDay));
                    } catch (SickDaysException e) {
                        System.out.println("Failed to deduct sick days: " + e.getMessage());
                        System.out.println("No changes were made.");
                    } catch (EmployeeNotFoundException e) {
                        System.out.println("Failed to deduct sick days: " + e.getMessage());
                        System.out.println("No changes were made.");
                    }
                })
                .option("Print Pay Stubs", () -> payroll.printAllPayStubs())
                .option("Reset Full Time Staff Sick Days", () -> {
                    payroll.yearlySickDayReset();
                    System.out.println("Full Time Sick Days Reset.");
                })
                .option("Reset Part Time Staff Sick Days", () -> {
                    payroll.monthlySickDayReset();
                    System.out.println("Part Time Sick Days Reset.");
                })
                .option("Save Information", () -> {
                    try {
                        payroll.saveStaffList("payroll.dat");
                        System.out.println("Successfully saved staff list.");
                    } catch (IOException e) {
                        System.out.println("Something went wrong when saving the file. Check to make you have permissions to save.");
                    }
                })
                .option("Load Information", () -> {
                    try {
                        payroll.loadStaffList("payroll.dat");
                        System.out.println("Successfully loaded staff list.");
                    }catch(IOException e) {
                        System.out.println("Something went wrong when loading the file. Check to make you it exists and is not corrupt.");
                    }
                })
                .option("See Average Full-time Salary", () -> System.out.println("The average yearly salary is " + payroll.averageSalary()))
                .option("See Average Part-time Hourly Rate", () -> System.out.println("The average hourly rate is " + payroll.averageHourlyRate()))
                .option("See Which Full-time Employee Has the Most Sick Days", () -> {
                    System.out.println("The employee with the most sick days is: ");
                    System.out.println(payroll.mostAbsentFullTime());
                })
                .option("See Which Part-time Employee Has the Most Sick Days", () -> {
                    System.out.println("The employee with the most sick days is: ");
                    System.out.println(payroll.mostAbsentPartTime());
                })
                .error(e -> System.out.print("Sorry! Something wrong happened, please try again.\n" +
                        " Technical Details: " + e.getMessage()))
                .exit(() -> System.out.print("Goodbye, thanks for using Payroll Runner!"));
        menu.run();
    }
}
