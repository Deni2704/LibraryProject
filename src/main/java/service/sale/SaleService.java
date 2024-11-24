package service.sale;

import model.Book;

import java.util.List;

public interface SaleService {
    List<Book> findSalesBook();
    boolean saveSale(Book book);
    boolean updateStock(Book book);


}
