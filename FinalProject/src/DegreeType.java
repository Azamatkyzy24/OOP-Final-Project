/**
 * Тип академической степени студента.
 */
public enum DegreeType {
    BACHELOR("Bachelor"),
    MASTER("Master"),
    PHD("PhD");

    private final String displayName;

    DegreeType(String displayName) { this.displayName = displayName; }

    @Override
    public String toString() { return displayName; }
}
