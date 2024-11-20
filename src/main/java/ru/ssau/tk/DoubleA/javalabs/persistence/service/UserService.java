package ru.ssau.tk.DoubleA.javalabs.persistence.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ssau.tk.DoubleA.javalabs.persistence.dao.UserDAO;
import ru.ssau.tk.DoubleA.javalabs.security.user.Role;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.User;
import ru.ssau.tk.DoubleA.javalabs.persistence.dto.UserDTO;
import ru.ssau.tk.DoubleA.javalabs.security.user.UserDetailsImpl;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private UserDAO userDAO;
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }

        return new UserDetailsImpl(user);
    }

    public List<User> getAllUsers() {
        return userDAO.findAllUsers();
    }

    public boolean existsByUsername(String username) {
        return userDAO.existsByUsername(username);
    }


    public User getUserById(int id) {
        return userDAO.getUserById(id);
    }

    public User getUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }


    public void registerUser(UserDTO userDTO) {
        userDAO.registerUser(
                convertToUser(userDTO)
        );
    }

    private User convertToUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(Role.USER);
        return user;
    }

    public User deleteUserById(int id) {
        return userDAO.deleteUserById(id);
    }

    public User updateUserRole(int id) {
        return userDAO.updateUserRole(id);
    }
}
