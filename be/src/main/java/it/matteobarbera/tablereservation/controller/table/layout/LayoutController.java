package it.matteobarbera.tablereservation.controller.table.layout;


import it.matteobarbera.tablereservation.http.LayoutAPIResult;
import it.matteobarbera.tablereservation.http.request.LayoutDTO;
import it.matteobarbera.tablereservation.mapper.LayoutMapper;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.model.table.layout.SimpleMatrixLayout;
import it.matteobarbera.tablereservation.service.table.layout.TableLayoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

import static it.matteobarbera.tablereservation.Constants.LAYOUT_API_ENDPOINT;

@RestController
@RequestMapping(LAYOUT_API_ENDPOINT)
public class LayoutController {


    private final TableLayoutService tableLayoutService;
    private final LayoutMapper layoutMapper;

    public LayoutController(TableLayoutService tableLayoutService, LayoutMapper layoutMapper) {
        this.tableLayoutService = tableLayoutService;
        this.layoutMapper = layoutMapper;
    }

    @PostMapping("/create/")
    public ResponseEntity<?> createLayout(@RequestBody LayoutDTO layoutDTO){

        LayoutAPIResult result = tableLayoutService.createLayout(
                layoutDTO.getName(),
                layoutDTO.getAdjacencyRawObject()
        );


        if (result instanceof LayoutAPIResult.Success)
            return ResponseEntity.ok().body(result);
        return ResponseEntity.badRequest().body(result.getStatus());
    }
}
