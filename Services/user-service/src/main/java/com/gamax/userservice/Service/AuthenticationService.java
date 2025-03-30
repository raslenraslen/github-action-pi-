package com.gamax.userservice.Service;

import com.gamax.userservice.Entity.User;
import com.gamax.userservice.Repository.UserRepository;
import com.gamax.userservice.TDO.LoginUserDto;
import com.gamax.userservice.TDO.RegisterUserDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(RegisterUserDto input) {
        if (input.getFullName() == null || input.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }

        String[] nameParts = input.getFullName().trim().split(" ", 2);
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        User user = new User();
        user.setFirstName(firstName);
        user.setUsername(("Ahmed"+firstName+lastName));
        user.setAge(15);
        user.setBirthday(new Date());
        user.setAccountCreationDate(new Date());
        user.setLastName(lastName);
        user.setEmail(input.getEmail());
        user.setPassword( //input.getPassword()    // TODO encrypt password
                passwordEncoder.encode(input.getPassword())
        );

        return userRepository.save(user);
    }


    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
