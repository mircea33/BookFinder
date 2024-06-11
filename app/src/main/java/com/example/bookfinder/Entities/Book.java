package com.example.bookfinder.Entities;

public class Book
{
    private String bookName;
    private String author;
    private String resume;
    private String genre;

    public Book(String bookName, String author, String resume, String genre)
    {
        this.bookName = bookName;
        this.author = author;
        this.resume = resume;
        this.genre = genre;
    }

    public String getBookName() {
        return bookName;
    }

    public String getAuthor() {
        return author;
    }

    public String getResume() {
        return resume;
    }

    public String getGenre() {
        return genre;
    }
}
