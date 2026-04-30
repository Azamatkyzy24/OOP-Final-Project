
import java.io.*;
import java.util.*;

/**
 * 
 */
public abstract class Employee extends User implements Messageable {

    /**
     * Default constructor
     */
    public Employee() {
    }



    /**
     * @return
     */
    private double salary() {
        // TODO implement here
        return 0.0d;
    }

    /**
     * @return
     */
    private String department() {
        // TODO implement here
        return "";
    }

    /**
     * @return
     */
    private Date hireDate() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public double checkSalary() {
        // TODO implement here
        return 0.0d;
    }

    /**
     * @return
     */
    public void submitComplaint() {
        // TODO implement here
        return null;
    }

    /**
     * @param to 
     * @param msg
     */
    public void sendMessage(void to, void msg) {
        // TODO implement here
    }

    /**
     * @param f 
     * @return
     */
    public void addFile(File f) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public List getMessages() {
        // TODO implement here
        return null;
    }

    /**
     * @param to 
     * @param msg
     */
    public void sendMessage(Employee to, String msg) {
        // TODO implement Messageable.sendMessage() here
    }

    /**
     * @return
     */
    public List getMessages() {
        // TODO implement Messageable.getMessages() here
        return null;
    }

}