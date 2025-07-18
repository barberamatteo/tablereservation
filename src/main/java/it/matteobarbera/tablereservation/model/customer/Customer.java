package it.matteobarbera.tablereservation.model.customer;


import jakarta.annotation.Nullable;
import jakarta.persistence.*;

@Entity
@Table
public class Customer {
    @Id
    @SequenceGenerator(
            name = "customer_sequence",
            sequenceName = "customer_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_sequence"
    )
    private Long id;
    @Nullable
    private String email;
    private String name;
    private String phoneNumber;

    public Customer(String phoneNumber, String name, @Nullable String email) {
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.email = email;
    }

    protected Customer() {
    }

    public Long getId() {
        return id;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    public void setEmail(@Nullable String email) {
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
}
