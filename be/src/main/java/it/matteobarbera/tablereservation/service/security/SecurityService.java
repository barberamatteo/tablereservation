package it.matteobarbera.tablereservation.service.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import static it.matteobarbera.tablereservation.Constants.ROLE_ADMIN;

@Service
@DependsOn("userDetailsService")
public class SecurityService {


    private static final Logger log = LoggerFactory.getLogger(SecurityService.class);
    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityService(UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    public String createAdmin(String username, String password) {
        String encodedPassword = encodePassword(password);
        UserDetails userDetails = User
                .withUsername(username)
                .password(encodedPassword)
                .roles(ROLE_ADMIN)
                .build();
        try{
            userDetailsManager.createUser(userDetails);
            return encodedPassword;
        } catch (IllegalArgumentException e){
            log.atError().log("Admin with username {} already exists!", username);
            return null;
        }
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
