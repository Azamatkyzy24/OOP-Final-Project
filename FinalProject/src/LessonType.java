public enum LessonType {
    LECTURE("Lecture"),
    PRACTICE("Practice"),
    LABORATORY("Laboratory");

    private final String displayName;

    LessonType(String displayName) { this.displayName = displayName; }

    @Override
    public String toString() { return displayName; }
}
