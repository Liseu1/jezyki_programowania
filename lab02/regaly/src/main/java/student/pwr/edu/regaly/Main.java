package student.pwr.edu.regaly;

import student.pwr.edu.regaly.config.LibraryConfig;
import student.pwr.edu.regaly.io.DataFactory;
import student.pwr.edu.regaly.io.LibraryDataset;
import student.pwr.edu.regaly.model.Book;
import student.pwr.edu.regaly.model.LibrarySolution;
import student.pwr.edu.regaly.model.Result;
import student.pwr.edu.regaly.model.Shelf;
import student.pwr.edu.regaly.solver.LibraryAlgorithm;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        LibraryConfig.initializeConfig(scanner);

        if (LibraryConfig.generateNewData) {
            DataFactory.generateData();
        }

        LibraryDataset dataset = new LibraryDataset(LibraryConfig.shelvesPath, LibraryConfig.booksPath);

        LibraryAlgorithm solver = new LibraryAlgorithm();
        Result result = solver.solve(dataset);
        LibrarySolution solution = (LibrarySolution) result;

        for (Shelf shelf : solution.getShelves()) {
            System.out.print("\nNa półce o id " + shelf.getId() + " Znajdują się książki: ");
            for (Book book : shelf.getBooks()) {
                System.out.print(book.getId() + " ");
            }
        }

        scanner.close();
    }
}
