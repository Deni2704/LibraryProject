package repository.sale;

import model.Book;
import model.builder.BookBuilder;
import repository.book.BookRepositoryMySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleRepositoryMySQL implements SaleRepository{
    private final Connection connection;
    public SaleRepositoryMySQL(Connection connection){
        this.connection = connection;
    }
    @Override
    public List<Book> findAllSaleBooks() {
        String sql = "SELECT * FROM sales;";
        List<Book> soldBooks = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Book book = new BookBuilder()
                        .setTitle(resultSet.getString("title"))
                        .setAuthor(resultSet.getString("author"))
                        .setPrice(resultSet.getInt("price"))
                        .build();
                soldBooks.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return soldBooks;
    }

    @Override
    public boolean saveSale(Book book) {
        String sql = "INSERT INTO sales (id,author,title,price) VALUES (NULL,?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,book.getAuthor());
            statement.setString(2, book.getTitle());
            statement.setInt(3,book.getPrice());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean updateStock(Book book) {
        if (book.getStock() == null || book.getStock() <= 0) {
            return false; // Out of stock
        }
        book.setStock(book.getStock() - 1); // Reduce stock by 1

        String updateStockSql = "UPDATE book SET stock = ? WHERE author = ? AND title = ?";
        try (PreparedStatement statement = connection.prepareStatement(updateStockSql)) {
            statement.setInt(1, book.getStock());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getTitle());
            int rowsUpdated = statement.executeUpdate();

            return rowsUpdated > 0; // True if stock was updated
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
