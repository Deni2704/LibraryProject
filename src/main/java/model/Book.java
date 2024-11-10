package model;

import java.time.LocalDate;

public class Book {
    private Long id;
    private String author;
    private String title;
    private LocalDate publishedDate;
    private Integer price;
    private Integer stock;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
    public void setStock(Integer stock){
        this.stock = stock;
    }

    @Override
    public String toString(){
        return "Book: Id: " + id + " Title:" + title + " Author:" + author +" Published Date: " + publishedDate + "Price: " + price + "Stock: "+ stock;
    }
}
