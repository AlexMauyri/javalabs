package ru.ssau.tk.DoubleA.javalabs.persistence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ssau.tk.DoubleA.javalabs.persistence.dao.UserRepo;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.User;
import ru.ssau.tk.DoubleA.javalabs.security.UserDetailsImpl;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);

        if (user != null) {
            return new UserDetailsImpl(user);
        }

        throw new UsernameNotFoundException(username);
    }
}
