package controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;


import javafx.stage.Stage;
import mapper.BookMapper;
import mapper.UserMapper;
import model.Role;
import model.User;
import model.validator.Notification;
import service.book.BookService;
import service.sale.SaleService;
import service.user.AuthenticationService;
import service.user.UserService;
import view.AdminView;
import view.EmployeeView;
import view.LoginView;
import view.model.BookDTO;
import view.model.UserDTO;

import java.util.List;

public class LoginController {

    private final LoginView loginView;
    private final AuthenticationService authenticationService;
    private final BookService bookService;
    private final SaleService saleService;
    private final UserService userService;

    public LoginController(LoginView loginView, AuthenticationService authenticationService,
                           BookService bookService, SaleService saleService, UserService userService) {
        this.loginView = loginView;
        this.authenticationService = authenticationService;
        this.bookService = bookService;
        this.saleService = saleService;
        this.userService = userService;

        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());
    }

    private class LoginButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<User> loginNotification = authenticationService.login(username, password);

            if (loginNotification.hasErrors()){
                loginView.setActionTargetText(loginNotification.getFormattedErrors());
            }else {
                User userLogged = loginNotification.getResult();
                List<Role> userRoles = userLogged.getRoles();
                loginView.setActionTargetText("LogIn Successfull!");



                if (hasRole(userRoles, "administrator")) {
                    openAdminView(userLogged);
                } else if (hasRole(userRoles, "employee")) {
                    openEmployeeView(userLogged);
                } else if(hasRole(userRoles, "customer")){
                    loginView.setActionTargetText("You are a customer. Need an employee to order a book!");
                }

                else {
                    loginView.setActionTargetText("No valid role found!");
                }

                }
            }
        }
    private boolean hasRole(List<Role> roles, String roleName) {
        for (Role role : roles) {
            if (role.getRole().equalsIgnoreCase(roleName)) {
                return true;
            }
        }
        return false;
    }

    private void openAdminView(User user) {
        Stage adminStage = new Stage();
        List<UserDTO> userDTOList = UserMapper.convertUserListToUserDTOList(authenticationService.findAllUsers());
        List<BookDTO> soldBookDTOS = BookMapper.convertBookListToBookDTOListSales(saleService.findSalesBook());

        AdminView adminView = new AdminView(adminStage, userDTOList,soldBookDTOS,user);
        new AdminController(adminView, authenticationService, userService);

        adminStage.show();
    }

    private void openEmployeeView(User user) {
        Stage employeeStage = new Stage();
        List<BookDTO> bookDTOS = BookMapper.convertBookListToBookDTOList(bookService.findAll());
        List<BookDTO> soldBookDTOS = BookMapper.convertBookListToBookDTOListSales(saleService.findSalesBook());
        EmployeeView employeeView = new EmployeeView(employeeStage, bookDTOS, soldBookDTOS,user);

        System.out.println("numele user ului:" +  user.getUsername());
        new EmployeeController(employeeView, bookService, saleService);
        employeeStage.show();
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();
            String role = "customer";
            Notification<Boolean> registerNotification = authenticationService.register(username, password,role);

            if (registerNotification.hasErrors()) {
                loginView.setActionTargetText(registerNotification.getFormattedErrors());
            } else {
                loginView.setActionTargetText("Register successful!");
            }
        }
    }
}