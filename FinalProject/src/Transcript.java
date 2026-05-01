import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Академическая транскрипция студента.
 */
public class Transcript implements Serializable {

    private static final long serialVersionUID = 1L;

    private Student     student;
    private List<Mark>  marks;
    private double      gpa;
    private Date        generatedAt;
    private boolean     signed;
    private Employee    signedBy;

    public Transcript() {}

    public Transcript(Student student, List<Mark> marks) {
        this.student     = student;
        this.marks       = new ArrayList<>(marks);
        this.generatedAt = new Date();
        this.signed      = false;
        this.gpa         = calcGPA();
    }

    // ─── Бизнес-логика ───────────────────────────────────────────────────────

    public double calcGPA() {
        if (marks == null || marks.isEmpty()) return 0.0;
        double avg = marks.stream().mapToDouble(Mark::getTotal).average().orElse(0);
        if (avg >= 90) return 4.0;
        if (avg >= 80) return 3.0;
        if (avg >= 70) return 2.0;
        if (avg >= 55) return 1.0;
        return 0.0;
    }

    public void generate() {
        this.generatedAt = new Date();
        this.gpa = calcGPA();
        System.out.println("Transcript generated for " + student.getFullName());
    }

    public void print() {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║         ACADEMIC TRANSCRIPT              ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.printf("  Student  : %s%n", student.getFullName());
        System.out.printf("  Faculty  : %s%n", student.getFaculty());
        System.out.printf("  Degree   : %s%n", student.getDegree());
        System.out.printf("  Year     : %d%n", student.getYear());
        System.out.printf("  Generated: %s%n",
                new SimpleDateFormat("dd.MM.yyyy HH:mm").format(generatedAt));
        System.out.println("──────────────────────────────────────────");
        if (marks.isEmpty()) {
            System.out.println("  No marks recorded.");
        } else {
            System.out.printf("  %-20s  %6s  %6s  %7s  %6s  %s%n",
                    "Course", "ATT1", "ATT2", "Final", "Total", "Grade");
            System.out.println("  " + "─".repeat(68));
            for (Mark m : marks) {
                System.out.printf("  %-20s  %6.1f  %6.1f  %7.1f  %6.1f  %s%n",
                        m.getCourse().getName(),
                        m.getAtt1(), m.getAtt2(), m.getFinalExam(),
                        m.getTotal(), m.getLetterGrade());
            }
        }
        System.out.println("──────────────────────────────────────────");
        System.out.printf("  GPA: %.2f / 4.00%n", gpa);
        if (signed) System.out.printf("  Signed by: %s%n", signedBy.getFullName());
        System.out.println("══════════════════════════════════════════");
    }

    /** Подписать транскрипцию. */
    public void sign(Employee signer) {
        this.signed   = true;
        this.signedBy = signer;
        System.out.println("Transcript signed by " + signer.getFullName());
    }

    // ─── Геттеры ─────────────────────────────────────────────────────────────

    public Student    getStudent()     { return student; }
    public List<Mark> getMarks()       { return marks; }
    public double     getGpa()         { return gpa; }
    public Date       getGeneratedAt() { return generatedAt; }
    public boolean    isSigned()       { return signed; }
    public Employee   getSignedBy()    { return signedBy; }
}
