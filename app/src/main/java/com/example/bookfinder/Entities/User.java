package com.example.bookfinder.Entities;

import java.util.ArrayList;
import java.util.List;

public class User
{
    private String username;
    private String name;
    private String email;
    private String password;
    List<Book> books = new ArrayList<>();

    public User(String username, String name, String email, String password, List<Book> books) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<Book> getBooks() {
        return books;
    }
}
