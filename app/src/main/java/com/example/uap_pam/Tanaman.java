package com.example.uap_pam;

public class Tanaman {
    private int id;
    private String plant_name;
    private String description;
    private String price;

    public int getId() { return id; }
    public String getPlant_name() { return plant_name; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }

    public Tanaman(String plant_name, String description, String price) {
        this.plant_name = plant_name;
        this.description = description;
        this.price = price;
    }
}
