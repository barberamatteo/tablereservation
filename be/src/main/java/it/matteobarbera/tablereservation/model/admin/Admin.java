package it.matteobarbera.tablereservation.model.admin;

import jakarta.persistence.*;

@Entity
@Table

public class Admin {

    @Id
    @SequenceGenerator(
            name = "admin_sequence",
            sequenceName = "admin_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "admin_sequence"
    )
    private Long id;
    private String name;
    private String password;

    public Admin(String name, String password) {
        this.name = name;
        this.password = password;
    }

    protected Admin() {}

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
