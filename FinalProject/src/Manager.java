import java.io.*;
import java.util.*;

public class Manager extends Employee {

    private static final long serialVersionUID = 1L;

    private ManagerType type;

    public Manager() {}

    public Manager(String id, String login, String password,
                   String name, String surname,
                   double salary, String department, ManagerType type) {
        super(id, login, password, name, surname, salary, department);
        this.type = type;
    }

    @Override
    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n=== MANAGER MENU — " + getFullName() + " (" + type + ") ===");
            System.out.println("1.  Approve student registrations");
            System.out.println("2.  Add course for registration");
            System.out.println("3.  Assign teacher to course");
            System.out.println("4.  View students (sorted)");
            System.out.println("5.  View teachers");
            System.out.println("6.  Check/create report");
            System.out.println("7.  Manage news");
            System.out.println("8.  Fire employee");
            System.out.println("9.  Assign research supervisor");
            System.out.println("10. View requests");
            System.out.println("11. Sign report");
            System.out.println("12. Send message");
            System.out.println("13. View inbox");
            System.out.println("14. Change password");
            System.out.println("0.  Log out");
            System.out.print("Choice: ");
            String ch = sc.nextLine().trim();
            switch (ch) {
                case "1"  -> approveRegistration(sc);
                case "2"  -> addCourseForRegistration(sc);
                case "3"  -> assignTeacher(sc);
                case "4"  -> viewStudentsSortedByGPA(sc);
                case "5"  -> viewTeachers();
                case "6"  -> checkReport();
                case "7"  -> manageNews(sc);
                case "8"  -> fireEmployee(sc);
                case "9"  -> assignResearcher(sc);
                case "10" -> viewRequests();
                case "11" -> signReport(sc);
                case "12" -> sendMessageMenu(sc);
                case "13" -> viewInbox();
                case "14" -> changePasswordMenu(sc);
                case "0"  -> running = false;
                default   -> System.out.println("Invalid option.");
            }
        }
    }

    public void approveRegistration(Student s) {
        List<RegistrationRequest> requests = Database.getInstance().getPendingRequests();
        Optional<RegistrationRequest> req = requests.stream()
                .filter(r -> r.getStudent().equals(s)).findFirst();
        if (req.isEmpty()) { System.out.println("No request found for " + s.getFullName()); return; }
        s.enrollInCourse(req.get().getCourse());
        req.get().getCourse().enrollStudent(s);
        Database.getInstance().removeRequest(req.get());
        System.out.println("Approved: " + s.getFullName() + " → " + req.get().getCourse().getName());
    }

    private void approveRegistration(Scanner sc) {
        List<RegistrationRequest> requests = Database.getInstance().getPendingRequests();
        if (requests.isEmpty()) { System.out.println("No pending registrations."); return; }
        System.out.println("\n=== Pending Registrations ===");
        for (int i = 0; i < requests.size(); i++) {
            RegistrationRequest r = requests.get(i);
            System.out.printf("%d. %s → %s (%d credits)%n",
                    i + 1, r.getStudent().getFullName(), r.getCourse().getName(), r.getCourse().getCredits());
        }
        System.out.print("Select # (0=cancel): ");
        try {
            int idx = Integer.parseInt(sc.nextLine().trim()) - 1;
            if (idx < 0) return;
            RegistrationRequest req = requests.get(idx);
            System.out.print("Approve? (y/n): ");
            if (sc.nextLine().trim().equalsIgnoreCase("y")) {
                req.getStudent().enrollInCourse(req.getCourse());
                req.getCourse().enrollStudent(req.getStudent());
                Database.getInstance().removeRequest(req);
                System.out.println("Approved.");
            } else {
                Database.getInstance().removeRequest(req);
                System.out.println("Rejected.");
            }
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
    }

    public void addCourseForRegistration() {
        System.out.println("Use addCourseForRegistration(Scanner sc).");
    }

    private void addCourseForRegistration(Scanner sc) {
        System.out.print("Course name: ");      String name = sc.nextLine().trim();
        System.out.print("Credits: ");           int credits = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Target year (1-4): "); int year = Integer.parseInt(sc.nextLine().trim());
        System.out.println("Faculty: 1.SITE 2.BS 3.ISE 4.SE 5.SAM 6.SEPI");
        Faculty[] faculties = Faculty.values();
        int fi = Integer.parseInt(sc.nextLine().trim()) - 1;
        System.out.println("Term: 1.Spring 2.Fall 3.Summer");
        Term[] terms = Term.values();
        int ti = Integer.parseInt(sc.nextLine().trim()) - 1;
        String id = "CRS-" + System.currentTimeMillis();
        Course course = new Course(id, name, credits, year, faculties[fi], terms[ti]);
        Database.getInstance().addCourse(course);
        System.out.println("Course added: " + name);
    }

    public void assignTeacher(Teacher t, Course c) {
        c.addTeacher(t);
        t.addCourse(c);
        System.out.println("Assigned " + t.getFullName() + " to " + c.getName());
    }

    private void assignTeacher(Scanner sc) {
        List<Course> courses = Database.getInstance().getAllCourses();
        List<Teacher> teachers = Database.getInstance().getAllTeachers();
        if (courses.isEmpty() || teachers.isEmpty()) { System.out.println("No courses or teachers."); return; }
        System.out.println("Select course:");
        for (int i = 0; i < courses.size(); i++)
            System.out.println((i + 1) + ". " + courses.get(i).getName());
        System.out.print("Choice: ");
        try {
            int ci = Integer.parseInt(sc.nextLine().trim()) - 1;
            System.out.println("Select teacher:");
            for (int i = 0; i < teachers.size(); i++)
                System.out.println((i + 1) + ". " + teachers.get(i));
            int ti = Integer.parseInt(sc.nextLine().trim()) - 1;
            assignTeacher(teachers.get(ti), courses.get(ci));
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
    }

    public void checkReport() {
        List<Report> reports = Database.getInstance().getAllReports();
        if (reports.isEmpty()) { System.out.println("No reports."); return; }
        System.out.println("\n=== Reports ===");
        reports.forEach(System.out::println);
    }

    public void manageNews() { System.out.println("Use manageNews(Scanner sc)."); }

    private void manageNews(Scanner sc) {
        System.out.println("1. Add news  2. Delete news  3. View all");
        System.out.print("Choice: ");
        String ch = sc.nextLine().trim();
        Database db = Database.getInstance();
        switch (ch) {
            case "1" -> {
                System.out.print("Title: ");   String title = sc.nextLine();
                System.out.print("Content: "); String content = sc.nextLine();
                News news = new News(title, content, this);
                news.publish();
                db.addNews(news);
            }
            case "2" -> {
                List<News> list = db.getAllNews();
                if (list.isEmpty()) { System.out.println("No news."); return; }
                for (int i = 0; i < list.size(); i++)
                    System.out.println((i + 1) + ". " + list.get(i).getTitle());
                System.out.print("Delete #: ");
                try {
                    int idx = Integer.parseInt(sc.nextLine().trim()) - 1;
                    list.get(idx).delete();
                    db.removeNews(list.get(idx));
                } catch (Exception e) { System.out.println("Error."); }
            }
            case "3" -> db.getAllNews().forEach(System.out::println);
        }
    }

    public void viewStudentsSortedByGPA() {
        Database.getInstance().getAllStudents().stream()
                .sorted(Comparator.comparingDouble(Student::getGpa).reversed())
                .forEach(s -> System.out.printf("  %-25s GPA: %.1f  Year: %d%n",
                        s.getFullName(), s.getGpa(), s.getYear()));
    }

    private void viewStudentsSortedByGPA(Scanner sc) {
        System.out.println("Sort: 1.GPA  2.Name  3.Year");
        System.out.print("Choice: ");
        String ch = sc.nextLine().trim();
        List<Student> students = Database.getInstance().getAllStudents();
        if (students.isEmpty()) { System.out.println("No students."); return; }
        Comparator<Student> cmp = switch (ch) {
            case "1" -> Comparator.comparingDouble(Student::getGpa).reversed();
            case "2" -> Comparator.comparing(Student::getSurname);
            case "3" -> Comparator.comparingInt(Student::getYear);
            default  -> Comparator.comparing(Student::getFullName);
        };
        System.out.println("\n=== Students ===");
        students.stream().sorted(cmp).forEach(s ->
                System.out.printf("  %-25s Year:%d  GPA:%.1f  %s%n",
                        s.getFullName(), s.getYear(), s.getGpa(), s.getFaculty()));
    }

    private void viewTeachers() {
        List<Teacher> teachers = Database.getInstance().getAllTeachers();
        if (teachers.isEmpty()) { System.out.println("No teachers."); return; }
        System.out.println("\n=== Teachers ===");
        teachers.forEach(t -> System.out.printf("  %-25s  %s  Rating:%.1f%n",
                t.getFullName(), t.getTitle(), t.getRating()));
    }

    public void fireEmployee(Employee e) {
        Database.getInstance().removeEmployee(e);
        System.out.println(e.getFullName() + " has been fired.");
    }

    private void fireEmployee(Scanner sc) {
        List<Employee> employees = Database.getInstance().getAllEmployees();
        if (employees.isEmpty()) { System.out.println("No employees."); return; }
        System.out.println("Select employee:");
        for (int i = 0; i < employees.size(); i++)
            System.out.println((i + 1) + ". " + employees.get(i).getFullName());
        System.out.print("Choice: ");
        try {
            int idx = Integer.parseInt(sc.nextLine().trim()) - 1;
            Employee emp = employees.get(idx);
            System.out.print("Confirm? (y/n): ");
            if (sc.nextLine().trim().equalsIgnoreCase("y")) fireEmployee(emp);
        } catch (Exception e) { System.out.println("Error."); }
    }

    public void viewRequests() {
        List<RegistrationRequest> reqs = Database.getInstance().getPendingRequests();
        System.out.println("\n=== Pending Requests ===");
        if (reqs.isEmpty()) { System.out.println("None."); return; }
        reqs.forEach(r -> System.out.printf("  %s → %s%n",
                r.getStudent().getFullName(), r.getCourse().getName()));
    }

    public void signReport(Report r) {
        r.sign(this);
        System.out.println("Report signed.");
    }

    private void signReport(Scanner sc) {
        List<Report> reports = Database.getInstance().getUnsignedReports();
        if (reports.isEmpty()) { System.out.println("No reports to sign."); return; }
        System.out.println("Select report:");
        for (int i = 0; i < reports.size(); i++)
            System.out.println((i + 1) + ". " + reports.get(i).getType());
        System.out.print("Choice: ");
        try {
            int idx = Integer.parseInt(sc.nextLine().trim()) - 1;
            signReport(reports.get(idx));
        } catch (Exception e) { System.out.println("Error."); }
    }

    public void assignResearcher(Student s, ResearcherDecorator r) {
        try {
            s.setSupervisor(r);
            System.out.println("Supervisor assigned: " + r.getOwnerName() + " → " + s.getFullName());
        } catch (LowHIndexException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void assignResearcher(Scanner sc) {
        List<Student> fourthYear = Database.getInstance().getAllStudents().stream()
                .filter(s -> s.getYear() == 4 && s.getSupervisor() == null).toList();
        if (fourthYear.isEmpty()) { System.out.println("No eligible students."); return; }
        System.out.println("Select student:");
        for (int i = 0; i < fourthYear.size(); i++)
            System.out.println((i + 1) + ". " + fourthYear.get(i).getFullName());
        System.out.print("Choice: ");
        try {
            int si = Integer.parseInt(sc.nextLine().trim()) - 1;
            List<ResearcherDecorator> researchers = Database.getInstance().getAllResearchers();
            if (researchers.isEmpty()) { System.out.println("No researchers."); return; }
            System.out.println("Select supervisor:");
            for (int i = 0; i < researchers.size(); i++)
                System.out.printf("  %d. %s (h-index: %d)%n",
                        i + 1, researchers.get(i).getOwnerName(), researchers.get(i).getHIndex());
            int ri = Integer.parseInt(sc.nextLine().trim()) - 1;
            assignResearcher(fourthYear.get(si), researchers.get(ri));
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
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

    public ManagerType getType() { return type; }
    public void setType(ManagerType type) { this.type = type; }

    @Override
    public String toString() { return super.toString() + " | " + type; }
}