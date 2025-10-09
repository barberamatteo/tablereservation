package it.matteobarbera.tablereservation.mapper;

import it.matteobarbera.tablereservation.model.customer.Customer;
import it.matteobarbera.tablereservation.model.dto.ReservationDTO;
import it.matteobarbera.tablereservation.model.preferences.UserPreferences;
import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.utils.DateUtils;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReservationMapper implements Converter<ReservationDTO, Reservation> {

    private final ModelMapper modelMapper;
    private final UserPreferences userPreferences;

    public ReservationMapper(ModelMapper modelMapper, UserPreferences userPreferences) {
        this.modelMapper = modelMapper;
        this.modelMapper.addConverter(this);
        this.userPreferences = userPreferences;
    }


    public Reservation toEntity(ReservationDTO reservationDTO) {
        return modelMapper.map(reservationDTO, Reservation.class);
    }

    public Reservation toEntity(ReservationDTO reservationDTO, Customer customer) {
        Reservation toRet = modelMapper.map(reservationDTO, Reservation.class);
        toRet.setCustomer(customer);
        return toRet;
    }


    @Override
    public Reservation convert(MappingContext<ReservationDTO, Reservation> mappingContext) {
        ReservationDTO src = mappingContext.getSource();
        if (src.getEndDateTime() == null)
            src.setEndDateTime(
                    DateUtils.offsetFrom(
                            userPreferences.DEFAULT_LEAVE_TIME_MINUTES_OFFSET,
                            src.getStartDateTime()
                    )
            );
        return new Reservation(
                LocalDateTime.parse(src.getStartDateTime()),
                LocalDateTime.parse(src.getEndDateTime()),
                src.getNumberOfPeople()
        );

    }
}
