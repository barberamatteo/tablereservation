package it.matteobarbera.tablereservation.model.reservation;

import it.matteobarbera.tablereservation.model.table.CustomTable;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, ScheduleIdRecord> {


    @Transactional
    @Query(
            "SELECT DISTINCT s " +
            "FROM Schedule s " +
            "LEFT JOIN FETCH s.reservation r " +
            "WHERE s.parsedDate = :parsedDate AND s.table.tableDefinition.standaloneCapacity >= :numberOfPeople"
    )
    List<Schedule> getSchedulesByParsedDateAndAdequateTable(String parsedDate, Integer numberOfPeople);


    Schedule getScheduleByTable_Id(Long id);


    List<Schedule> getSchedulesByTable(CustomTable table);
}
