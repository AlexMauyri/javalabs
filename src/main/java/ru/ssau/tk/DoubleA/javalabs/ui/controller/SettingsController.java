package ru.ssau.tk.DoubleA.javalabs.ui.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.ssau.tk.DoubleA.javalabs.ui.FabricType;

@Controller
public class SettingsController {

    @GetMapping("/settings")
    public String settings() {
        return "settings";
    }

    @PostMapping("/settings")
    public void saveSettings(@RequestBody String settings, HttpServletResponse response) {
        Cookie cookie = new Cookie("fabricType", settings);
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        response.addCookie(cookie);
    }
}
