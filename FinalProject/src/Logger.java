import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Singleton Logger — пишет события в память и на диск.
 */
public class Logger {

    private static Logger instance;
    private static final String LOG_FILE = "university.log";

    private File logFile;
    private List<String> logs;

    private Logger() {
        logFile = new File(LOG_FILE);
        logs    = new ArrayList<>();
        // Подгрузить старые логи, если файл есть
        if (logFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(logFile))) {
                String line;
                while ((line = br.readLine()) != null) logs.add(line);
            } catch (IOException e) {
                System.err.println("[Logger] Could not read log file: " + e.getMessage());
            }
        }
    }

    public static Logger getInstance() {
        if (instance == null) instance = new Logger();
        return instance;
    }

    /**
     * Записать событие.
     * @param action  тип действия (LOGIN, ADD_USER, ...)
     * @param user    кто совершил
     * @param details подробности
     */
    public void log(String action, User user, String details) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String entry = String.format("[%s] %s | %s | %s",
                timestamp, action,
                user != null ? user.getLogin() : "SYSTEM",
                details);
        logs.add(entry);
        writeToFile(entry);
    }

    /** Перегрузка без деталей. */
    public void log(String action, User user) {
        log(action, user, "");
    }

    /** Обновление (Observer-совместимость). */
    public void update(String event) {
        log("EVENT", null, event);
    }

    /** Получить все логи. */
    public List<String> getLogs() {
        return Collections.unmodifiableList(logs);
    }

    /** Получить файл логов. */
    public File getLogFile() { return logFile; }

    /** Очистить (для тестов). */
    public void clearLogs() {
        logs.clear();
        try (PrintWriter pw = new PrintWriter(logFile)) { /* truncate */ }
        catch (FileNotFoundException ignored) {}
    }

    private void writeToFile(String entry) {
        try (FileWriter fw = new FileWriter(logFile, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(entry);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("[Logger] Write error: " + e.getMessage());
        }
    }
}
