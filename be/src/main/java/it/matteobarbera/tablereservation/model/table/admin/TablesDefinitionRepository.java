package it.matteobarbera.tablereservation.model.table.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TablesDefinitionRepository extends JpaRepository<TableDefinition, String> {

    @Query("SELECT td FROM TableDefinition td WHERE td.categoryName ILIKE CONCAT('%', :category)")
    Optional<TableDefinition> getTableDefinitionByCategoryName(String category);
}
