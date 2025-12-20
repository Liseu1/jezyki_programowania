package pl.edu.pwr.model;

public class Employee {
    private final int id;
    private String name;

    public Employee(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ID: " + id + " - " + name;
    }
}