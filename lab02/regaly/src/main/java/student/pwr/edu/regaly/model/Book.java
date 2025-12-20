package student.pwr.edu.regaly.model;

public class Book {
    private final int id;
    private final int height;
    private final int thickness;
    private final int subject;

    public Book(int id, int height, int thickness, int subject) {
        this.height = height;
        this.thickness = thickness;
        this.subject = subject;
        this.id = id;
    }

    public Book(Book other) {
        this.height = other.height;
        this.thickness = other.thickness;
        this.id = other.id;
        this.subject = other.subject;
    }

    public int getId() {
        return this.id;
    }

    public int getHeight() {
        return this.height;
    }

    public int getThickness() {
        return thickness;
    }

    public int getSubject() {
        return subject;
    }
}
