package it.matteobarbera.tablereservation.model.customer;

import it.matteobarbera.tablereservation.http.response.CommonJSONBodies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static it.matteobarbera.tablereservation.Constants.CUSTOMER_CRUD_API_ENDPOINT;
import static it.matteobarbera.tablereservation.http.CustomersAPIError.TOO_FEW_DIGITS;
import static it.matteobarbera.tablereservation.log.CustomerLog.*;

@RestController
@RequestMapping(path = CUSTOMER_CRUD_API_ENDPOINT)
public class CustomerController {
    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/getbyphonenumber/")
    public Customer getCustomerByPhoneNumber(
            @RequestParam("phoneNumber") String customerPhoneNumber)
    {

        Customer customer = customerService.getCustomerByPhoneNumber(customerPhoneNumber);
        if (customer == null) {
            log.atInfo().log(NO_CUSTOMER_FOUND);
        } else {
            log.atInfo().log(CUSTOMER_FOUND, customer.getId());
        }
        return customer;
    }

    @GetMapping("/getall/")
    public Set<Customer> getCustomers() {
        Set<Customer> allCustomers = customerService.getCustomers();
        if (allCustomers.isEmpty()) {
            log.atInfo().log(NO_CUSTOMER_FOUND);
        } else {
            log.atInfo().log(ALL_CUSTOMERS_FOUND, allCustomers.size());
        }
        return customerService.getCustomers();
    }

    @GetMapping("/getbyphonenumberstartingwith/")
    @CrossOrigin
    public ResponseEntity<?> getCustomersByPhoneNumberStartingWith(@RequestParam("regex") String partialNumber){
        Set<Customer> customersFound = customerService.getCustomersByPartialPhoneNumber(partialNumber);
        log.atInfo().log(FOUND_CUSTOMERS_MATCHING, customersFound.size(), partialNumber);
        return (
                partialNumber.length() < 3
                ? ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(
                                CommonJSONBodies.fromStatusAndMsg(
                                        HttpStatus.BAD_REQUEST.value(),
                                        TOO_FEW_DIGITS.getMessage()
                                )
                        )
                : ResponseEntity.ok(customersFound)
        );
    }
}
