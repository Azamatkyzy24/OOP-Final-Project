/**
 * Исключение: студент превысил лимит кредитов (21).
 */
public class CreditLimitException extends RuntimeException {

    public CreditLimitException(Student student, Course course) {
        super(String.format(
                "'%s' cannot register for '%s': credit limit exceeded (current: %d, adding: %d, max: 21)",
                student.getFullName(), course.getName(),
                student.getCredits(), course.getCredits()));
    }
}
