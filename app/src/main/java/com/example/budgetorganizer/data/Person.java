package com.example.budgetorganizer.data;

import java.io.Serializable;

public class Person implements Comparable<Person>, Serializable {
    private long id;
    private String name;
    private int budget;
    private int TotalGifBought;
    public Person() {
    }

    public Person(String name, int budget) {
        this.name = name;
        this.budget = budget;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }
    public int getTotalGifBought() {
        return TotalGifBought;
    }

    public void setTotalGifBought(int TotalGifBought) {
        this.TotalGifBought = TotalGifBought;
    }

    @Override
    public int compareTo(Person c) {
        return this.getName().compareTo(c.getName());
    }
}
