package it.matteobarbera.tablereservation.model.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(path = "api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/getbyphonenumber/")
    public Customer getCustomer(@RequestParam("phoneNumber") String customerPhoneNumber){
        return customerService.getCustomerByPhoneNumber(customerPhoneNumber);
    }

    @GetMapping
    public Set<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping("/getbyphonenumberstartingwith/")
    @CrossOrigin
    public Set<Customer> getCustomersByPhoneNumberStartingWith(@RequestParam("regex") String partialNumber){
        return customerService.getCustomerByPartialPhoneNumber(partialNumber);
    }
}
