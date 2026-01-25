package it.matteobarbera.tablereservation.model.reservation;


import com.fasterxml.jackson.annotation.JsonIgnore;
import it.matteobarbera.tablereservation.model.customer.Customer;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
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
    private Set<AbstractTable> jointTables;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startDateTime", column = @Column(name = "start_date_time")),
            @AttributeOverride(name = "endDateTime", column = @Column(name = "end_date_time"))
    })
    private Interval interval;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private Integer numberOfPeople;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "reservation_schedules",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = {
                    @JoinColumn(name = "table_id"),
                    @JoinColumn(name = "date"),
                    @JoinColumn(name = "layout_id")
            }
    )
    @JsonIgnore
    private Set<Schedule> schedules;

    public Reservation(
            Set<AbstractTable> jointTables,
            Interval interval,
            Customer customer,
            Integer numberOfPeople,
            Set<Schedule> schedules
    ) {
        this.jointTables = jointTables;
        this.interval = interval;
        this.customer = customer;
        this.numberOfPeople = numberOfPeople;
        this.schedules = schedules;
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

    public Reservation(
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            Integer numberOfPeople
    ) {
        this(startDateTime, endDateTime, null, numberOfPeople);
    }

    public Reservation(Reservation reservation, Set<Schedule> schedules) {
        this(
                reservation.jointTables,
                reservation.interval,
                reservation.customer,
                reservation.numberOfPeople,
                schedules
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

    public Set<AbstractTable> getJointTables() {
        return jointTables;
    }


    public void setJointTables(Set<AbstractTable> jointTables) {
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

    public Set<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(Set<Schedule> schedule) {
        this.schedules = schedule;
    }

    public void addSchedule(Schedule schedule){
        if (this.schedules == null) {
            this.schedules = new HashSet<>();
            this.schedules.add(schedule);
        } else {
            this.schedules.add(schedule);
        }
    }
    public LocalDate getStartDate(){
        return this.interval.getStartDate();
    }

    public LocalDate getEndDate(){
        return this.interval.getEndDate();
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Reservation that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
