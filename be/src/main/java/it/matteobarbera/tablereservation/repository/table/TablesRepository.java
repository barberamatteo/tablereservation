package it.matteobarbera.tablereservation.repository.table;

import it.matteobarbera.tablereservation.model.table.CustomTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TablesRepository extends JpaRepository<CustomTable, Long> {
    Optional<CustomTable> findByNumberInLounge(int numberInLounge);

    @Query(
            "SELECT t " +
            "FROM CustomTable t JOIN TableDefinition td ON t.tableDefinition = td " +
            "WHERE td.standaloneCapacity >= :numberOfPeople"
    )
    Set<CustomTable> getAdequateTables(Integer numberOfPeople);
}
