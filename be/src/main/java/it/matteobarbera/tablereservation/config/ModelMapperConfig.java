package it.matteobarbera.tablereservation.config;

import it.matteobarbera.tablereservation.model.dto.ReservationDTO;
import it.matteobarbera.tablereservation.model.reservation.Interval;
import it.matteobarbera.tablereservation.model.reservation.Reservation;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {


    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        Converter<ReservationDTO, Interval> intervalConverter = mappingContext -> {
            if (mappingContext.getSource() == null)
                return null;
            return new Interval(
                    mappingContext.getSource().getStartDateTime(),
                    mappingContext.getSource().getEndDateTime()
            );
        };

        modelMapper.typeMap(Reservation.class, ReservationDTO.class).addMappings(mapper -> {
            mapper.map(reservation -> reservation.getInterval().getStartDateTime(), ReservationDTO::setStartDateTime);
            mapper.map(reservation -> reservation.getInterval().getEndDateTime(), ReservationDTO::setEndDateTime);
        });

        return modelMapper;
    }
}
