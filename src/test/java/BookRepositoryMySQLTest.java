import database.DatabaseConnectionFactory;
import model.Book;
import model.builder.BookBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repository.BookRepository;
import repository.BookRepositoryMock;
import repository.BookRepositoryMySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookRepositoryMySQLTest {
    private static BookRepository bookRepository;

    private static Connection connection;
    @BeforeAll
    public static void setup() throws SQLException {
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(true).getConnection();
        bookRepository = new BookRepositoryMySQL(connection);

    }
    @Test
    public void findAll(){
        List<Book> books = bookRepository.findAll();
        assertEquals(0, books.size());
    }
    @Test
    public void findById(){
        final Optional<Book> book = bookRepository.findById(1L);
        assertTrue(book.isEmpty());
    }
    @Test
    public void save(){
        assertTrue(bookRepository.save(new BookBuilder()
                .setTitle("Harry Potter")
                .setAuthor("J K Rowling")
                .setPublishedDate(LocalDate.of(2008,10,2))
                .setPrice(40)
                .setStock(5)
                .build()));
    }
}
