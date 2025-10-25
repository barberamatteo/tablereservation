package it.matteobarbera.tablereservation.mapper;

import it.matteobarbera.tablereservation.model.dto.TableDTO;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.model.table.SimpleJoinableTable;
import it.matteobarbera.tablereservation.model.table.SimpleTable;
import it.matteobarbera.tablereservation.model.table.TableDefinition;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class TableMapper implements Converter<TableDTO, AbstractTable> {


    private final ModelMapper modelMapper;

    public TableMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.modelMapper.addConverter(this);
    }

    public AbstractTable toEntity(TableDTO tableDTO, TableDefinition tableDefinition) {
        AbstractTable toRet = modelMapper.map(tableDTO, AbstractTable.class);
        toRet.setTableDefinition(tableDefinition);

        return toRet;
    }

    @Override
    public AbstractTable convert(MappingContext<TableDTO, AbstractTable> mappingContext) {
        TableDTO tableDTO = mappingContext.getSource();
        if (tableDTO.getJoiningCapacity() == null || tableDTO.getHeadCapacity() == null)
            return new SimpleTable(tableDTO.getNumber());
        else
            return new SimpleJoinableTable(
                    tableDTO.getNumber(),
                    tableDTO.getHeadCapacity(),
                    tableDTO.getJoiningCapacity()
            );
    }

}
