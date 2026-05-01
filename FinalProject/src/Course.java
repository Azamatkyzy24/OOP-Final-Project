import java.io.Serializable;
import java.util.*;

/**
 * Учебный курс.
 */
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    private String         id;
    private String         name;
    private int            credits;
    private List<Teacher>  teachers;
    private List<Student>  enrolledStudents;
    private List<Lesson>   lessons;
    private Term           targetTerm;
    private Faculty        targetFaculty;
    private int            targetYear;   // для какого года студентов

    public Course() {}

    public Course(String id, String name, int credits,
                  int targetYear, Faculty targetFaculty, Term targetTerm) {
        this.id              = id;
        this.name            = name;
        this.credits         = credits;
        this.targetYear      = targetYear;
        this.targetFaculty   = targetFaculty;
        this.targetTerm      = targetTerm;
        this.teachers        = new ArrayList<>();
        this.enrolledStudents = new ArrayList<>();
        this.lessons         = new ArrayList<>();
    }

    // ─── Методы ──────────────────────────────────────────────────────────────

    public void addTeacher(Teacher t) {
        if (!teachers.contains(t)) teachers.add(t);
    }

    public void removeTeacher(Teacher t) { teachers.remove(t); }

    /** enroll() — псевдоним из диаграммы */
    public void enroll(Student s)        { enrollStudent(s); }

    public void enrollStudent(Student s) {
        if (!enrolledStudents.contains(s)) enrolledStudents.add(s);
    }

    public void removeStudent(Student s) { enrolledStudents.remove(s); }

    public void addLesson(Lesson l)      { lessons.add(l); }

    /** getStudents() — псевдоним из диаграммы */
    public List<Student> getStudents()   { return getEnrolledStudents(); }

    // ─── Геттеры / Сеттеры ──────────────────────────────────────────────────

    public String         getId()             { return id; }
    public String         getName()           { return name; }
    public void           setName(String n)   { this.name = n; }
    public int            getCredits()        { return credits; }
    public void           setCredits(int c)   { this.credits = c; }
    public List<Teacher>  getTeachers()       { return teachers; }
    public List<Student>  getEnrolledStudents(){ return enrolledStudents; }
    public List<Lesson>   getLessons()        { return lessons; }
    public Term           getTargetTerm()     { return targetTerm; }
    public Faculty        getTargetFaculty()  { return targetFaculty; }
    public int            getTargetYear()     { return targetYear; }
    public void           setTargetYear(int y){ this.targetYear = y; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        return Objects.equals(id, ((Course) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return String.format("[Course] %s (%d cr) | Year %d | %s | %s | students: %d",
                name, credits, targetYear, targetFaculty, targetTerm, enrolledStudents.size());
    }
}
