package it.matteobarbera.tablereservation.model.customer;

import it.matteobarbera.tablereservation.http.response.CommonJSONBodies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static it.matteobarbera.tablereservation.http.CustomersAPIError.TOO_FEW_DIGITS;

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
    public ResponseEntity<?> getCustomersByPhoneNumberStartingWith(@RequestParam("regex") String partialNumber){
        System.out.println("found");
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
                : ResponseEntity.ok(customerService.getCustomerByPartialPhoneNumber(partialNumber))
        );
    }
}
