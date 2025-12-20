package student.pwr.edu.regaly.solver;

import student.pwr.edu.regaly.io.Dataset;
import student.pwr.edu.regaly.model.Book;
import student.pwr.edu.regaly.model.LibrarySolution;
import student.pwr.edu.regaly.model.Result;
import student.pwr.edu.regaly.model.Shelf;

import java.util.*;

public class LibraryAlgorithm extends Algorithm {
    private static final int INITIAL_POPULATION_SIZE = 100;
    private static final int TOURNAMENT_SIZE = 5;
    private static final int GENERATIONS = 100;
    private static final double MUTATION_RATE = 1;

    private final Random random = new Random();

    public Result solve(Dataset dataset) {
        List<LibrarySolution> population = new ArrayList<>();
        for (int i = 0; i < INITIAL_POPULATION_SIZE; i++) {
            LibrarySolution newPopulation = createRandomSolution(dataset.getBooks(), dataset.getShelves());

            newPopulation.calculateScore();
            population.add(newPopulation);
        }

        for (int i = 0; i < GENERATIONS; i++) {
            List<LibrarySolution> nextPopulation = new ArrayList<>();
            for (int j = 0; j < INITIAL_POPULATION_SIZE; j++) {
                LibrarySolution parent1 = getParent(population);
                LibrarySolution parent2 = getParent(population);
                LibrarySolution child = crossover(parent1, parent2);
                repairSolution(child);
                mutate(child);
                child.calculateScore();
                nextPopulation.add(child);
            }
            population = nextPopulation;
            System.out.println("\nNajlepszy wynik generacji nr. " + i + " " + population.stream().max(Comparator.comparing(LibrarySolution::getScore)).orElse(null).getScore());
        }

        return population.stream().max(Comparator.comparing(LibrarySolution::getScore)).orElse(null);
    }

    private LibrarySolution createRandomSolution(List<Book> allBooks, List<Shelf> originalShelves) {
        List<Book> unplacedBooks = new ArrayList<>();
        List<Shelf> newShelves = new ArrayList<>();

        for (Shelf shelf : originalShelves) {
            newShelves.add(new Shelf(shelf));
        }

        List<Book> booksToPlace = new ArrayList<>();
        for (Book book : allBooks) {
            booksToPlace.add(new Book(book));
        }

        for (Book book : booksToPlace) {
            int shelfNumber = random.nextInt(0, newShelves.size());
            if (!newShelves.get(shelfNumber).addBook(book)) {
                unplacedBooks.add(book);
            }
        }

        return new LibrarySolution(newShelves, unplacedBooks);
    }

    private LibrarySolution getParent(List<LibrarySolution> population) {
        LibrarySolution bestPop = null;

        for (int i = 0; i < TOURNAMENT_SIZE; i++) {
            LibrarySolution randomPop = population.get(random.nextInt(0, population.size()));
            if (bestPop == null || randomPop.getScore() > bestPop.getScore()) {
                bestPop = randomPop;
            }
        }

        return bestPop;
    }

    private LibrarySolution crossover(LibrarySolution parent1, LibrarySolution parent2) {
        List<Shelf> shelves1 = parent1.getShelves();
        List<Shelf> shelves2 = parent2.getShelves();
        int crossover = random.nextInt(0, shelves1.size());
        List<Shelf> childShelves = new ArrayList<>();

        for (int i = 0; i < crossover; i++) {
            childShelves.add(new Shelf(shelves1.get(i)));
        }

        for (int i = crossover; i < shelves2.size(); i++) {
            childShelves.add(new Shelf(shelves2.get(i)));
        }

        Set<Integer> placedBookIds = new HashSet<>();
        for (Shelf shelf : childShelves) {
            for (Book book : shelf.getBooks()) {
                placedBookIds.add(book.getId());
            }
        }

        List<Book> childUnplacedBooks = new ArrayList<>();

        Set<Book> unplacedFromParents = new HashSet<>();
        unplacedFromParents.addAll(parent1.getUnplacedBooks());
        unplacedFromParents.addAll(parent2.getUnplacedBooks());

        for (Book book : unplacedFromParents) {
            if (!placedBookIds.contains(book.getId())) {
                childUnplacedBooks.add(book);
            }
        }

        return new LibrarySolution(childShelves, childUnplacedBooks);
    }

    private void mutate(LibrarySolution pop) {
        if (random.nextDouble() > MUTATION_RATE) {
            return;
        }

        if (random.nextDouble() >= 0.5) {
            List<Shelf> shelves = pop.getShelves();
            int totalBooks = 0;
            for (Shelf shelf : shelves) {
                totalBooks += shelf.getBookCount();
            }

            if (totalBooks <= 0) {
                return;
            }

            while (true) {
                Shelf fromShelf = shelves.get(random.nextInt(0, shelves.size()));
                if (fromShelf.getBookCount() > 0) {
                    Book bookToMove = fromShelf.getBooks().get(random.nextInt(0, fromShelf.getBookCount()));
                    Shelf toShelf = shelves.get(random.nextInt(0, shelves.size()));
                    fromShelf.removeBook(bookToMove);
                    if (toShelf.addBook(bookToMove)) {
                        return;
                    }
                    fromShelf.addBook(bookToMove);
                    return;
                }
            }
        } else {
            List<Book> unplacedBooks = pop.getUnplacedBooks();

            if (unplacedBooks.isEmpty()) {
                return;
            }

            Book bookToMove = unplacedBooks.get(random.nextInt(unplacedBooks.size()));
            Shelf shelfTo = pop.shelves.get(random.nextInt(pop.shelves.size()));

            if (shelfTo.addBook(bookToMove)) {
                unplacedBooks.remove(bookToMove);
                return;
            }

            if (shelfTo.getBookCount() > 0) {
                Book bookToSwap = shelfTo.getBooks().get(random.nextInt(shelfTo.getBookCount()));

                shelfTo.removeBook(bookToSwap);

                if (shelfTo.addBook(bookToMove)) {
                    unplacedBooks.remove(bookToMove);
                    unplacedBooks.add(bookToSwap);
                } else {
                    shelfTo.addBook(bookToSwap);
                }
            }
        }
    }

    private void repairSolution(LibrarySolution pop) {
        Map<Integer, List<Shelf>> bookLocations = new HashMap<>();
        Map<Integer, Book> bookIdToObject = new HashMap<>();
        for (Shelf shelf : pop.getShelves()) {
            for (Book book : shelf.getBooks()) {
                int bookId = book.getId();
                bookLocations.putIfAbsent(bookId, new ArrayList<>());
                bookLocations.get(bookId).add(shelf);
                bookIdToObject.putIfAbsent(bookId, book);
            }
        }


        for (Map.Entry<Integer, List<Shelf>> entry : bookLocations.entrySet()) {
            List<Shelf> shelvesWithThisBook = entry.getValue();

            if (shelvesWithThisBook.size() > 1) {
                int bookId = entry.getKey();
                Book bookToRemove = bookIdToObject.get(bookId);

                Shelf shelfToKeep = shelvesWithThisBook.get(random.nextInt(shelvesWithThisBook.size()));

                for (Shelf shelf : shelvesWithThisBook) {
                    shelf.removeAllBooksById(bookId);
                }

                shelfToKeep.addBook(bookToRemove);
            }
        }
    }
}
