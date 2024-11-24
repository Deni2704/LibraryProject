package view;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import model.Book;
import view.model.BookDTO;

import java.util.List;
public class EmployeeView{
    private TableView bookTableView;
    private TableView saleTableView;
    private final ObservableList<BookDTO> booksObservableList;
    private final ObservableList<BookDTO> booksObservableListSale;
    private TextField authorTextField;
    private TextField titleTextField;
    private TextField priceTextField;
    private TextField stockTextField;
    private Label authorLabel;
    private Label titleLabel;
    private Label priceLabel;
    private Label stockLabel;
    private Button saveButton;
    private Button deleteButoon;
    private Button saleButton;

    public EmployeeView(Stage primaryStage, List<BookDTO> books, List<BookDTO> soldBooks){
        primaryStage.setTitle("Library");
        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);



        Scene scene = new Scene(gridPane, 1020,480);
        primaryStage.setScene(scene);

        booksObservableList = FXCollections.observableArrayList(books);
        booksObservableListSale = FXCollections.observableArrayList(soldBooks);
        initTableView(gridPane);
        initTableViewSale(gridPane);

        initSaveOptions(gridPane);
        primaryStage.show();

    }
    private void initializeGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25,25,25,25));

    }
    private void initTableView(GridPane gridPane){
        bookTableView = new TableView<BookDTO>();

        bookTableView.setPlaceholder(new Label("No books to display"));
        TableColumn<BookDTO, String> titleColumn = new TableColumn<BookDTO,String>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<BookDTO, String> authorColumn = new TableColumn<BookDTO,String>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<BookDTO, Integer> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<BookDTO, Integer> stockColumn = new TableColumn<>("Stock");
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));


        bookTableView.getColumns().addAll(titleColumn, authorColumn, priceColumn, stockColumn);
        bookTableView.setItems(booksObservableList);

        gridPane.add(bookTableView,0,0,5,1);


    }
    private void initTableViewSale(GridPane gridPane) {
        saleTableView = new TableView<BookDTO>();
        saleTableView.setPlaceholder(new Label("No sales to display"));

        TableColumn<BookDTO, String> saleTitleColumn = new TableColumn<>("Title");
        saleTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<BookDTO, String> saleAuthorColumn = new TableColumn<>("Author");
        saleAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<BookDTO, Integer> salePriceColumn = new TableColumn<>("Price");
        salePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));



        saleTableView.getColumns().addAll(saleTitleColumn, saleAuthorColumn, salePriceColumn);
        saleTableView.setItems(booksObservableListSale);


        gridPane.add(saleTableView, 5, 0, 5, 1);
    }

    private void initSaveOptions(GridPane gridPane){
        titleLabel = new Label("Title");
        gridPane.add(titleLabel, 1, 1);

        titleTextField = new TextField();
        gridPane.add(titleTextField,2,1);

        authorLabel = new Label("Author");
        gridPane.add(authorLabel,3, 1);

        authorTextField = new TextField();
        gridPane.add(authorTextField,4,1);

        priceLabel = new Label("Price");
        gridPane.add(priceLabel,5,1);

        priceTextField = new TextField();
        gridPane.add(priceTextField,6,1);

        stockLabel = new Label("Stock");
        gridPane.add(stockLabel,7,1);

        stockTextField = new TextField();
        gridPane.add(stockTextField,8,1);


        saveButton = new Button("Save");
        gridPane.add(saveButton,9 , 1);

        deleteButoon = new Button("Delete");
        gridPane.add(deleteButoon,10,1);

        saleButton = new Button("Sale");
        gridPane.add(saleButton,11,1);


    }

    public void addSaveButtonListener(EventHandler<ActionEvent> saveButtonListener){
        saveButton.setOnAction(saveButtonListener);
    }
    public void addDeleteButtonListener(EventHandler<ActionEvent> deleteButtonListener){
        deleteButoon.setOnAction(deleteButtonListener);
    }
    public void addSaleButtonListener(EventHandler<ActionEvent> saleButtonListener){
        saleButton.setOnAction(saleButtonListener);
    }
    public void addDisplayAlertMesssage(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION );
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public String getTitle(){
        return titleTextField.getText();
    }
    public String getAuthor(){
        return authorTextField.getText();
    }
    public Integer getPrice() { return Integer.valueOf(priceTextField.getText()); }
    public Integer getStock() {return Integer.valueOf(stockTextField.getText()); }
    public void addBookToObservableList(BookDTO bookDTO){
        this.booksObservableList.add(bookDTO);
    }
    public void addBookToObservableListSale(BookDTO bookDTO) { this.booksObservableListSale.add(bookDTO);}
    public void removeBookFromObservableList(BookDTO bookDTO){
        this.booksObservableList.remove(bookDTO);
    }
    public void updateBookInTable(BookDTO updatedBook) {
        ObservableList<BookDTO> books = bookTableView.getItems();
        for (int i = 0; i < books.size(); i++) {
            BookDTO book = books.get(i);
            if (book.getTitle().equals(updatedBook.getTitle()) && book.getAuthor().equals(updatedBook.getAuthor())) {
                books.set(i, updatedBook);
                break;
            }
        }
        bookTableView.refresh();
    }
    public TableView getBookTableView(){
        return bookTableView;
    }
}