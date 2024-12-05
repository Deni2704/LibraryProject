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
                        .setStock(resultSet.getInt("stock"))
                        .build();
                soldBooks.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return soldBooks;
    }

    @Override
    public boolean saveSale(Book book,int user_id) {

            return insertNewBookInSales(book,user_id);

    }

    @Override
    public Integer getBookStockInSales(Book book) {
        String checkBookSql = "SELECT stock FROM sales WHERE author = ? AND title = ?";
        try (PreparedStatement statement = connection.prepareStatement(checkBookSql)) {
            statement.setString(1, book.getAuthor());
            statement.setString(2, book.getTitle());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("stock");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    @Override
    public boolean insertNewBookInSales(Book book, int user_id) {
        String insertBookSql = "INSERT INTO sales (id, author, title, price, stock,user_id) VALUES (NULL, ?, ?, ?, ?,?)";
        try (PreparedStatement statement = connection.prepareStatement(insertBookSql)) {
            statement.setString(1, book.getAuthor());
            statement.setString(2, book.getTitle());
            statement.setInt(3, book.getPrice());
            statement.setInt(4, 1);
            statement.setInt(5, Math.toIntExact(user_id));
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateStock(Book book) {
        if (book.getStock() == null || book.getStock() <= 0) {
            return false;
        }
        book.setStock(book.getStock() - 1);

        String updateStockSql = "UPDATE book SET stock = ? WHERE author = ? AND title = ?";
        try (PreparedStatement statement = connection.prepareStatement(updateStockSql)) {
            statement.setInt(1, book.getStock());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getTitle());
            int rowsUpdated = statement.executeUpdate();

            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
