package it.matteobarbera.tablereservation.mapper;

import it.matteobarbera.tablereservation.http.request.LayoutDTO;
import it.matteobarbera.tablereservation.model.table.layout.SimpleMatrixLayout;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class LayoutMapper implements Converter<LayoutDTO, SimpleMatrixLayout> {


    private final ModelMapper modelMapper;

    public LayoutMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public SimpleMatrixLayout toEntity(LayoutDTO layoutDTO) {
        return modelMapper.map(layoutDTO, SimpleMatrixLayout.class);
    }

    @Override
    public SimpleMatrixLayout convert(MappingContext<LayoutDTO, SimpleMatrixLayout> mappingContext) {
        LayoutDTO layoutDTO = mappingContext.getSource();
        //SimpleMatrixLayout layout = new SimpleMatrixLayout(layoutDTO.getName(), layoutDTO.getAdjacencyMap());
        return null;
    }
}
