package it.matteobarbera.tablereservation.repository.admin;

import it.matteobarbera.tablereservation.model.admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

}
