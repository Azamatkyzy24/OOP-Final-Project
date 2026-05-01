import java.io.*;
import java.util.*;

public class Admin extends Employee {

    private static final long serialVersionUID = 1L;

    public Admin() {}

    public Admin(String id, String login, String password,
                 String name, String surname,
                 double salary, String department) {
        super(id, login, password, name, surname, salary, department);
    }

    @Override
    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n=== ADMIN MENU — " + getFullName() + " ===");
            System.out.println("1. Add user");
            System.out.println("2. Remove user");
            System.out.println("3. Update user");
            System.out.println("4. View all users");
            System.out.println("5. View log files");
            System.out.println("6. Manage salary");
            System.out.println("7. Send message");
            System.out.println("8. View inbox");
            System.out.println("9. Change password");
            System.out.println("0. Log out");
            System.out.print("Choice: ");
            String ch = sc.nextLine().trim();
            switch (ch) {
                case "1" -> addUser(sc);
                case "2" -> removeUser(sc);
                case "3" -> updateUser(sc);
                case "4" -> manageUsers();
                case "5" -> viewLogFiles();
                case "6" -> manageSalary(sc);
                case "7" -> sendMessageMenu(sc);
                case "8" -> viewInbox();
                case "9" -> changePasswordMenu(sc);
                case "0" -> running = false;
                default  -> System.out.println("Invalid option.");
            }
        }
    }

    public void manageUsers() {
        List<User> users = Database.getInstance().getAllUsers();
        if (users.isEmpty()) { System.out.println("No users."); return; }
        System.out.println("\n=== All Users ===");
        users.forEach(System.out::println);
    }

    public void addUser(User u) {
        Database.getInstance().addUser(u);
        Logger.getInstance().log("ADD_USER", this, "Added: " + u.getLogin());
        System.out.println("User added: " + u);
    }

    private void addUser(Scanner sc) {
        System.out.println("Type: 1.Student  2.Teacher  3.Manager  4.Admin  5.FinanceManager");
        System.out.print("Choice: ");
        String type = sc.nextLine().trim();
        System.out.print("ID: ");        String id = sc.nextLine();
        System.out.print("Login: ");     String login = sc.nextLine();
        System.out.print("Password: ");  String password = sc.nextLine();
        System.out.print("Name: ");      String name = sc.nextLine();
        System.out.print("Surname: ");   String surname = sc.nextLine();

        try {
            switch (type) {
                case "1" -> {
                    System.out.print("Year (1-4): "); int year = Integer.parseInt(sc.nextLine().trim());
                    System.out.println("Faculty: 1.SITE 2.BS 3.ISE 4.SE 5.SAM 6.SEPI");
                    Faculty faculty = Faculty.values()[Integer.parseInt(sc.nextLine().trim()) - 1];
                    System.out.println("Degree: 1.BACHELOR 2.MASTER 3.PHD");
                    DegreeType degree = DegreeType.values()[Integer.parseInt(sc.nextLine().trim()) - 1];
                    addUser(new Student(id, login, password, name, surname, year, faculty, degree));
                }
                case "2" -> {
                    System.out.print("Salary: ");     double sal = Double.parseDouble(sc.nextLine().trim());
                    System.out.print("Department: "); String dept = sc.nextLine();
                    System.out.println("Title: 1.TUTOR 2.LECTOR 3.SENIOR_LECTOR 4.PROFESSOR");
                    TeacherTitle title = TeacherTitle.values()[Integer.parseInt(sc.nextLine().trim()) - 1];
                    addUser(new Teacher(id, login, password, name, surname, sal, dept, title));
                }
                case "3" -> {
                    System.out.print("Salary: ");     double sal = Double.parseDouble(sc.nextLine().trim());
                    System.out.print("Department: "); String dept = sc.nextLine();
                    System.out.println("Type: 1.OR 2.DEPARTMENT 3.DEAN 4.RECTOR");
                    ManagerType mt = ManagerType.values()[Integer.parseInt(sc.nextLine().trim()) - 1];
                    addUser(new Manager(id, login, password, name, surname, sal, dept, mt));
                }
                case "4" -> {
                    System.out.print("Salary: ");     double sal = Double.parseDouble(sc.nextLine().trim());
                    System.out.print("Department: "); String dept = sc.nextLine();
                    addUser(new Admin(id, login, password, name, surname, sal, dept));
                }
                case "5" -> {
                    System.out.print("Salary: ");     double sal = Double.parseDouble(sc.nextLine().trim());
                    System.out.print("Department: "); String dept = sc.nextLine();
                    System.out.print("Budget: ");     double budget = Double.parseDouble(sc.nextLine().trim());
                    addUser(new FinanceManager(id, login, password, name, surname, sal, dept, budget));
                }
                default -> System.out.println("Unknown type.");
            }
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
    }

    public void removeUser(User u) {
        Database.getInstance().removeUser(u);
        Logger.getInstance().log("REMOVE_USER", this, "Removed: " + u.getLogin());
        System.out.println("Removed: " + u.getFullName());
    }

    private void removeUser(Scanner sc) {
        List<User> users = Database.getInstance().getAllUsers();
        if (users.isEmpty()) { System.out.println("No users."); return; }
        System.out.println("Select user:");
        for (int i = 0; i < users.size(); i++)
            System.out.println((i + 1) + ". " + users.get(i));
        System.out.print("Choice: ");
        try {
            int idx = Integer.parseInt(sc.nextLine().trim()) - 1;
            User u = users.get(idx);
            System.out.print("Confirm remove " + u.getFullName() + "? (y/n): ");
            if (sc.nextLine().trim().equalsIgnoreCase("y")) removeUser(u);
        } catch (Exception e) { System.out.println("Error."); }
    }

    public void updateUser(User u) {
        Database.getInstance().saveUser(u);
        Logger.getInstance().log("UPDATE_USER", this, "Updated: " + u.getLogin());
        System.out.println("User updated.");
    }

    private void updateUser(Scanner sc) {
        List<User> users = Database.getInstance().getAllUsers();
        if (users.isEmpty()) { System.out.println("No users."); return; }
        System.out.println("Select user:");
        for (int i = 0; i < users.size(); i++)
            System.out.println((i + 1) + ". " + users.get(i));
        System.out.print("Choice: ");
        try {
            int idx = Integer.parseInt(sc.nextLine().trim()) - 1;
            User u = users.get(idx);
            System.out.println("Field: 1.Name  2.Surname  3.Login");
            System.out.print("Choice: "); String field = sc.nextLine().trim();
            System.out.print("New value: "); String val = sc.nextLine().trim();
            switch (field) {
                case "1" -> u.setName(val);
                case "2" -> u.setSurname(val);
                case "3" -> u.setLogin(val);
                default -> { System.out.println("Unknown field."); return; }
            }
            updateUser(u);
        } catch (Exception e) { System.out.println("Error."); }
    }

    public void viewLogFiles() {
        List<String> logs = Logger.getInstance().getLogs();
        if (logs.isEmpty()) { System.out.println("No logs."); return; }
        System.out.println("\n=== Log Files ===");
        logs.forEach(System.out::println);
    }

    public User viewUser(String id) {
        return Database.getInstance().getUserById(id);
    }

    private void manageSalary(Scanner sc) {
        List<Employee> employees = Database.getInstance().getAllEmployees();
        if (employees.isEmpty()) { System.out.println("No employees."); return; }
        System.out.println("Select employee:");
        for (int i = 0; i < employees.size(); i++)
            System.out.printf("  %d. %-25s %.2f KZT%n",
                    i + 1, employees.get(i).getFullName(), employees.get(i).getSalary());
        System.out.print("Choice: ");
        try {
            int idx = Integer.parseInt(sc.nextLine().trim()) - 1;
            System.out.print("New salary: ");
            double sal = Double.parseDouble(sc.nextLine().trim());
            employees.get(idx).setSalary(sal);
            Logger.getInstance().log("UPDATE_SALARY", this,
                    "Salary updated: " + employees.get(idx).getLogin());
            System.out.println("Salary updated.");
        } catch (Exception e) { System.out.println("Error."); }
    }

    private void sendMessageMenu(Scanner sc) {
        List<Employee> employees = Database.getInstance().getAllEmployees();
        for (int i = 0; i < employees.size(); i++)
            System.out.println((i + 1) + ". " + employees.get(i).getFullName());
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

    @Override
    public String toString() { return super.toString() + " | role: Admin"; }
}