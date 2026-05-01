import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Запись о посещаемости студента на конкретном занятии.
 */
public class Attendance implements Serializable {

    private static final long serialVersionUID = 1L;

    private Lesson  lesson;
    private Student student;
    private boolean present;
    private Date    date;

    public Attendance() {}

    public Attendance(Lesson lesson, Student student, boolean present) {
        this.lesson  = lesson;
        this.student = student;
        this.present = present;
        this.date    = new Date();
    }

    // ─── Методы ──────────────────────────────────────────────────────────────

    /** Отметить присутствие. */
    public void mark(boolean present) {
        this.present = present;
        System.out.printf("Marked %s as %s%n",
                student.getFullName(), present ? "PRESENT" : "ABSENT");
    }

    /**
     * Рассчитать процент посещаемости студента по курсу.
     * Статический хелпер — используется из списка записей.
     */
    public static double getRate(Student s, Course c, List<Attendance> all) {
        List<Attendance> relevant = all.stream()
                .filter(a -> a.getStudent().equals(s)
                        && a.getLesson() != null
                        && a.getLesson().getCourse().equals(c))
                .toList();
        if (relevant.isEmpty()) return 0.0;
        long attended = relevant.stream().filter(a -> a.present).count();
        return (attended * 100.0) / relevant.size();
    }

    // ─── Геттеры ─────────────────────────────────────────────────────────────

    public Lesson  getLesson()  { return lesson; }
    public Student getStudent() { return student; }
    public boolean isPresent()  { return present; }
    public Date    getDate()    { return date; }

    @Override
    public String toString() {
        return String.format("[Attendance] %s | %s | %s | %s",
                student != null ? student.getFullName() : "?",
                lesson  != null ? lesson.getCourse().getName() : "?",
                present ? "PRESENT" : "ABSENT",
                date != null ? new SimpleDateFormat("dd.MM.yyyy").format(date) : "?");
    }
}
