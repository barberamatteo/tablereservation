package it.matteobarbera.tablereservation.security;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static it.matteobarbera.tablereservation.Constants.AUTH_UTILS_ENDPOINT;

@RestController
@RequestMapping(path = AUTH_UTILS_ENDPOINT)
public class SecurityController {

    @GetMapping("/check")
    public ResponseEntity<String> isAuthenticated() {
        return (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        );
    }
}
