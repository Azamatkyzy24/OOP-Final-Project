import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Отчёт сотрудника, который может быть подписан менеджером.
 */
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    private String   type;       // тип отчёта (например: "Financial", "Academic", "HR")
    private String   content;
    private Employee author;
    private Employee signedBy;
    private Date     date;
    private boolean  signed;

    public Report() {}

    public Report(String type, String content, Employee author) {
        this.type    = type;
        this.content = content;
        this.author  = author;
        this.date    = new Date();
        this.signed  = false;
    }

    // ─── Методы ──────────────────────────────────────────────────────────────

    /** Сгенерировать отчёт (обновить дату). */
    public void generate() {
        this.date = new Date();
        System.out.println("Report generated: " + type + " by " +
                (author != null ? author.getFullName() : "?"));
    }

    /** Подписать отчёт. */
    public void sign(Employee manager) {
        this.signed   = true;
        this.signedBy = manager;
        System.out.println("Report \"" + type + "\" signed by " + manager.getFullName());
    }

    /**
     * Экспортировать отчёт в файл.
     * @return созданный файл или null при ошибке
     */
    public File export() {
        String fname = "report_" + type.replaceAll("\\s+", "_") + "_"
                + new SimpleDateFormat("yyyyMMdd_HHmmss").format(date) + ".txt";
        File f = new File(fname);
        try (PrintWriter pw = new PrintWriter(f)) {
            pw.println("===== REPORT: " + type + " =====");
            pw.println("Author  : " + (author != null ? author.getFullName() : "?"));
            pw.println("Date    : " + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(date));
            pw.println("Signed  : " + (signed ? signedBy.getFullName() : "NOT SIGNED"));
            pw.println("─────────────────────────────");
            pw.println(content);
            System.out.println("Report exported to: " + fname);
            return f;
        } catch (FileNotFoundException e) {
            System.err.println("Export error: " + e.getMessage());
            return null;
        }
    }

    // ─── Геттеры ─────────────────────────────────────────────────────────────

    public String   getType()      { return type; }
    public String   getContent()   { return content; }
    public Employee getAuthor()    { return author; }
    public Employee getSignedBy()  { return signedBy; }
    public Date     getDate()      { return date; }
    public boolean  isSigned()     { return signed; }

    public void setContent(String c) { this.content = c; }
    public void setType(String t)    { this.type = t; }

    @Override
    public String toString() {
        String d = date != null ? new SimpleDateFormat("dd.MM.yyyy").format(date) : "?";
        return String.format("[Report] %s | %s | Author: %s | %s",
                type, d,
                author != null ? author.getFullName() : "?",
                signed ? "Signed by " + signedBy.getFullName() : "UNSIGNED");
    }
}
