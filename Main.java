import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

enum Gender {
    MALE, FEMALE
}

abstract class Employee {
    private int id;
    private String name;
    private Gender gender;
    private LocalDate hireDate;
    private Department department;

    public Employee(int id, String name, Gender gender, LocalDate hireDate) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.hireDate = hireDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }

    public abstract double calculateSalary();

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name +
               ", Gender: " + gender + ", Hire Date: " + hireDate;
    }
}

class CommissionEmployee extends Employee {
    private double baseSalary;
    private double commissionRate;
    private double salesAmount;

    public CommissionEmployee(int id, String name, Gender gender, LocalDate hireDate,
                              double baseSalary, double commissionRate, double salesAmount) {
        super(id, name, gender, hireDate);
        this.baseSalary = baseSalary;
        this.commissionRate = commissionRate;
        this.salesAmount = salesAmount;
    }

    public double getBaseSalary() { return baseSalary; }
    public void setBaseSalary(double baseSalary) { this.baseSalary = baseSalary; }

    public double getCommissionRate() { return commissionRate; }
    public void setCommissionRate(double commissionRate) { this.commissionRate = commissionRate; }

    public double getSalesAmount() { return salesAmount; }
    public void setSalesAmount(double salesAmount) { this.salesAmount = salesAmount; }

    public double calculateCommission() {
        return salesAmount * commissionRate;
    }

    @Override
    public double calculateSalary() {
        return baseSalary + calculateCommission();
    }
}

class MonthlyEmployee extends Employee {
    private double monthlySalary;
    private int vacationDays;
    private double bonusPercentage;
    private boolean hasHealthInsurance;

    public MonthlyEmployee(int id, String name, Gender gender, LocalDate hireDate,
                           double monthlySalary, int vacationDays,
                           double bonusPercentage, boolean hasHealthInsurance) {
        super(id, name, gender, hireDate);
        this.monthlySalary = monthlySalary;
        this.vacationDays = vacationDays;
        this.bonusPercentage = bonusPercentage;
        this.hasHealthInsurance = hasHealthInsurance;
    }

    public double getMonthlySalary() { return monthlySalary; }
    public void setMonthlySalary(double monthlySalary) { this.monthlySalary = monthlySalary; }

    public int getVacationDays() { return vacationDays; }
    public void setVacationDays(int vacationDays) { this.vacationDays = vacationDays; }

    public double getBonusPercentage() { return bonusPercentage; }
    public void setBonusPercentage(double bonusPercentage) { this.bonusPercentage = bonusPercentage; }

    public boolean isHasHealthInsurance() { return hasHealthInsurance; }
    public void setHasHealthInsurance(boolean value) { hasHealthInsurance = value; }

    @Override
    public double calculateSalary() {
        return monthlySalary + (monthlySalary * bonusPercentage);
    }

    public int calculateAdditionalVacation() {
        return vacationDays / 5;
    }
}

class HourlyEmployee extends Employee {
    private double hourlyRate;
    private double hoursWorked;
    private double overtimeRate;

    public HourlyEmployee(int id, String name, Gender gender, LocalDate hireDate,
                          double hourlyRate, double hoursWorked, double overtimeRate) {
        super(id, name, gender, hireDate);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
        this.overtimeRate = overtimeRate;
    }

    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    public double getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(double hoursWorked) { this.hoursWorked = hoursWorked; }

    public double getOvertimeRate() { return overtimeRate; }
    public void setOvertimeRate(double overtimeRate) { this.overtimeRate = overtimeRate; }

    @Override
    public double calculateSalary() {
        double regularHours = Math.min(hoursWorked, 40);
        double overtimeHours = Math.max(hoursWorked - 40, 0);
        return regularHours * hourlyRate + overtimeHours * overtimeRate;
    }
}

class Department {
    private int id;
    private String name;
    private Employee manager;
    private List<Employee> employees;

    public Department(int id, String name, Employee manager) {
        this.id = id;
        this.name = name;
        this.manager = manager;
        this.employees = new ArrayList<>();

        if (manager != null) {
            manager.setDepartment(this);
        }
    }

    public Department(int id, String name, Employee manager, List<Employee> employees) {
        this(id, name, manager);
        setEmployees(employees);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Employee getManager() { return manager; }
    public void setManager(Employee manager) {
        this.manager = manager;
        if (manager != null) manager.setDepartment(this);
    }

    public List<Employee> getEmployees() { return employees; }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees != null ? employees : new ArrayList<>();
        for (Employee employee : this.employees) {
            if (employee != null) employee.setDepartment(this);
        }
    }

    public void addEmployee(Employee employee) {
        if (employee != null && !employees.contains(employee)) {
            employees.add(employee);
            employee.setDepartment(this);
        }
    }

    public void removeEmployee(Employee employee) {
        if (employee != null) employees.remove(employee);
    }

    public Employee findEmployee(int employeeId) {
        for (Employee employee : employees) {
            if (employee.getId() == employeeId) return employee;
        }
        return null;
    }

    public double calculateTotalPayroll() {
        double total = 0;
        for (Employee employee : employees) {
            total += employee.calculateSalary();
        }
        return total;
    }

    public void printAllEmployees() {
        for (Employee employee : employees) {
            System.out.println(employee);
            System.out.printf("Salary: %.2f%n", employee.calculateSalary());
        }
    }
}

public class Main {
    public static void main(String[] args) {

        CommissionEmployee employee1 = new CommissionEmployee(
                1, "Ahmed", Gender.MALE, LocalDate.of(2024, 1, 10),
                8000, 0.05, 50000
        );

        MonthlyEmployee employee2 = new MonthlyEmployee(
                2, "Mariam", Gender.FEMALE, LocalDate.of(2023, 5, 15),
                12000, 10, 0.10, true
        );

        HourlyEmployee employee3 = new HourlyEmployee(
                3, "Omar", Gender.MALE, LocalDate.of(2025, 2, 1),
                100, 45, 150
        );

        Department salesDepartment =
                new Department(101, "Sales", employee1);

        salesDepartment.addEmployee(employee1);
        salesDepartment.addEmployee(employee2);
        salesDepartment.addEmployee(employee3);

        System.out.println("===== ALL EMPLOYEES =====");
        salesDepartment.printAllEmployees();

        System.out.println("\n===== TOTAL PAYROLL =====");
        System.out.printf("Total Payroll: %.2f%n",
                salesDepartment.calculateTotalPayroll());

        System.out.println("\n===== FIND EMPLOYEE =====");
        Employee found = salesDepartment.findEmployee(2);
        System.out.println(found != null
                ? "Employee Found: " + found.getName()
                : "Employee Not Found");

        System.out.println("\n===== ADDITIONAL VACATION =====");
        System.out.println("Mariam: " +
                employee2.calculateAdditionalVacation() +
                " additional vacation day(s)");
    }
}
