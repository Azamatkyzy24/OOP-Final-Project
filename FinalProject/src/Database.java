import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Singleton Database — хранит все данные в памяти и сохраняет в JSON файл.
 * Файл: data/university_data.json
 */
public class Database {

    // ─── Путь к файлу данных (отдельно от Main) ──────────────────────────────
    private static final String DATA_DIR  = "data";
    private static final String FILE_PATH = DATA_DIR + "/university_data.json";

    private static Database instance;

    // ─── Хранилища ────────────────────────────────────────────────────────────
    private Map<String, User>            users              = new LinkedHashMap<>();
    private List<Course>                 courses            = new ArrayList<>();
    private List<Mark>                   marks              = new ArrayList<>();
    private List<Attendance>             attendances        = new ArrayList<>();
    private List<News>                   news               = new ArrayList<>();
    private List<Report>                 reports            = new ArrayList<>();
    private List<RegistrationRequest>    requests           = new ArrayList<>();
    private List<ResearcherDecorator>    researchers        = new ArrayList<>();
    private List<SupervisorRequest>      supervisorRequests = new ArrayList<>();
    private List<ResearchProject>        projects           = new ArrayList<>();

    private Database() {}

    public static Database getInstance() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    // ─── Users ────────────────────────────────────────────────────────────────

    public void addUser(User u)    { users.put(u.getId(), u); save(); }
    public void removeUser(User u) { users.remove(u.getId()); save(); }
    public void saveUser(User u)   { users.put(u.getId(), u); save(); }

    public List<User> getAllUsers() { return new ArrayList<>(users.values()); }

    public User getUserById(String id) { return users.get(id); }

    public User getUserByLogin(String login) {
        return users.values().stream()
                .filter(u -> u.getLogin().equals(login))
                .findFirst().orElse(null);
    }

    public List<Student> getAllStudents() {
        return users.values().stream()
                .filter(u -> u instanceof Student)
                .map(u -> (Student) u)
                .collect(Collectors.toList());
    }

    public List<Teacher> getAllTeachers() {
        return users.values().stream()
                .filter(u -> u instanceof Teacher)
                .map(u -> (Teacher) u)
                .collect(Collectors.toList());
    }

    public List<Employee> getAllEmployees() {
        return users.values().stream()
                .filter(u -> u instanceof Employee)
                .map(u -> (Employee) u)
                .collect(Collectors.toList());
    }

    public void removeEmployee(Employee e) { users.remove(e.getId()); save(); }

    // ─── Courses ──────────────────────────────────────────────────────────────

    public void addCourse(Course c)    { courses.add(c); save(); }
    public void removeCourse(Course c) { courses.remove(c); save(); }
    public List<Course> getAllCourses() { return new ArrayList<>(courses); }

    public List<Course> getAvailableCourses(int year, Faculty faculty) {
        return courses.stream()
                .filter(c -> c.getTargetYear() == year && c.getTargetFaculty() == faculty)
                .collect(Collectors.toList());
    }

    // ─── Marks ────────────────────────────────────────────────────────────────

    public void saveMark(Mark m) {
        marks.removeIf(x -> x.getStudent().equals(m.getStudent())
                && x.getCourse().equals(m.getCourse()));
        marks.add(m);
        save();
    }

    public List<Mark> getMarksForStudent(Student s) {
        return marks.stream().filter(m -> m.getStudent().equals(s)).collect(Collectors.toList());
    }

    // ─── Attendance ───────────────────────────────────────────────────────────

    public void saveAttendance(Attendance a) { attendances.add(a); save(); }

    public List<Attendance> getAttendanceForStudent(Student s) {
        return attendances.stream()
                .filter(a -> a.getStudent().equals(s))
                .collect(Collectors.toList());
    }

    // ─── News ─────────────────────────────────────────────────────────────────

    public void addNews(News n)    { news.add(n); save(); }
    public void removeNews(News n) { news.remove(n); save(); }
    public List<News> getAllNews() { return new ArrayList<>(news); }

    // ─── Reports ──────────────────────────────────────────────────────────────

    public void addReport(Report r)           { reports.add(r); save(); }
    public List<Report> getAllReports()        { return new ArrayList<>(reports); }
    public List<Report> getUnsignedReports()  {
        return reports.stream().filter(r -> !r.isSigned()).collect(Collectors.toList());
    }

    // ─── Registration Requests ────────────────────────────────────────────────

    public void addRegistrationRequest(Student s, Course c) {
        requests.add(new RegistrationRequest(s, c));
        save();
    }
    public void removeRequest(RegistrationRequest r) { requests.remove(r); save(); }
    public List<RegistrationRequest> getPendingRequests() { return new ArrayList<>(requests); }

    // ─── Researchers ──────────────────────────────────────────────────────────

    public void addResearcher(ResearcherDecorator r) {
        if (!researchers.contains(r)) { researchers.add(r); save(); }
    }
    public void removeResearcher(ResearcherDecorator r) { researchers.remove(r); save(); }
    public List<ResearcherDecorator> getAllResearchers()  { return new ArrayList<>(researchers); }

    // ─── Supervisor Requests ──────────────────────────────────────────────────

    public void addSupervisorRequest(Student s, ResearcherDecorator r) {
        supervisorRequests.add(new SupervisorRequest(s, r));
        save();
    }
    public List<SupervisorRequest> getPendingSupervisorRequests() {
        return new ArrayList<>(supervisorRequests);
    }
    public void removeSupervisorRequest(SupervisorRequest sr) {
        supervisorRequests.remove(sr); save();
    }

    // ─── Research Projects ────────────────────────────────────────────────────

