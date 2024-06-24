package org.example.domain;


import org.example.domain.exception.InvalidBookException;
import org.example.domain.exception.InvalidCustomerException;

import java.io.Serializable;

public class Book implements Serializable {
    private final String id;
    private String title;
    private String author;
    private String description;
    private int availableCopies;

    public Book(String id,String title, String author, String description, int availableCopies){
        if (id == null || title == null || author == null || description == null
                || id.isBlank()  ||  title.isBlank() || author.isBlank() || description.isBlank()
                || availableCopies < 0) throw new InvalidBookException("Book Information cannot be null or blank or avaliable copies is negative number");
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.availableCopies = availableCopies;
    }

    public String getId() {
        return id;
    }
    public String gettitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public String getDescription() {
        return description;
    }
    public int getAvailableCopies() {
        return availableCopies;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setAvailableCopies(int availableCopies) {
        if (availableCopies < 0) throw new InvalidBookException("avaliable copies cannot be negative number");
        this.availableCopies = availableCopies;
    }

    public boolean isAvailable(){
        return availableCopies > 0;
    }


    @Override
    public boolean equals(Object obj) {
        Book b = (Book)obj;
        return this == obj
                || obj != null
                && this.id == b.id
                && this.title == b.title
                && this.author == b.author
                && this.description == b.description;
    }

    @Override
    public String toString() {
        return this.getId()+"["+this.gettitle()+"]";
    }

}



