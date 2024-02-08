package com.virtual.software.mybuddymakemoney;
public class Card {
    private int id;
    private String name;
    private String prediction;
    private String brain;
    private String Initialize;
    private String skip;


    private String wait;

    public Card(int id, String name, String prediction, String brain, String initialize, String skip, String wait) {
        this.id = id;
        this.name = name;
        this.prediction = prediction;
        this.brain = brain;
        Initialize = initialize;
        this.skip = skip;
        this.wait = wait;
    }

    public String getSkip() {
        return skip;
    }

    public void setSkip(String skip) {
        this.skip = skip;
    }

    public String getInitialize() {
        return Initialize;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrediction() {
        return prediction;
    }

    public String getBrain() {
        return brain;
    }

    public String getWait() {
        return wait;
    }

    // Setter methods (if needed)
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", prediction='" + prediction + '\'' +
                ", brain='" + brain + '\'' +
                ", Initialize='" + Initialize + '\'' +
                ", skip='" + skip + '\'' +
                ", wait='" + wait + '\'' +
                '}';
    }


// Additional methods can be added as needed
}
