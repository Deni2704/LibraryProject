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
import model.User;
import view.model.UserDTO;
import view.model.BookDTO;

import java.util.List;

public class AdminView {
    // Tabele pentru angajați și vânzări
    private TableView<UserDTO> employeeTableView;
    private TableView<BookDTO> saleTableView;

    // Observabile pentru a adăuga datele
    private final ObservableList<UserDTO> userObservableList;
    private final ObservableList<BookDTO> booksObservableListSale;

    // Câmpuri de text pentru adăugarea unui utilizator
    private TextField usernameTextField;
    private PasswordField passwordTextField;

    // Etichete pentru câmpurile de text
    private Label usernameLabel;
    private Label passwordLabel;

    // Butonul pentru adăugarea unui utilizator
    private Button addUserButton;
    private Label roleLabel;
    private Button deleteUserButton;
    private Button generateReport;
    private ComboBox roleComboBox;
    private ComboBox<String> monthComboBox;

    private User currentUser;

    public AdminView(Stage primaryStage, List<UserDTO> users, List<BookDTO> soldBooks, User currentUser) {
        this.currentUser = currentUser;

        // Setează titlul ferestrei
        primaryStage.setTitle("Admin Panel");

        // Grid pane pentru organizarea elementelor UI
        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        // Crează scena
        Scene scene = new Scene(gridPane, 1200, 600);
        primaryStage.setScene(scene);

        // Inițializează tabelele
        booksObservableListSale = FXCollections.observableArrayList(soldBooks);
        userObservableList = FXCollections.observableArrayList(users);
        initEmployeeTableView(gridPane);
        initSaleTableView(gridPane);

        // Inițializează câmpurile și butonul pentru adăugarea unui utilizator
        initUserAddOptions(gridPane);

        primaryStage.show();
    }

    // Configurarea GridPane-ului
    private void initializeGridPane(GridPane gridPane) {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    // Inițializare tabel cu angajați
    private void initEmployeeTableView(GridPane gridPane) {
        employeeTableView = new TableView<UserDTO>();

        // Definirea coloanelor pentru tabel
        TableColumn<UserDTO, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<UserDTO, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Adăugăm coloanele la tabel
        employeeTableView.getColumns().addAll(usernameColumn, roleColumn);

        // Setează datele pentru tabelul de angajați
        employeeTableView.setItems(userObservableList);

        // Adăugăm tabelul pe GridPane
        gridPane.add(employeeTableView, 0, 0, 5, 1);
    }

    // Inițializare tabel cu vânzări
    private void initSaleTableView(GridPane gridPane) {
        saleTableView = new TableView<BookDTO>();

        // Definirea coloanelor pentru tabelul de vânzări
        TableColumn<BookDTO, String> saleTitleColumn = new TableColumn<>("Title");
        saleTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<BookDTO, String> saleAuthorColumn = new TableColumn<>("Author");
        saleAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<BookDTO, Integer> salePriceColumn = new TableColumn<>("Price");
        salePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Adăugăm coloanele la tabelul de vânzări
        saleTableView.getColumns().addAll(saleTitleColumn, saleAuthorColumn, salePriceColumn);

        // Setează datele pentru tabelul de vânzări
        saleTableView.setItems(booksObservableListSale);

        // Adăugăm tabelul pe GridPane
        gridPane.add(saleTableView, 5, 0, 5, 1);
    }

    // Inițializare câmpuri pentru adăugarea unui utilizator
    private void initUserAddOptions(GridPane gridPane) {
        // Crearea etichetelor și câmpurilor de text pentru numele de utilizator și parolă
        usernameLabel = new Label("Username");
        gridPane.add(usernameLabel, 1, 2);

        usernameTextField = new TextField();
        gridPane.add(usernameTextField, 2, 2);

        passwordLabel = new Label("Password");
        gridPane.add(passwordLabel, 3, 2);

        passwordTextField = new PasswordField();
        gridPane.add(passwordTextField, 4, 2);

        // Crearea butonului pentru adăugarea unui utilizator
        addUserButton = new Button("Add User");
        gridPane.add(addUserButton, 5, 2);

        deleteUserButton = new Button("Delete User");
        gridPane.add(deleteUserButton, 6,2);

        generateReport = new Button("Generate Report");
        gridPane.add(generateReport, 7 ,2);
        Label roleLabel = new Label("Role");
        gridPane.add(roleLabel, 1, 3);

        Label monthLabel = new Label("Select Month");
        gridPane.add(monthLabel, 1, 4);

        monthComboBox = new ComboBox<>();
        monthComboBox.getItems().addAll(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        );
        monthComboBox.setValue("January"); // Set a default value
        gridPane.add(monthComboBox, 2, 4);


        roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Employee", "Customer");
        roleComboBox.setValue("Employee"); // Setăm o valoare implicită
        gridPane.add(roleComboBox, 2, 3);
    }

    // Setează listener pentru butonul de adăugare utilizator
    public void addUserButtonListener(EventHandler<ActionEvent> addUserButtonListener) {
        addUserButton.setOnAction(addUserButtonListener);
    }
    public void deleteUserButtonListener(EventHandler<ActionEvent> addUserButtonListener) {
        deleteUserButton.setOnAction(addUserButtonListener);
    }
    public void generateButtonListener(EventHandler<ActionEvent> generateButtonListener){
        generateReport.setOnAction(generateButtonListener);
    }


    // Afișează un mesaj de alertă
    public void addDisplayAlertMessage(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Accesori pentru câmpuri de text
    public String getUsername() {
        return usernameTextField.getText();
    }

    public String getPassword() {
        return passwordTextField.getText();
    }

    // Metoda pentru adăugarea unui utilizator în tabel
    public void addUserToObservableList(UserDTO userDTO) {
        this.userObservableList.add(userDTO);
    }

    public TableView<UserDTO> getEmployeeTableView() {
        return employeeTableView;
    }

    public TableView<BookDTO> getSaleTableView() {
        return saleTableView;
    }
    public String getSelectedRole() {
        return (String) roleComboBox.getValue();
    }
    public ComboBox<String> getMonthComboBox() {
        return monthComboBox;
    }
    public void removeUserFromObservableList(UserDTO userDTO){
        this.userObservableList.remove(userDTO);
    }
}
