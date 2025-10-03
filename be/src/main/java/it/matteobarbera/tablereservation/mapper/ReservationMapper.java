package it.matteobarbera.tablereservation.mapper;

import it.matteobarbera.tablereservation.model.customer.Customer;
import it.matteobarbera.tablereservation.model.dto.ReservationDTO;
import it.matteobarbera.tablereservation.model.reservation.Reservation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    private final ModelMapper modelMapper;

    public ReservationMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public Reservation toEntity(ReservationDTO reservationDTO) {
        return modelMapper.map(reservationDTO, Reservation.class);
    }

    public Reservation toEntity(ReservationDTO reservationDTO, Customer customer) {
        Reservation toRet = modelMapper.map(reservationDTO, Reservation.class);
        toRet.setCustomer(customer);
        return toRet;
    }
}
