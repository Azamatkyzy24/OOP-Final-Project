
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public abstract class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String login;
    private String password;
    private String name;
    private String surname;

    public User() {}

    public User(String id, String login, String password, String name, String surname) {
        this.id = id;
        this.login = login;
        this.password = hashPassword(password);
        this.name = name;
        this.surname = surname;
    }

    public abstract void showMenu();

    public boolean login(String login, String password) {
        return this.login.equals(login) && this.password.equals(hashPassword(password));
    }

    public void logout() {
        System.out.println(getFullName() + " logged out.");
    }

    public void changePassword(String oldPassword, String newPassword) {
        if (!this.password.equals(hashPassword(oldPassword))) {
            System.out.println("Wrong current password.");
            return;
        }
        this.password = hashPassword(newPassword);
        System.out.println("Password changed successfully.");
    }

    public void viewNews() {
        List<News> newsList = Database.getInstance().getAllNews();
        if (newsList.isEmpty()) { System.out.println("No news available."); return; }
        System.out.println("\n=== News ===");
        newsList.forEach(System.out::println);
    }

    public void makeReport() {
        System.out.println("Report feature available to specific roles.");
    }

    protected String hashPassword(String p) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(p.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing error", e);
        }
    }

    // Getters & Setters
    public String getId()       { return id; }
    public String getLogin()    { return login; }
    public String getName()     { return name; }
    public String getSurname()  { return surname; }
    public String getFullName() { return name + " " + surname; }

    public void setLogin(String login)     { this.login = login; }
    public void setName(String name)       { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }

    public boolean checkPassword(String raw) {
        return this.password.equals(hashPassword(raw));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return Objects.equals(id, ((User) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return String.format("[%s] %s %s (login: %s)",
                getClass().getSimpleName(), name, surname, login);
    }
}