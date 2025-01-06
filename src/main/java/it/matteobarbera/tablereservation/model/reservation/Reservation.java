package it.matteobarbera.tablereservation.model.reservation;


import it.matteobarbera.tablereservation.model.customer.Customer;
import it.matteobarbera.tablereservation.model.table.CustomTable;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    private Long id;

    @OneToMany
    private List<CustomTable> jointTables;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "start_date_time"),
            @JoinColumn(name = "end_date_time")
    })
    private Interval interval;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private Integer numberOfPeople;

    public Reservation() {}

    public Reservation(
            List<CustomTable> jointTables,
            Interval interval,
            Customer customer,
            Integer numberOfPeople
    ) {
        this.jointTables = jointTables;
        this.interval = interval;
        this.customer = customer;
        this.numberOfPeople = numberOfPeople;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CustomTable> getJointTables() {
        return jointTables;
    }

    public void setJointTables(List<CustomTable> jointTables) {
        this.jointTables = jointTables;
    }

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }
}
