package it.matteobarbera.tablereservation.controller.customer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.matteobarbera.tablereservation.http.response.CommonJSONBodies;
import it.matteobarbera.tablereservation.model.customer.Customer;
import it.matteobarbera.tablereservation.service.customer.CustomerService;
import it.matteobarbera.tablereservation.model.dto.CustomerDTO;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
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
import static it.matteobarbera.tablereservation.logging.CustomerLog.*;

@RestController
@RequestMapping(path = CUSTOMER_CRUD_API_ENDPOINT)
public class CustomerController {
    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerService customerService;

    private final ModelMapper modelMapper;
    @Autowired
    public CustomerController(CustomerService customerService, ModelMapper modelMapper) {
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }


    @Operation(
            summary = "Creates a new customer",
            description = "Creates a new customer with the essential data (phone number, name, email)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Customer with phoneNumber <phoneNumber> created"),
            @ApiResponse(
                    responseCode = "400",
                    description = "The specified email <email> is already associated with an existing customer." +
                                    " Please use another email"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "The specified phone number <phoneNumber> is already associated with an existing " +
                                    "customer. Please use another phone number")
    })
    @PostMapping("/newcustomer")
    public ResponseEntity<?> createCustomer(@Valid @RequestBody CustomerDTO customerDTO){
        if (customerDTO.getEmail() != null && !customerDTO.getEmail().isEmpty()) {
            if (customerService.getCustomerByEmail(customerDTO.getEmail()) != null){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(
                                CommonJSONBodies.fromStatusAndMsg(
                                        HttpStatus.BAD_REQUEST.value(),
                                        CUSTOMER_WITH_EMAIL_ALREADY_EXISTS.getMessage(customerDTO.getEmail())
                                )
                        );

            }
        }

        if (customerService.getCustomerByPhoneNumber(customerDTO.getPhoneNumber()) != null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                            CommonJSONBodies.fromStatusAndMsg(
                                    HttpStatus.BAD_REQUEST.value(),
                                    CUSTOMER_WITH_PHONE_NUMBER_ALREADY_EXISTS.getMessage(customerDTO.getPhoneNumber())
                            )
                    );
        }

        Customer customer = modelMapper.map(customerDTO, Customer.class);
        customerService.createCustomer(customer);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        CommonJSONBodies.fromStatusAndMsg(
                                HttpStatus.OK.value(),
                                CUSTOMER_CREATED.getMessage(customerDTO.getPhoneNumber())
                        )
                );
    }



    @Operation(
            summary = "Get customer objects",
            description = "Returns a customer object by providing the phone number"
    )
    @ApiResponse(responseCode = "200")
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

    @Operation(
            summary = "Get all customers",
            description = "Returns all customer objects from the DB (debug endpoint)."
    )
    @ApiResponse(responseCode = "200")
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

    @Operation(
            summary = "Gets customer by phone number",
            description = "Returns all costumer objects having <partialNumber> as a prefix of their phone number." +
                            " Used by frontend to achieve autocompletion."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "The input <regex> must be at least 3 digits long")
    })
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
