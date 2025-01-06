package it.matteobarbera.tablereservation.model.table.admin;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tabledefinitions")
public class TableDefinition {
    @Id
    private String categoryName;

    private Integer standaloneCapacity;

    public TableDefinition(String categoryName, Integer standaloneCapacity) {
        this.categoryName = categoryName;
        this.standaloneCapacity = standaloneCapacity;
    }
    public TableDefinition() {}

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getStandaloneCapacity() {
        return standaloneCapacity;
    }

    public void setStandaloneCapacity(Integer standaloneCapacity) {
        this.standaloneCapacity = standaloneCapacity;
    }

    @Override
    public String toString() {
        return "TableDefinition{" +
                "categoryName='" + categoryName + '\'' +
                ", standaloneCapacity=" + standaloneCapacity +
                '}';
    }

}
