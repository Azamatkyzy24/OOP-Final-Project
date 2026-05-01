import java.io.Serializable;
import java.util.*;

/**
 * Сервис аутентификации — управляет сессиями пользователей.
 */
public class AuthService {

    // token -> User
    private Map<String, User> sessions = new HashMap<>();

    public AuthService() {}

    /**
     * Аутентифицировать пользователя по логину и паролю.
     * @return User если успешно, иначе null
     */
    public User authenticate(String login, String password) {
        User user = Database.getInstance().getUserByLogin(login);
        if (user == null) {
            System.out.println("User not found: " + login);
            return null;
        }
        if (!user.checkPassword(password)) {
            System.out.println("Wrong password for: " + login);
            return null;
        }
        String token = UUID.randomUUID().toString();
        sessions.put(token, user);
        Logger.getInstance().log("LOGIN", user, "Logged in, token=" + token);
        System.out.println("Authenticated: " + user.getFullName());
        return user;
    }

    /**
     * Завершить сессию по токену.
     */
    public void logout(String token) {
        User user = sessions.remove(token);
        if (user != null) {
            Logger.getInstance().log("LOGOUT", user, "Session ended");
            System.out.println(user.getFullName() + " logged out.");
        }
    }

    /**
     * Проверить, авторизован ли пользователь как данная роль.
     * @param u    пользователь
     * @param role класс (Student.class, Teacher.class, etc.)
     */
    public boolean isAuthorized(User u, Class<?> role) {
        if (u == null || role == null) return false;
        return role.isAssignableFrom(u.getClass());
    }

    /** Получить активные сессии. */
    public Map<String, User> getSessions() {
        return Collections.unmodifiableMap(sessions);
    }

    /** Проверить, есть ли активная сессия у пользователя. */
    public boolean isLoggedIn(User u) {
        return sessions.containsValue(u);
    }

    /** Завершить все сессии (для тестов / административного сброса). */
    public void clearSessions() {
        sessions.clear();
    }
}
