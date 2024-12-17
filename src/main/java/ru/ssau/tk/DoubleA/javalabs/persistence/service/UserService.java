package ru.ssau.tk.DoubleA.javalabs.persistence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ssau.tk.DoubleA.javalabs.persistence.dao.UserRepo;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.User;
import ru.ssau.tk.DoubleA.javalabs.security.Role;
import ru.ssau.tk.DoubleA.javalabs.security.UserDetailsImpl;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);

        if (user != null) {
            return new UserDetailsImpl(user);
        }

        throw new UsernameNotFoundException(username);
    }

    public boolean existsByUsername(String username) {
        User user = userRepo.findByUsername(username);
        return user != null;
    }

    public void addUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setRole(Role.USER);
        userRepo.save(user);
    }

    public int getUserIdByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);

        if (user != null) {
            return user.getUser_id();
        }

        throw new UsernameNotFoundException(username);
    }
}
