package repository.sale;

import model.Book;

import java.util.List;

public interface SaleRepository {
    List<Book> findAllSaleBooks();
    boolean saveSale(Book book);
    public boolean updateStock(Book book);
    Integer getBookStockInSales(Book book);
    boolean updateBookStockInSales(Book book, Integer currentStock);
    boolean insertNewBookInSales(Book book);
}
