import java.util.List;

/**
 * Интерфейс для объектов, поддерживающих обмен сообщениями.
 */
public interface Messageable {

    /**
     * Отправить сообщение сотруднику.
     */
    void sendMessage(Employee to, String msg);

    /**
     * Получить список входящих сообщений.
     */
    List<Message> getMessages();
}
