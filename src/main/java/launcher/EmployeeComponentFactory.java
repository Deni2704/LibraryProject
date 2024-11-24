package launcher;

import controller.BookController;
import controller.EmployeeController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import mapper.BookMapper;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;
import repository.sale.SaleRepository;
import repository.sale.SaleRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.sale.SaleService;
import service.sale.SaleServiceImpl;
import view.BookView;
import view.EmployeeView;
import view.model.BookDTO;

import java.sql.Connection;
import java.util.List;

public class EmployeeComponentFactory {
    private final EmployeeView employeeView;
    private final EmployeeController employeeController;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private final SaleRepository saleRepository;
    private final SaleService saleService;
    private static EmployeeComponentFactory instance;
    public synchronized static EmployeeComponentFactory getInstance(Boolean componentsForTest, Stage primaryStage){
        if( instance == null){
            instance = new EmployeeComponentFactory(componentsForTest, primaryStage);
        }
        return instance;
    }
    public EmployeeComponentFactory(Boolean componentsForTest, Stage primaryStage)
    {
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();
        this.bookRepository = new BookRepositoryMySQL(connection);
        this.bookService = new BookServiceImpl(bookRepository);
        this.saleRepository = new SaleRepositoryMySQL(connection);
        this.saleService = new SaleServiceImpl(saleRepository);
        List<BookDTO> bookDTOS = BookMapper.convertBookListToBookDTOList(bookService.findAll());
        List<BookDTO> soldBookDTOS = BookMapper.convertBookListToBookDTOListSales(saleService.findSalesBook());
        this.employeeView = new EmployeeView(primaryStage, bookDTOS, soldBookDTOS);
        this.employeeController= new EmployeeController(employeeView, bookService,saleService);
    }


    public EmployeeView getEmployeeView() {
        return employeeView;
    }

    public EmployeeController getEmployeeController() {
        return employeeController;
    }

    public BookRepository getBookRepository() {
        return bookRepository;
    }

    public BookService getBookService() {
        return bookService;
    }

    public SaleRepository getSaleRepository() {
        return saleRepository;
    }

    public SaleService getSaleService() {
        return saleService;
    }
}