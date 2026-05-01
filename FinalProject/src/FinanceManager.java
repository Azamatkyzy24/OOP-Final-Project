import java.io.Serializable;
import java.util.List;
import java.util.Scanner;

/**
 * Финансовый менеджер — управляет бюджетом и зарплатами.
 */
public class FinanceManager extends Employee {

    private static final long serialVersionUID = 1L;

    private double budget;

    public FinanceManager() {}

    public FinanceManager(String id, String login, String password,
                          String name, String surname,
                          double salary, String department,
                          double budget) {
        super(id, login, password, name, surname, salary, department);
        this.budget = budget;
    }

    @Override
    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n=== FINANCE MANAGER — " + getFullName() + " ===");
            System.out.println("1. View budget");
            System.out.println("2. Give salary");
            System.out.println("3. Check salary paid");
            System.out.println("4. Check debts");
            System.out.println("5. Send message");
            System.out.println("6. View inbox");
            System.out.println("7. Change password");
            System.out.println("0. Log out");
            System.out.print("Choice: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> viewBudget();
                case "2" -> giveSalary(sc);
                case "3" -> checkSalaryPaid();
                case "4" -> checkDebts();
                case "5" -> sendMessageMenu(sc);
                case "6" -> viewInbox();
                case "7" -> changePasswordMenu(sc);
                case "0" -> running = false;
                default  -> System.out.println("Invalid option.");
            }
        }
    }

    /** Просмотреть бюджет. */
    public double viewBudget() {
        System.out.printf("Current university budget: %.2f KZT%n", budget);
        return budget;
    }

    /** Выдать зарплаты всем сотрудникам. */
    public void giveSalary(Scanner sc) {
        List<Employee> employees = Database.getInstance().getAllEmployees();
        if (employees.isEmpty()) { System.out.println("No employees."); return; }

        double total = employees.stream().mapToDouble(Employee::getSalary).sum();
        System.out.printf("Total salaries to pay: %.2f KZT%n", total);

        if (budget < total) {
            System.out.println("WARNING: Insufficient budget! Debt: " + (total - budget));
            return;
        }

        System.out.print("Pay all salaries? (y/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("y")) {
            budget -= total;
            employees.forEach(e ->
                    System.out.printf("  Paid %.2f KZT to %s%n", e.getSalary(), e.getFullName()));
            System.out.printf("Remaining budget: %.2f KZT%n", budget);
            Logger.getInstance().log("SALARIES_PAID", this, "Total: " + total);
        }
    }

    /** Показать зарплаты всех. */
    public void checkSalaryPaid() {
        System.out.println("=== Salary Status ===");
        Database.getInstance().getAllEmployees().forEach(e ->
                System.out.printf("  %-25s  %.2f KZT%n", e.getFullName(), e.getSalary()));
    }

    /** Проверить задолженности. */
    public void checkDebts() {
        double total = Database.getInstance().getAllEmployees()
                .stream().mapToDouble(Employee::getSalary).sum();
        System.out.println("=== Budget Check ===");
        System.out.printf("Budget          : %.2f KZT%n", budget);
        System.out.printf("Monthly salaries: %.2f KZT%n", total);
        if (budget < total)
            System.out.printf("DEBT            : %.2f KZT%n", total - budget);
        else
            System.out.printf("Surplus         : %.2f KZT%n", budget - total);
    }

    /** Управление зарплатой конкретного сотрудника (из диаграммы). */
    public void manageSalary() {
        Scanner sc = new Scanner(System.in);
        giveSalary(sc);
    }

    // ─── Приватные вспомогательные ───────────────────────────────────────────

    private void sendMessageMenu(Scanner sc) {
        List<Employee> employees = Database.getInstance().getAllEmployees();
        System.out.println("Send to:");
        for (int i = 0; i < employees.size(); i++)
            System.out.println((i + 1) + ". " + employees.get(i));
        System.out.print("Select: ");
        try {
            int idx = Integer.parseInt(sc.nextLine().trim()) - 1;
            System.out.print("Message: ");
            sendMessage(employees.get(idx), sc.nextLine());
        } catch (Exception e) { System.out.println("Error."); }
    }

    private void changePasswordMenu(Scanner sc) {
        System.out.print("Current password: ");
        String old = sc.nextLine();
        System.out.print("New password: ");
        changePassword(old, sc.nextLine());
    }

    // ─── Геттеры / Сеттеры ───────────────────────────────────────────────────

    public double getBudget()           { return budget; }
    public void   setBudget(double b)   { this.budget = b; }

    @Override
    public String toString() {
        return super.toString() + String.format(" | budget: %.2f KZT", budget);
    }
}
