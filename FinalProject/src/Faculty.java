/**
 * Факультеты университета (точно по диаграмме).
 */
public enum Faculty {
    SITE("School of Information Technology and Engineering"),
    BS("Business School"),
    ISE("International School of Economics"),
    SE("School of Engineering"),
    SAM("School of Applied Mathematics"),
    SEPI("School of Energy and Petroleum Industry");

    private final String fullName;

    Faculty(String fullName) { this.fullName = fullName; }

    public String getFullName() { return fullName; }

    @Override
    public String toString() { return name() + " — " + fullName; }
}
