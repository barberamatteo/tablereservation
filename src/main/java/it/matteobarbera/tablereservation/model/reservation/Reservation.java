package it.matteobarbera.tablereservation.model.reservation;


import it.matteobarbera.tablereservation.model.customer.Customer;
import it.matteobarbera.tablereservation.model.table.CustomTable;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class Reservation {

    @Id
    @SequenceGenerator(
            name = "reservation_sequence",
            sequenceName = "reservation_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "reservation_sequence"
    )
    private Long id;

    @ManyToMany
    private Set<CustomTable> jointTables;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startDateTime", column = @Column(name = "start_date_time")),
            @AttributeOverride(name = "endDateTime", column = @Column(name = "end_date_time"))
    })
    private Interval interval;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private Integer numberOfPeople;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "table_id", referencedColumnName = "table_id"),
            @JoinColumn(name = "date", referencedColumnName = "date")
    })
    private Schedule schedule;

    public Reservation(
            Set<CustomTable> jointTables,
            Interval interval,
            Customer customer,
            Integer numberOfPeople,
            Schedule schedule
    ) {
        this.jointTables = jointTables;
        this.interval = interval;
        this.customer = customer;
        this.numberOfPeople = numberOfPeople;
        this.schedule = schedule;
    }

    public Reservation(
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            Customer customer,
            Integer numberOfPeople
    ) {
        this.jointTables = new HashSet<>();
        this.interval = new Interval(startDateTime, endDateTime);
        this.customer = customer;
        this.numberOfPeople = numberOfPeople;
    }

    public Reservation(Reservation reservation, Schedule schedule) {
        this(
                reservation.jointTables,
                reservation.interval,
                reservation.customer,
                reservation.numberOfPeople,
                schedule
        );
    }
    public Reservation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<CustomTable> getJointTables() {
        return jointTables;
    }

    public void setJointTables(Set<CustomTable> jointTables) {
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

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", jointTables=" + jointTables +
                ", interval=" + interval +
                ", customer=" + customer +
                ", numberOfPeople=" + numberOfPeople +
                '}';
    }
}
