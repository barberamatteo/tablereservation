package it.matteobarbera.tablereservation.http.request;

import it.matteobarbera.tablereservation.model.table.SimpleJoinableTable;

import java.util.Map;
import java.util.Set;

public class LayoutSubmissionRequest {

    Map<SimpleJoinableTable, Set<SimpleJoinableTable>> adjacencyMap;


    public LayoutSubmissionRequest(Map<SimpleJoinableTable, Set<SimpleJoinableTable>> adjacencyMap) {
        this.adjacencyMap = adjacencyMap;
    }
}
