package it.matteobarbera.tablereservation.model.table.layout;

public interface Joinable {
    int getStandaloneCapacity();
    int getJoiningCapacity();
    int getHeadCapacity();
}
