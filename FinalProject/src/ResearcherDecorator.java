
import java.io.*;
import java.util.*;

/**
 * 
 */
public class ResearcherDecorator implements Researcher, Researcher {

    /**
     * Default constructor
     */
    public ResearcherDecorator() {
    }

    /**
     * @return
     */
    private List<ResearchPaper> papers() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    private List<ResearchProject> projects() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    private int hIndex() {
        // TODO implement here
        return 0;
    }

    /**
     * @param c
     */
    public void printPapers(Comparator c) {
        // TODO implement here
    }

    /**
     * @param p 
     * @return
     */
    public void addPaper(void p) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public int getHIndex() {
        // TODO implement here
        return 0;
    }

    /**
     * @param rp 
     * @return
     */
    public void joinProject(void rp) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public int checkCitations() {
        // TODO implement here
        return 0;
    }

    /**
     * @param c
     */
    public void printPapers(Comparator c) {
        // TODO implement Researcher.printPapers() here
    }

    /**
     * @param p
     */
    public void addPaper(ResearchPaper p) {
        // TODO implement Researcher.addPaper() here
    }

    /**
     * @return
     */
    public int getHIndex() {
        // TODO implement Researcher.getHIndex() here
        return 0;
    }

    /**
     * @return
     */
    public List getProjects() {
        // TODO implement Researcher.getProjects() here
        return null;
    }

}