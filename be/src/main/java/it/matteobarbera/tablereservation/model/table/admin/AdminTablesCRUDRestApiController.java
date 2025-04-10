package it.matteobarbera.tablereservation.model.table.admin;

import it.matteobarbera.tablereservation.Constants;
import it.matteobarbera.tablereservation.http.response.CommonJSONBodies;
import it.matteobarbera.tablereservation.http.response.SuccessAdmin;
import it.matteobarbera.tablereservation.model.table.CustomTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

import static it.matteobarbera.tablereservation.http.TableAPIInfo.TABLE_CREATED;
import static it.matteobarbera.tablereservation.http.TablesAPIError.NO_SUCH_CATEGORY_DEFINED;
import static it.matteobarbera.tablereservation.http.TablesAPIError.NUMBER_CONFLICT;
import static it.matteobarbera.tablereservation.log.TableLog.*;

@RestController
@RequestMapping(Constants.ADMIN_TABLES_CRUD_API_ENDPOINT)
public class AdminTablesCRUDRestApiController {

    private static final Logger log = LoggerFactory.getLogger(AdminTablesCRUDRestApiController.class);
    private final TablesService tablesService;
    private final TablesDefinitionService tablesDefinitionService;

    @Autowired
    public AdminTablesCRUDRestApiController(TablesService tablesService, TablesDefinitionService tablesDefinitionService) {
        this.tablesService = tablesService;
        this.tablesDefinitionService = tablesDefinitionService;
    }

    @CrossOrigin
    @GetMapping("/getall/")
    public Set<CustomTable> getAllTables() {
        Set<CustomTable> tables = tablesService.getAllTables();
        if (tables.isEmpty()) {
            log.atInfo().log(NO_TABLES_FOUND);
        } else {
            log.atInfo().log(ALL_TABLES_FOUND, tables.size());
        }
        return tables;
    }

    @GetMapping("/getbynum/{num}")
    public CustomTable getTableByNum(@PathVariable("num") Integer num) {
        Optional<CustomTable> tableByNum = tablesService.getTableByNum(num);
        if (tableByNum.isPresent()) {
            log.atInfo().log(TABLE_FOUND, tableByNum.get().getId());
        } else {
            log.atInfo().log(NO_TABLE_FOUND_WITH_NUMBER, num);
            throw new TableCRUDException(
                    HttpStatus.NOT_FOUND,
                    num,
                    TableCRUDException.Cause.TABLE_NOT_FOUND
            );
        }
        return tableByNum.get();
    }

    @PostMapping("/create/")
    public ResponseEntity<?> createTable(
            @RequestParam(name = "category") String category,
            @RequestParam(name = "number") int number
    ){


        Optional<TableDefinition> tableDefinitionOptional =
                tablesDefinitionService.getDefByCategory(category);

        if (tableDefinitionOptional.isEmpty()) {
            log.atError().log(NO_CATEGORY_DEFINED, category);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                            CommonJSONBodies.fromStatusAndMsg(
                                    HttpStatus.BAD_REQUEST.value(),
                                    NO_SUCH_CATEGORY_DEFINED.getMessage(category)
                            )
                    );
        }

        Optional<CustomTable> tableWithSameNumberOptional = tablesService.getTableByNum(number);
        if (tableWithSameNumberOptional.isPresent()) {
            log.atError().log(TABLE_WITH_SAME_NUMBER, number);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                            CommonJSONBodies.fromStatusAndMsg(
                                    HttpStatus.BAD_REQUEST.value(),
                                    NUMBER_CONFLICT.getMessage(number)
                            )
                    );
        }


        tablesService.createTable(category.toLowerCase(), number);
        log.atInfo().log(TABLE_CREATED_WITH_NUMBER_AND_CATEGORY, number, category);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        CommonJSONBodies.fromStatusAndMsg(
                                HttpStatus.OK.value(),
                                TABLE_CREATED.getMessage(number, category)
                        )
                );
    }



}
