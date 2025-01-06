package it.matteobarbera.tablereservation.model.customer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CustomerConfig {
    @Bean
    CommandLineRunner initCustomerTable(CustomerRepository customerRepository) {
        return args ->
                customerRepository.saveAll(
                        List.of(
                                new Customer(
                                        "3921318124",
                                        "Matteo Mb",
                                        "matteobarbera@hotmail.it"
                                )
                        )
                );
    }
}
