package mapper;

import model.Book;
import model.User;
import model.builder.BookBuilder;
import model.builder.UserBuilder;
import view.model.BookDTO;
import view.model.UserDTO;
import view.model.builder.BookDTOBuilder;
import view.model.builder.UserDTOBuilder;

import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDTO convertUserToUserDTO(User user){
        return new UserDTOBuilder()
                .setUsername(user.getUsername())
                .build();
    }
    public static User convertUserDTOTOUser(UserDTO userDTO){
        return new UserBuilder()
                .setUsername(userDTO.getUsername())
                .build();
    }
    public static List<UserDTO> convertUserListToUserDTOList(List<User> users){
        return users.parallelStream().map(UserMapper::convertUserToUserDTO).collect(Collectors.toList());
    }

}