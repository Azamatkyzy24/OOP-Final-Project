import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Новость университета.
 */
public class News implements Serializable {

    private static final long serialVersionUID = 1L;

    private String   title;
    private String   content;
    private Employee author;
    private Date     date;
    private boolean  published;
    private String   comment;

    public News() {}

    public News(String title, String content, Employee author) {
        this.title   = title;
        this.content = content;
        this.author  = author;
        this.date    = new Date();
        this.published = false;
        this.comment = "";
    }

    // ─── Методы ──────────────────────────────────────────────────────────────

    public void publish() {
        this.published = true;
        this.date = new Date();
        System.out.println("News published: " + title);
    }

    public void edit(String newContent) {
        this.content = newContent;
        this.date    = new Date();
        System.out.println("News updated: " + title);
    }

    /** Удалить (снять с публикации). */
    public void delete() {
        this.published = false;
        System.out.println("News unpublished: " + title);
    }

    public String comment() { return comment; }
    public void   setComment(String c) { this.comment = c; }

    // ─── Геттеры ─────────────────────────────────────────────────────────────

    public String   getTitle()     { return title; }
    public String   getContent()   { return content; }
    public Employee getAuthor()    { return author; }
    public Date     getDate()      { return date; }
    public boolean  isPublished()  { return published; }

    public void setTitle(String t)   { this.title = t; }
    public void setContent(String c) { this.content = c; }

    @Override
    public String toString() {
        String d = date != null ? new SimpleDateFormat("dd.MM.yyyy").format(date) : "?";
        return String.format("[News] \"%s\" | %s | by %s | %s",
                title, d,
                author != null ? author.getFullName() : "?",
                published ? "PUBLISHED" : "DRAFT");
    }
}
