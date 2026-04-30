
import java.io.*;
import java.util.*;

/**
 * 
 */
public interface Messageable {

    /**
     * @param to 
     * @param msg
     */
    public void sendMessage(Employee to, String msg);

    /**
     * @return
     */
    public List getMessages();

}