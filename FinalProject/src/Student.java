import java.io.*;
import java.util.*;

public class Student extends User {

    private static final long serialVersionUID = 1L;

    private static final int MAX_CREDITS = 21;
    private static final int MAX_FAILS   = 3;

    private int year;
    private double gpa;
    private int credits;
    private int failCount;
    private Faculty faculty;
    private DegreeType degree;
    private Researcher supervisor;
    private List<Course> enrolledCourses;
    private List<Mark> marks;
    private ResearcherDecorator researcherRole;

    public Student() {}

    public Student(String id, String login, String password,
                   String name, String surname,
                   int year, Faculty faculty, DegreeType degree) {
        super(id, login, password, name, surname);
        this.year = year;
        this.faculty = faculty;
        this.degree = degree;
        this.enrolledCourses = new ArrayList<>();
        this.marks = new ArrayList<>();
        this.gpa = 0.0;
        this.credits = 0;
        this.failCount = 0;
    }

    @Override
    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n=== STUDENT MENU — " + getFullName() + " | Year " + year + " ===");
            System.out.println("1. View available courses");
            System.out.println("2. Register for course");
            System.out.println("3. Drop course");
            System.out.println("4. View marks");
            System.out.println("5. View transcript");
            System.out.println("6. Rate teacher");
            System.out.println("7. View attestation");
            System.out.println("8. View journal");
            System.out.println("9. View news");
            System.out.println("10. Change password");
            if (year == 4) System.out.println("11. Ask for research supervisor");
            if (researcherRole != null) System.out.println("12. Research menu");
            System.out.println("0. Log out");
            System.out.print("Choice: ");
            String ch = sc.nextLine().trim();
            switch (ch) {
                case "1"  -> viewCourses();
                case "2"  -> registerForCourse(sc);
                case "3"  -> dropCourse(sc);
                case "4"  -> viewMarks();
                case "5"  -> getTranscript().print();
                case "6"  -> rateTeacher(sc);
                case "7"  -> viewAttestation();
                case "8"  -> viewJournal();
                case "9"  -> viewNews();
                case "10" -> changePasswordMenu(sc);
                case "11" -> { if (year == 4) askForResearcher(); }
                case "12" -> { if (researcherRole != null) researcherRole.showResearchMenu(sc); }
                case "0"  -> running = false;
                default   -> System.out.println("Invalid option.");
            }
        }
    }

    public void registerForCourse(Course c) {
        if (credits + c.getCredits() > MAX_CREDITS)
            throw new CreditLimitException(this, c);
        Database.getInstance().addRegistrationRequest(this, c);
        System.out.println("Registration request sent for: " + c.getName());
    }

    private void registerForCourse(Scanner sc) {
        List<Course> available = Database.getInstance().getAvailableCourses(year, faculty);
        available.removeAll(enrolledCourses);
        if (available.isEmpty()) { System.out.println("No courses available."); return; }
        System.out.println("\nAvailable courses:");
        for (int i = 0; i < available.size(); i++)
            System.out.println((i + 1) + ". " + available.get(i));
        System.out.print("Choice: ");
        try {
            int idx = Integer.parseInt(sc.nextLine().trim()) - 1;
            registerForCourse(available.get(idx));
        } catch (CreditLimitException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid selection.");
        }
    }

    public void dropCourse(Course c) {
        if (!enrolledCourses.contains(c)) { System.out.println("Not enrolled in this course."); return; }
        enrolledCourses.remove(c);
        c.removeStudent(this);
        credits -= c.getCredits();
        System.out.println("Dropped: " + c.getName());
    }

    private void dropCourse(Scanner sc) {
        if (enrolledCourses.isEmpty()) { System.out.println("No courses enrolled."); return; }
        System.out.println("Select course to drop:");
        for (int i = 0; i < enrolledCourses.size(); i++)
            System.out.println((i + 1) + ". " + enrolledCourses.get(i).getName());
        System.out.print("Choice: ");
        try {
            int idx = Integer.parseInt(sc.nextLine().trim()) - 1;
            dropCourse(enrolledCourses.get(idx));
        } catch (Exception e) { System.out.println("Invalid selection."); }
    }

    public void viewMarks() {
        if (marks.isEmpty()) { System.out.println("No marks yet."); return; }
        System.out.println("\n=== My Marks ===");
        marks.forEach(m -> System.out.println("  " + m));
        System.out.printf("GPA: %.2f%n", gpa);
    }

    public Transcript getTranscript() {
        return new Transcript(this, new ArrayList<>(marks));
    }

    public void rateTeacher(Teacher t, int r) {
        t.addRating(r);
        System.out.printf("Rated %s: %d/5%n", t.getFullName(), r);
    }

    private void rateTeacher(Scanner sc) {
        if (enrolledCourses.isEmpty()) { System.out.println("No courses enrolled."); return; }
        System.out.println("Select course:");
        for (int i = 0; i < enrolledCourses.size(); i++)
            System.out.println((i + 1) + ". " + enrolledCourses.get(i).getName());
        System.out.print("Choice: ");
        try {
            int ci = Integer.parseInt(sc.nextLine().trim()) - 1;
            List<Teacher> teachers = enrolledCourses.get(ci).getTeachers();
            if (teachers.isEmpty()) { System.out.println("No teacher assigned."); return; }
            System.out.println("Select teacher:");
            for (int i = 0; i < teachers.size(); i++)
                System.out.println((i + 1) + ". " + teachers.get(i).getFullName());
            int ti = Integer.parseInt(sc.nextLine().trim()) - 1;
            System.out.print("Rating (1-5): ");
            int score = Integer.parseInt(sc.nextLine().trim());
            rateTeacher(teachers.get(ti), score);
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
    }

    public void viewCourses() {
        List<Course> available = Database.getInstance().getAvailableCourses(year, faculty);
        System.out.println("\n=== Available Courses (Year " + year + ") ===");
        available.forEach(c -> System.out.println("  " + c));
        System.out.println("\n=== My Enrolled Courses ===");
        if (enrolledCourses.isEmpty()) System.out.println("  None.");
        else enrolledCourses.forEach(c -> System.out.println("  " + c));
    }

    public void askForResearcher() {
        if (year != 4) { System.out.println("Only 4th year students can request a supervisor."); return; }
        if (supervisor != null) { System.out.println("You already have a supervisor."); return; }
        List<ResearcherDecorator> researchers = Database.getInstance().getAllResearchers();
        List<ResearcherDecorator> eligible = researchers.stream()
                .filter(r -> r.getHIndex() >= 3).toList();
        if (eligible.isEmpty()) { System.out.println("No eligible supervisors."); return; }
        System.out.println("\nEligible supervisors (h-index ≥ 3):");
        for (int i = 0; i < eligible.size(); i++)
            System.out.printf("  %d. %s (h-index: %d)%n",
                    i + 1, eligible.get(i).getOwnerName(), eligible.get(i).getHIndex());
        Scanner sc = new Scanner(System.in);
        System.out.print("Select: ");
        try {
            int idx = Integer.parseInt(sc.nextLine().trim()) - 1;
            Database.getInstance().addSupervisorRequest(this, eligible.get(idx));
            System.out.println("Request sent. Waiting for manager approval.");
        } catch (Exception e) { System.out.println("Error."); }
    }

    public void viewAttestation() {
        if (marks.isEmpty()) { System.out.println("No attestation data."); return; }
        System.out.println("\n=== Attestation ===");
        for (Mark m : marks) {
            System.out.printf("  %-20s ATT1:%.1f ATT2:%.1f Final:%.1f Total:%.1f (%s)%n",
                    m.getCourse().getName(),
                    m.getAtt1(), m.getAtt2(), m.getFinalExam(),
                    m.getTotal(), m.getLetterGrade());
        }
    }

    public void viewJournal() {
        System.out.println("\n=== Journal (Attendance) ===");
        List<Attendance> list = Database.getInstance().getAttendanceForStudent(this);
        if (list.isEmpty()) { System.out.println("No records."); return; }
        list.forEach(System.out::println);
    }

    public void enrollInCourse(Course course) {
        enrolledCourses.add(course);
        credits += course.getCredits();
    }

    public void addMark(Mark mark) {
        marks.removeIf(m -> m.getCourse().equals(mark.getCourse()));
        marks.add(mark);
        if (mark.isFailed()) {
            failCount++;
            if (failCount > MAX_FAILS)
                throw new MaxFailsException(this);
        }
        recalcGPA();
    }

    private void recalcGPA() {
        if (marks.isEmpty()) { gpa = 0; return; }
        double avg = marks.stream().mapToDouble(Mark::getTotal).average().orElse(0);
        // шкала 4.0
        if (avg >= 90) gpa = 4.0;
        else if (avg >= 80) gpa = 3.0;
        else if (avg >= 70) gpa = 2.0;
        else if (avg >= 55) gpa = 1.0;
        else gpa = 0.0;
    }

    public void becomeResearcher() {
        if (researcherRole == null) {
            researcherRole = new ResearcherDecorator(this);
            System.out.println(getFullName() + " is now a Researcher.");
        }
    }

    public void setSupervisor(Researcher supervisor) {
        if (supervisor instanceof ResearcherDecorator rd && rd.getHIndex() < 3)
            throw new LowHIndexException(rd);
        this.supervisor = supervisor;
        System.out.println("Supervisor assigned: " + supervisor.toString());
    }

    private void changePasswordMenu(Scanner sc) {
        System.out.print("Current password: ");
        String old = sc.nextLine();
        System.out.print("New password: ");
        changePassword(old, sc.nextLine());
    }

    // Getters
    public int getYear()                    { return year; }
    public void setYear(int year)           { this.year = year; }
    public double getGpa()                  { return gpa; }
    public int getCredits()                 { return credits; }
    public int getFailCount()               { return failCount; }
    public Faculty getFaculty()             { return faculty; }
    public DegreeType getDegree()           { return degree; }
    public Researcher getSupervisor()       { return supervisor; }
    public List<Course> getEnrolledCourses(){ return enrolledCourses; }
    public List<Mark> getMarks()            { return marks; }
    public boolean isResearcher()           { return researcherRole != null; }
    public ResearcherDecorator getResearcherRole() { return researcherRole; }

    @Override
    public String toString() {
        return super.toString() +
                String.format(" | Year %d | %s | GPA: %.1f | Credits: %d", year, faculty, gpa, credits);
    }
}