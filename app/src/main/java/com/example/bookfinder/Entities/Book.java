package com.example.bookfinder.Entities;

public class Book
{
    private String bookName;
    private String author;
    private String resume;
    private boolean isFavourite;

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

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }
}
