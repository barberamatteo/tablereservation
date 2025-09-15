package it.matteobarbera.tablereservation.controller.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Authentication check",
            description = "Used by frontend to decide whether to load the login page or the main lounge page"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "401")
    })
    @GetMapping("/check")
    public ResponseEntity<String> isAuthenticated() {
        return (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        );
    }
}
