package pl.edu.pwr.model;

public class Organizer {
    private final int id;
    private String name;



    public Organizer(int id, String name){
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
}
