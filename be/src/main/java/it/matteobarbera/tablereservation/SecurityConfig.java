package it.matteobarbera.tablereservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {

    private static final InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();


    public String createUserWithRoleAndNameAndPassword(String role, String name, String password) {
        String encodedPassword = encoder.encode(password);
        manager.createUser(
                User.withUsername(name).password(encodedPassword).roles(role).build()
        );
        
        return encodedPassword;
    }
}
