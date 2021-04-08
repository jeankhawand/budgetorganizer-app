package com.example.budgetorganizer.data;

import java.io.Serializable;

public class Gift implements Comparable<Gift>, Serializable {
    private long id;
    private String name;
    private int price;
    private long person_id;
    private String photo_path;
    private String date;

    public Gift() {
    }

    public Gift(long id, String name, int price, long person_id, String photo_path, String date) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.person_id = person_id;
        this.photo_path = photo_path;
        this.date = date;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getPersonId() {
        return person_id;
    }

    public void setPersonId(long person_id) {
        this.person_id = person_id;
    }

    public String getPhotoPath() {
        return photo_path;
    }

    public void setPhotoPath(String photo_path) {
        this.photo_path = photo_path;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int compareTo(Gift g) {
        return this.getName().compareTo(g.getName());
    }
}
