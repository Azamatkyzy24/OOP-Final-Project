/**
 * Тип менеджера.
 */
public enum ManagerType {
    OR("Office of Registration"),
    DEPARTMENT("Department Manager"),
    DEAN("Dean"),
    RECTOR("Rector");

    private final String displayName;

    ManagerType(String displayName) { this.displayName = displayName; }

    @Override
    public String toString() { return displayName; }
}
