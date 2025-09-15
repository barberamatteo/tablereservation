package it.matteobarbera.tablereservation.service.customer;

import it.matteobarbera.tablereservation.model.customer.Customer;
import it.matteobarbera.tablereservation.model.dto.CustomerDTO;
import it.matteobarbera.tablereservation.repository.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Set<Customer> getCustomers() {
        return Set.copyOf(customerRepository.findAll());
    }

    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }

    public Customer getCustomerByPhoneNumber(String phoneNumber) {
        return customerRepository.findByPhoneNumber(phoneNumber);
    }

    public Set<Customer> getCustomersByPartialPhoneNumber(String partialNumber) {
        return customerRepository.findByPartialPhoneNumber(partialNumber);
    }

    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public void createCustomer(CustomerDTO customerDTO) {
        customerRepository.save(
                new Customer(
                        customerDTO.getPhoneNumber(),
                        customerDTO.getName(),
                        customerDTO.getEmail()
                )
        );
    }
}
