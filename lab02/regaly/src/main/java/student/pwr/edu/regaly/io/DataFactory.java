package student.pwr.edu.regaly.io;

import student.pwr.edu.regaly.config.LibraryConfig;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class DataFactory {
    private static final Random random = new Random();

    public static void generateData() {
        try {
            generateShelves();
            generateBooks();
        } catch (IOException e) {
            System.err.println("Błąd przy generowaniu książek");
            e.printStackTrace();
        }
    }

    public static void generateBooks() throws IOException {
        try (FileWriter writer = new FileWriter(LibraryConfig.booksPath)) {
            StringBuilder sb = new StringBuilder();
            sb.append("# nr książki, wysokość książki, grubość książki, tematyka książki \n");

            for (int i = 1; i <= LibraryConfig.bookCount; i++) {
                int height = random.nextInt(LibraryConfig.minBookHeight, LibraryConfig.maxBookHeight);
                int thickness = random.nextInt(LibraryConfig.minThickness, LibraryConfig.maxThickness);
                int topic = random.nextInt(1, LibraryConfig.topicAmount);

                sb.append(i).append(", ");
                sb.append(height).append(", ");
                sb.append(thickness).append(", ");
                sb.append(topic).append("\n");
            }
            writer.write(sb.toString());
            System.out.print("Wygenerowano książki! \n");
        }
    }

    public static void generateShelves() throws IOException {
        try (FileWriter writer = new FileWriter(LibraryConfig.shelvesPath)) {
            StringBuilder sb = new StringBuilder();
            sb.append("# nr półki, wysokość półki, szerokość półki \n");

            for (int i = 1; i <= LibraryConfig.shelvesCount; i++) {
                int height = random.nextInt(LibraryConfig.minShelfHeight, LibraryConfig.maxShelfHeight);
                int width = random.nextInt(LibraryConfig.minWidth, LibraryConfig.maxWidth);

                sb.append(i).append(", ");
                sb.append(height).append(", ");
                sb.append(width).append("\n");
            }
            writer.write(sb.toString());
            System.out.print("Wygenerowano półki!\n");
        }
    }
}
