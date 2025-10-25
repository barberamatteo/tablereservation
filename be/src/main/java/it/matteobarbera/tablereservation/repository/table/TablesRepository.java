package it.matteobarbera.tablereservation.repository.table;

import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.model.table.SimpleTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TablesRepository extends JpaRepository<AbstractTable, Long> {
    Optional<AbstractTable> findByNumberInLounge(int numberInLounge);

}
