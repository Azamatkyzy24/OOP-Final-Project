import java.io.Serializable;
import java.util.*;

/**
 * Оценка студента по курсу.
 * Итог = ATT1 (0-30) + ATT2 (0-30) + Final (0-40) = 100 max
 */
public class Mark implements Serializable {

    private static final long serialVersionUID = 1L;

    private Student student;
    private Course  course;
    private double  att1;
    private double  att2;
    private double  finalExam;
    private double  total;

    public Mark() {}

    public Mark(Student student, Course course,
                double att1, double att2, double finalExam) {
        this.student   = student;
        this.course    = course;
        this.att1      = clamp(att1,  0, 30);
        this.att2      = clamp(att2,  0, 30);
        this.finalExam = clamp(finalExam, 0, 40);
        this.total     = calcTotal();
    }

    // ─── Бизнес-логика ───────────────────────────────────────────────────────

    public double calcTotal() {
        total = att1 + att2 + finalExam;
        return total;
    }

    /**
     * Буквенная оценка по 100-балльной шкале (КБТУ / Болонья).
     */
    public String getLetterGrade() {
        if (total >= 95) return "A";
        if (total >= 90) return "A-";
        if (total >= 85) return "B+";
        if (total >= 80) return "B";
        if (total >= 75) return "B-";
        if (total >= 70) return "C+";
        if (total >= 65) return "C";
        if (total >= 60) return "C-";
        if (total >= 55) return "D+";
        if (total >= 50) return "D";
        return "F";
    }

    /** Считается проваленным, если итог < 50. */
    public boolean isFailed() { return total < 50; }

    // ─── Геттеры / Сеттеры ───────────────────────────────────────────────────

    public Student getStudent()   { return student; }
    public Course  getCourse()    { return course; }
    public double  getAtt1()      { return att1; }
    public double  getAtt2()      { return att2; }
    public double  getFinalExam() { return finalExam; }
    public double  getTotal()     { return total; }

    public void setAtt1(double v)      { att1 = clamp(v, 0, 30); total = calcTotal(); }
    public void setAtt2(double v)      { att2 = clamp(v, 0, 30); total = calcTotal(); }
    public void setFinalExam(double v) { finalExam = clamp(v, 0, 40); total = calcTotal(); }

    // ─── Вспомогательное ─────────────────────────────────────────────────────

    private double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    @Override
    public String toString() {
        return String.format("%-20s  ATT1:%.1f  ATT2:%.1f  Final:%.1f  Total:%.1f  (%s)",
                course != null ? course.getName() : "?",
                att1, att2, finalExam, total, getLetterGrade());
    }
}
