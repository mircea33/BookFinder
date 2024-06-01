package com.example.bookfinder.Entities;

import java.util.ArrayList;
import java.util.List;

public class User
{
    private String name, email, username, password;
    private List<Book> favourites = new ArrayList<>();

    public User(String name, String email, String username, String password)
    {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }
}