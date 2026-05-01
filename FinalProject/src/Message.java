import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Сообщение между сотрудниками. Может быть обычным или жалобой.
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private Employee from;
    private Employee to;
    private String   content;
    private Date     sentAt;
    private boolean  isComplaint;

    public Message() {}

    public Message(Employee from, Employee to, String content, boolean isComplaint) {
        this.from        = from;
        this.to          = to;
        this.content     = content;
        this.sentAt      = new Date();
        this.isComplaint = isComplaint;
    }

    /** Отправить (зафиксировать время). */
    public void send() {
        this.sentAt = new Date();
    }

    // ─── Геттеры ─────────────────────────────────────────────────────────────

    public Employee getFrom()        { return from; }
    public Employee getTo()          { return to; }
    public String   getContent()     { return content; }
    public Date     getSentAt()      { return sentAt; }
    public boolean  isComplaint()    { return isComplaint; }

    @Override
    public String toString() {
        String type = isComplaint ? "COMPLAINT" : "MSG";
        String time = sentAt != null
                ? new SimpleDateFormat("dd.MM.yyyy HH:mm").format(sentAt)
                : "?";
        return String.format("[%s] From: %s → To: %s | %s | %s",
                type,
                from  != null ? from.getFullName() : "?",
                to    != null ? to.getFullName()   : "?",
                time, content);
    }
}
