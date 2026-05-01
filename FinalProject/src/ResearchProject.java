import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Исследовательский проект, в котором могут участвовать несколько Researcher'ов.
 */
public class ResearchProject implements Serializable {

    private static final long serialVersionUID = 1L;

    private String            topic;
    private List<Researcher>  participants;
    private List<ResearchPaper> papers;
    private Date              startDate;
    private Date              endDate;

    public ResearchProject() {}

    public ResearchProject(String topic, Date startDate, Date endDate) {
        this.topic        = topic;
        this.participants = new ArrayList<>();
        this.papers       = new ArrayList<>();
        this.startDate    = startDate;
        this.endDate      = endDate;
    }

    // ─── Методы ──────────────────────────────────────────────────────────────

    /**
     * Добавить статью к проекту (и сохранить в базе).
     */
    public void publishPaper(ResearchPaper p) {
        if (!papers.contains(p)) {
            papers.add(p);
            System.out.println("Paper published: " + p.getTitle() + " → project: " + topic);
        }
    }

    /**
     * Добавить участника в проект.
     * Бросает NotResearcherException если объект не является Researcher.
     */
    public void addParticipant(Researcher r) {
        if (!participants.contains(r)) {
            participants.add(r);
            System.out.println("Participant added to project \"" + topic + "\"");
        }
    }

    public List<Researcher>    getParticipants() { return participants; }
    public List<ResearchPaper> getPapers()        { return papers; }

    // ─── Геттеры ─────────────────────────────────────────────────────────────

    public String getTopic()      { return topic; }
    public Date   getStartDate()  { return startDate; }
    public Date   getEndDate()    { return endDate; }

    public void   setTopic(String t)     { this.topic = t; }
    public void   setStartDate(Date d)   { this.startDate = d; }
    public void   setEndDate(Date d)     { this.endDate = d; }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return String.format("[Project] \"%s\" | %s — %s | participants: %d | papers: %d",
                topic,
                startDate != null ? sdf.format(startDate) : "?",
                endDate   != null ? sdf.format(endDate)   : "ongoing",
                participants.size(), papers.size());
    }
}
