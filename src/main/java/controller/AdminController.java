package controller;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import database.DatabaseConnectionFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.validator.Notification;
import service.user.AuthenticationService;
import service.user.UserService;
import view.AdminView;
import view.model.UserDTO;
import view.model.builder.UserDTOBuilder;

import com.itextpdf.text.Document;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
public class AdminController {

    private final AdminView adminView;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    public AdminController(AdminView adminView, AuthenticationService authenticationService,UserService userService) {
        this.adminView = adminView;
        this.authenticationService = authenticationService;
        this.userService = userService;


        this.adminView.addUserButtonListener(new AddUserButtonListener());
        this.adminView.deleteUserButtonListener(new DeleteUserButtonListener());
        this.adminView.generateButtonListener(new GenerateReportListener());
    }
//parolaAdmin!2
    private class AddUserButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            String username = adminView.getUsername();
            String password = adminView.getPassword();
            String role = adminView.getSelectedRole();

            if (username.isEmpty() || password.isEmpty()) {
                adminView.addDisplayAlertMessage("Save Error", "Problem at username or password fields", "Can not have an empty username or password field." );
                return;
            }

            Notification<Boolean> registerNotification = authenticationService.register(username, password,role);

            if (registerNotification.hasErrors()) {
                adminView.addDisplayAlertMessage("Save Error", "Problem at adding User", "There was a problem at adding the user tot the database");
            } else {
                UserDTO userDTO = new UserDTOBuilder()
                        .setUsername(username)
                        .setRole(role)
                                .build();
                System.out.println("Adding user: " + username + " with role: " + role);
                adminView.addUserToObservableList(userDTO);
                adminView.addDisplayAlertMessage("User saved succesfully", "user added", " User was successfully added tot the database");
            }
        }
    }
    private class DeleteUserButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            UserDTO selectedUserDTO = (UserDTO) adminView.getEmployeeTableView().getSelectionModel().getSelectedItem();

            if (selectedUserDTO != null) {
                // Dacă utilizatorul a fost selectat, apelăm serviciul de ștergere
                Notification<Boolean> deleteUserNotification = userService.deleteUserByUsername(selectedUserDTO.getUsername());

                if (deleteUserNotification.getResult()) {
                    // Dacă ștergerea a avut succes, afișăm mesajul de succes
                    adminView.addDisplayAlertMessage("Delete Successful", "User Deleted", "User was successfully deleted from the database.");
                    adminView.removeUserFromObservableList(selectedUserDTO);  // În cazul în care vrei să actualizezi lista
                } else {
                    // Dacă a apărut o eroare, afișăm mesajul de eroare
                    adminView.addDisplayAlertMessage("Delete Error", "Problem at deleting user", "There was a problem with the database. Please try again!");
                }
            } else {
                // Dacă nu a fost selectat niciun utilizator, afișăm un mesaj de eroare
                adminView.addDisplayAlertMessage("Delete Error", "Problem at deleting user", "You must select a user before pressing the delete button.");
            }
        }
        }
    public class GenerateReportListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String selectedMonth = adminView.getMonthComboBox().getValue(); // Access the ComboBox value here
            int monthNumber = getMonthNumber(selectedMonth);
            // Folosim conexiunea din DatabaseConnectionFactory
            try (Connection conn = DatabaseConnectionFactory.getConnectionWrapper(false).getConnection()) {
                String query = "SELECT s.user_id, s.title, s.author, s.price, s.stock, s.timeStamp " +
                        "FROM sales s " +
                        "WHERE MONTH(s.timeStamp) = " + monthNumber + " " +
                        "ORDER BY s.user_id";


                try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                    // Crearea documentului PDF
                    Document document = new Document();
                    PdfWriter.getInstance(document, new FileOutputStream("SalesReport.pdf"));
                    document.open();

                    // Adăugarea unui titlu la raport
                    document.add(new Paragraph("Sales Report"));

                    // Crearea unui tabel cu 5 coloane
                    PdfPTable table = new PdfPTable(6);  // 6 columns: User ID, Title, Author, Price, Stock, Timestamp
                    table.addCell("User ID");
                    table.addCell("Title");
                    table.addCell("Author");
                    table.addCell("Price");
                    table.addCell("Stock");
                    table.addCell("Timestamp");

// Loop through the result set and add data to the PDF table
                    while (rs.next()) {
                        table.addCell(String.valueOf(rs.getInt("user_id")));
                        table.addCell(rs.getString("title"));
                        table.addCell(rs.getString("author"));
                        table.addCell(String.valueOf(rs.getInt("price")));
                        table.addCell(String.valueOf(rs.getInt("stock")));
                        table.addCell(rs.getTimestamp("timeStamp").toString());
                    }

// Add the table to the document
                    document.add(table);
                    document.close();

                    // Mesaj de succes
                    System.out.println("Sales report generated successfully.");
                } catch (SQLException | DocumentException | java.io.IOException e) {
                    e.printStackTrace();
                    System.out.println("Error generating report.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error connecting to the database.");
            }
        }
    }
    private int getMonthNumber(String monthName) {
        switch (monthName) {
            case "January": return 1;
            case "February": return 2;
            case "March": return 3;
            case "April": return 4;
            case "May": return 5;
            case "June": return 6;
            case "July": return 7;
            case "August": return 8;
            case "September": return 9;
            case "October": return 10;
            case "November": return 11;
            case "December": return 12;
            default: return 1; // Default to January if invalid
        }
    }
}
