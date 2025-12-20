package student.pwr.edu.regaly.io;

import student.pwr.edu.regaly.model.Book;
import student.pwr.edu.regaly.model.Shelf;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LibraryDataset extends Dataset {
    private final List<Book> books;
    private final List<Shelf> shelves;

    public LibraryDataset(String shelvesPath, String booksPath) {
        this.books = new ArrayList<>();
        this.shelves = new ArrayList<>();

        File books = new File(booksPath);
        File shelves = new File(shelvesPath);

        try (Scanner booksReader = new Scanner(books)) {
            while (booksReader.hasNextLine()) {
                String line = booksReader.nextLine();
                if (line.startsWith("#")) {
                    continue;
                }
                String[] parameters = line.split(", ");
                this.books.add(new Book(Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]), Integer.parseInt(parameters[2]), Integer.parseInt(parameters[3])));
            }
        } catch (FileNotFoundException error) {
            System.out.println("Błąd przy ładowaniu książek");
            error.printStackTrace();
        }

        try (Scanner shelvesReader = new Scanner(shelves)) {
            while (shelvesReader.hasNextLine()) {
                String line = shelvesReader.nextLine();
                if (line.startsWith("#")) {
                    continue;
                }
                String[] parameters = line.split(", ");
                this.shelves.add(new Shelf(Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]), Integer.parseInt(parameters[2])));
            }
        } catch (FileNotFoundException error) {
            System.out.println("Błąd przy ładowaniu półek");
            error.printStackTrace();
        }
    }

    public List<Book> getBooks() {
        return this.books;
    }

    public List<Shelf> getShelves() {
        return this.shelves;
    }
}
