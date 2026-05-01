/**
 * Учёное звание преподавателя.
 */
public enum TeacherTitle {
    TUTOR("Tutor"),
    LECTOR("Lector"),
    SENIOR_LECTOR("Senior Lector"),
    ASSOCIATE_PROFESSOR("Associate Professor"),
    PROFESSOR("Professor");

    private final String displayName;

    TeacherTitle(String displayName) { this.displayName = displayName; }

    /** Является ли звание профессорским (включая Associate Professor). */
    public boolean isProfessor() {
        return this == PROFESSOR || this == ASSOCIATE_PROFESSOR;
    }

    @Override
    public String toString() { return displayName; }
}
