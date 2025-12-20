package student.pwr.edu.regaly.io;

import student.pwr.edu.regaly.model.Book;
import student.pwr.edu.regaly.model.Shelf;

import java.util.List;

abstract public class Dataset {
    public abstract List<Book> getBooks();

    public abstract List<Shelf> getShelves();
}
