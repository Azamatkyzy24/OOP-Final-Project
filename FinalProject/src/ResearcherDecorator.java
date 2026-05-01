import java.io.Serializable;
import java.util.*;

/**
 * Decorator — добавляет роль исследователя любому User (Teacher или Student).
 * Реализует интерфейс Researcher.
 */
public class ResearcherDecorator implements Researcher, Serializable {

    private static final long serialVersionUID = 1L;

    private User                owner;       // Teacher или Student
    private List<ResearchPaper> papers;
    private List<ResearchProject> projects;
    private int                 hIndex;

    public ResearcherDecorator() {}

    public ResearcherDecorator(User owner) {
        this.owner    = owner;
        this.papers   = new ArrayList<>();
        this.projects = new ArrayList<>();
        this.hIndex   = 0;
        // Зарегистрировать в базе
        Database.getInstance().addResearcher(this);
    }

    // ─── Researcher interface ─────────────────────────────────────────────────

    @Override
    public void printPapers(Comparator<ResearchPaper> comparator) {
        if (papers.isEmpty()) { System.out.println("No papers."); return; }
        System.out.println("\n=== Papers of " + getOwnerName() + " ===");
        papers.stream()
              .sorted(comparator)
              .forEach(p -> System.out.println("  " + p));
    }

    @Override
    public void addPaper(ResearchPaper p) {
        if (!papers.contains(p)) {
            papers.add(p);
            recalcHIndex();
            System.out.println("Paper added: " + p.getTitle()
                    + " (h-index now: " + hIndex + ")");
        }
    }

    @Override
    public int getHIndex() { return hIndex; }

    @Override
    public List<ResearchProject> getProjects() { return projects; }

    // ─── Дополнительные методы ───────────────────────────────────────────────

    /**
     * Вступить в исследовательский проект.
     */
    public void joinProject(ResearchProject rp) {
        if (!projects.contains(rp)) {
            projects.add(rp);
            rp.addParticipant(this);
            System.out.println(getOwnerName() + " joined project: " + rp.getTopic());
        }
    }

    /**
     * Общее количество цитирований всех статей.
     */
    public int checkCitations() {
        int total = papers.stream().mapToInt(ResearchPaper::getCitations).sum();
        System.out.println(getOwnerName() + " total citations: " + total);
        return total;
    }

    /**
     * Меню для исследователя — вызывается из showMenu() Teacher/Student.
     */
    public void showResearchMenu(Scanner sc) {
        boolean running = true;
        while (running) {
            System.out.println("\n=== RESEARCH MENU — " + getOwnerName()
                    + " (h-index: " + hIndex + ") ===");
            System.out.println("1. View papers");
            System.out.println("2. Add paper");
            System.out.println("3. View projects");
            System.out.println("4. Join project");
            System.out.println("5. Check citations");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            String ch = sc.nextLine().trim();
            switch (ch) {
                case "1" -> printPapers(Comparator.comparing(ResearchPaper::getDate,
                                Comparator.nullsLast(Comparator.reverseOrder())));
                case "2" -> addPaperMenu(sc);
                case "3" -> viewProjects();
                case "4" -> joinProjectMenu(sc);
                case "5" -> checkCitations();
                case "0" -> running = false;
                default  -> System.out.println("Invalid option.");
            }
        }
    }

    // ─── Приватные вспомогательные ───────────────────────────────────────────

    /**
     * Пересчитать h-index:
     * h — наибольшее число, при котором ≥ h статей имеют ≥ h цитирований.
     */
    private void recalcHIndex() {
        List<Integer> citations = papers.stream()
                .map(ResearchPaper::getCitations)
                .sorted(Comparator.reverseOrder())
                .toList();
        int h = 0;
        for (int i = 0; i < citations.size(); i++) {
            if (citations.get(i) >= i + 1) h = i + 1;
            else break;
        }
        this.hIndex = h;
    }

    private void addPaperMenu(Scanner sc) {
        System.out.print("Title: ");      String title = sc.nextLine().trim();
        System.out.print("Authors (comma-separated): "); String[] authorsArr = sc.nextLine().split(",");
        List<String> authors = Arrays.stream(authorsArr).map(String::trim).toList();
        System.out.print("Pages: ");
        int pages = 0;
        try { pages = Integer.parseInt(sc.nextLine().trim()); } catch (Exception ignored) {}
        addPaper(new ResearchPaper(title, authors, new Date(), pages));
    }

    private void viewProjects() {
        if (projects.isEmpty()) { System.out.println("No projects."); return; }
        System.out.println("\n=== Projects ===");
        projects.forEach(p -> System.out.println("  " + p));
    }

    private void joinProjectMenu(Scanner sc) {
        List<ResearchProject> all = Database.getInstance().getAllProjects();
        List<ResearchProject> available = all.stream().filter(p -> !projects.contains(p)).toList();
        if (available.isEmpty()) { System.out.println("No projects to join."); return; }
        System.out.println("Available projects:");
        for (int i = 0; i < available.size(); i++)
            System.out.println((i + 1) + ". " + available.get(i).getTopic());
        System.out.print("Choice: ");
        try {
            int idx = Integer.parseInt(sc.nextLine().trim()) - 1;
            joinProject(available.get(idx));
        } catch (Exception e) { System.out.println("Error."); }
    }

    // ─── Геттеры ─────────────────────────────────────────────────────────────

    public User             getOwner()      { return owner; }
    public String           getOwnerName()  { return owner != null ? owner.getFullName() : "?"; }
    public List<ResearchPaper> getPapers()  { return papers; }

    @Override
    public String toString() {
        return String.format("[Researcher] %s | h-index: %d | papers: %d | projects: %d",
                getOwnerName(), hIndex, papers.size(), projects.size());
    }
}
