package launcher;

import controller.LoginController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;
import repository.sale.SaleRepository;
import repository.sale.SaleRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.sale.SaleService;
import service.sale.SaleServiceImpl;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImpl;
import service.user.UserService;
import service.user.UserServiceImp;
import view.LoginView;

import java.sql.Connection;

public class LoginComponentFactory {
    private final LoginView loginView;
    private final LoginController loginController;
    private AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private final SaleRepository saleRepository;
    private final SaleService saleService;
    private final UserService userService;
    private static LoginComponentFactory instance;
    public synchronized static LoginComponentFactory getInstance(Boolean componentsForTests, Stage stage){
        if (instance == null){
            instance = new LoginComponentFactory(componentsForTests, stage);
        }
        return instance;
    }
    public LoginComponentFactory(Boolean componentsForTests, Stage stage){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTests).getConnection();
        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.authenticationService = new AuthenticationServiceImpl(userRepository, rightsRolesRepository);
        this.loginView = new LoginView(stage);
        //ca sa folosim interfata bookService
        this.bookRepository = new BookRepositoryMySQL(connection);
        this.bookService = new BookServiceImpl(bookRepository);
        this.saleRepository = new SaleRepositoryMySQL(connection);
        this.saleService = new SaleServiceImpl(saleRepository);
        this.userService = new UserServiceImp(userRepository);
        this.loginController = new LoginController(loginView, authenticationService,bookService,saleService,userService);
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public LoginView getLoginView() {
        return loginView;
    }

    public RightsRolesRepository getRightsRolesRepository() {
        return rightsRolesRepository;
    }

    public BookRepository getBookRepository() {
        return bookRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public BookService getBookService() {
        return bookService;
    }
}
