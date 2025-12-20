package student.pwr.edu.toysounds.factory;
import student.pwr.edu.toysounds.toys.*;

import java.util.ArrayList;
import java.util.List;

import java.util.Random;

public class ToyFactory {
    public Toy createRandomToy() {
        Random r = new Random();
        int toyType = r.nextInt(3);

        return switch (toyType) {
            case 0 -> new Car();
            case 1 -> new Cat();
            case 2 -> new Dog();
            default -> throw new IllegalArgumentException("Error during toy generation");
        };
    }

    public List<Toy> createRandomToyList(int length) {
        if(length <= 0) {
            throw new IllegalArgumentException("Invalid length");
        }
        List<Toy> toys = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            toys.add(createRandomToy());
        }

        return toys;
    }
}
