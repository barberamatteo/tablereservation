package it.matteobarbera.tablereservation.repository.table.layout;

import it.matteobarbera.tablereservation.model.table.layout.SimpleMatrixLayout;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableLayoutRepository extends JpaRepository<SimpleMatrixLayout, Long> {
}
