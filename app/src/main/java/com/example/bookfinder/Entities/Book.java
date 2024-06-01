package com.example.bookfinder.Entities;

public class Book
{
    private String bookName;
    private String author;
    private String resume;

    public Book(String bookName, String author, String resume)
    {
        this.bookName = bookName;
        this.author = author;
        this.resume = resume;
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
}
