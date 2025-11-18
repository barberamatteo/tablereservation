package it.matteobarbera.tablereservation.service.customer;

import it.matteobarbera.tablereservation.http.CustomerAPIError;
import it.matteobarbera.tablereservation.http.CustomerAPIInfo;
import it.matteobarbera.tablereservation.http.CustomerAPIResult;
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

    public CustomerAPIResult getCustomerById(Long customerId) {
        var result = customerRepository.findById(customerId);
        if (result.isPresent()) {
            return new CustomerAPIResult.Success(result.get(), CustomerAPIInfo.CUSTOMER_FETCHED_OK);
        } else {
            return new CustomerAPIResult.Failure(CustomerAPIError.NO_SUCH_CUSTOMER_WITH_ID);
        }
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

    public void createCustomer(Customer customer) {
        customerRepository.save(customer);
    }
}
