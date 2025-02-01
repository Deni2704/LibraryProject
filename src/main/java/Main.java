import database.DatabaseConnectionFactory;
import model.Book;
import model.builder.BookBuilder;
import repository.book.BookRepository;
import repository.book.BookRepositoryCacheDecorator;
import repository.book.BookRepositoryMySQL;
import repository.book.Cache;
import service.book.BookService;
import service.book.BookServiceImpl;

import java.sql.Connection;
import java.time.LocalDate;

public class Main {
   public static void main(String [] args)
   {
       System.out.println("hello world");


       Book book = new BookBuilder()
               .setTitle("Ion")
               .setAuthor("Liviu Rebreanu")
               .setPublishedDate(LocalDate.of(1910, 10, 20))
               .build();
       //System.out.println(book);

       /*BookRepository bookRepository = new BookRepositoryMock();
       bookRepository.save(book);
       bookRepository.save(new BookBuilder().setAuthor("Ioan Slavici")
               .setTitle("Moara cu noroc")
               .setPublishedDate(LocalDate.of(1950,2,7)).build());
       System.out.println(bookRepository.findAll());
       bookRepository.removeAll();
       System.out.println(bookRepository.findAll());*/
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(false).getConnection();
        BookRepository bookRepository = new BookRepositoryCacheDecorator(new BookRepositoryMySQL(connection), new Cache<>());
       BookService bookService = new BookServiceImpl(bookRepository);

       bookService.save(book);
       System.out.println(bookService.findAll());
       /* Book bookMoaraCuNoroc = new BookBuilder().setAuthor("Ioan Slavici")
                .setTitle("Moara cu noroc")
                .setPublishedDate(LocalDate.of(1950,2,7))
                .build();

       bookService.save(bookMoaraCuNoroc);
       System.out.println(bookService.findAll());*/
       //bookService.delete(bookMoaraCuNoroc);
      // bookService.delete(book);
       //System.out.println(bookService.findAll());
   }
}
