/**
 * Исключение: h-index исследователя ниже минимального (3) для назначения научруком.
 */
public class LowHIndexException extends RuntimeException {

    public LowHIndexException(ResearcherDecorator researcher) {
        super(String.format(
                "Cannot assign '%s' as supervisor: h-index is %d (minimum required: 3)",
                researcher.getOwnerName(), researcher.getHIndex()));
    }
}
