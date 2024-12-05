package service.user;

import model.User;
import model.validator.Notification;

public interface UserService {
    boolean save(User user);
    Notification<Boolean> deleteUserByUsername(String username);
}
