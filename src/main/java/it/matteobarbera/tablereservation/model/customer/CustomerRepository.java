package it.matteobarbera.tablereservation.model.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByPhoneNumber(String phoneNumber);

    @Query("SELECT c FROM Customer c WHERE c.phoneNumber LIKE CONCAT(:partialNumber, '%')")
    Set<Customer> findByPartialPhoneNumber(String partialNumber);
}
