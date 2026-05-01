import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Одно занятие в рамках курса.
 */
public class Lesson implements Serializable {

    private static final long serialVersionUID = 1L;

    private LessonType    type;
    private Course        course;
    private Teacher       teacher;
    private LocalDateTime dateTime;
    private String        room;
    private int           duration; // в минутах

    public Lesson() {}

    public Lesson(LessonType type, Course course, Teacher teacher,
                  LocalDateTime dateTime, String room, int duration) {
        this.type     = type;
        this.course   = course;
        this.teacher  = teacher;
        this.dateTime = dateTime;
        this.room     = room;
        this.duration = duration;
    }

    // ─── Методы ──────────────────────────────────────────────────────────────

    public LessonType getType() { return type; }

    public void reschedule(LocalDateTime newDateTime) {
        this.dateTime = newDateTime;
        System.out.printf("Lesson rescheduled to %s%n",
                newDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
    }

    // ─── Геттеры / Сеттеры ───────────────────────────────────────────────────

    public Course        getCourse()   { return course; }
    public Teacher       getTeacher()  { return teacher; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String        getRoom()     { return room; }
    public int           getDuration() { return duration; }

    public void setType(LessonType t)        { this.type = t; }
    public void setCourse(Course c)          { this.course = c; }
    public void setTeacher(Teacher t)        { this.teacher = t; }
    public void setDateTime(LocalDateTime dt){ this.dateTime = dt; }
    public void setRoom(String r)            { this.room = r; }
    public void setDuration(int d)           { this.duration = d; }

    @Override
    public String toString() {
        String dt = dateTime != null
                ? dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
                : "?";
        return String.format("[%s] %s | %s | Room: %s | %dmin | Teacher: %s",
                type,
                course != null ? course.getName() : "?",
                dt, room, duration,
                teacher != null ? teacher.getFullName() : "?");
    }
}
