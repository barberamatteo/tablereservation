package it.matteobarbera.tablereservation.repository.table.layout;

import it.matteobarbera.tablereservation.model.table.layout.TableGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface TableGraphRepository extends JpaRepository<TableGraph, Long> {
}