    public void addProject(ResearchProject p)    { projects.add(p); save(); }
    public List<ResearchProject> getAllProjects() { return new ArrayList<>(projects); }

    // ─── Сброс ────────────────────────────────────────────────────────────────

    public void reset() {
        users.clear(); courses.clear(); marks.clear();
        attendances.clear(); news.clear(); reports.clear();
        requests.clear(); researchers.clear();
        supervisorRequests.clear(); projects.clear();
        save();
    }

    // ─── JSON сохранение / загрузка ───────────────────────────────────────────

    /** Gson с поддержкой полиморфизма (User → Student/Teacher/...) */
    private static Gson buildGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                // Регистрируем RuntimeTypeAdapterFactory для User
                .registerTypeHierarchyAdapter(User.class, new UserTypeAdapter())
                .serializeNulls()
                .create();
    }

    /** Сохранить в data/university_data.json */
    public void save() {
        try {
            // Создать папку data/ если не существует
            new File(DATA_DIR).mkdirs();

            Gson gson = buildGson();
            DatabaseSnapshot snapshot = new DatabaseSnapshot(this);

            try (Writer writer = new FileWriter(FILE_PATH)) {
                gson.toJson(snapshot, writer);
            }
        } catch (IOException e) {
            System.err.println("[Database] Save error: " + e.getMessage());
        }
    }

    /** Загрузить из data/university_data.json или создать новый */
    private static Database load() {
        File f = new File(FILE_PATH);
        if (!f.exists()) {
            System.out.println("[Database] No save file found, starting fresh.");
            return new Database();
        }
        try {
            Gson gson = buildGson();
            try (Reader reader = new FileReader(f)) {
                DatabaseSnapshot snapshot = gson.fromJson(reader, DatabaseSnapshot.class);
                if (snapshot != null) {
                    System.out.println("[Database] Loaded from " + FILE_PATH);
                    return snapshot.toDatabase();
                }
            }
        } catch (Exception e) {
            System.err.println("[Database] Load error: " + e.getMessage());
            System.err.println("[Database] Starting with empty database.");
        }
        return new Database();
    }

    // ─── Вспомогательный snapshot класс ──────────────────────────────────────

    /**
     * Плоская структура для сериализации в JSON.
     * Все объекты хранятся с явным полем "type" для полиморфизма.
     */
    static class DatabaseSnapshot {
        List<Map<String, Object>> users;
        List<Course>              courses;
        List<Mark>                marks;
        List<Attendance>          attendances;
        List<News>                news;
        List<Report>              reports;
        List<RegistrationRequest> requests;
        List<ResearchProject>     projects;

        DatabaseSnapshot() {}

        DatabaseSnapshot(Database db) {
            // Пользователей сохраняем с полем "type"
            this.users = new ArrayList<>();
            for (User u : db.users.values()) {
                Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
                Map<String, Object> map = g.fromJson(g.toJson(u), new TypeToken<Map<String, Object>>(){}.getType());
                map.put("type", u.getClass().getSimpleName());
                this.users.add(map);
            }
            this.courses     = db.courses;
            this.marks       = db.marks;
            this.attendances = db.attendances;
            this.news        = db.news;
            this.reports     = db.reports;
            this.requests    = db.requests;
            this.projects    = db.projects;
        }

        Database toDatabase() {
            Database db = new Database();
            Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

            if (users != null) {
                for (Map<String, Object> map : users) {
                    String type = (String) map.get("type");
                    String json = g.toJson(map);
                    User u = switch (type != null ? type : "") {
                        case "Student"        -> g.fromJson(json, Student.class);
                        case "Teacher"        -> g.fromJson(json, Teacher.class);
                        case "Manager"        -> g.fromJson(json, Manager.class);
                        case "Admin"          -> g.fromJson(json, Admin.class);
                        case "FinanceManager" -> g.fromJson(json, FinanceManager.class);
                        default               -> null;
                    };
                    if (u != null) db.users.put(u.getId(), u);
                }
            }
            if (courses     != null) db.courses     = courses;
            if (marks       != null) db.marks       = marks;
            if (attendances != null) db.attendances = attendances;
            if (news        != null) db.news        = news;
            if (reports     != null) db.reports     = reports;
            if (requests    != null) db.requests    = requests;
            if (projects    != null) db.projects    = projects;

            return db;
        }
    }

    // ─── TypeAdapter для полиморфизма User ───────────────────────────────────

    static class UserTypeAdapter implements JsonSerializer<User>, JsonDeserializer<User> {
        @Override
        public JsonElement serialize(User src, Type type, JsonSerializationContext ctx) {
            JsonObject obj = ctx.serialize(src, src.getClass()).getAsJsonObject();
            obj.addProperty("type", src.getClass().getSimpleName());
            return obj;
        }

        @Override
        public User deserialize(JsonElement json, Type type, JsonDeserializationContext ctx)
                throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            String userType = obj.has("type") ? obj.get("type").getAsString() : "";
            return switch (userType) {
                case "Student"        -> ctx.deserialize(json, Student.class);
                case "Teacher"        -> ctx.deserialize(json, Teacher.class);
                case "Manager"        -> ctx.deserialize(json, Manager.class);
                case "Admin"          -> ctx.deserialize(json, Admin.class);
                case "FinanceManager" -> ctx.deserialize(json, FinanceManager.class);
                default -> throw new JsonParseException("Unknown user type: " + userType);
            };
        }
    }

    @Override
    public String toString() {
        return String.format("Database[users=%d, courses=%d, marks=%d, file=%s]",
                users.size(), courses.size(), marks.size(), FILE_PATH);
    }
}