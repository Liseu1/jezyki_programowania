package student.pwr.edu.regaly.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Shelf {
    private final int height;
    private final int initialWidth;
    private final int id;
    private int width;
    private final List<Book> books;

    public Shelf(int id, int height, int initialWidth) {
        this.id = id;
        this.initialWidth = initialWidth;
        this.width = initialWidth;
        this.height = height;
        this.books = new ArrayList<>();
    }

    public Shelf(Shelf other) {
        this.height = other.height;
        this.initialWidth = other.initialWidth;
        this.width = other.width;
        this.id = other.id;
        this.books = new ArrayList<>();
        for (Book book : other.getBooks()) {
            this.books.add(new Book(book));
        }
    }

    public boolean addBook(Book book) {
        if (book.getHeight() > this.height || book.getThickness() > this.width) {
            return false;
        } else {
            this.width -= book.getThickness();
            books.add(book);
        }
        return true;
    }

    public int getBookCount() {
        return books.size();
    }

    public int getFill() {
        return initialWidth - width;
    }

    public long getUniqueSubjects() {
        return books.stream().map(Book::getSubject).distinct().count();
    }

    public void removeBook(Book book) {
        this.width += book.getThickness();
        this.books.remove(book);
    }

    public List<Book> getBooks() {
        return this.books;
    }

    public void removeAllBooksById(int bookId) {
        Iterator<Book> iterator = this.books.iterator();

        while (iterator.hasNext()) {
            Book book = iterator.next();

            if (book.getId() == bookId) {
                width += book.getThickness();
                iterator.remove();
            }
        }
    }

    public int getId() {
        return this.id;
    }
}
