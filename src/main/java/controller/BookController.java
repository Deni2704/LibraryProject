package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mapper.BookMapper;
import service.BookService;
import view.BookView;
import view.model.BookDTO;
import view.model.builder.BookDTOBuilder;

public class BookController {
    private final BookView bookView;
    private final BookService bookService;
    public BookController(BookView bookView, BookService bookService){
        this.bookView=bookView;
        this.bookService=bookService;

        this.bookView.addSaveButtonListener(new SaveButtonListener());
        this.bookView.addDeleteButtonListener(new DeleteButtonListener());
        this.bookView.addSaleButtonListener(new SaleButtonListener());
    }

    private class SaveButtonListener implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event){
            String title = bookView.getTitle();
            String author = bookView.getAuthor();
            Integer price = bookView.getPrice();
            Integer stock = bookView.getStock();

            if(title.isEmpty() || author.isEmpty()){
                bookView.addDisplayAlertMesssage("Save Error","Problem at author or title fields","Can not have an empty title or author field");
            } else{
                BookDTO bookDTO = new BookDTOBuilder()
                        .setTitle(title)
                        .setAuthor(author)
                        .setPrice(price)
                        .setStock(stock)
                        .build();
                boolean savedBook = bookService.save(BookMapper.convertBookDTOToBook(bookDTO));

                if(savedBook){
                    bookView.addDisplayAlertMesssage("Save Successful","Book Added","Book was successfully added to database");
                    bookView.addBookToObservableList(bookDTO);
                }
                else {
                    bookView.addDisplayAlertMesssage("Save Error","Problem at adding Book","There was a problem at adding Book please try again");
                }
            }
        }
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event) {
            BookDTO bookDTO = (BookDTO) bookView.getBookTableView().getSelectionModel().getSelectedItem();
            if(bookDTO != null){
                boolean deletionSuccessful = bookService.delete(BookMapper.convertBookDTOToBook(bookDTO));

                if(deletionSuccessful){
                    bookView.addDisplayAlertMesssage("Delete Successful","Book deleted","Book was successfully deleted to database");
                    bookView.removeBookFromObservableList(bookDTO);
                } else{
                    bookView.addDisplayAlertMesssage("Delete Error","Problem at deleting Book","There was a problem at deleting Book please try again");
                }
            }
            else{
                bookView.addDisplayAlertMesssage("Delete Error","Problem at deleting","You must select a book before pressing the delete button");
            }
        }
    }
    private class SaleButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            BookDTO saleBook = (BookDTO) bookView.getBookTableView().getSelectionModel().getSelectedItem();
            if (saleBook != null) {
                if (saleBook.getStock() > 0) {
                    saleBook.setStock(saleBook.getStock() - 1);
                    bookView.getBookTableView().refresh();
                    if (saleBook.getStock() == 0) {
                        bookView.removeBookFromObservableList(saleBook);
                    }
                    bookView.addBookToObservableListSale(saleBook);
                    boolean saleSaved = bookService.saveSaleBook(BookMapper.convertBookDTOToBook(saleBook));
                    if (saleSaved) {
                        bookView.addDisplayAlertMesssage("Sale Successful", "Book sold", "The book was sold successfully.");
                    } else {
                        bookView.addDisplayAlertMesssage("Sale Error", "Database Error", "There was a problem saving the sale to the database.");
                    }
                } else {
                    bookView.addDisplayAlertMesssage("Sale Error", "Out of Stock", "The selected book is out of stock.");
                }
            } else {
                bookView.addDisplayAlertMesssage("Sale Error", "No Selection", "You must select a book before pressing the sale button.");
            }
        }

    }}
