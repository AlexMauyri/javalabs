package ru.ssau.tk.DoubleA.javalabs.security.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") int id) {
        return userService.getUserById(id);
    }

    @GetMapping("/whoami")
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUserByUsername(username);
    }

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable("id") int id) {
        return userService.deleteUserById(id);
    }

    @PutMapping("/{id}")
    public User updateUserRole(@PathVariable("id") int id) {
        return userService.updateUserRole(id);
    }
}
