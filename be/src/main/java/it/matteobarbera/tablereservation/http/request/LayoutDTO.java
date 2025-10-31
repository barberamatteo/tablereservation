package it.matteobarbera.tablereservation.http.request;

import it.matteobarbera.tablereservation.model.table.AbstractTable;
import jakarta.persistence.Transient;

import java.util.Map;
import java.util.Set;

public class LayoutDTO {

    private String name;
    private Map<Long, Set<Long>> adjacencyRawObject;


    public LayoutDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Long, Set<Long>> getAdjacencyRawObject() {
        return adjacencyRawObject;
    }

    public void setAdjacencyRawObject(Map<Long, Set<Long>> adjacencyRawObject) {
        this.adjacencyRawObject = adjacencyRawObject;
    }

    public Set<Long> getTableIds(){
        return adjacencyRawObject.keySet();
    }

}
