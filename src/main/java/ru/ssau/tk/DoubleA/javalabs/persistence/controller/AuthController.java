package ru.ssau.tk.DoubleA.javalabs.persistence.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.DoubleA.javalabs.persistence.service.UserService;
import ru.ssau.tk.DoubleA.javalabs.persistence.service.jwt.JwtService;

import java.util.Map;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        if (userService.existsByUsername(username)) {
            return new ResponseEntity<>("Пользователь с таким именем уже существует", HttpStatus.CONFLICT);
        }

        userService.addUser(username, password);
        Long userId = (long) userService.getUserIdByUsername(username);
        String token = jwtService.generateToken(userId, username);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(auth);

            Long userId = (long) userService.getUserIdByUsername(username);

            String token = jwtService.generateToken(userId, username);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>("Неправильное имя пользователя или пароль", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>("Ошибка при входе", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return ResponseEntity.ok("Выход успешен");
    }
}