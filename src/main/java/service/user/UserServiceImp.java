package service.user;

import model.User;
import model.validator.Notification;
import repository.user.UserRepository;

public class UserServiceImp implements UserService{
    private final UserRepository userRepository;
    public UserServiceImp(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public boolean save(User user) {
        return false;
    }

    @Override
    public Notification<Boolean> deleteUserByUsername(String username) {
        return userRepository.deleteUserByUsername(username);
    }
}
