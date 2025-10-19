package it.matteobarbera.tablereservation.model.reservation.strategies.multitable;


import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.model.table.SimpleJoinableTable;
import it.matteobarbera.tablereservation.model.table.layout.SimpleMatrixLayout;
import it.matteobarbera.tablereservation.service.reservation.ScheduleService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Primary
public class MultiTableFillScheduleFirst implements MultiTableReservationStrategy {

//
//    private final SimpleMatrixLayout simpleMatrixLayout;
//
//    public MultiTableFillScheduleFirst(SimpleMatrixLayout simpleMatrixLayout) {
//        this.simpleMatrixLayout = simpleMatrixLayout;
//    }


    @Override
    public Set<SimpleJoinableTable> postReservation(ScheduleService scheduleService, Reservation reservation) {
        return null;
    }
}
