package repository.sale;

import model.Book;

import java.util.List;

public interface SaleRepository {
    List<Book> findAllSaleBooks();
    boolean saveSale(Book book,int user_id);
    public boolean updateStock(Book book);
    Integer getBookStockInSales(Book book);

    boolean insertNewBookInSales(Book book, int user_id);
}
