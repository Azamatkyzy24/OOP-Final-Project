
import java.io.*;
import java.util.*;

/**
 * 
 */
public interface Researcher {



    /**
     * @param c
     */
    public void printPapers(Comparator c);

    /**
     * @param p
     */
    public void addPaper(ResearchPaper p);

    /**
     * @return
     */
    public int getHIndex();

    /**
     * @return
     */
    public List getProjects();

}