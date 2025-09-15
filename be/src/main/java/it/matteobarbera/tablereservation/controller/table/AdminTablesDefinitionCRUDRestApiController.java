package it.matteobarbera.tablereservation.controller.table;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.matteobarbera.tablereservation.Constants;
import it.matteobarbera.tablereservation.http.response.CommonJSONBodies;
import it.matteobarbera.tablereservation.model.table.TableDefinition;
import it.matteobarbera.tablereservation.service.table.TablesDefinitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

import static it.matteobarbera.tablereservation.http.TableDefinitionAPIError.EXISTING_CATEGORY_NAME;
import static it.matteobarbera.tablereservation.http.TableDefinitionAPIInfo.TABLE_DEFINITION_CREATED;
import static it.matteobarbera.tablereservation.logging.TableDefinitionLog.*;

@RestController
@RequestMapping(Constants.ADMIN_TABLESDEFINITION_CRUD_API_ENDPOINT)
public class AdminTablesDefinitionCRUDRestApiController {

    private static final Logger log = LoggerFactory.getLogger(AdminTablesDefinitionCRUDRestApiController.class);
    private final TablesDefinitionService tablesDefinitionService;

    @Autowired
    public AdminTablesDefinitionCRUDRestApiController(TablesDefinitionService tablesDefinitionService) {
        this.tablesDefinitionService = tablesDefinitionService;
    }


    @Operation(
            summary = "Define a new category",
            description = "Defines a new table category, specifying a name and a standalone (per table) capability"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "A table definition with the category name <category> already exists."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Table definition with name <category> and standalone capacity <standaloneCapacity " +
                            "created"
            )
    })
    @PostMapping("/define/")
    @CrossOrigin
    public ResponseEntity<?> defineNewCategory(
            @RequestParam("category") String category,
            @RequestParam("standaloneCapacity") Integer standaloneCapacity
    ){
        Optional<TableDefinition> existingCategory = tablesDefinitionService.getDefByCategory(category.toLowerCase());

        if (existingCategory.isPresent()) {
            log.atError().log(DEFINITION_ALREADY_EXISTS, category);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                            CommonJSONBodies.fromStatusAndMsg(
                                    HttpStatus.BAD_REQUEST.value(),
                                    EXISTING_CATEGORY_NAME.getMessage(category)
                            )
                    );
        }

        tablesDefinitionService.createNewDef(category.toLowerCase(), standaloneCapacity);
        log.atInfo().log(DEFINITION_CREATED, category, standaloneCapacity);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        CommonJSONBodies.fromStatusAndMsg(
                                HttpStatus.OK.value(),
                                TABLE_DEFINITION_CREATED.getMessage(category, standaloneCapacity)
                        )
                );
    }

    @Operation(
            summary = "Get all definitions",
            description = "Returns all the table definitions created by the admin"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @GetMapping("/getall/")
    @CrossOrigin
    public Set<TableDefinition> getAllTableDefinitions() {
        Set<TableDefinition> tableDefinitions = tablesDefinitionService.getAllTablesDefinitions();
        if (tableDefinitions.isEmpty()) {
            log.atWarn().log(NO_DEFINITION_FOUND);
        } else {
            log.atInfo().log(ALL_DEFINITIONS_FOUND, tableDefinitions.size());
        }

        return tableDefinitions;
    }
}
