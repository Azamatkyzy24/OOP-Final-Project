
import java.io.*;
import java.util.*;

/**
 * 
 */
public abstract class User {

    /**
     * Default constructor
     */
    public User() {
    }



    /**
     * @return
     */
    private String id() {
        // TODO implement here
        return "";
    }

    /**
     * @return
     */
    private String login() {
        // TODO implement here
        return "";
    }

    /**
     * @return
     */
    private String password() {
        // TODO implement here
        return "";
    }

    /**
     * @return
     */
    private String name() {
        // TODO implement here
        return "";
    }

    /**
     * @return
     */
    private String surname() {
        // TODO implement here
        return "";
    }

    /**
     * @return
     */
    public boolean login() {
        // TODO implement here
        return false;
    }

    /**
     * @return
     */
    public void logout() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public void changePassword() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public void viewNews() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public void makeReport() {
        // TODO implement here
        return null;
    }

    /**
     * @param p 
     * @return
     */
    protected String hashPassword(void p) {
        // TODO implement here
        return "";
    }

}