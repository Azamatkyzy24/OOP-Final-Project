import java.io.*;
import java.util.*;

public abstract class Employee extends User implements Messageable {

    private static final long serialVersionUID = 1L;

    private double salary;
    private String department;
    private Date hireDate;
    private List<Message> inbox;
    private List<Message> sent;

    public Employee() {}

    public Employee(String id, String login, String password,
                    String name, String surname,
                    double salary, String department) {
        super(id, login, password, name, surname);
        this.salary = salary;
        this.department = department;
        this.hireDate = new Date();
        this.inbox = new ArrayList<>();
        this.sent = new ArrayList<>();
    }

    public double checkSalary() {
        System.out.println("Your salary: " + salary + " KZT");
        return salary;
    }

    public void submitComplaint(Employee to, String content) {
        Message complaint = new Message(this, to, content, true);
        complaint.send();
        sent.add(complaint);
        to.receiveMessage(complaint);
        System.out.println("Complaint submitted to " + to.getFullName());
    }

    public void submitComplaint() {
        System.out.println("Use submitComplaint(Employee to, String content).");
    }

    // Messageable
    @Override
    public void sendMessage(Employee to, String msg) {
        Message message = new Message(this, to, msg, false);
        message.send();
        sent.add(message);
        to.receiveMessage(message);
        System.out.println("Message sent to " + to.getFullName());
    }

    @Override
    public List<Message> getMessages() { return inbox; }

    public void receiveMessage(Message msg) { inbox.add(msg); }

    public void addFile(File f) {
        System.out.println("File added: " + f.getName());
    }

    public void viewInbox() {
        if (inbox.isEmpty()) { System.out.println("Inbox is empty."); return; }
        System.out.println("\n=== Inbox ===");
        inbox.forEach(System.out::println);
    }

    // Getters & Setters
    public double getSalary()        { return salary; }
    public void setSalary(double s)  { this.salary = s; }
    public String getDepartment()    { return department; }
    public void setDepartment(String d) { this.department = d; }
    public Date getHireDate()        { return hireDate; }

    @Override
    public String toString() {
        return super.toString() + " | dept: " + department;
    }
}