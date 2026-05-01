import java.io.*;
import java.util.*;

public class Teacher extends Employee {

    private static final long serialVersionUID = 1L;

    private TeacherTitle title;
    private List<Course> courses;
    private double rating;
    private int ratingCount;
    private ResearcherDecorator researcherRole; // Decorator pattern

    public Teacher() {}

    public Teacher(String id, String login, String password,
                   String name, String surname,
                   double salary, String department,
                   TeacherTitle title) {
        super(id, login, password, name, surname, salary, department);
        this.title = title;
        this.courses = new ArrayList<>();
        this.rating = 0.0;
        this.ratingCount = 0;
        // профессора — всегда исследователи
        if (title.isProfessor()) {
            this.researcherRole = new ResearcherDecorator(this);
        }
    }

    @Override
    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n=== TEACHER MENU — " + getFullName() + " (" + title + ") ===");
            System.out.println("1. View my courses");
            System.out.println("2. View students");
            System.out.println("3. Put mark");
            System.out.println("4. Manage attendance");
            System.out.println("5. Send message");
            System.out.println("6. Submit complaint");
            System.out.println("7. View inbox");
            System.out.println("8. Change password");
            if (isResearcher()) System.out.println("9. Research menu");
            System.out.println("0. Log out");
            System.out.print("Choice: ");
            String ch = sc.nextLine().trim();
            switch (ch) {
                case "1" -> viewCourses();
                case "2" -> viewStudents();
                case "3" -> putMarkMenu(sc);
                case "4" -> manageAttendance();
                case "5" -> sendMessageMenu(sc);
                case "6" -> complaintMenu(sc);
                case "7" -> viewInbox();
                case "8" -> changePasswordMenu(sc);
                case "9" -> { if (isResearcher()) researcherRole.showResearchMenu(sc); }
                case "0" -> running = false;
                default  -> System.out.println("Invalid option.");
            }
        }
    }

    public void viewCourses() {
        if (courses.isEmpty()) { System.out.println("No courses assigned."); return; }
        System.out.println("\n=== My Courses ===");
        for (int i = 0; i < courses.size(); i++)
            System.out.println((i + 1) + ". " + courses.get(i));
    }

    public void viewStudents() {
        if (courses.isEmpty()) { System.out.println("No courses."); return; }
        for (Course c : courses) {
            System.out.println("\nCourse: " + c.getName());
            List<Student> students = c.getEnrolledStudents();
            if (students.isEmpty()) { System.out.println("  No students."); continue; }
            students.forEach(s -> System.out.println("  " + s));
        }
    }

    // putMark(Student s, Course c, Mark m) — signature from diagram
    public void putMark(Student s, Course c, Mark m) {
        s.addMark(m);
        Database.getInstance().saveMark(m);
        System.out.println("Mark saved: " + m);
    }

    private void putMarkMenu(Scanner sc) {
        if (courses.isEmpty()) { System.out.println("No courses."); return; }
        viewCourses();
        System.out.print("Select course: ");
        try {
            int ci = Integer.parseInt(sc.nextLine().trim()) - 1;
            Course course = courses.get(ci);
            List<Student> students = course.getEnrolledStudents();
            if (students.isEmpty()) { System.out.println("No students."); return; }

            System.out.println("Select student:");
            for (int i = 0; i < students.size(); i++)
                System.out.println((i + 1) + ". " + students.get(i).getFullName());
            int si = Integer.parseInt(sc.nextLine().trim()) - 1;
            Student student = students.get(si);

            System.out.print("ATT1 (0-30): ");
            double a1 = Double.parseDouble(sc.nextLine().trim());
            System.out.print("ATT2 (0-30): ");
            double a2 = Double.parseDouble(sc.nextLine().trim());
            System.out.print("Final (0-40): ");
            double fin = Double.parseDouble(sc.nextLine().trim());

            Mark mark = new Mark(student, course, a1, a2, fin);
            putMark(student, course, mark);
            if (mark.isFailed()) System.out.println("WARNING: Student failed.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void manageAttendance() {
        Scanner sc = new Scanner(System.in);
        if (courses.isEmpty()) { System.out.println("No courses."); return; }
        viewCourses();
        System.out.print("Select course: ");
        try {
            int ci = Integer.parseInt(sc.nextLine().trim()) - 1;
            Course course = courses.get(ci);
            List<Student> students = course.getEnrolledStudents();
            if (students.isEmpty()) { System.out.println("No students."); return; }
            for (Student s : students) {
                System.out.print(s.getFullName() + " present? (y/n): ");
                boolean present = sc.nextLine().trim().equalsIgnoreCase("y");
                Attendance att = new Attendance(null, s, present);
                Database.getInstance().saveAttendance(att);
            }
            System.out.println("Attendance saved.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public boolean isProfessor() { return title != null && title.isProfessor(); }

    public void addRating(int score) {
        if (score < 1 || score > 5) { System.out.println("Rating must be 1-5."); return; }
        rating = (rating * ratingCount + score) / (ratingCount + 1);
        ratingCount++;
    }

    public void becomeResearcher() {
        if (researcherRole == null) {
            researcherRole = new ResearcherDecorator(this);
            System.out.println(getFullName() + " is now a Researcher.");
        }
    }

    public void addCourse(Course course) { courses.add(course); }
    public void removeCourse(Course course) { courses.remove(course); }
    public boolean isResearcher() { return researcherRole != null; }
    public ResearcherDecorator getResearcherRole() { return researcherRole; }
    public TeacherTitle getTitle() { return title; }
    public void setTitle(TeacherTitle title) {
        this.title = title;
        if (title.isProfessor() && researcherRole == null)
            researcherRole = new ResearcherDecorator(this);
    }
    public List<Course> getCourses() { return courses; }
    public double getRating() { return rating; }

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

    private void complaintMenu(Scanner sc) {
        List<Employee> employees = Database.getInstance().getAllEmployees();
        for (int i = 0; i < employees.size(); i++)
            System.out.println((i + 1) + ". " + employees.get(i).getFullName());
        System.out.print("Select recipient: ");
        try {
            int idx = Integer.parseInt(sc.nextLine().trim()) - 1;
            System.out.print("Complaint text: ");
            submitComplaint(employees.get(idx), sc.nextLine());
        } catch (Exception e) { System.out.println("Error."); }
    }

    private void changePasswordMenu(Scanner sc) {
        System.out.print("Current password: ");
        String old = sc.nextLine();
        System.out.print("New password: ");
        changePassword(old, sc.nextLine());
    }

    @Override
    public String toString() {
        return super.toString() + " | " + title +
                (isResearcher() ? " | Researcher h=" + researcherRole.getHIndex() : "") +
                String.format(" | rating: %.1f", rating);
    }
}