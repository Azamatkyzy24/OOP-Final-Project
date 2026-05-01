import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Научная статья / публикация.
 * Implements Comparable для сортировки по дате.
 */
public class ResearchPaper implements Comparable<ResearchPaper>, Serializable {

    private static final long serialVersionUID = 1L;

    private String       title;
    private List<String> authors;   // имена авторов как строки
    private Date         date;
    private int          pages;
    private int          citations;

    public ResearchPaper() {}

    public ResearchPaper(String title, List<String> authors, Date date, int pages) {
        this.title     = title;
        this.authors   = new ArrayList<>(authors);
        this.date      = date;
        this.pages     = pages;
        this.citations = 0;
    }

    // ─── Методы ──────────────────────────────────────────────────────────────

    public void addCitation()  { citations++; }
    public int  getCitations() { return citations; }

    @Override
    public int compareTo(ResearchPaper o) {
        if (this.date == null && o.date == null) return 0;
        if (this.date == null) return 1;
        if (o.date    == null) return -1;
        return this.date.compareTo(o.date);
    }

    // ─── Геттеры ─────────────────────────────────────────────────────────────

    public String       getTitle()   { return title; }
    public List<String> getAuthors() { return authors; }
    public Date         getDate()    { return date; }
    public int          getPages()   { return pages; }

    public void setTitle(String t)    { this.title = t; }
    public void setCitations(int c)   { this.citations = c; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResearchPaper)) return false;
        return Objects.equals(title, ((ResearchPaper) o).title)
                && Objects.equals(date, ((ResearchPaper) o).date);
    }

    @Override
    public int hashCode() { return Objects.hash(title, date); }

    @Override
    public String toString() {
        String d = date != null ? new SimpleDateFormat("yyyy").format(date) : "?";
        return String.format("\"%s\" (%s) — %s | %d pages | %d citations",
                title, d, String.join(", ", authors), pages, citations);
    }
}
