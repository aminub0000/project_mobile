package com.example.project1;

public class account {
    String id;
    String nom;
    String email;
    String password;
    String image;

    public account(String id, String nom, String email, String password, String image) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.password = password;
        this.image = image;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
