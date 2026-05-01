/**
 * Исключение: пользователь не является исследователем.
 */
public class NotResearcherException extends RuntimeException {

    public NotResearcherException(User user) {
        super(String.format(
                "'%s' is not a Researcher and cannot join a ResearchProject",
                user.getFullName()));
    }
}
