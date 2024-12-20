package com.example.ettt;

public class Employee {
    private int id;
    private String name;
    private String position;
    private double salary;
    private String password;

    // Constructeur avec tous les paramètres nécessaires
    public Employee(int id, String name, String position, double salary, String password) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.password = password;
    }

    // Getters et setters pour tous les attributs
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
