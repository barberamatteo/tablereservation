package it.matteobarbera.tablereservation.service.table.layout;

import it.matteobarbera.tablereservation.model.table.layout.SimpleMatrixLayout;
import it.matteobarbera.tablereservation.repository.table.layout.TableLayoutRepository;
import org.springframework.stereotype.Service;

@Service
public class LayoutService {


    private final TableLayoutRepository tableLayoutRepository;

    public LayoutService(TableLayoutRepository tableLayoutRepository) {
        this.tableLayoutRepository = tableLayoutRepository;
    }

    public void saveLayout(SimpleMatrixLayout layout) {
        tableLayoutRepository.save(layout);
    }

    public SimpleMatrixLayout getLayout(Long id) {
        var res = tableLayoutRepository.findById(id);
        return res.orElse(null);
    }
}
