package it.matteobarbera.tablereservation.repository.reservation;

import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.model.reservation.Schedule;
import it.matteobarbera.tablereservation.model.reservation.ScheduleIdRecord;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.model.table.layout.SimpleMatrixLayout;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, ScheduleIdRecord> {



    @Transactional
    @Query(
            "SELECT s " +
            "FROM Schedule s " +
            "WHERE s.id.parsedDate = :parsedDate AND s.table.tableDefinition.standaloneCapacity >= :numberOfPeople" +
            " ORDER BY s.table.tableDefinition.standaloneCapacity, s.table.numberInLounge ASC"
    )
    List<Schedule> getSchedulesByDateAndAdequateTable(String parsedDate, Integer numberOfPeople);

    @Query(
            "SELECT s " +
            "FROM Schedule s WHERE s.id.parsedDate = :arrivalDate"
    )
    Set<Schedule> getSchedulesByParsedDate(String arrivalDate);

    @Query("SELECT s FROM Schedule s WHERE s.id.parsedDate = :day AND s.id.tableId = :table")
    Optional<Schedule> getScheduleByRecordId(LocalDate day, Long table);

    Optional<Schedule> getScheduleByReservationsContaining(Reservation reservation);

    @Query("SELECT s " +
            "FROM Schedule s JOIN s.table t " +
            "WHERE t IN :joinableTables AND s.id.parsedDate = :startDate AND s.layout = :layout"
    )
    Set<Schedule> getSchedulesOfTables(
            Set<AbstractTable> joinableTables,
            String startDate,
            SimpleMatrixLayout layout
    );
}
