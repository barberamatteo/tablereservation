package it.matteobarbera.tablereservation.model.reservation;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, ScheduleIdRecord> {



    @Transactional
    @Query(
            "SELECT s " +
            "FROM Schedule s " +
            "WHERE s.id.parsedDate = :parsedDate AND s.id.table.tableDefinition.standaloneCapacity >= :numberOfPeople" +
            " ORDER BY s.id.table.tableDefinition.standaloneCapacity ASC"
    )
    Set<Schedule> getSchedulesByParsedDateAndAdequateTable(String parsedDate, Integer numberOfPeople);




    @Query(
            "SELECT s " +
            "FROM Schedule s WHERE s.id.parsedDate = :arrivalDate"
    )
    Set<Schedule> getSchedulesByParsedDate(String arrivalDate);
}
