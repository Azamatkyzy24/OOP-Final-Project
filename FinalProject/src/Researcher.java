import java.util.Comparator;
import java.util.List;

/**
 * Интерфейс исследователя (используется Decorator pattern).
 */
public interface Researcher {

    /**
     * Вывести список статей, отсортированных по компаратору.
     */
    void printPapers(Comparator<ResearchPaper> comparator);

    /**
     * Добавить статью исследователю.
     */
    void addPaper(ResearchPaper p);

    /**
     * Получить h-index исследователя.
     */
    int getHIndex();

    /**
     * Получить список проектов.
     */
    List<ResearchProject> getProjects();
}
