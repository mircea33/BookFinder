package com.example.bookfinder.Entities;

import java.util.HashSet;
import java.util.Set;

public class User
{
    private String name;
    private String email;
    private String username;
    private String password;
    private Set<String> favourites = new HashSet<>();
    private Set<String> library = new HashSet<>();

    public User(String name, String email, String username, String password)
    {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Set<String> getFavourites() {
        return favourites;
    }

    public Set<String> getLibrary()
    {
        return library;
    }
}