                                                                    package repository.book;

                                                                    import model.Book;
                                                                    import model.builder.BookBuilder;

                                                                    import java.sql.*;
                                                                    import java.util.ArrayList;
                                                                    import java.util.List;
                                                                    import java.util.Optional;

                                                                    public class BookRepositoryMySQL implements BookRepository{
                                                                        private final Connection connection;
                                                                        public BookRepositoryMySQL(Connection connection){
                                                                            this.connection = connection;
                                                                        }
                                                                        @Override
                                                                        public List<Book> findAll() {
                                                                            String sql = "SELECT * FROM book;";

                                                                            List<Book> books = new ArrayList<>();
                                                                            try {
                                                                                Statement statement = connection.createStatement();
                                                                                ResultSet resultSet = statement.executeQuery(sql);

                                                                                while(resultSet.next()){
                                                                                    System.out.println(getBookFromResultSet(resultSet));
                                                                                    books.add(getBookFromResultSet(resultSet));
                                                                                }
                                                                            } catch (SQLException e){
                                                                                e.printStackTrace();
                                                                            }
                                                                            return books;
                                                                        }

                                                                        @Override
                                                                        public Optional<Book> findById(Long id) {
                                                                            String sql = "SELECT * FROM book WHERE id = ? ";
                                                                            Optional<Book> book = Optional.empty();
                                                                            try{
                                                                                PreparedStatement statement = connection.prepareStatement(sql);
                                                                                statement.setLong(1,id);
                                                                                ResultSet resultSet = statement.executeQuery();

                                                                                if(resultSet.next()){
                                                                                    book = Optional.of(getBookFromResultSet(resultSet));
                                                                                }
                                                                            } catch(SQLException e){
                                                                                e.printStackTrace();
                                                                            }
                                                                            return book;
                                                                        }

                                                                        @Override
                                                                        public boolean save(Book book) {
                                                                            String newSql = "INSERT INTO book(id, author, title, publishedDate) VALUES(NULL, ?, ?, ?) ";
                                                                            try{
                                                                                PreparedStatement statement = connection.prepareStatement(newSql);
                                                                                statement.setString(1, book.getAuthor());
                                                                                statement.setString(2, book.getTitle());
                                                                                statement.setDate(3, Date.valueOf((book.getPublishedDate())));
                                                                                statement.executeUpdate();
                                                                            } catch (SQLException e){
                                                                                e.printStackTrace();
                                                                                return false;
                                                                            }
                                                                            return true;
                                                                        }

                                                                        @Override
                                                                        public boolean delete(Book book) {
                                                                            String newSql = "DELETE FROM book WHERE author = ? AND title = ?";
                                                                            try{
                                                                                PreparedStatement statement = connection.prepareStatement(newSql);
                                                                                statement.setString(1, book.getAuthor());
                                                                                statement.setString(2, book.getTitle());
                                                                                statement.executeUpdate();
                                                                            } catch(SQLException e){
                                                                                e.printStackTrace();
                                                                                return false;
                                                                            }
                                                                            return true;
                                                                        }

                                                                        @Override
                                                                        public void removeAll() {
                                                                            String sql = "TRUNCATE TABLE book;";
                                                                            try{
                                                                                Statement statement = connection.createStatement();
                                                                                statement.executeUpdate(sql);
                                                                            } catch(SQLException e){
                                                                                e.printStackTrace();
                                                                            }
                                                                        }


                                                                        private Book getBookFromResultSet(ResultSet resultSet) throws SQLException{
                                                                            return new BookBuilder()
                                                                                    .setId(resultSet.getLong("id"))
                                                                                    .setTitle(resultSet.getString("title"))
                                                                                    .setAuthor(resultSet.getString("author"))
                                                                                    .setPublishedDate(new java.sql.Date(resultSet.getDate("publishedDate").getTime()).toLocalDate())
                                                                                    .build();
                                                                        }
                                                                    }
