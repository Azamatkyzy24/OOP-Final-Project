/**
 * Исключение: студент превысил допустимое количество провальных курсов (3).
 */
public class MaxFailsException extends RuntimeException {

    public MaxFailsException(Student student) {
        super(String.format(
                "Student '%s' has exceeded the maximum number of failed courses (3). Academic dismissal required.",
                student.getFullName()));
    }
}
