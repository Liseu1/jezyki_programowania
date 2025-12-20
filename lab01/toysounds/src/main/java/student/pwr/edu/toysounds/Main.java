package student.pwr.edu.toysounds;

import student.pwr.edu.toysounds.factory.ToyFactory;
import student.pwr.edu.toysounds.toys.Toy;

import java.util.List;

public class Main {
        static void main(String[] args) {
        ToyFactory toyFactory = new ToyFactory();
        List<Toy> toys = toyFactory.createRandomToyList(10);

        for(Toy toy : toys) {
            System.out.println(toy.makeSound());
        }
    }
}
