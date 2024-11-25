package service.sale;

import model.Book;
import repository.sale.SaleRepository;

import java.util.List;

public class SaleServiceImpl implements SaleService{
    private final SaleRepository saleRepository;
    public SaleServiceImpl(SaleRepository saleRepository){
        this.saleRepository = saleRepository;
    }
    @Override
    public List<Book> findSalesBook() {
        return saleRepository.findAllSaleBooks();
    }

    @Override
    public boolean saveSale(Book book) {
        return saleRepository.saveSale(book);
    }

    @Override
    public boolean updateStock(Book book) {
        return saleRepository.updateStock(book);
    }




}
