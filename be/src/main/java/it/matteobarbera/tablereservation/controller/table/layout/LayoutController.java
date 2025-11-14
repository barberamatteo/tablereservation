package it.matteobarbera.tablereservation.controller.table.layout;


import it.matteobarbera.tablereservation.http.LayoutAPIResult;
import it.matteobarbera.tablereservation.http.request.LayoutDTO;
import it.matteobarbera.tablereservation.service.table.layout.TableLayoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static it.matteobarbera.tablereservation.Constants.LAYOUT_API_ENDPOINT;

@RestController
@RequestMapping(LAYOUT_API_ENDPOINT)
public class LayoutController {

    private final TableLayoutService tableLayoutService;

    public LayoutController(TableLayoutService tableLayoutService) {
        this.tableLayoutService = tableLayoutService;
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


    @GetMapping("/getbyid/{id}")
    public ResponseEntity<?> getLayoutById(@PathVariable Long id){
        LayoutAPIResult result = tableLayoutService.getLayout(id);
        if (result instanceof LayoutAPIResult.Success)
            return ResponseEntity.ok().body(result);
        return ResponseEntity.badRequest().body(result);
    }
}
