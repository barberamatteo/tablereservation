package it.matteobarbera.tablereservation.model.customer;

import it.matteobarbera.tablereservation.http.response.CommonJSONBodies;
import it.matteobarbera.tablereservation.model.dto.CustomerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static it.matteobarbera.tablereservation.Constants.CUSTOMER_CRUD_API_ENDPOINT;
import static it.matteobarbera.tablereservation.http.CustomerAPIInfo.CUSTOMER_CREATED;
import static it.matteobarbera.tablereservation.http.CustomersAPIError.*;
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

    @PostMapping("/newcustomer")
    public ResponseEntity<?> createCustomer(
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("name") String name,
            @RequestParam(value = "email", required = false) String email
    ){

        if (email != null && !email.isEmpty()) {
            if (customerService.getCustomerByEmail(email) != null){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(
                                CommonJSONBodies.fromStatusAndMsg(
                                        HttpStatus.BAD_REQUEST.value(),
                                        CUSTOMER_WITH_EMAIL_ALREADY_EXISTS.getMessage(email)
                                )
                        );

            }
        }

        if (customerService.getCustomerByPhoneNumber(phoneNumber) != null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                            CommonJSONBodies.fromStatusAndMsg(
                                    HttpStatus.BAD_REQUEST.value(),
                                    CUSTOMER_WITH_PHONE_NUMBER_ALREADY_EXISTS.getMessage(phoneNumber)
                            )
                    );
        }

        CustomerDTO customerDTO = new CustomerDTO(name, phoneNumber, email);
        customerService.createCustomer(customerDTO);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        CommonJSONBodies.fromStatusAndMsg(
                                HttpStatus.OK.value(),
                                CUSTOMER_CREATED.getMessage(phoneNumber)
                        )
                );
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

    @CrossOrigin
    @GetMapping("/getbyphonenumberstartingwith/")
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
