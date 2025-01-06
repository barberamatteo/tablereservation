package it.matteobarbera.tablereservation.model.table.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TablesDefinitionRepository extends JpaRepository<TableDefinition, String> {

    Optional<TableDefinition> getTableDefinitionByCategoryName(String categoryName);
}
