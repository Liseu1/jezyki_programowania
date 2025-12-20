package student.pwr.edu.regaly.model;

import student.pwr.edu.regaly.config.LibraryConfig;

import java.util.List;

public class LibrarySolution extends Result {
    public List<Shelf> shelves;
    private final List<Book> unplacedBooks;

    public LibrarySolution(List<Shelf> shelves, List<Book> unplacedBooks) {
        this.shelves = shelves;
        this.unplacedBooks = unplacedBooks;
    }

    public void calculateScore() {
        double fillScore, countScore, differenceScore;
        fillScore = countScore = differenceScore = 0;

        for (Shelf shelf : shelves) {
            fillScore += shelf.getFill();
            countScore += shelf.getBookCount();
            differenceScore += shelf.getBookCount() - shelf.getUniqueSubjects();
        }

        this.setScore(LibraryConfig.weightFill * fillScore + LibraryConfig.weightCount * countScore + LibraryConfig.weightDifference * differenceScore);
    }

    public List<Shelf> getShelves() {
        return this.shelves;
    }

    public List<Book> getUnplacedBooks() {
        return this.unplacedBooks;
    }
}
