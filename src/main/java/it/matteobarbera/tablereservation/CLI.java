package it.matteobarbera.tablereservation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Scanner;

@Configuration
public class CLI {


    private static final LoggingSystem loggingSystem = LoggingSystem.get(CLI.class.getClassLoader());
    private static final Logger log = LoggerFactory.getLogger(CLI.class);
    private static final Scanner scanner = new Scanner(System.in);
    private final SecurityConfig securityConfig;

    public CLI(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }

    @Bean
    CommandLineRunner runCli() throws Exception {
        return args -> new Thread(() -> {
            if (scanner.hasNextLine())
                cliModeGreeting();
        }).start();
    }


    private void cliModeGreeting() {
        System.out.println("\n\n\n");
        log.atInfo().log("Deactivating logging ...");
        log.atWarn().log("Entering in CLI mode ...");
        log.atWarn().log("Enter 'q' to quit CLI mode.");
        toggleLogging(false);

        System.out.print("\n > ");
        while (true) {

            String i = scanner.nextLine();
            String[] inputs = i.split(" ");
            switch (inputs[0]) {
                case "q":
                    toggleLogging(true);
                    log.atInfo().log("Quitting CLI mode ...");
                    log.atInfo().log("Reactivating logging ...");
                    return;
                case "createuser":
                    createUser(Arrays.copyOfRange(inputs, 1, inputs.length));
                    System.out.println(" > ");
                    break;
            }
        }
    }
    private void createUser(String[] args) {
        var role = args[0];
        var name = args[1];
        var password = args[2];

        String encryptedPassword = securityConfig.createUserWithRoleAndNameAndPassword(role, name, password);
        if (encryptedPassword != null)
            System.out.println("Created user '" + name + "' with password '" + encryptedPassword + "'.");
    }

    private void toggleLogging(boolean enabled) {
        loggingSystem.setLogLevel(
                "ROOT",
                (enabled ? LogLevel.INFO : LogLevel.OFF)
        );
    }
}
