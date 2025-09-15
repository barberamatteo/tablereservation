package it.matteobarbera.tablereservation;

import it.matteobarbera.tablereservation.model.admin.Admin;
import it.matteobarbera.tablereservation.service.admin.AdminService;
import it.matteobarbera.tablereservation.service.security.SecurityService;
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
public class CLI{


    private static final LoggingSystem loggingSystem = LoggingSystem.get(CLI.class.getClassLoader());
    private static final Logger log = LoggerFactory.getLogger(CLI.class);
    private static final Scanner scanner = new Scanner(System.in);
    private final SecurityService securityService;
    private final AdminService adminService;
    private boolean active = false;
    public CLI(SecurityService securityService, AdminService adminService) {
        this.securityService = securityService;
        this.adminService = adminService;
    }


    @Bean
    CommandLineRunner runCli() {
        return args -> {
            Thread cliThread = new Thread(() -> {
                while (true){
                    if (scanner.nextLine() != null && !active) {
                        active = true;
                        mainHandler();
                    }
                }

            });
            cliThread.setName("CLI Thread");
            cliThread.setDaemon(true);
            cliThread.start();
        };
    }


    private void mainHandler() {
        System.out.println("\n\n\n");
        log.atInfo().log("Deactivating logging ...");
        log.atWarn().log("Entering in CLI mode ...");
        log.atWarn().log("Enter 'q' to quit CLI mode.");
        toggleLogging(false);

        while (active) {
            System.out.print(" > ");
            String i = scanner.nextLine().trim();
            String[] inputs = i.split(" ");
            switch (inputs[0]) {
                case "q":
                case "exit":
                    toggleLogging(true);
                    log.atInfo().log("Quitting CLI mode ...");
                    log.atInfo().log("Reactivating logging ...");
                    active = false;
                    return;
                case "createadmin":
                    createAdmin(Arrays.copyOfRange(inputs, 1, inputs.length));
                    continue;
                case "":
                    continue;
                default:
                    System.out.println("Command " + inputs[0] + " not recognized.");
            }
        }
    }
    private void createAdmin(String[] args) {
        try{
            var name = args[0];
            var password = args[1];

            String encryptedPassword = securityService.createAdmin(name, password);
            if (encryptedPassword == null) {
                System.out.println("Admin creation failed due to an already existing user with that name");
            } else {
                System.out.println("Admin created with username " + name +
                        " and encrypted password " + encryptedPassword
                );
                adminService.addAdmin(new Admin(name, encryptedPassword));
            }
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Error: Incorrect number of arguments.\n" +
                                "Usage: createadmin <username> <password>"
            );
        }


    }

    private void toggleLogging(boolean enabled) {
        loggingSystem.setLogLevel(
                "ROOT",
                (enabled ? LogLevel.INFO : LogLevel.OFF)
        );
    }

}
