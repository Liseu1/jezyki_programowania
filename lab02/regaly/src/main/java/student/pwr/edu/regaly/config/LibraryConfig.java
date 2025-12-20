package student.pwr.edu.regaly.config;

import java.util.Scanner;

public class LibraryConfig {
    public static String booksPath;
    public static String shelvesPath;
    public static double weightFill;
    public static double weightCount;
    public static double weightDifference;
    public static boolean generateNewData;
    public static int minBookHeight = 5;
    public static int maxBookHeight = 21;
    public static int minThickness = 3;
    public static int maxThickness = 16;
    public static int topicAmount = 6;
    public static int minShelfHeight = 5;
    public static int maxShelfHeight = 25;
    public static int minWidth = 30;
    public static int maxWidth = 150;
    public static int bookCount = 100;
    public static int shelvesCount = 10;

    public static void initializeConfig(Scanner scanner) throws Exception {

        System.out.println("[0] Wczytaj dane \n[1] Wygeneruj nowe dane");

        String answer = scanner.nextLine();

        if (answer.equals("0")) {
            generateNewData = false;
        } else if (answer.equals("1")) {
            generateNewData = true;
            System.out.println("Czy chcesz zmienić parametry książek i półek? \n[0] Nie \n[1] Tak ");
            answer = scanner.nextLine();

            if (answer.equals("1")) {
                System.out.println("Podaj liczbę książek:");
                bookCount = Integer.parseInt(scanner.nextLine());
                System.out.println("Podaj liczbę półek:");
                shelvesCount = Integer.parseInt(scanner.nextLine());




                System.out.println("Podaj minimalną grubość książki:");
                minThickness = Integer.parseInt(scanner.nextLine());
                System.out.println("Podaj maksymalną grubość książki:");
                maxThickness = Integer.parseInt(scanner.nextLine()) + 1;

                if (minThickness > maxThickness) {
                    throw new Exception("bad input");
                }

                System.out.println("Podaj liczbę tematów:");
                topicAmount = Integer.parseInt(scanner.nextLine());

                System.out.println("Podaj minimalną szerokość półki:");
                minWidth = Integer.parseInt(scanner.nextLine());
                System.out.println("Podaj maksymalną szerokość półki:");
                maxWidth = Integer.parseInt(scanner.nextLine()) + 1;

                if (minWidth > maxWidth) {
                    throw new Exception("bad input");
                }

                System.out.println("Podaj minimalną wysokość półki:");
                minShelfHeight = Integer.parseInt(scanner.nextLine());
                System.out.println("Podaj maksymalną wysokość półki:");
                maxShelfHeight = Integer.parseInt(scanner.nextLine()) + 1;

                if (minShelfHeight > maxShelfHeight) {
                    throw new Exception("bad input");
                }

                System.out.println("Podaj minimalną wysokość książki:");
                minBookHeight = Integer.parseInt(scanner.nextLine());
                System.out.println("Podaj maksymalną wysokość książki:");
                maxBookHeight = Integer.parseInt(scanner.nextLine()) + 1;
                if (minBookHeight > maxBookHeight) {
                    throw new Exception("bad input");
                }
            }
        } else {
            throw new Exception("bad input");
        }

        System.out.println("Podaj nazwę pliku z książkami:");
        booksPath = scanner.nextLine();

        System.out.println("Podaj nazwę pliku z półkami:");
        shelvesPath = scanner.nextLine();

        System.out.println("Podaj wartość kryterium F (fill):");
        weightFill = scanner.nextDouble();

        System.out.println("Podaj wartość kryterium C (count):");
        weightCount = scanner.nextDouble();

        System.out.println("Podaj wartość kryterium D (difference):");
        weightDifference = scanner.nextDouble();
    }
}
