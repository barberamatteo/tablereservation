package it.matteobarbera.tablereservation.model.table.admin;

import it.matteobarbera.tablereservation.Constants;
import it.matteobarbera.tablereservation.http.response.CommonJSONBodies;
import it.matteobarbera.tablereservation.http.response.SuccessAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static it.matteobarbera.tablereservation.http.TableDefinitionAPIError.EXISTING_CATEGORY_NAME;
import static it.matteobarbera.tablereservation.http.TableDefinitionAPIInfo.TABLE_DEFINITION_CREATED;
import static it.matteobarbera.tablereservation.log.TableDefinitionLog.DEFINITION_ALREADY_EXISTS;
import static it.matteobarbera.tablereservation.log.TableDefinitionLog.DEFINITION_CREATED;

@RestController
@RequestMapping(Constants.ADMIN_TABLESDEFINITION_CRUD_API_ENDPOINT)
public class AdminTablesDefinitionCRUDRestApiController {

    private static final Logger log = LoggerFactory.getLogger(AdminTablesDefinitionCRUDRestApiController.class);
    private final TablesDefinitionService tablesDefinitionService;

    @Autowired
    public AdminTablesDefinitionCRUDRestApiController(TablesDefinitionService tablesDefinitionService) {
        this.tablesDefinitionService = tablesDefinitionService;
    }


    @PostMapping("/define/")
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
}
