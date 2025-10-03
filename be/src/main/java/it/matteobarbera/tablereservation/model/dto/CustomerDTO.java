package it.matteobarbera.tablereservation.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CustomerDTO {
    @NotBlank(message = "Please provide a valid name (must not be blank).")
    private String name;

    @NotBlank(message = "Please provide a valid phone number (must not be blank).")
    @Size(min = 9, max = 15, message = "The phone number length must be between 9 and 15.")
    private String phoneNumber;

    @Email(message = "Please provide a valid email address.")
    private String email;

    public CustomerDTO(String name, String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        String toString = "{"
                + "        \"name\":\"" + name + "\""
                + ",         \"phoneNumber\":\"" + phoneNumber + "\""
                + ",         \"email\":\"" + email + "\""
                + "}";
        return toString.replaceAll("[\n\r]", "   ");
    }
}
