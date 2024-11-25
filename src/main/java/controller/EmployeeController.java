package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mapper.BookMapper;
import model.Book;
import service.book.BookService;
import service.sale.SaleService;
import view.BookView;
import view.EmployeeView;
import view.model.BookDTO;
import view.model.builder.BookDTOBuilder;

public class EmployeeController {
    private final EmployeeView employeeView;
    private final BookService bookService;
    private final SaleService saleService;

    public EmployeeController(EmployeeView employeeView, BookService bookService, SaleService saleService) {
        this.employeeView = employeeView;
        this.bookService = bookService;
        this.saleService = saleService;
        this.employeeView.addSaveButtonListener(new SaveButtonListener());
        this.employeeView.addDeleteButtonListener(new DeleteButtonListener());
        this.employeeView.addSaleButtonListener(new SaleButtonListener());
    }

    private class SaveButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String title = employeeView.getTitle();
            String author = employeeView.getAuthor();
            Integer price = employeeView.getPrice();
            Integer stock = employeeView.getStock();

            if (title.isEmpty() || author.isEmpty()) {
                employeeView.addDisplayAlertMesssage("Save Error", "Problem at Author or Title fields", "Can not have an empty Title or Author field.");
            } else {
                BookDTO bookDTO = new BookDTOBuilder()
                        .setTitle(title)
                        .setAuthor(author)
                        .setPrice(price)
                        .setStock(stock)
                        .build();
                boolean savedBook = bookService.save(BookMapper.convertBookDTOToBook(bookDTO));

                if (savedBook) {
                    employeeView.addDisplayAlertMesssage("Save Successful", "Book Added", "Book was successfully added to the database.");
                    employeeView.addBookToObservableList(bookDTO);

                } else {
                    employeeView.addDisplayAlertMesssage("Save Error", "Problem at adding Book", "There was a problem at adding the book to the database. Please try again!");
                }
            }
        }
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            BookDTO bookDTO = (BookDTO) employeeView.getBookTableView().getSelectionModel().getSelectedItem();
            if (bookDTO != null) {
                boolean deletionSuccessful = bookService.delete(BookMapper.convertBookDTOToBook(bookDTO));
                if (deletionSuccessful) {
                    employeeView.addDisplayAlertMesssage("Delete Successful", "Book Deleted", "Book was successfully deleted from the database.");
                    employeeView.removeBookFromObservableList(bookDTO);
                } else {
                    employeeView.addDisplayAlertMesssage("Delete Error", "Problem at deleting book", "There was a problem with the database. Please try again!");
                }
            } else {
                employeeView.addDisplayAlertMesssage("Delete Error", "Problem at deleting book", "You must select a book before pressing the delete button.");
            }
        }
    }

    private class SaleButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            BookDTO saleBookDTO = (BookDTO) employeeView.getBookTableView().getSelectionModel().getSelectedItem();

            if (saleBookDTO == null) {
                employeeView.addDisplayAlertMesssage("Sale Error", "No Selection", "You must select a book for sale.");
                return;
            }

            Book saleBook = BookMapper.convertBookDTOToBook(saleBookDTO);

            if (saleBook.getStock() <= 0) {
                employeeView.addDisplayAlertMesssage("Sale Error", "Out of Stock", "The selected book is out of stock.");
                return;
            }

            boolean stockUpdated = saleService.updateStock(saleBook);
            if (stockUpdated) {
                if (saleBook.getStock() == 0) {
                    boolean deletionSuccessful = bookService.delete(saleBook);
                    if (deletionSuccessful) {
                        employeeView.removeBookFromObservableList(saleBookDTO);
                    }
                }
                saleService.saveSale(saleBook); // Add sale to the database
                saleBookDTO.setStock(saleBook.getStock()); // Update stock in DTO
                employeeView.addBookToObservableListSale(saleBookDTO);
                employeeView.addDisplayAlertMesssage("Sale Successful", "Book Sold", "The book was sold successfully.");
            } else {
                employeeView.addDisplayAlertMesssage("Sale Error", "Update Failed", "Failed to update the stock.");
            }
        }
    }

}


