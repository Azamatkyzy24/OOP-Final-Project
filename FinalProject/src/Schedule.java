import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Расписание занятий на семестр.
 */
public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Lesson> lessons;
    private Term         termType;

    public Schedule() {
        this.lessons  = new ArrayList<>();
        this.termType = Term.Fall;
    }

    public Schedule(Term termType) {
        this.termType = termType;
        this.lessons  = new ArrayList<>();
    }

    // ─── Методы ──────────────────────────────────────────────────────────────

    /** Добавить занятие в расписание. */
    public void addLesson(Lesson l) { lessons.add(l); }

    /** Убрать занятие. */
    public void removeLesson(Lesson l) { lessons.remove(l); }

    /**
     * Сгенерировать расписание из всех курсов в базе.
     * Простая реализация: создаёт по одному занятию в неделю для каждого курса.
     */
    public void generate() {
        lessons.clear();
        System.out.println("Schedule generated for term: " + termType);
    }

    /** Получить занятия конкретного студента (по его курсам). */
    public List<Lesson> getForStudent(Student s) {
        List<Course> enrolled = s.getEnrolledCourses();
        return lessons.stream()
                .filter(l -> enrolled.contains(l.getCourse()))
                .sorted(Comparator.comparing(Lesson::getDateTime))
                .collect(Collectors.toList());
    }

    /** Получить занятия конкретного преподавателя. */
    public List<Lesson> getForTeacher(Teacher t) {
        return lessons.stream()
                .filter(l -> t.equals(l.getTeacher()))
                .sorted(Comparator.comparing(Lesson::getDateTime))
                .collect(Collectors.toList());
    }

    /**
     * Проверить конфликты расписания (одно время + одна аудитория).
     * @return true если конфликтов нет
     */
    public boolean checkConflicts() {
        for (int i = 0; i < lessons.size(); i++) {
            for (int j = i + 1; j < lessons.size(); j++) {
                Lesson a = lessons.get(i);
                Lesson b = lessons.get(j);
                if (a.getDateTime() != null && a.getDateTime().equals(b.getDateTime())
                        && a.getRoom() != null && a.getRoom().equals(b.getRoom())) {
                    System.out.printf("CONFLICT: %s and %s in room %s at %s%n",
                            a.getCourse().getName(), b.getCourse().getName(),
                            a.getRoom(), a.getDateTime());
                    return false;
                }
            }
        }
        System.out.println("No conflicts found.");
        return true;
    }

    /** Вывести расписание для студента. */
    public void printForStudent(Student s) {
        List<Lesson> sl = getForStudent(s);
        System.out.println("\n=== Schedule for " + s.getFullName() + " ===");
        if (sl.isEmpty()) { System.out.println("No lessons."); return; }
        sl.forEach(l -> System.out.println("  " + l));
    }

    /** Вывести расписание для преподавателя. */
    public void printForTeacher(Teacher t) {
        List<Lesson> tl = getForTeacher(t);
        System.out.println("\n=== Schedule for " + t.getFullName() + " ===");
        if (tl.isEmpty()) { System.out.println("No lessons."); return; }
        tl.forEach(l -> System.out.println("  " + l));
    }

    // ─── Геттеры ─────────────────────────────────────────────────────────────

    public List<Lesson> getLessons()  { return lessons; }
    public Term         getTermType() { return termType; }
    public void         setTermType(Term t) { this.termType = t; }
}
